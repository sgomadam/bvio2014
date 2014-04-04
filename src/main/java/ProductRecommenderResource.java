import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;


@Path("data")
public class ProductRecommenderResource{

    private final Jedis jedis;
    private final String productLookupUrl = "http://api.bazaarvoice.com/data/products.json?PassKey=%1$s&ApiVersion=5.4&filter=id:%2$s";

    public ProductRecommenderResource() {
        jedis = new Jedis("localhost");
    }

    @GET
    @Path ("{resource: products}.json")
    @Produces({"application/json;charset=utf-8","text/javascript;charset=utf-8"})
    public Response query(@Context UriInfo uriInfo, @PathParam("resource") String resourceParam)
            throws Exception {

        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String client = String.valueOf(queryParams.getFirst("client"));
        String user = String.valueOf(queryParams.getFirst("user"));
        String ratingStr = String.valueOf(queryParams.getFirst("rating"));
        Integer rating  = Integer.parseInt(ratingStr);
        Set<String> prodIds = getIntersectingProducts(client, user, rating);
        Set<String> dummyProdIds = Sets.newHashSet();
        dummyProdIds.add("test1");
        dummyProdIds.add("test");
        return getProductInfo(queryParams, client, prodIds);
    }

    private Response getProductInfo(MultivaluedMap<String, String> queryParams, String client, Set<String> prodIds) throws IOException {
        String apiLookupKeyFormat = client + ".apikey";
        String apiKey = jedis.get(apiLookupKeyFormat);
        String commaSeparatedProdIds = getCommaSeparatedProducts(prodIds);
        String urlStr = String.format(productLookupUrl, apiKey, commaSeparatedProdIds);
        URL url = new URL(urlStr);
        URLConnection uc = url.openConnection();
        uc.setRequestProperty("X-Requested-With", "Curl");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(uc.getInputStream(), Map.class);
        return newResponse(queryParams, jsonMap );
    }

    public static Response newResponse(MultivaluedMap<String, String> queryParams, Object json) {
        Response.ResponseBuilder builder = Response.ok();
        builder.entity(json);
        return builder.build();
    }

    private String getCommaSeparatedProducts(Set<String> prodIds) {
        StringBuilder sb  = new StringBuilder();
        for(String prodId : prodIds) {
            if(sb.length() != 0){
                sb.append(",");
            }
            sb.append(prodId);
        }
        return sb.toString();
    }

    private Set<String> getIntersectingProducts(String client, String user, int rating) {
        String clientUserkey = client + ".users";
        Set<String> users = jedis.smembers(clientUserkey);
        //bestbuy.inter.1-2.1
        String userKeyPattern = client + "." + "inter" + "." + user + "-" +  "*" + "." + rating;
        Set<String> matchingUsers = jedis.keys(userKeyPattern);

        int bestMatchedProductCount = 0;
        String bestMatchedUserPair = "";
        for (String matchUser : matchingUsers){
            Set<String> productsReviews = jedis.smembers(matchUser);
            if(productsReviews.size() > bestMatchedProductCount) {
                bestMatchedProductCount = productsReviews.size();
                bestMatchedUserPair = matchUser;
            }
        }
        String bestMatchUser = getUser(bestMatchedUserPair, user);
        //bestbuy.users.1.1
        String currentUserKey = clientUserkey + "." + user + "." + rating;
        String bestMatchUserKey = clientUserkey + "." + bestMatchUser + "." + rating;
        return jedis.sdiff(bestMatchUserKey, currentUserKey);
    }

    private static String getUser(String userName, String currentUser) {

        String[] parts = userName.split("\\.");
        if(parts.length >= 4) {
            String userPart = parts[2];
            String[] twoUserPart = userPart.split("-");
            if(twoUserPart[0].equals(currentUser)) {
                return twoUserPart[1];
            }
            else {
                return twoUserPart[0];
            }

        }
        return null;
    }

}

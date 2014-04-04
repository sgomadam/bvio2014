import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Path("data")
public class ProductRecommenderResource{
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
        getProductInfo(client, prodIds);
        return null;
    }

    private void getProductInfo(String client, Set<String> prodIds) {
        
    }

    private Set<String> getIntersectingProducts(String client, String user, int rating) {
        Jedis jedis = new Jedis("localhost");
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

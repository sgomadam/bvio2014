import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import config.MatchingServiceConfig;
import redis.clients.jedis.Jedis;

public class MatchingService extends Service<MatchingServiceConfig>{

    public static void main(String[] args) throws Exception {
        //setupJedis();
        new MatchingService().run(args);
    }

    private static void setupJedis() {
        Jedis jedis = new Jedis("localhost");

        jedis.set("bestbuy.apikey", "e88x234atljrwymp14f7zek7i");

        jedis.sadd("bestbuy.users", "1", "2", "3", "4", "5");

        jedis.sadd("bestbuy.users.1.1", "test1", "2", "mi80100229", "999977000050005");
        //jedis.sadd("users.1.2", null);
        jedis.sadd("bestbuy.users.1.3", "abcat0105000", "999997300050001");
        jedis.sadd("bestbuy.users.1.4", "9999941", "999977100050007");
        jedis.sadd("bestbuy.users.1.5", "999977100050005", "999977100050002");

        jedis.sadd("bestbuy.users.2.1", "test1", "test");
        jedis.sadd("bestbuy.users.2.2", "abcat0105000");
        jedis.sadd("bestbuy.users.2.3", "mi80100229", "999997300050001");
        jedis.sadd("bestbuy.users.2.4", "999977100050002", "999977100050001");
        jedis.sadd("bestbuy.users.2.5", "999977100050007", "999977100050005");

        //jedis.sadd("users.3.1", null);
        //jedis.sadd("users.3.2", null);
        jedis.sadd("bestbuy.users.3.3", "test1", "test");
        jedis.sadd("bestbuy.users.3.4", "999977100050005", "999977100050002");
        //jedis.sadd("users.3.5", null);

        jedis.sadd("bestbuy.users.4.1", "test1", "test", "mi80100229", "999977100050000");
        //jedis.sadd("users.4.2", null);
        jedis.sadd("bestbuy.users.4.3", "abcat0105000", "999997300050001");
        jedis.sadd("bestbuy.users.4.4", "9999941", "999977100050007");
        jedis.sadd("bestbuy.users.4.5", "999977100050005", "999977100050002", "999977000050004");

        //jedis.sadd("users.5.1", null);
        //jedis.sadd("users.5.2", null);
        jedis.sadd("bestbuy.users.5.3", "test1", "test");
        jedis.sadd("bestbuy.users.5.4", "999977100050005", "999977100050002");
        //jedis.sadd("users.5.5", null);

        jedis.sadd("bestbuy.inter.1-2.1", "test1", "test");
        //jedis.sadd("inter.1-2.2", null);
        jedis.sadd("bestbuy.inter.1-2.3", "999997300050001");
        //jedis.sadd("inter.1-2.4", null);
        jedis.sadd("bestbuy.inter.1-2.5", "999977100050005");

        /*jedis.sadd("inter.1-3.1", null);
        jedis.sadd("inter.1-3.2", null);
        jedis.sadd("inter.1-3.3", null);
        jedis.sadd("inter.1-3.4", null);
        jedis.sadd("inter.1-3.5", null);*/

        jedis.sadd("bestbuy.inter.1-4.1", "test1", "test", "mi80100229");
        //jedis.sadd("inter.1-4.2", null);

        jedis.sadd("bestbuy.inter.1-4.3", "abcat0105000", "999997300050001");
        jedis.sadd("bestbuy.inter.1-4.4", "9999941", "999977100050007");
        jedis.sadd("bestbuy.inter.1-4.5", "999977100050005", "999977100050002");

        /*jedis.sadd("inter.1-5.1", null);
        jedis.sadd("inter.1-5.2", null);
        jedis.sadd("inter.1-5.3", null);
        jedis.sadd("inter.1-5.4", null);
        jedis.sadd("inter.1-5.5", null);

        jedis.sadd("inter.2-3.1", null);
        jedis.sadd("inter.2-3.2", null);
        jedis.sadd("inter.2-3.3", null);*/
        jedis.sadd("bestbuy.inter.2-3.4", "999977100050002");
        //jedis.sadd("inter.2-3.5", null);

        jedis.sadd("bestbuy.inter.2-4.1", "test1", "test");
        //jedis.sadd("inter.2-4.2", null);
        jedis.sadd("bestbuy.inter.2-4.3", "999997300050001");
        //jedis.sadd("inter.2-4.4", null);
        //jedis.sadd("inter.2-4.5", null);

        //jedis.sadd("inter.2-5.1", null);
        //jedis.sadd("inter.2-5.2", null);
        //jedis.sadd("inter.2-5.3", null);
        jedis.sadd("bestbuy.inter.2-5.4", "999977100050002");
        //jedis.sadd("inter.2-5.5", null);

        /*jedis.sadd("inter.3-4.1", null);
        jedis.sadd("inter.3-4.2", null);
        jedis.sadd("inter.3-4.3", null);
        jedis.sadd("inter.3-4.4", null);
        jedis.sadd("inter.3-4.5", null);

        jedis.sadd("inter.3-5.1", null);
        jedis.sadd("inter.3-5.2", null);*/
        jedis.sadd("bestbuy.inter.3-5.3", "test1", "test");
        jedis.sadd("bestbuy.inter.3-5.4", "999977100050005", "999977100050002");
        /*jedis.sadd("inter.3-5.5", null);

        jedis.sadd("inter.4-5.1", null);
        jedis.sadd("inter.4-5.2", null);
        jedis.sadd("inter.4-5.3", null);
        jedis.sadd("inter.4-5.4", null);
        jedis.sadd("inter.4-5.5", null); */

    }

    @Override
    public void initialize(Bootstrap bootstrap) {
        bootstrap.setName("productRecommendationService");
        bootstrap.addBundle(new AssetsBundle("/www/", "/www"));
    }

    @Override
    public void run(MatchingServiceConfig configuration, Environment environment) throws Exception {
        Injector injector = Guice.createInjector(makeModule(configuration, environment));
        environment.addResource(injector.getInstance(ProductRecommenderResource.class));

    }

    protected Module makeModule(MatchingServiceConfig config, Environment environment) {
        return new ProductRecomenderModule();
    }
}

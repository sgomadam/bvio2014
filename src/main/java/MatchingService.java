import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import config.MatchingServiceConfig;
import redis.clients.jedis.Jedis;

public class MatchingService extends Service<MatchingServiceConfig>{

    public static void main(String[] args) throws Exception {
        setupJedis();
        new MatchingService().run(args);
    }

    private static void setupJedis() {
        Jedis jedis = new Jedis("localhost");

        jedis.set("bestbuy.apikey", "e88x234atljrwymp14f7zek7i");

        jedis.sadd("bestbuy.users", "1", "2", "3", "4", "5");

        jedis.sadd("bestbuy.users.1.1", "1", "2", "3", "12");
        //jedis.sadd("users.1.2", null);
        jedis.sadd("bestbuy.users.1.3", "4", "5");
        jedis.sadd("bestbuy.users.1.4", "6", "7");
        jedis.sadd("bestbuy.users.1.5", "8", "9");

        jedis.sadd("bestbuy.users.2.1", "1", "2");
        jedis.sadd("bestbuy.users.2.2", "4");
        jedis.sadd("bestbuy.users.2.3", "3", "5");
        jedis.sadd("bestbuy.users.2.4", "9", "10");
        jedis.sadd("bestbuy.users.2.5", "7", "8");

        //jedis.sadd("users.3.1", null);
        //jedis.sadd("users.3.2", null);
        jedis.sadd("bestbuy.users.3.3", "1", "2");
        jedis.sadd("bestbuy.users.3.4", "8", "9");
        //jedis.sadd("users.3.5", null);

        jedis.sadd("bestbuy.users.4.1", "1", "2", "3", "11");
        //jedis.sadd("users.4.2", null);
        jedis.sadd("bestbuy.users.4.3", "4", "5");
        jedis.sadd("bestbuy.users.4.4", "6", "7");
        jedis.sadd("bestbuy.users.4.5", "8", "9", "13");

        //jedis.sadd("users.5.1", null);
        //jedis.sadd("users.5.2", null);
        jedis.sadd("bestbuy.users.5.3", "1", "2");
        jedis.sadd("bestbuy.users.5.4", "8", "9");
        //jedis.sadd("users.5.5", null);

        jedis.sadd("bestbuy.inter.1-2.1", "1", "2");
        //jedis.sadd("inter.1-2.2", null);
        jedis.sadd("bestbuy.inter.1-2.3", "5");
        //jedis.sadd("inter.1-2.4", null);
        jedis.sadd("bestbuy.inter.1-2.5", "8");

        /*jedis.sadd("inter.1-3.1", null);
        jedis.sadd("inter.1-3.2", null);
        jedis.sadd("inter.1-3.3", null);
        jedis.sadd("inter.1-3.4", null);
        jedis.sadd("inter.1-3.5", null);*/

        jedis.sadd("bestbuy.inter.1-4.1", "1", "2", "3");
        //jedis.sadd("inter.1-4.2", null);

        jedis.sadd("bestbuy.inter.1-4.3", "4", "5");
        jedis.sadd("bestbuy.inter.1-4.4", "6", "7");
        jedis.sadd("bestbuy.inter.1-4.5", "8", "9");

        /*jedis.sadd("inter.1-5.1", null);
        jedis.sadd("inter.1-5.2", null);
        jedis.sadd("inter.1-5.3", null);
        jedis.sadd("inter.1-5.4", null);
        jedis.sadd("inter.1-5.5", null);

        jedis.sadd("inter.2-3.1", null);
        jedis.sadd("inter.2-3.2", null);
        jedis.sadd("inter.2-3.3", null);*/
        jedis.sadd("bestbuy.inter.2-3.4", "9");
        //jedis.sadd("inter.2-3.5", null);

        jedis.sadd("bestbuy.inter.2-4.1", "1", "2");
        //jedis.sadd("inter.2-4.2", null);
        jedis.sadd("bestbuy.inter.2-4.3", "5");
        //jedis.sadd("inter.2-4.4", null);
        //jedis.sadd("inter.2-4.5", null);

        //jedis.sadd("inter.2-5.1", null);
        //jedis.sadd("inter.2-5.2", null);
        //jedis.sadd("inter.2-5.3", null);
        jedis.sadd("bestbuy.inter.2-5.4", "9");
        //jedis.sadd("inter.2-5.5", null);

        /*jedis.sadd("inter.3-4.1", null);
        jedis.sadd("inter.3-4.2", null);
        jedis.sadd("inter.3-4.3", null);
        jedis.sadd("inter.3-4.4", null);
        jedis.sadd("inter.3-4.5", null);

        jedis.sadd("inter.3-5.1", null);
        jedis.sadd("inter.3-5.2", null);*/
        jedis.sadd("bestbuy.inter.3-5.3", "1", "2");
        jedis.sadd("bestbuy.inter.3-5.4", "8", "9");
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

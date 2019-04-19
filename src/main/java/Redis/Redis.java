package Redis;

import redis.clients.jedis.Jedis;

public class Redis {
    private static Jedis jedis = new Jedis("localhost");

    public static Jedis getJedis() {
        return jedis;
    }
}

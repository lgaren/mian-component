package com.dsg.api.demo.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created on 2018/4/122018 四月 星期四下午 17:42
 * ai-recommend
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  RedisInsert, V 0.0  2018/4/12 下午 17:42 DSG Exp$$
 */
public class RedisInsert {

//    url 10.200.2.105:7000,10.200.2.105:7001,10.200.2.105:7002,10.200.2.105:7003,10.200.2.105:7004,10.200.2.105:7005

    public static void main(String[] args) throws IOException {
        JedisCluster jedisCluster;
        LinkedHashSet nodes = new LinkedHashSet<HostAndPort>();
        Arrays.stream(args[2].split(",")).map( e -> e.split(":")).forEach(n -> {
            nodes.add(new HostAndPort(n[0], Integer.valueOf(n[1])));
        });
        jedisCluster = new JedisCluster(nodes, new JedisPoolConfig());
        Arrays.stream(args[3].split(",")).forEach( op -> {
            switch (op) {
                case "get" :
                    jedisCluster.get(args[0]);
                    break;
                case "insert" :
//                    jedisCluster.set()
                    jedisCluster.set(args[0],args[1]);
                    break;
                case "del" :
                    jedisCluster.del(args[0]);
                    break;
                default:
                    break;
            }
                }
        );
        jedisCluster.close();
    }
}

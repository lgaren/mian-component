package com.lvmama.common.api

import com.dsg.common.conf.DSGConfig
import redis.clients.jedis.{HostAndPort, JedisCluster, JedisPoolConfig}

/**
  *
  * Created on 2018/9/27  Thu PM 15:13
  * mian-component
  *
  * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
  * @Version: SimilarityMeasures V 0.0, Sep 27, 2018 DSG Exp$$
  * @Since 1.8
  * @Description :
  *
  *
  */
class RedisClient() {

  def newInstance: JedisCluster = {
    val poolConfig = new JedisPoolConfig()
    poolConfig.setMaxTotal(100)
    poolConfig.setMaxIdle(50)
    poolConfig.setMaxWaitMillis(1000)
    val nodes = new java.util.LinkedHashSet[HostAndPort]()
    val redistHosts = DSGConfig.getVar( DSGConfig.ConfVars.REDIS_SERVER_CLUSTER).split(",").toList.map(_.trim).map(_.split(":").map(_.trim))
    redistHosts.foreach(n => {
      nodes.add(new HostAndPort(n(0), n(1).toInt))
    })
     new JedisCluster(nodes, poolConfig)
  }
}

object RedisClient{
  var jedisCluster:JedisCluster = null

  def apply() :JedisCluster ={

    if (jedisCluster == null)
      jedisCluster = new RedisClient().newInstance
     jedisCluster
  }

  def close() : Unit = {
    if (jedisCluster != null) {
      jedisCluster.close()
      jedisCluster = null
    }

  }
}
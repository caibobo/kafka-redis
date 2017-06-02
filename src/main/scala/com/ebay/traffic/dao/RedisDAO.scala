package com.ebay.traffic.dao

import com.ebay.traffic.config.Configuration
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}
import scala.util.{Failure, Success, Try}
import org.apache.logging.log4j.LogManager

/**
  * Created by lliu15 on 2017/5/22.
  */

class RedisDAO {

  val logger = LogManager.getLogger()

  val jedisPoolConfig = new JedisPoolConfig()
  jedisPoolConfig.setMaxTotal(Configuration.redisPoolMaxTotal)
  jedisPoolConfig.setMaxIdle(Configuration.redisPoolMaxIdle)
  jedisPoolConfig.setMinIdle(Configuration.redisPoolMinIdle)
  lazy val jedisPool = new JedisPool(jedisPoolConfig, Configuration.redisHost, Configuration.redisPort, Configuration.redisTimeout)

  /**
    * Get a jedis connection from Pool
    * @return Jedis connection
    */
  def getJedis: Option[Jedis] = Try {
    jedisPool.getResource
  } match {
    case Success(jedis) =>
      Some(jedis)

    case Failure(ex) =>
      logger.error("Failed to get redis connection", ex)
      //meter(Map("data_type"->"redis_connection", "result"->"failure"), 1)
      None
  }
}

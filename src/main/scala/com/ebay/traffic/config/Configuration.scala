package com.ebay.traffic.config

import com.typesafe.config.ConfigFactory

/**
  * Created by lliu15 on 2017/5/22.
  */
object Configuration {
  //
  lazy val conf = ConfigFactory.load("config.conf")

  //kafka configuration
  lazy val kafkaZkServer = conf.getString("kafka.zk")
  lazy val kafkaHost = conf.getString("kafka.host")
  lazy val kafkaGroup = conf.getString("kafka.group")
  lazy val kafkaTopicIntime = conf.getString("kafka.topic.test")

  lazy val kafkaQueueSize = conf.getInt("internal_queues.kafka_queue_size")

  // redis configuration
  lazy val redisHost = conf.getString("redis.host")
  lazy val redisPort = conf.getInt("redis.port")
  lazy val redisPoolMaxTotal = conf.getInt("redis.connection_pool.max_total")
  lazy val redisPoolMaxIdle = conf.getInt("redis.connection_pool.max_idle")
  lazy val redisPoolMinIdle = conf.getInt("redis.connection_pool.min_idle")
  lazy val redisTimeout = conf.getInt("redis.connection_pool.timeout")
}

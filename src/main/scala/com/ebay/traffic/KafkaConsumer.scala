package com.ebay.traffic


import com.ebay.traffic.config.Configuration
import com.ebay.traffic.dao.RedisDAO
import com.ebay.traffic.config.Configuration._
import org.apache.logging.log4j.LogManager
import java.util.{Collections, Properties}
import scala.concurrent.ExecutionContext.Implicits.global

import kafka.consumer.{Consumer, ConsumerConfig}

import scala.collection.JavaConversions._
import java.util.Properties
import java.util.concurrent.LinkedBlockingDeque

import kafka.consumer.{Consumer, ConsumerConfig}
import kafka.serializer.StringDecoder
import org.apache.logging.log4j.LogManager

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


/**
  * Created by lliu15 on 2017/5/22.
  */

class KafkaConsumerA(queue: LinkedBlockingDeque[String]) {
  //val logger = LogManager.getLogger()
  private lazy val logger = LogManager.getLogger(this.getClass)
  private lazy val topic = Configuration.kafkaTopicIntime


  //val props = createConsumerConfig()
  //val consumer = new KafkaConsumer[String, String](props)

  //  def createConsumerConfig(): Properties = {
  //    val props = new Properties()
  //    props.put("bootstrap.servers",Configuration.kafkaHost)
  //    props.put("group.id", Configuration.kafkaGroup)
  //    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  //    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  //    props
  //  }
  lazy val consumer = {
    val props = new Properties()
    props.put("zookeeper.connect", Configuration.kafkaZkServer)
    props.put("group.id", Configuration.kafkaGroup)
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
    props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")

    Consumer.create(new ConsumerConfig(props))
  }

  //  def read() = {
  //    consumer.subscribe(Collections.singletonList(this.topic))
  //    //val redis = new RedisDAO
  //    //val jedis = redis.getJedis
  //    while(true){
  //      val records = consumer.poll(1000)
  //      for (record <- records) {
  //        val result = record.value()
  //        //logger.debug(result)
  //        println(result)
  //        //jedis.get.set(result,result)
  //      }
  //    }
  //  }
  /**
    * Read in time delete events from Kafka into internal blocking queue
    */
  def read() = {

    consumer.createMessageStreams(Map(topic -> 1), new StringDecoder, new StringDecoder).get(topic).foreach(_.foreach(_.foreach(message => {
      //      val redis = new RedisDAO
      //      val jedis = redis.getJedis
      logger.debug(s"BES event: ${message.message}")
      //jedis.get.set(message.message,message.message)
      //print(message.message)
      // will block the current thread when queue is full
      queue.put(message.message)
    })))
  }

}

object KafkaConsumerA extends App {
  lazy val kafkaQueue = new LinkedBlockingDeque[String](kafkaQueueSize)
  val example = new KafkaConsumerA(kafkaQueue)
  private lazy val logger = LogManager.getLogger(this.getClass)
  //example.read()
  val redis = new RedisDAO
  val jedis = redis.getJedis
  val f1 = Future {
    example.read()
  }
  val f2 = Future {
    while (true) {
      try {
        val item = kafkaQueue.take()
        //write to redis
        jedis.get.set(item, item)
        //TODO
        /*
        analysis the json data via using spray-json
         */
        logger.debug(s"Publisher record: $item")
      } catch {
        case t: Throwable => logger.error("Error Writing Record: ", t)
      }
    }
  }
  logger.info("Application started.")
  (List(f1, f2)).foreach(f => Await.ready(f, Duration.Inf))
}

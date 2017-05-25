package com.ebay.traffic

import java.util.concurrent.LinkedBlockingDeque

import config.Configuration._
import org.apache.logging.log4j.LogManager

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by lliu15 on 2017/5/23.
  */
case class Perpson(name:String,firstName:String,age:Int)
object Main {

  private lazy val logger = LogManager.getLogger(this.getClass)

  def main(args: Array[String]){

    //lazy val kafkaQueue = new LinkedBlockingDeque[String](kafkaQueueSize)
    logger.info("i am info logger")
    logger.warn("i am info warn")
    logger.debug("i am info debug")
    logger.error("i am info error")






  }
}

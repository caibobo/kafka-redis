package com.ebay.traffic.bean

import spray.json.DefaultJsonProtocol

/**
  * Created by lliu15 on 2017/5/23.
  */
object Deal extends DefaultJsonProtocol{
  //implicit val _format = jsonFormat11(apply)
}

case class Deal(itenID:String,dealID:String){

}

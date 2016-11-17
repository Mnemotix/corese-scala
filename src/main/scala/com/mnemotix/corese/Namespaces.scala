package com.mnemotix.corese

import com.typesafe.config.{Config, ConfigFactory}
import play.api.libs.json.{Json, _}

import scala.collection.JavaConverters._
/**
  * Created by Nicolas Delaforge on 10/03/2016.
  */

object Namespaces{

  lazy val values:List[Namespace] = NamespaceConfig.entries

  lazy val indexByPrefix:Map[String,String] = values.map{value => (value.prefix -> value.value)}.toMap

  lazy val indexByNamespace:Map[String,String] = values.map{value => (value.value -> value.prefix)}.toMap

  def getNamespace(prefix:String):Option[String] = indexByPrefix.get(prefix)

  def getPrefix(namespace:String):Option[String] = indexByNamespace.get(namespace)
}

case class Namespace(prefix:String, value:String)

object Namespace {
  implicit lazy val format:Format[Namespace]= Json.format[Namespace]
}

object NamespaceConfig {
  lazy val conf: Config = Option(ConfigFactory.load().getConfig("namespaces")).getOrElse(ConfigFactory.empty())
  lazy val entries: List[Namespace] = Option(conf.getConfigList("entries").asScala.map{obj:Config =>
    Namespace(obj.getString("prefix"),obj.getString("value"))
  }.toList).getOrElse(List.empty)
}

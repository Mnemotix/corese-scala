package com.mnemotix.corese

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._
/**
  * Created by Nicolas Delaforge on 25/10/2016.
  */
object KGramConfig {
  lazy val conf: Config = Option(ConfigFactory.load().getConfig("kgram")).getOrElse(ConfigFactory.empty())
  lazy val withEntailment: Boolean = Option(conf.getBoolean("entailment.active")).getOrElse(true)
  lazy val entailmentBase: String = Option(conf.getString("entailment.base")).getOrElse("http://ns.mnemotix.com/")
  lazy val defaultGraph: String = Option(conf.getString("default.graph")).getOrElse("http://ns.mnemotix.com/")
  lazy val extFilters:List[String] = Option(conf.getStringList("ext.filters").asScala.toList).getOrElse(List.empty[String])
  lazy val modelsDirs:List[String] = Option(conf.getStringList("models.dirs").asScala.toList).getOrElse(List.empty[String])
  lazy val dataDirs:List[String] = Option(conf.getStringList("data.dirs").asScala.toList).getOrElse(List.empty[String])
}

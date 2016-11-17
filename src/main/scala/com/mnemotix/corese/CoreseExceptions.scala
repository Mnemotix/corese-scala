package com.mnemotix.corese

/**
  * Created by Nicolas Delaforge on 14/04/2016.
  */
case class ReasonerInitException(message:String, cause:Throwable) extends Exception(message, cause){
  def this(message:String) = this(message, null)
}

case class InvalidConfigurationException(message:String, cause:Throwable) extends Exception(message, cause){
  def this(message:String) = this(message, null)
}

case class URIAlreadyInUseException(message:String, cause:Throwable) extends Exception(message, cause){
  def this(message:String) = this(message, null)
}

case class KGramLoadException(message:String, cause:Throwable) extends Exception(message, cause){
  def this(message:String) = this(message, null)
}

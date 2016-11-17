package com.mnemotix.corese

import java.io.{File, FileFilter, FileInputStream, InputStream}

import fr.inria.edelweiss.kgram.core.Mappings
import fr.inria.edelweiss.kgraph.api.GraphListener
import fr.inria.edelweiss.kgraph.core.Graph
import fr.inria.edelweiss.kgraph.query.QueryProcess
import fr.inria.edelweiss.kgtool.load.{Load, LoadException}
import scala.collection.JavaConverters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Nicolas Delaforge on 16/11/2016.
  */
class KGram {

  lazy val withEntailment: Boolean = KGramConfig.withEntailment
  lazy val entailmentBase: String = KGramConfig.entailmentBase
  lazy val defaultGraph: String = KGramConfig.defaultGraph
  lazy val extFilters: List[String] = KGramConfig.extFilters
  lazy val modelsDirs: List[String] = KGramConfig.modelsDirs
  lazy val dataDirs: List[String] = KGramConfig.dataDirs

  val RDFXML_FORMAT: Int = 0
  val RDFA_FORMAT: Int = 1
  val TURTLE_FORMAT: Int = 2
  val NT_FORMAT: Int = 3
  val JSONLD_FORMAT: Int = 4
  val RULE_FORMAT: Int = 5
  val QUERY_FORMAT: Int = 6
  val UNDEF_FORMAT: Int = 7
  val TRIG_FORMAT: Int = 8
  val NQUADS_FORMAT: Int = 9

  lazy val graph: Graph = Graph.create(withEntailment)
  lazy val loader: Load = Load.create(graph)

  def addListener(listener: GraphListener) = graph.addListener(listener)

  @throws(classOf[LoadException])
  @throws(classOf[InvalidConfigurationException])
  def load():Unit = {
    modelsDirs.foreach{ f:String=> load(new File(f), defaultGraph) }
    dataDirs.foreach{ f:String=> load(new File(f), defaultGraph) }
  }

  @throws(classOf[LoadException])
  @throws(classOf[InvalidConfigurationException])
  def load(filepath: String, source: String):Unit = load(new File(filepath), source)

  @throws(classOf[LoadException])
  @throws(classOf[InvalidConfigurationException])
  def load(files: Seq[File], source: String):Unit = files.foreach(load(_, source))

  @throws(classOf[LoadException])
  @throws(classOf[InvalidConfigurationException])
  def load(file: File, source: String):Unit = {
    if (file.exists) {
      if (file.isDirectory) {
        println(Console.CYAN + s"Scanning dir : ${file.getAbsolutePath}" + Console.RESET)
        file.listFiles(new FileFilter {
          override def accept(f: File): Boolean = {
            val okFilter:Seq[Boolean] = extFilters.map(f.getName.endsWith(_)) ++ Seq(f.isDirectory)
            okFilter.foldLeft(false)(_||_)
          }
        }).foreach{f=>
          load(f, source)
        }
      } else {
        println(Console.BLUE + s"Loading file : ${file.getAbsolutePath}" + Console.RESET)
        load(new FileInputStream(file), source)
//        loader.loadWE(file.getAbsolutePath, source, RDFXML_FORMAT)
      }
    }
    else throw new InvalidConfigurationException(s"The path you are trying to load into KGram was not found : ${file.getAbsolutePath}", null)
  }

  @throws(classOf[LoadException])
  def load(stream: InputStream, source: String):Unit = {
    try {
      loader.load(stream, source, source, entailmentBase, RDFXML_FORMAT)
    } catch {
      case t:Throwable => throw new KGramLoadException("Unable to load data", t)
    }
  }

  def executeSync(query: String): Mappings = {
    val exec: QueryProcess = QueryProcess.create(graph)
    exec.query(query)
  }

  def execute(query: String): Future[Mappings] = {
    Future {
      val exec: QueryProcess = QueryProcess.create(graph)
      exec.query(query)
    }
  }

  def clear() = {
    graph.clearDefault()
  }
  def shutdown() = clear()

  def countNodes = graph.getAllNodes.iterator().asScala.size

//  def countEdges = graph.getAllEdges().iterator().asScala.size
}

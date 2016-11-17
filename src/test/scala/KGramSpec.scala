import com.mnemotix.corese.KGram
import fr.inria.acacia.corese.api.IDatatype
import fr.inria.edelweiss.kgram.core.Mappings
import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class KGramSpec extends FlatSpec with Matchers {

  behavior of "KGram"

  var kgram:KGram = new KGram

  def countSchemes():Int = {
    val queryStr: String =
      s"""PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
          |SELECT DISTINCT (count(DISTINCT ?scheme) as ?c)
          |WHERE {
          |  ?scheme a skos:ConceptScheme
          |}""".stripMargin
    val mappings: Mappings = kgram.executeSync(queryStr)
    mappings.get(0).getValue("?c").asInstanceOf[IDatatype].intValue()
  }

  def countConcepts():Int = {
    val queryStr: String =
      s"""PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
          |SELECT DISTINCT (count(DISTINCT ?concept) as ?c)
          |WHERE {
          |  ?concept a skos:Concept
          |}""".stripMargin
    val mappings: Mappings = kgram.executeSync(queryStr)
    mappings.get(0).getValue("?c").asInstanceOf[IDatatype].intValue()
  }

  it should "initialize without error" in {
    kgram.modelsDirs.length shouldBe > (0)
    kgram.dataDirs.length shouldBe > (0)
  }

  it should "clear" in {
    kgram.clear()
    kgram.countNodes should equal (0)
  }

  it should "load data" in {
    kgram.load()
    println(s"# KGram Nodes : ${kgram.countNodes}")
    kgram.countNodes shouldBe > (0)
  }

  it should "execute blocking SPARQL query" in {
    val c1 = countConcepts()
    println(s"# Concepts : $c1")
    c1 shouldBe > (0)

    val c2 = countSchemes()
    println(s"# Schemes : $c2")
    c2 shouldBe > (0)
  }

  it should "execute non blocking SPARQL query" in {

    val queryStr: String =
      s"""PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
          |SELECT DISTINCT (count(DISTINCT ?concept) as ?c)
          |WHERE {
          |  ?concept a skos:Concept
          |}""".stripMargin

    val futureMappings: Future[Mappings] = kgram.execute(queryStr)
    val res = Await.result(futureMappings, 30.seconds)
    val c = res.get(0).getValue("?c").asInstanceOf[IDatatype].intValue()
    c shouldBe > (0)
  }
}

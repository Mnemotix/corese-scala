import com.mnemotix.corese.KGram
import fr.inria.acacia.corese.api.IDatatype
import fr.inria.acacia.corese.exceptions.EngineException
import fr.inria.edelweiss.kgram.core.Mappings
import org.scalatest._

class KGramSpec extends FlatSpec with Matchers {

  behavior of "KGram"

  var kgram:KGram = new KGram

  @throws(classOf[EngineException])
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

  @throws(classOf[EngineException])
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

  @throws(classOf[EngineException])
  def countConceptsByLang(lang: String):Int = {
    val queryStr: String =
      s"""PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
          |SELECT DISTINCT (count(DISTINCT ?concept) as ?c)
          |WHERE {
          |  ?concept a skos:Concept .
          |  ?concept ?r ?label .
          |  FILTER(?r = skos:prefLabel) .
          |  FILTER(LANG(?label) = '$lang')
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

  it should "execute SPARQL query" in {
    val c1 = countConcepts()
    println(s"# Concepts : $c1")
    c1 shouldBe > (0)

    val c2 = countSchemes()
    println(s"# Schemes : $c2")
    c2 shouldBe > (0)
  }
}

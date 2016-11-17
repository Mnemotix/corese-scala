import com.mnemotix.corese.KGram
import org.scalatest._

class KGramSpec extends FlatSpec with Matchers {

  behavior of "KGram"

  var kgram:KGram = new KGram

  it should "initialize without error" in {
    kgram.modelsDirs.length shouldBe > (0)
    kgram.dataDirs.length shouldBe > (0)
  }

  it should "load one file" in {
    kgram.load("/Users/nico/Dev/data/foaf", kgram.defaultGraph)
    kgram.load("/Users/nico/Dev/data/skos", kgram.defaultGraph)
    kgram.countNodes shouldBe > (0)
  }

  it should "clear" in {
    kgram.clear()
    kgram.countNodes should equal (0)
  }

  it should "load data" in {
    kgram.load()
    println(kgram.countNodes)
    kgram.countNodes shouldBe > (0)
  }
}

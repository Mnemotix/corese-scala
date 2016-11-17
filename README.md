# Scala Wrapper for Corese KGRAM

Corese-Scala is a scala wrapper for the [Corese/KGRAM](https://github.com/Wimmics/corese) project.
It also provides some useful features like :
* a configuration file
* a namespace declaration helper

## Usage

You must add it as a dependency to your SBT project

```scala
resolvers ++= Seq(
  "MNX Nexus (releases)" at "https://repo.mnemotix.com/repository/maven-releases/",
  "MNX Nexus (snapshots)" at "https://repo.mnemotix.com/repository/maven-snapshots/"
)

libraryDependencies += "com.mnemotix" %% "corese-scala" % "1.0-SNAPSHOT"

```

## Configuration
`corese-scala` uses the Typesafe Config library to load configuration.
So it is waiting for some configuration sections in an `application.conf` file located into the classpath.

```apacheconf
// Reasoner configuration

kgram {
  entailment.active = true
  entailment.base = "http://ns.example.com/"
  default.graph = "default-ng"
  ext.filters = [
    ".owl",
    ".rdfs",
    ".rdf",
    ".nt",
    ".xml"
  ]
  models.dirs = [
    "path/to/your/models"
  ]
  data.dirs = [
    "path/to/your/data"
  ]
}

// List here the namespaces you want to be pre-loaded into Corese and that will be natively available to SPARQL queries
namespaces {
  entries = [
    {"prefix" : "dc", "value" : "http://purl.org/dc/elements/1.1/"},
    {"prefix" : "dcterms", "value" : "http://purl.org/dc/terms/"},
    {"prefix" : "dctypes", "value" : "http://purl.org/dc/dcmitype/"},
    {"prefix" : "foaf", "value" : "http://xmlns.com/foaf/0.1/"},
    {"prefix" : "geo", "value" : "http://www.w3.org/2003/01/geo/wgs84_pos"},
    {"prefix" : "ldp", "value" : "http://www.w3.org/ns/ldp#"},
    {"prefix" : "oa", "value" : "http://www.w3.org/ns/oa#"},
    {"prefix" : "owl", "value" : "http://www.w3.org/2002/07/owl#"},
    {"prefix" : "prov", "value" : "http://www.w3.org/ns/prov#"},
    {"prefix" : "rdf", "value" : "http://www.w3.org/1999/02/22-rdf-syntax-ns#"},
    {"prefix" : "rdfs", "value" : "http://www.w3.org/2000/01/rdf-schema#"},
    {"prefix" : "rr", "value" : "http://www.w3.org/ns/r2rml#"},
    {"prefix" : "skos", "value" : "http://www.w3.org/2004/02/skos/core#"},
    {"prefix" : "xsd", "value" : "http://www.w3.org/2001/XMLSchema-datatypes"}
  ]
}

```

## API

The corese-scala API is pretty simple.
Once the configuration is properly set, you just have to instanciate the reasoner and to ask it to load files.

```scala
    import com.mnemotix.corese.KGram
    import fr.inria.acacia.corese.api.IDatatype
    import fr.inria.edelweiss.kgram.core.Mappings
    
    import scala.concurrent.duration._
    import scala.concurrent.{Await, Future}
    import scala.util.{Failure, Success}

    val kgram:KGram = new KGram
    kgram.load() // loads files from conf
    kgram.clear() // reset the graph dropping all nodes and edges
    
    val query: String = s"""PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
          |SELECT DISTINCT (count(DISTINCT ?concept) as ?c)
          |WHERE {
          |  ?concept a skos:Concept
          |}""".stripMargin
    
    val mappings: Mappings = kgram.executeSync(query) // execute a blocking SPARQL query
    mappings.get(0).getValue("?c").asInstanceOf[IDatatype].intValue()

    // execute a non blocking SPARQL query
    val futureMappings: Future[Mappings] = kgram.execute(query)
    futureMappings onComplete {
      case Success(m) => {
        m.get(0).getValue("?c").asInstanceOf[IDatatype].intValue()
      }
      case Failure(e) => e.printStackTrace
    }
 
```
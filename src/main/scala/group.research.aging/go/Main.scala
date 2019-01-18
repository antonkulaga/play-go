package group.research.aging.go

import java.io.{File => JFile}

import better.files._
import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.repository.sail.{SailRepository, SailRepositoryConnection}
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.sail.memory.MemoryStore
import scala.collection.compat._

object Main extends scala.App  {
  val explorer = ExplorerGO.fileToMemory("/data/indexes/GO/go-plus.owl")
  val baseURI = "http://purl.obolibrary.org/obo/go.owl"
  //val root = f.go("0008152")
  val root = explorer.go("0008150")


}

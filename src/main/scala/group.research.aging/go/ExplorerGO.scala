package group.research.aging.go
import group.research.aging.go._
import java.io.{File => JFile}

import better.files._
import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.vocabulary.RDFS
import org.eclipse.rdf4j.repository.sail.{SailRepository, SailRepositoryConnection}
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.sail.memory.MemoryStore

import scala.collection.compat._

object ExplorerGO {
  def fileToMemory(path: String, baseURI: String = "http://purl.obolibrary.org/obo/go.owl#"): ExplorerGO = {
    val repo = new SailRepository(new MemoryStore())
    repo.initialize()
    val file = new JFile(path)
    val con: SailRepositoryConnection = repo.getConnection()
    con.add(file, baseURI, RDFFormat.RDFXML)
    ExplorerGO(con)
  }
}

case class ExplorerGO(con: SailRepositoryConnection) {

  type Row = (String, String, List[String])
  type Rows = Seq[Row] //parent, label, children

  def go(str: String): IRI = con.getValueFactory.go(str)

  /**
    * Format:
    *  go, label, all children in a list
    * @param node
    * @return
    */
  def makeRowRecursive(node: IRI): (String, String, List[String]) = {
    val go = node.goString
    val label = con.label(node)
    val children = con.subClassOf(node, true)
    (go, label , children.map(v=>v.goString)) //.mkString(";")
  }

  def makeRow(node: IRI, depth: Int): (String, String, List[String]) = {
    val go = node.goString
    val label = con.label(node)
    val children = con.subClassOf(node, depth)
    (go, label , children.map(v=>v.goString)) //.mkString(";")
  }

  def prepareGroups(go: String, depth: Int): List[(String, String, List[String])] = {
    val node = con.getValueFactory.go(go)
    val children: Seq[IRI] = con.subClassOf(node, depth)
    children.iterator.map(v=>makeRowRecursive(v)).toList
  }


  def prepareGroups(go: String): List[(String, String, List[String])] = {
    val node = con.getValueFactory.go(go)
    val children: Seq[IRI] = con.subClassOf(node, false)
    children.iterator.map(v=>makeRowRecursive(v)).toList
  }


  def writeGOes(go: String, where: String, sep: String = ";"): Unit = {
    val groups = prepareGroups(go)
    val f = File(where)
    f.createIfNotExists()
    for( (g, l, children) <-groups ) f.appendLine(g + "\t" + l + "\t" + children.size.toString + "\t" + children.mkString(sep))
  }

  /*
  def traverseFlat(startGO: String, depth: Int): Rows = traverseFlat(Seq(makeRow(go(startGO), 1)), depth)

  def traverseFlat(rows: Rows, depth: Int): Rows = if(depth<=0) rows else {
    val updated: Rows = rows.flatMap{ case (p, _, list) => list.map(ch=>makeRow(go(ch), 1))}
    traverseFlat(updated, depth -1)
  }
  */


  def writeGOesFlat(startGO: String, where: String, depth: Int): File =   {
    val groups: Rows = prepareGroups(startGO, depth)
    flatWrite(where, groups)
  }

  protected def flatWrite(where: String, groups: Rows ): File = {
    val f = File(where)
    f.createIfNotExists()
    for{
      (g, l, children) <-groups
      child <- children
    } f.appendLine(g + "\t" + l  + "\t" + children.size.toString+ "\t" + child)
    f
  }

  def writeGOesFlat(go: String, where: String): File = {
    val groups: Rows = prepareGroups(go)
    flatWrite(where, groups)
  }

}

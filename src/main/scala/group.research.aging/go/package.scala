package group.research.aging

import org.eclipse.rdf4j.repository.{Repository, RepositoryConnection, RepositoryResult}
import java.io.File
import java.net.URL

import org.eclipse.rdf4j.model.vocabulary.RDFS
import org.eclipse.rdf4j.model.{IRI, Resource, Statement, ValueFactory}
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.sail.{SailRepository, SailRepositoryConnection}
import org.eclipse.rdf4j.sail.memory.MemoryStore
import org.eclipse.rdf4j.rio.RDFFormat
import scala.collection.compat._

package object go {

  implicit class ValueFactoryExt(f: ValueFactory) {
    def go(str: String): IRI = f.createIRI(s"http://purl.obolibrary.org/obo/GO_${str.replace("GO:", "")}")
  }

  implicit class RepositoryResultExt(result: RepositoryResult[Statement]) extends Iterator[Statement]{
    override def hasNext: Boolean = result.hasNext

    override def next(): Statement = result.next()
  }

  implicit class IRIExt(res: Resource) {
    def goString: String = res.stringValue().replace("http://purl.obolibrary.org/obo/GO_", "GO:")

  }

  implicit class ConnectionExt(con: SailRepositoryConnection) {
    def bySub(sub: Resource): Iterator[Statement] = con.getStatements(sub, null, null)
    def byObj(obj: Resource): Iterator[Statement] = con.getStatements(null, null, obj).filter(_.getSubject.isInstanceOf[IRI])
    def byPred(pred: IRI): Iterator[Statement] = con.getStatements(null.asInstanceOf[IRI], pred, null).filter(_.getSubject.isInstanceOf[IRI])
    def byPredObj(pred: IRI, obj: Resource): Iterator[Statement] = con.getStatements(null, pred, obj)
    def bySubPred(sub: Resource, pred: IRI): Iterator[Statement] = con.getStatements(sub, pred, null)

    def label(sub: Resource): String = bySubPred(sub, RDFS.LABEL).take(1).toList.map(s=>s.getObject.stringValue()).head
    def labels(subs: Seq[Resource]): Seq[String] = subs.map(label)
    def labeled(subs: Seq[Resource]): Seq[(Resource, String)] = subs.map(go=> go ->label(go))


    def subClassOf(iri: IRI): List[IRI] = subClassOf(iri, true)
    def subClassOf(iri: IRI, recursive: Boolean): List[IRI] = {
      val children = byPredObj(RDFS.SUBCLASSOF, iri).collect{ case st if st.getSubject.isInstanceOf[IRI] && st.getSubject != iri => st.getSubject.asInstanceOf[IRI]}.toList
      if(recursive) iri::children.flatMap(s=>subClassOf(s, recursive)) else children
    }

    def subClassOf(iri: IRI, depth: Int): List[IRI] = traverse(List(iri), RDFS.SUBCLASSOF, depth)

    def traverse(nodes: List[IRI], property: IRI, depth: Int): List[IRI] = if(depth<=0) nodes else {
      traverse(nodes.flatMap{ iri =>
        byPredObj(property, iri).collect{ case st if st.getSubject.isInstanceOf[IRI] && st.getSubject != iri => st.getSubject.asInstanceOf[IRI]}.toList
      }, property, depth -1)
    }

  }


}

import ammonite.ops._
import coursier.MavenRepository
interp.repositories() ++= Seq(MavenRepository("https://dl.bintray.com/comp-bio-aging/main"))
import $ivy.`group.aging-research::play-go:0.0.3.3`

import group.research.aging.go._
val explorer = ExplorerGO.fileToMemory("/data/indexes/GO/go-plus.owl", "http://purl.obolibrary.org/obo/go.owl#")
val root = explorer.go("0008150")

//explorer.writeGOesFlat("0008150", "/data/results/gray-whale/other/pro_2.tsv", 2)
//explorer.writeGOesFlat("0008150", "/data/results/gray-whale/other/pro_1.tsv", 1)

//explorer.writeGOesFlat("0005575", "/data/results/gray-whale/other/comp_2.tsv", 2)
explorer.writeGOesFlat("0005575", "/data/results/gray-whale/other/components_2.tsv", 2)
explorer.writeGOesFlat("0003674", "/data/results/gray-whale/other/functions_2.tsv", 2)
explorer.writeGOesFlat("0005575", "/data/results/gray-whale/other/components_1.tsv", 1)
explorer.writeGOesFlat("0003674", "/data/results/gray-whale/other/functions_1.tsv", 1)
//
//explorer.writeGOesFlat("0003674", "/data/results/gray-whale/other/fun_1.tsv", 1)

explorer.con.subClassOf(explorer.go("0005575"), 1).toList
//explorer.writeGOesFlat("0008150", "/data/results/gray-whale/other/pro_1.tsv")


//explorer.writeGOesFlat("0008150", "/data/results/gray-whale/other/pro_one.tsv")
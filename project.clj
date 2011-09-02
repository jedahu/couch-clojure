(defproject
  me.panzoo/couch-clojure "0.1.0-SNAPSHOT"

  :description "Clojure views for CouchDb and BigCouch (Cloudant)"

  :dependencies
  [[org.clojure/clojure "1.2.1"]
   [org.apache.lucene/lucene-core "3.0.2"]
   [org.json/json "20090211"]]

  :source-path "clj-src"
  :java-source-path "src"
  
  :aot [me.panzoo.couch-clojure.ClojureView])

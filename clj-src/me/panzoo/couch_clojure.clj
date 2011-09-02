(ns me.panzoo.couch-clojure
  (:use
    me.panzoo.couch-clojure.ClojureView))

(defmacro defcouchview
  "map => (fn [map & [conf]] ...)
  reduce => (fn [seq & [conf]] ...)
  rereduce => (fn [seq & [conf]] ...)
  log => (fn [msg & [conf]] ...)
  
  Creates a java class that extends me.panzoo.couch-clojure.ClojureView
  which itself extends com.cloudant.couchdbjavaserver.JavaView.

  The fully qualified name of the class will be
  (str (ns-name *ns*) \".\" name).
  
  Options should be a set of key/value pairs. All but :map are optional.
  If :reduce is supplied, :rereduce must be supplied too.
  
  Each function is given an optional argument. This argument (conf) is the
  result of running read-string on the argument passed to JavaView.Configure()
  (the \"configure\": field in the design document).
  
  :map function [doc & [conf]]
  
  A function taking a couchdb document in the form of a closure map and
  returning a sequence of key/value pairs, e.g., [[key value] ...]. If there
  are no map results the function must return nil.
  
  :reduce function [seq & [conf]]
  
  A function taking a sequence of the form [[[key id] value] ...] and
  returning a single value.
  
  :rereduce function [seq & [conf]]
  
  A function taking a sequence of values and returning a single value.

  :log function [msg & [conf]]

  A function taking a string and returning nothing. To log to the couchdb
  log file, write a json array with first value \"log\" and second value
  msg, e.g., (pr (str \"[\\\"log\\\", \\\"\" msg \"\\\"]\"))."
  [name & {:keys [map reduce rereduce log]}]
  `(gen-class
     :name ~(symbol (str (ns-name *ns*) "." name))
     :extends me.panzoo.couch-clojure.ClojureView
     :post-init -setup
     :prefix ~name)
  `(defn ~(symbol (str name "-setup")) [this#]
     (.setup this#
       {:mapfn ~map
        :reducefn ~reduce
        :rereducefn ~rereduce
        :logfn ~log})))

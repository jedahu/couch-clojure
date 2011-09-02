(ns me.panzoo.couch-clojure.ClojureView
  (:use
    me.panzoo.couch-clojure.json)
  (:gen-class
    :implements [com.cloudant.couchdbjavaserver.JavaView]
    :state state
    :init init
    :methods [[setup [clojure.lang.IPersistentMap] void]]
    :prefix m-))

(defn m-init []
  [[] (atom nil)])

(defn m-setup [this & opts]
  (swap! (.state this) (constantly opts)))

(defn m-MapDoc [this doc]
  (let [{:keys [mapfn conf]} @(.state this)] 
    (when-not mapfn (throw (Exception. "Map function not defined.")))
    (jarray<- (or (mapfn (map<- doc) conf) [[]]))))

(defn m-Reduce [this results]
  (let [{:keys [reducefn conf]} @(.state this)]
    (when-not reducefn (throw (Exception. "Reduce function not defined.")))
    (jarray<- [(reducefn (seq<- results) conf)])))

(defn m-ReReduce [this results]
  (let [{:keys [rereducefn conf]} @(.state this)]
    (when-not rereducefn (throw (Exception. "Rereduce function not defined.")))
    (jarray<- [(rereducefn (seq<- results) conf)])))

(defn m-Configure [this string]
  (let [s @(.state this)]
    (swap! (.state this) (assoc s :conf (read-string string)))))

(defn m-Log [this msg]
  (let [{:keys [logfn conf]} @(.state this)]
    ((or logfn (constantly nil)) msg conf)))

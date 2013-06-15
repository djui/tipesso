(ns tipesso.core
  (:require [clojure.data.json :as json]
            [tipesso.provider.github :as github]
            [tipesso.provider.clojars :as clojars]
            [tipesso.provider.leiningen :as leiningen])
  (:use [clojure.string :only [trim]]))


(def ^:const INF Double/POSITIVE_INFINITY)

(defn- default-providers
  "Return a list of default providers. The list is ordered by detection
  priority."
  [] [github/responsible?
      clojars/responsible?
      leiningen/responsible?])

(defn- sanitize
  "Assert origin is an usable URI and clean it up."
  [origin] (trim origin))

(defn- fixpoint
  "Main part of the fixpoint engine. Take data with at least an origin URI and
  create a tip-tree, a tree structure of tippable items. Options is a map
  containing API providers under :provider and the max iteration count :ttl."
  [data-in & [{:keys [providers ttl] :or {ttl INF} :as opts}]]
  (conj
   (when (pos? ttl)
     (when-let [res (some #(% data-in) providers)]
       (let [data-out (if (sequential? res) res [res])
             opts (assoc opts :ttl (dec ttl))]
         (map #(fixpoint % opts) data-out))))
   data-in))

(defn- tippables
  "Traverse tip-tree and filter tippable items.
  {:a nil :tippable {:foo 1} :b [{:c nil :tippable {:bar 2}}]}
  => [{:foo 1} {:bar 2}]"
  [tip-tree]
  (let [nodes (tree-seq sequential? identity tip-tree)]
    (flatten (keep :tippables nodes))))

(defn discover
  "Take URI of a subject (project, dependency, etc.) and list the subject's
   tippables."
  ([str] ;; For testing
     (discover str (default-providers)))
  ([str providers]
     (let [uri (sanitize str)
           data {:origin uri}
           tip-tree (fixpoint data {:providers providers})
           tippables (tippables tip-tree)]
       (json/write-str tippables))))

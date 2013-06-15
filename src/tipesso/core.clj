(ns tipesso.core
  (:require [clojure.data.json :as json]
            [tipesso.provider.github :as github]
            [tipesso.provider.clojars :as clojars]
            [tipesso.provider.leiningen :as leiningen])
  (:use [clojure.string :only [trim]]))


(def ^:const INF Double/POSITIVE_INFINITY)

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
  "Traverse tip-tree and filter tippables."
  [tip-tree]
  (->> tip-tree (tree-seq sequential? identity) (keep :tippables) flatten))

(defn discover
  "Take URI of a subject (project, dependency, etc.) and list the subject's
   tippables."
  ([uri] (discover uri [github/responsible?
                        leiningen/responsible?
                        clojars/responsible?]))
  ([uri providers] ;; For testing
     (let [data {:origin (trim uri)}
           tip-tree (fixpoint data {:providers providers})
           tippables (tippables tip-tree)]
       (json/write-str tippables))))

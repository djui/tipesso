(ns tipesso.core
  (:require [clojure.data.json :as json]
            [tipesso.provider.github :as github]
            [tipesso.provider.clojars :as clojars]
            [tipesso.provider.leiningen :as leiningen]
            [tipesso.provider.gittip :as gittip])
  (:use [clojure.contrib.djui.coll :only [to-sequence tree-seq+]]
        [clojure.string :only [trim]]))


(defn- extract
  "Traverse tip-tree and filter tippables."
  [tip-tree]
  (flatten (keep :tippable tip-tree)))

(defn- discover
  "Main part of the fixpoint engine. Take root data with at least an origin URI
  and create a tip-tree, a tree structure of tippable items. Options is a map
  containing API :providers and the max iteration :limit.
  tree-seq+ traverses the data structure as a tree creating branches and nodes
  as it traverses.
  Providers will be asked in given order if they are responsible for the
  provided input and must return a map or list of maps as output if they are
  responsible or a logical false if they are not. The iteration ends when all
  provider respond with a logical false.
  The iteration of each branch runs sequential (some-fn) but all branches are
  traversed in parallel, as branches are independant from each other and most
  provider calls will be slow web api calls."
  [root providers & [opts]]
  (letfn [(first-responder [in-data provider]
            (let [;;_ (prn '<< (:name (meta provider)) in-data) ; debug only!
                  out-data (to-sequence (provider in-data))
                  ;;_ (prn '>> (:name (meta provider)) out-data) ; debug only!
                  ]
              out-data))
          (branch? [node] (not (nil? node)))
          (children [node] (some #(first-responder node %) providers))]
    (reverse (tree-seq+ branch? children root opts))))

(defn tippables
  "Take URI of a subject (project, dependency, etc.) and list the subject's
   tippables."
  ([uri]
     (tippables uri [(with-meta github/handler    {:name "github"})
                     (with-meta leiningen/handler {:name "leiningen"})
                     (with-meta clojars/handler   {:name "clojars"})
                     (with-meta gittip/handler    {:name "gittip"})]))
  ([uri providers] ;; For testing
     (let [data {:url (trim uri)}
           tip-tree (discover data providers {:limit 6 :parallel? false})
           tippables (extract tip-tree)]
       (json/write-str tippables))))

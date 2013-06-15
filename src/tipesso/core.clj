(ns tipesso.discoverer
  (:require [clojure.data.json :as json]
            [tipesso.hosters.github :as github]
            [tipesso.hosters.clojars :as clojars]
            [tipesso.builders.leiningen :as leiningen])
  (:use [clojure.string :only [trim]]))


(defn default-providers
  "Return a map with default providers. The list is ordered by detection
  priority."
  [] {:hosters [github/project clojars/project]
      :builders [leiningen/dependencies]
      :brokers []})

(defn sanitize
  "Assert origin is an usable URI; and clean it up."
  [origin] (trim origin))

(defn create-tiptree
  "Take an origin URI and map of API provider and create a tiptree, a tree
  structure of tippable items."
  ([origin providers]
     (create-tiptree origin providers 1))
  ([origin providers depth]
     (let [hosters (:hosters providers)
           builders (:builders providers)
           ;; 1. Iteration
           project (some #(% origin) hosters)
           ;; 2. Iteration
           dependencies (some #(% project) builders)
           ;; 3. Iteration
           dependency-projects (map (fn [dep] (some #(% (:origin dep)) hosters)) dependencies)
           ;; Merge all together
           tiptree (assoc-in project [:dependencies] dependency-projects)]
       tiptree)))

(defn filter-tippables
  "Filter out non-tippable items given the :$tippable marker."
  [tiptree]
  (let [author [{:location :github
                 :username (get-in tiptree [:tippables 0 :username])
                 :reason "Project author"}]
        deps (map (fn [dep] {:location :clojars
                             :username (:uri dep)
                             :reason "Dependency author"}) (:dependencies tiptree))]
    (concat author deps)))

(defn discover
  "Takes the origin URI of a subject (project, ...) and a map of API providers
  and list the subject's tippable items."
  ([origin]
     (discover origin (default-providers)))
  ([origin providers]
     (-> origin
         sanitize
         (create-tiptree providers 1)
         filter-tippables
         json/write-str)))

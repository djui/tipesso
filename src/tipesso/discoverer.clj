(ns tipesso.discoverer
  (:require [clojure.data.json :as json]
            (tipesso.hosters [github :as github]))
  (:use [clojure.string :only [trim]]))


;;; Builder ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Leiningen ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn leiningen-dependencies [project]
  (let [asset-result ((:assets project) project "project.clj")
        content (if-let [raw-content (:content asset-result)]
                  (String. raw-content)
                  "[]")
        data (binding [*read-eval* false] (read-string content))
        map (apply hash-map (drop 3 data))
        dependencies (:dependencies map)]
    dependencies))

;;; Main ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn default-providers
  "Return a map with default providers. The list is ordered by detection
  priority."
  [] {:hosters [github/project]
      :builders [leiningen-dependencies]
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
     (let [project (some #(% origin) (:hosters providers))
           dependencies (some #(% project) (:builders providers))
           tiptree (assoc-in project [:dependencies] dependencies)]
       tiptree)))

(defn filter-tippables
  "Filter out non-tippable items given the :$tippable marker."
  [tiptree] (:dependencies tiptree))

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

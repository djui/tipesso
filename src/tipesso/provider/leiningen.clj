(ns tipesso.provider.leiningen
  (:use [clojure.contrib.djui.str :only [safe-parse]]))


(defn- transform
  "Construct dependency data structure."
  [[id version]]
  {:provider :clojars
   :id id
   :version version})

(defn- responsible?
  "If possible, i.e. responsible, extract the main build information."
  [data]
  (let [{:keys [language asset]} data]
    (when (= language :Clojure)
      (asset "project.clj"))))

(defn handler
  "Find and return dependencies given a typical leiningen project setup."
  [data]
  (some->> data
           responsible?
           safe-parse
           (drop 3)
           (apply hash-map)
           :dependencies
           (map transform)))

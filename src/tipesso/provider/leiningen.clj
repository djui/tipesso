(ns tipesso.provider.leiningen)


(defn- safe-parse
  "Parse a clojure term with evaluating anything."
  [str]
  (binding [*read-eval* false] (read-string str)))

(defn- prj->deps
  "Extract dependencies from project.clj file string."
  [src]
  (->> src safe-parse (drop 3) (apply hash-map) :dependencies))

(defn- asset-api
  "Return asset api given a project data structure."
  [project]
  (:assets project))

(defn extend-dep
  "Construct dependency data structure."
  [dependency]
  (let [id (first dependency)
        version (second dependency)
        origin (format "https://clojars.org/%s/versions/%s" id version)]
    {:id id
     :version version
     :origin origin}))

(defn dependencies
  "Find and return dependencies given a typical leiningen project setup."
  [project]
  (let [prj-file ((asset-api project) "project.clj")
        deps (prj->deps prj-file)]
    (map extend-dep deps)))

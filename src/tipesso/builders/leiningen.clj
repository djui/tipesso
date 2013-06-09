(ns tipesso.builders.leiningen)


(defn parse-clj-src 
  "Parse Clojure source code string."
  [str]
  (binding [*read-eval* false] (read-string str)))

(defn prj->deps
  "Extract dependencies from project.clj file string."
  [src]
  (->> src
      (parse-clj-src)
      (drop 3)
      (apply hash-map)
      (:dependencies)))

(defn asset-api
  "Return asset api given a project data structure."
  [project]
  (:assets project))

(defn dependencies [project]
  (let [asset (asset-api project)]
    (-> project
        (asset "project.clj")
        (prj->deps))))

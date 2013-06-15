(ns tipesso.provider.leiningen)


(defn- safe-parse
  "Parse a clojure term with evaluating anything."
  [str]
  (binding [*read-eval* false] (read-string str)))

(defn- prj->deps
  "Extract dependencies from project.clj file string."
  [src]
  (->> src safe-parse (drop 3) (apply hash-map) :dependencies))

(defn- extend-dep
  "Construct dependency data structure."
  [[id version]]
  (let [origin (format "https://clojars.org/%s/versions/%s" id version)]
    {:origin origin
     :id id
     :version version}))

(defn responsible?
  "Find and return dependencies given a typical leiningen project setup."
  [{:keys [assets] :as data}]
  #_(prn "leiningen" data)
  (when assets
    (->> "project.clj"
        assets
        prj->deps
        (map extend-dep))))

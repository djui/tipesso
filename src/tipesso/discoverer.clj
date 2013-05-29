(ns tipesso.discoverer
  (:require [tentacles.repos :as repos :only [specific-repo]]
            [clojure.data.json :as json])
  (:use [clojure.string :only [trim]]))

(defn builder-type [origin]
  :leiningen)

(defmulti deps-from-builder builder-type)

(defn deps-from-builder :leiningen
  "Builder handler for leiningen projects."
  [origin]
  (let [user (first ((origin :project) :authors))
        repo ((origin :project) :name)
        content (repos/contents user repo "project.clj")
        data    (binding [*read-eval* false] (read-string content))
        map     (apply hash-map (drop 3 data))]
    (map :dependencies)))

(defn language-type
  "..."
  [tiptree]
  (let [languages (get-in tiptree [:origin :project :languages])
        main-lang ((first languages) :name)]
    main-lang))

(defmulti identify-languages language-type)

(defmethod identify-languages :Clojure [tiptree]
  (assoc-in tiptree [:origin :project :languages 0] :builder {:name :leiningen}))

(defn project-type
  "Map an origin to a project type."
  [tiptree]
  :github)

(defmulti identify-project project-type)

(defmethod identify-project :github [tiptree]
  (let [project (repos/specific-repo user repo)
        languages (repos/languages user repo)
        langs (map (partial hash-map :name) (keys languages))]
    (assoc-in tiptree [:origin :project] {:name (project :name)
                                          :authors [((project :owner) :login)]
                                          :languages langs})))
(defn origin-type
  "Map an URI to an origin type."
  [tiptree]
  (let [uri (tiptree :source)]
    (cond (re-matches #"^https?://github.com/.+?/.+?(\.git|/.*)?$" uri) :github
          (re-matches #"^https?://.+?.github.(io|com)/.+?/?.*$" uri) :github
          :else nil)))

(defmulti identify-origin origin-type)

(defmethod identify-origin :github [tiptree]
  (let [[user repo]
        (if-let [[_ user repo _]
                 (re-matches #"^https?://github.com/(.+?)/(.+?)(\.git|/.*)?$" uri)]
          [user repo]
          (if-let [[ _ user _ repo]
                   (re-matches #"^https?://(.+?).github.(io|com)/(.+?)/?.*$" uri)]
            [user repo]))
        project (repos/specific-repo user repo)
    {:type :github, :uri (project :html_url)}))
  
(defmethod identify-origin nil [tiptree]
  {:type nil})

(defn get-config [builder, uri]
  (let [deps ("identify-depencies" config)]
    ))

(defn get-dependencies [config]
  (let [deps ("identify-depencies" config)]
    ))

(defn sanitize
  "Assert source is an usable URI. And clean it up."
  [src] (trim src))

(defn new-tiptree
  "Return an empty tiptree."
  [uri] {:source uri})

(defn create-tiptree
  "Take an URI and create a tiptree, a tree structure of tippable items."
  [src & _depth]
  (get-dependencies src)
  #_(-> src
      new-tiptree
      identify-origin
      identify-project
      identify-authors
      identify-langs
      identify-builder
      identify-config
      identify-deps))

(defn filter-tippables [tiptree]
  ;; TODO Filter out non-tippable items given the :$tippable marker.
  tiptree)

(defn discover
  "Take the URI of a subject (project, ...) and list its tippable items."
  [subject]
  (-> subject
      sanitize
      (create-tiptree 1)
      filter-tippables
      json/write-str))

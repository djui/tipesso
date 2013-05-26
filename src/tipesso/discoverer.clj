(ns tipesso.discoverer
  :require [clojure.string :only trim]
           [tentacles.repos :as repos :only specific-repo])


(defn builder-handler-leiningen 
  "Builder handler for leiningen projects."
  [origin]
  (let [user (first ((origin :project) :authors))
        repo ((origin :project) :name)
        content (repos/contents user repo "project.clj")
        data    (binding [*read-eval* false] (read-string content))
        map     (apply hash-map (drop 3 data))]
    (map :dependencies)))

(defn origin-handler-github
  "Origin handler for github projects."
  [uri]
  (let [[user repo]
        (if-let [[_ user repo _]
                 (re-matches #"^https?://github.com/(.+?)/(.+?)(\.git|/.*)?$" uri)]
          [user repo]
          (if-let [[ _ user _ repo]
                   (re-matches #"^https?://(.+?).github.(io|com)/(.+?)/?.*$" uri)]
            [user repo]))
        project (repos/specific-repo user repo)
        languages (repos/languages user repo)]
    {:type :github
     :uri (project :html_url)
     :project {:name (project :name)
               :authors [((project :owner) :login)]
               :languages languages}}))

(defn origin-type 
  "Map an URI to an origin type."
  [uri]
  (cond (re-matches #"^https?://github.com/.+?/.+?(\.git|/.*)?$" uri) :github
        (re-matches #"^https?://.+?.github.(io|com)/.+?/?.*$" uri) :github))

(defn resolve-project
  "Extract project metadata out of URI."
  [s]
  (let [uri (trim s)
        type (origin-type uri)]
    (case type
      :github (project-handler-github uri)
      ;; ...
      nil
      )))

(defn discover
  "list tipable dependencies of a project given its URI."
  [subject]
  (pr-str (pr-str (resolve-project subject)))
  
(comment
  {:source "http://github.com/djui/tipesso.git"
   :origin
   {:type :github
    :uri "https://github.com/djui/tipesso"
    :project
    {:name "tipesso"
     :authors
     [{:username "djui"
       :realname "Uwe Dauernheim"
       :uri      "http://github.com/djui"}
      {:username "onlyafly"
       :realname "Kevin Albrecht"
       :uri      "http://github.com/onlyafly"}]
     :languages
     [{:name :clojure
       :builder
       {:name :leiningen
        :config
        {:filename "project.clj"
         :dependencies
         [{:name    "org.clojure/clojure"
           :version "1.5.1"
           :origin  "http://..."}
          {:name    "compojure"
           :version "1.1.5"
           :origin  "http://..."}
          {:name    "hiccup"
           :version "1.0.3"
           :origin  "http://..."}
          {:name    "lein-ring"
           :version "0.8.3"
           :origin  "http://..."}]}}}]}}}
  )


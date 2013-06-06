(ns tipesso.discoverer
  (:require [tentacles.repos :as repos :only [specific-repo]]
            [clojure.data.json :as json])
  (:use [clojure.string :only [trim]]))


;;; Builder ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Leiningen ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn leinigen-dependencies [project]
  (let [content ((:assets project) "project.clj")
        data    (binding [*read-eval* false] (read-string content))
        map     (apply hash-map (drop 3 data))]
    map))

;;; Hoster ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Github ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn user-repo [origin]
  (if-let [[_ user repo _] (re-matches #"^https?://github.com/(.+?)/(.+?)(\.git|/.*)?$" origin)]
    [user repo]
    (if-let [[ _ user _ repo] (re-matches #"^https?://(.+?).github.(io|com)/(.+?)/?.*$" origin)]
      [user repo])))

(defn github-project [origin]
  (let [[user repo] (user-projectname origin)
        project (repos/specific-repo user repo)
        languages (keys (repos/languages user repo))]
    {:origin origin
     :hoster :github
     :name (:name project)
     :description (:description project)
     :type :github
     :uri (:html_url project)
     :authors [{:username (get-in project [:owner login])
                :realname (get-in project [:owner id])
                :uri (get-in project [:owner htm_url])}]
     :languages languages
     :dependencies nil
     :assets 'github-asset}))

(defn github-asset [project filename]
  (let [user (get-in project [:authors 0 :username])
        repo (:name project)]
    (repos/contents user repo filename)))

;;; Main ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn default-providers
  "Return a map with default providers. The list is ordered by detection
  priority."
  [] {:hosters [github]
      :builders [leiningen]
      :brokers []})

(defn sanitize
  "Assert origin is an usable URI; and clean it up."
  [origin] (trim origin))

(defn create-tiptree
  "Take an origin URI and map of API provider and create a tiptree, a tree
  structure of tippable items."
  ([origin providers] (create-tiptree origin providers 1))
  ([origin providers depth]
     (let [project (some #(%-project origin) (:hosters providers))
           dependencies (some #(%-dependencies project) (:builders providers))]
       (assoc-in project :dependencies dependencies))))

(defn filter-tippables
  "Filter out non-tippable items given the :$tippable marker."
  [tiptree] tiptree)

(defn discover
  "Takes the origin URI of a subject (project, ...) and a map of API providers
  and list the subject's tippable items."
  ([origin] (discover (default-providers)))
  ([origin providers]
     (-> origin
         sanitize
         (create-tiptree providers 1)
         filter-tippables
         json/write-str)))

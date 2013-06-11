(ns tipesso.hosters.github
  (:use environ.core)
  (:use [tentacles.core :as core :only [with-defaults]])
  (:use [tentacles.repos :as repos :only [contents, languages, specific-repo]]))

;;---------- Github communication

(defn- github-defaults
  "To maximize Github's rate-limit (from 60 to 5000 requests per hour) set the
  environment variable GITHUB_TOKEN to your Github OAuth token for this
  application."
  []
  {:oauth-token (env :github-token)})

(defn- github-contents [user repo filepath]
  (core/with-defaults (github-defaults)
    (repos/contents user repo filepath {:str? true})))

(defn- github-specific-repo [user repo]
  (core/with-defaults (github-defaults)
    (repos/specific-repo user repo)))

(defn- github-languages [user repo]
  (core/with-defaults (github-defaults)
    (repos/languages user repo)))

;;---------- Helpers

(defn user-repo [origin]
  (if-let [[_ user repo _] (re-matches #"^https?://github.com/(.+?)/(.+?)(\.git|/.*)?$" origin)]
    [user repo]
    (when-let [[ _ user _ repo] (re-matches #"^https?://(.+?).github.(io|com)/(.+?)/?.*$" origin)]
      [user repo])))

(defn- asset [user repo filepath]
  (let [result (github-contents user repo filepath)]
    (:content result)))

;;---------- API

(defn project [origin]
  (when-let [[user repo] (user-repo origin)]
    (let [prj (github-specific-repo user repo)
          languages (keys (github-languages user repo))]
      {:origin origin
       :hoster project
       :name (:name prj)
       :description (:description prj)
       :type :github
       :uri (:html_url prj)
       :authors [{:username (get-in prj [:owner :login])
                  :realname (get-in prj [:owner :id])
                  :uri (get-in prj [:owner :html_url])}]
       :languages languages
       :assets (fn [filepath] (asset user repo filepath))
       :builder nil
       :dependencies nil})))

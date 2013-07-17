(ns tipesso.provider.github
  (:use [clojure.contrib.djui.core :only [when-all-let]]
        [clojure.contrib.djui.coll :only [pjuxt pmapcat distinct-by]]
        environ.core)
  (:require [tentacles.core :as core]
            [tentacles.repos :as repos]))


;;; Tentacle wrappers

(defmacro  with-github-defaults
  "To maximize Github's rate-limit (from 60 to 5000 requests per hour) set the
  environment variable GITHUB_TOKEN to your Github OAuth token for this
  application."
  [body]
  `(core/with-defaults {:oauth-token (env :github-token)}
     (let [res# ~body]
       (if (= 403 (:status res#)) ; rate-limit check, sort-of
         (prn "Rate-limit exceeded!")
         (or #_(prn "Calls remaining:" (:call-remaining (core/api-meta res#))) ; debug only!
             res#)))))

(defn- github-contents [user repo filepath]
  (with-github-defaults (repos/contents user repo filepath {:str? true})))

(defn- github-owners [user repo]
  (with-github-defaults (-> (repos/specific-repo user repo) :owner vector)))

(defn- github-collaborators [user repo]
  (with-github-defaults (repos/collaborators user repo)))

(defn- github-contributors [user repo]
  (with-github-defaults (repos/contributors user repo)))

(defn- github-languages [user repo]
  (with-github-defaults (repos/languages user repo)))

;;;

(defn- asset
  "Return a function that can return the content of a Github repo file."
  [user repo]
  (fn [filepath]
    (:content (github-contents user repo filepath))))

(defn- languages
  "Return a list of languages in a Github hosted repo."
  [user repo]
  (keys (github-languages user repo)))

(defn- participants
  "Return a list of participants in a Github hosted repo.
  To spare the rate limit and reduce execution time for web API call latency,
  assume that contributors is a ordered superset of owner and collaborators and
  that the contribution distribution is: owners > collaborators > contributors.
  (See Clojure itself as an example why this assumption can be insufficient)
  Without this restriction, use order: owners + collaborators + contributors."
  [user repo]
  (let [f (fn [[type type-fn]]
            (->> (type-fn user repo)
                 (filter not-empty) ; BUG Tentacle always appends an empty map?
                 (map :login)
                 (map vector (repeat type))))
        participant-types [[:owner github-owners]
                           [:collaborator github-collaborators]
                           [:contributor github-contributors]]
        ;; Run in parallel, to counter slow web calls
        participants (pmapcat f participant-types)]
    (distinct-by second participants)))

(defn- transform
  "Construct output data structure."
  [[languages asset participants]]
  (let [l (fn [lang] {:asset asset, :language lang})
        p (fn [[type id]] {:provider :github, :type type, :id id})]
    (concat (map l languages) (map p participants))))

(defn- responsible?
  "If possible, i.e. responsible, extract repo and user name from repo URL."
  [data]
  (when-all-let [url (:url data)
                 code-regexp #"^https?://github.com/(.+?)/(.+?)(\.git|/.*)?$"
                 site-regexp #"^https?://(.+?).github.(io|com)/(.+?)/?.*$"
                 user-regexp #"^https?://github.com/(.+?)/?.*$"]
    (condp re-matches url
      code-regexp :>> (fn [[_ user repo _]] [user repo])
      site-regexp :>> (fn [[_ user _ repo]] [user repo])
      user-regexp :>> (fn [[_ user]] [user nil])
      nil)))

(defn handler
  "Find and return languages and contributors.
  Run (likely slow) web APIs calls in parallel."
  {:name :github}
  [data]
  (let [aggregate (pjuxt languages asset participants)]
    (some->> data
             responsible?
             (apply aggregate)
             transform)))

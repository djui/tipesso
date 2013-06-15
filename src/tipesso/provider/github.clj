(ns tipesso.provider.github
  (:use environ.core)
  (:use [tentacles.core :as core :only [with-defaults]])
  (:use [tentacles.repos :as repos :only [contents, languages, specific-repo]]))


(defn- github-defaults
  "To maximize Github's rate-limit (from 60 to 5000 requests per hour) set the
  environment variable GITHUB_TOKEN to your Github OAuth token for this
  application."
  []
  (prn (core/with-defaults {:oauth-token (env :github-token)} (core/rate-limit))) ;; debug only!
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

(defn- user-repo [url]
  (if-let [[_ user repo _] (re-matches #"^https?://github.com/(.+?)/(.+?)(\.git|/.*)?$" url)]
    [user repo]
    (when-let [[ _ user _ repo] (re-matches #"^https?://(.+?).github.(io|com)/(.+?)/?.*$" url)]
      [user repo])))

(defn- asset [user repo filepath]
  (:content (github-contents user repo filepath)))

(defn responsible?
  "Find and return languages, assets function and tippables."
  [{:keys [origin] :as data}]
  #_(prn "github" data)
  (when-let [[user repo] (and origin (user-repo origin))]
    (let [{{:keys [login id html_url]} :owner} (github-specific-repo user repo)]
      {:tippables [{:username login, :reason "Project owner", :origin html_url}]
       :languages (keys (github-languages user repo))
       :assets #(asset user repo %)})))

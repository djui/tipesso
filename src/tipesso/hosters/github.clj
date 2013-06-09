(ns tipesso.hosters.github
  (:use [tentacles.repos :as repos :only [specific-repo]]))

;;---------- Github communication

;; FIX replace sample github results with real results
#_(
   (defn github-contents [user repo filename]
     (repos/contents user repo filename {:str? true}))
   
   (defn github-specific-repo [user repo]
     (repos/specific-repo user repo))
   
   (defn github-languages [user repo]
     (repos/languages user repo)))

(defn github-contents [user repo filename]
  {:path "project.clj",
   :sha "07c48a4c8d1d009947f47e5922ed3e2c406a5cc5",
   :content (pr-str '(defproject tipesso "0.1.0-SNAPSHOT"
                       :description "Discover who to tip"
                       :url "http://djui.github.io/tipesso"
                       :dependencies [[org.clojure/clojure "1.5.1"]
                                      [compojure "1.1.5"]
                                      [hiccup "1.0.3"]
                                      [tentacles "0.2.5"]
                                      ;; Parsing and writing JSON
                                      [org.clojure/data.json "0.2.2"]]
                       :plugins [[lein-ring "0.8.3"]]
                       :ring {:handler tipesso.handler/app}
                       :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}})),
   :name "project.clj",
   :html_url "https://github.com/djui/tipesso/blob/master/project.clj",
   :size 493,
   :git_url "https://api.github.com/repos/djui/tipesso/git/blobs/07c48a4c8d1d009947f47e5922ed3e2c406a5cc5",
   :url "https://api.github.com/repos/djui/tipesso/contents/project.clj?ref=master",
   :_links {:self "https://api.github.com/repos/djui/tipesso/contents/project.clj?ref=master",
            :git "https://api.github.com/repos/djui/tipesso/git/blobs/07c48a4c8d1d009947f47e5922ed3e2c406a5cc5",
            :html "https://github.com/djui/tipesso/blob/master/project.clj"},
   :type "file",
   :encoding "base64"})

(defn github-specific-repo [user repo]
  {:archive_url "https://api.github.com/repos/djui/tipesso/{archive_format}{/ref}",
   :has_issues true,
   :notifications_url "https://api.github.com/repos/djui/tipesso/notifications{?since,all,participating}",
   :forks_count 0,
   :git_tags_url "https://api.github.com/repos/djui/tipesso/git/tags{/sha}",
   :issue_comment_url "https://api.github.com/repos/djui/tipesso/issues/comments/{number}",
   :contributors_url "https://api.github.com/repos/djui/tipesso/contributors",
   :compare_url "https://api.github.com/repos/djui/tipesso/compare/{base}...{head}",
   :fork false, :labels_url "https://api.github.com/repos/djui/tipesso/labels{/name}",
   :collaborators_url "https://api.github.com/repos/djui/tipesso/collaborators{/collaborator}",
   :pushed_at "2013-06-06T10:22:23Z",
   :git_commits_url "https://api.github.com/repos/djui/tipesso/git/commits{/sha}",
   :trees_url "https://api.github.com/repos/djui/tipesso/git/trees{/sha}",
   :name "tipesso",
   :default_branch "master",
   :clone_url "https://github.com/djui/tipesso.git",
   :hooks_url "https://api.github.com/repos/djui/tipesso/hooks",
   :watchers 0,
   :updated_at "2013-06-06T10:22:24Z",
   :assignees_url "https://api.github.com/repos/djui/tipesso/assignees{/user}",
   :has_wiki true, :stargazers_url "https://api.github.com/repos/djui/tipesso/stargazers",
   :html_url "https://github.com/djui/tipesso",
   :teams_url "https://api.github.com/repos/djui/tipesso/teams",
   :git_refs_url "https://api.github.com/repos/djui/tipesso/git/refs{/sha}",
   :milestones_url "https://api.github.com/repos/djui/tipesso/milestones{/number}",
   :network_count 0,
   :owner {:following_url "https://api.github.com/users/djui/following{/other_user}",
           :gists_url "https://api.github.com/users/djui/gists{/gist_id}",
           :starred_url "https://api.github.com/users/djui/starred{/owner}{/repo}",
           :followers_url "https://api.github.com/users/djui/followers",
           :gravatar_id "de1ad56f8a7b7167491f7f07f66013d9",
           :avatar_url "https://secure.gravatar.com/avatar/de1ad56f8a7b7167491f7f07f66013d9?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
           :html_url "https://github.com/djui",
           :received_events_url "https://api.github.com/users/djui/received_events",
           :login "djui",
           :url "https://api.github.com/users/djui",
           :organizations_url "https://api.github.com/users/djui/orgs",
           :type "User",
           :events_url "https://api.github.com/users/djui/events{/privacy}",
           :repos_url "https://api.github.com/users/djui/repos",
           :id 99752,
           :subscriptions_url "https://api.github.com/users/djui/subscriptions"},
   :language "Clojure", :merges_url "https://api.github.com/repos/djui/tipesso/merges",
   :size 620, :created_at "2013-05-25T11:27:00Z", :branches_url "https://api.github.com/repos/djui/tipesso/branches{/branch}", :issues_url "https://api.github.com/repos/djui/tipesso/issues{/number}", :private false, :homepage nil, :git_url "git://github.com/djui/tipesso.git", :mirror_url nil, :url "https://api.github.com/repos/djui/tipesso", :issue_events_url "https://api.github.com/repos/djui/tipesso/issues/events{/number}", :subscribers_url "https://api.github.com/repos/djui/tipesso/subscribers", :has_downloads true, :full_name "djui/tipesso", :watchers_count 0, :statuses_url "https://api.github.com/repos/djui/tipesso/statuses/{sha}", :open_issues_count 2, :master_branch "master", :ssh_url "git@github.com:djui/tipesso.git", :languages_url "https://api.github.com/repos/djui/tipesso/languages", :commits_url "https://api.github.com/repos/djui/tipesso/commits{/sha}", :forks_url "https://api.github.com/repos/djui/tipesso/forks", :subscription_url "https://api.github.com/repos/djui/tipesso/subscription", :contents_url "https://api.github.com/repos/djui/tipesso/contents/{+path}", :events_url "https://api.github.com/repos/djui/tipesso/events", :tags_url "https://api.github.com/repos/djui/tipesso/tags", :open_issues 2, :id 10283180, :forks 0, :svn_url "https://github.com/djui/tipesso", :downloads_url "https://api.github.com/repos/djui/tipesso/downloads", :blobs_url "https://api.github.com/repos/djui/tipesso/git/blobs{/sha}", :description "Discover who to tip",
   :pulls_url "https://api.github.com/repos/djui/tipesso/pulls{/number}",
   :comments_url "https://api.github.com/repos/djui/tipesso/comments{/number}",
   :keys_url "https://api.github.com/repos/djui/tipesso/keys{/key_id}"})

(defn github-languages [user repo]
  {:Clojure 1922, :JavaScript 256})

;;---------- Helpers

(defn user-repo [origin]
  (if-let [[_ user repo _] (re-matches #"^https?://github.com/(.+?)/(.+?)(\.git|/.*)?$" origin)]
    [user repo]
    (when-let [[ _ user _ repo] (re-matches #"^https?://(.+?).github.(io|com)/(.+?)/?.*$" origin)]
      [user repo])))

(defn asset [prj filename]
  ;; TODO: User (And maybe repo) should not be guessed but delared in project data structure
  (let [user (get-in prj [:authors 0 :username])
        repo (:name prj)
        result (github-contents user repo filename)]
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
       :assets asset
       :builder nil
       :dependencies nil})))

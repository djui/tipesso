(ns tipesso.provider.clojars
  (:require [xenopath.xpath :as xpath])
  (:use [clojure.contrib.djui.core :only [ignorantly when-all-let]]
        [clojure.string :only [join split]]))


;; TODO: Some projects' pom filename has to be discovered using the
;; `maven-metadata.xml` file to get the correct version string like:
;;   artifact
;;   + XPath:"/metadata/versioning/snapshotVersions/snapshotVersion:first/value"
;;   + ".pom"

(defn- pom-url
  "Return URL to pom file."
  [repo artifact version]
  (let [filename (format "%s-%s.pom" artifact version)]
    (join "/" [repo filename])))

(defn- project-url
  "Try to find and parse the project URL."
  [xml-string]
  (ignorantly (xpath/lookup-string "/project/url" xml-string)))

(defn- pom
  "Try to find and parse the maven manifest POM file."
  [repo-url artifact version]
  (let [pom-url (pom-url repo-url artifact version)]
    (ignorantly (slurp pom-url))))

(defn- repo
  "Return URL to repo."
  [group artifact version]
  (let [base "https://clojars.org/repo"
        domains (split group #"\.")
        parts (flatten [base domains artifact version])]
    [(join "/" parts) artifact version]))

(defn- responsible?
  "If possible, i.e. responsible, extract group id, artifact id, and version
  from repo URL."
  [data]
  (when-all-let [{:keys [provider id version]} data
                 _ (= provider :clojars)
                 origin (format "https://clojars.org/%s/versions/%s" id version)
                 regexp #"^https?://clojars\.org/((.+?)/(.+?)|(.+?))/versions/(.+)$"
                 [_ group-artifact group artifact _ version] (re-matches regexp origin)]
    (if group
      [group artifact version]
      [group-artifact group-artifact version])))

(defn handler
  "Find and return origin of Clojars hosted Clojure project."
  [data]
  (some->> data
           responsible?
           (apply repo)
           (apply pom)
           project-url
           (hash-map :url)))

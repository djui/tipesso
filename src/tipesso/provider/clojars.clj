(ns tipesso.provider.clojars
  (:use [clojure.string :only [join, split]])
  (:require [xenopath.xpath :as xpath]))


(defn dep-repo
  "Deconstruct repo URL into group id, artifact id, and version."
  [origin]
  (when-let [[_ group-artifact group artifact _ version]
             (re-matches #"^https?://clojars\.org/((.+?)/(.+?)|(.+?))/versions/(.+)$" origin)]
    (if group
      [group artifact version]
      [group-artifact group-artifact version])))

(defn- create-url
  "Create a URL path given a (possibly nested) vector."
  [& args]
  (join "/" args))

(defn- create-repo-url
  "Return URL to repo."
  [group artifact version]
  (let [base "https://clojars.org/repo"
        domains (split group #"\.")
        parts (flatten [base domains artifact version])]
    (apply create-url parts)))

(defn- create-pom-url
  "Return URL to pom file."
  [repo artifact version]
  (let [filename (format "%s-%s.pom" artifact version)]
    ;; TODO: Some project's pom filename has to be discovered using the
    ;; "maven-metadata.xml" file to get the correct version string: artifact +
    ;; XPath:"/metadata/versioning/snapshotVersions/snapshotVersion:first/value"
    ;; + ".pom"
    (create-url repo filename)))

(defn- pom-file
  "Try to find and parse the maven manifest POM file."
  [repo-url artifact version]
  (let [pom-url (create-pom-url repo-url artifact version)]
    (try (slurp pom-url)
         (catch Exception _))))

(defn- project-url
  "Try to find and parse the project URL."
  [xml-string]
  (try (xpath/lookup-string "/project/url" xml-string)
       (catch Exception _)))

(defn project [origin]
  (when-let [[group artifact version] (dep-repo origin)]
    (let [repo-url (create-repo-url group artifact version)]
      (when-let [pom (pom-file repo-url artifact version)]
        (when-let [uri (project-url pom)]
          {:origin origin
           :hoster project
           :type :clojars
           :name 'artifact
           :id (format "%s/%s" 'group 'artifact)
           :uri uri
           :tippables []})))))

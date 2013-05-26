(ns tipesso.discoverer)

(defn fetch-url [address]
  (with-open [stream (.openStream (java.net.URL. address))]
    (let [buf (java.io.BufferedReader. (java.io.InputStreamReader. stream))]
      (apply str (line-seq buf)))))

(defn discover [link]
  (let [[_ user repo]   (re-matches #"^https://github.com/(.+?)/(.+?)$" link)
        project-url     (format "https://raw.github.com/%s/%s/master/project.clj" user repo)
        project-content (fetch-url project-url)
        project-data    (binding [*read-eval* false] (read-string project-content))
        project-map     (apply hash-map (drop 3 project-data))]
    (pr-str (pr-str (project-map :dependencies)))))

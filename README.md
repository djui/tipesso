# Tipesso

Discover who to tip.

# Idea

```
         +------------------------------------------------------------------------------------------------------------------+
(Input)  |                                                                                                                  |
+-----+ 1|1  +----------+ 1 1 +------------+ 1 *  +----------+ 1 1 +-----------+ 1 1 +---------------+ 1 * +------------+   |
| URI | -+-> | ORIGIN   | --> | PROJECT    | -+-> | LANGUAGE | --> | BUILDER   | --> | CONFIG        | --> | DEPENDENCY | --+
+-----+      +----------+     +------------+  |   +----------+     +-----------+     +---------------+     +------------+
             | :type    |     | :name      |  |   | :name    |     | :name     |     | :filename     |     | :name      |
             | :uri     |     | :authors   |  |   | :builder |     | :config   |     | :dependencies |     | :version   |
             | :project |     | :languages |  |   +----------+     +-----------+     +---------------+     | :origin    |
             +----------+     +------------+  |   : clojure  :     : leiningen :     : project.clj   :     +------------+
             : github   :                     |   : erlang   :     : rebar     :     : rebar.config  :
             +----------+                     |   +----------+     +-----------+     +---------------+
                                              |
                                              |*  +-----------+
                                              +-> | AUTHOR    |
                                                  +-----------+
                                                  | $tippable |
                                                  | :username |
                                                  | :realname |
                                                  | :uri      |
                                                  +-----------+
```

```
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
         [{:name :Clojure
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
                     :origin  "http://..."}]}}}
                    {:name :javascript}]}}}
```

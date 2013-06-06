# Tipesso

Discover who to tip.

# Idea

To make it easier and more convientient to find authors you want to support, we
identify dependencies in projects and provide tipping information about their
authors.

## Discover

Hosted projects will be scanned to identify authors. A dependency of a project
is seen as yet another project with a authors.

## Who

Authors can be the main author of a project, contributors, or collaborators.

## Tip

Identified authors are "tippable" meaning a tipping provider called *brokers*
(such as [Gittip](http://gittip.com/) and [Flattr](http://flattr.com/)) can be
asked to transfer a chosen amount to the author.

# Providers

Three provider APIs are exposed:

* **Hoster**s
* **Builder**s
* **Broker**s

## Hoster

All hosters shall answer the question if they are responsible for a given
project origin.

Input to *hoster*s is an origin and outputs are authors and languages. A
*hoster* identifies the project to generate its outputs from that.

Furthermore, the *hoster*s provide an asset API, which input is an origin and
filename and its output is the file content.

## Builder

All builders shall answer the question if they are responsible for a given
language.

Input to *builder*s is an origin and language and outputs are dependencies. A
*builder* identifies the configuration to generate its outputs from that.

The builder asks the hoster to provide data about project files it requires to
identify the dependencies.

## Broker

Input to *broker*s is an author URI and outputs is an URI to the provider's API.

# APIs

## Hoster

```clojure
(hoster/project origin) ; => 'project-data
(hoster/asset origin filename) ; => 'asset-data
```

## Builder

```clojure
(builder/dependencies project-data) ; => [dependency]
```

## Broker

```clojure
(broker/locate author) ; => 'api-uri
```

# Data model

Entity relation model:

```
            +------------------------------------------------------------------------------------------------------------------+
            |                                                                                                                  |
+--------+ 1|1  +----------+ 1 1 +------------+ 1 *  +----------+ 1 1 +-----------+ 1 1 +---------------+ 1 * +------------+   |
| ORIGIN | -+-> | HOSTER   | --> | PROJECT    | -+-> | LANGUAGE | --> | BUILDER   | --> | CONFIG        | --> | DEPENDENCY | --+
+--------+      +----------+     +------------+  |   +----------+     +-----------+     +---------------+     +------------+
| :uri   |      | :type    |     | :name      |  |   | :name    |     | :name     |     | :filename     |     | :name      |
+--------+      | :uri     |     | :authors   |  |   | :builder |     | :config   |     | :dependencies |     | :version   |
                | :project |     | :languages |  |   +----------+     +-----------+     +---------------+     | :origin    |
                +----------+     +------------+  |   : clojure  :     : leiningen :     : project.clj   :     +------------+
                : github   :                     |   : erlang   :     : rebar     :     : rebar.config  :
                +----------+                     |   +----------+     +-----------+     +---------------+
                                                 |
                                                 |*  +-----------+ 1 * +--------+
                                                 +-> | AUTHOR    | --> | BROKER |
                                                     +-----------+     +--------+
                                                     | $tippable |     | :api   |
                                                     | :username |     +--------+
                                                     | :realname |
                                                     | :uri      |
                                                     +-----------+
```

Example data structure:

```clojure
Project => {:origin "https://github.com/djui/tipesso"
            :url "https://github.com/djui/tipesso"
            :hoster :github
            :name "tipesso"
            :description "..."
            :authors [{:username "djui"
                       :realname "Uwe Dauernheim"
                       :uri      "http://github.com/djui"}
                      {:username "onlyafly"
                       :realname "Kevin Albrecht"
                       :uri      "http://github.com/onlyafly"}]
            :languages [:Clojure :javascript]
            :dependencies Dependencies
            :assets 'github/asset}
            
Dependencies => [{:name    "org.clojure/clojure"
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
                  :origin  "http://..."}]
```

# Credits

...

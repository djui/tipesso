# Tipesso

Discover who to tip.

# Idea

```
         +------------------------------------------------------------------------------------------------------------------+
(Input)  |                                                                                                                  |
+-----+  |   +----------+     +------------+      +----------+     +-----------+     +---------------+     +------------+   |
| URI | -+-> | ORIGIN   | --> | PROJECT    | -+-> | LANGUAGE | --> | BUILDER   | --> | CONFIG        | --> | DEPENDENCY | --+
+-----+      +----------+     +------------+  |   +----------+     +-----------+     +---------------+     +------------+
             | :type    |     | $tippable  |  |   | :name    |     | :name     |     | :filename     |     | :name      |
             | :uri     |     | :name      |  |   | :builder |     | :config   |     | :dependencies |     | :version   |
             | :project |     | :authors   |  |   +----------+     +-----------+     +---------------+     | :origin    |
             +----------+     | :languages |  |   : clojure  :     : leiningen :     : project.clj   :     +------------+
             : github   :     +------------+  |   : erlang   :     : rebar     :     : rebar.config  :
             +----------+                     |   +----------+     +-----------+     +---------------+
                                              |
                                              |   +-----------+
                                              +-> | AUTHOR    |
                                                  +-----------+
                                                  | $tippable |
                                                  | :username |
                                                  | :realname |
                                                  | :uri      |
                                                  +-----------+
```

(ns tipesso.macros)

(defmacro create-map [& args]
  (into {} (map (fn [arg]
                  [(keyword arg) arg])
                args)))
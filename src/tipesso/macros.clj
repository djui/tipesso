(ns tipesso.macros)

(defmacro create-map [& args]
  (zipmap (map keyword args) args))

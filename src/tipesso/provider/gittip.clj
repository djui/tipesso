(ns tipesso.provider.gittip
  (:use [clojure.contrib.djui.core :only [when-all-let]]))


(defn- responsible?
  "If possible, i.e. responsible, convert items into tippables."
  [data]
  (when-all-let [{:keys [provider type id]} data
                 _ (= provider :github)]
    {:reason type ;; TODO: Map reason type to string
     :username id
     :url (format "http://gittip.com/user/%s" id)}))

(defn handler
  "Convert participants, authors, etc. into tippables on Gittip."
  [data]
  (some->> data
           responsible?
           (hash-map :tippable)))

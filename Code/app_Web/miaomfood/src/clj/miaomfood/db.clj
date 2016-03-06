(ns miaomfood.db
    (:require
      [mount.core :refer [defstate start]]
      [environ.core :refer [env]]
      [clojure.edn :as edn :refer [read-string]]
      [datomic.api :as d]
      [miaomfood.conf :refer [config]]))


(def schema-tx (edn/read-string (slurp "resources/miaomfood-schema.edn")))

(defn- new-connection [conf]
  (let [uri (if-let [env-uri (env :datomic-uri)]
              env-uri
              (get-in conf [:datomic :uri]))]
    (when (d/create-database uri)
      (let [conn (d/connect uri)]
        @(d/transact conn schema-tx)))
    (d/connect uri)))

(defstate conn :start (new-connection config))

; hook for mount to initiate from
(defn init []
  (start))

(ns miaomfood.db
    (:require
      [mount.core :refer [defstate start]]
      [environ.core :refer [env]]
      [datomic.api :as d]
      [miaomfood.conf :refer [config]]))

(defn- get-uri [conf]
  (if-let [env-uri (env :datomic-uri)]
    env-uri
    (get-in conf [:datomic :uri])))

(def uri (get-uri config))
(defstate conn :start (d/connect uri))

; hook for mount to initiate from
(defn init []
  (start))

;; Be cautious, just for development !!!
;; #######################################
(def schema-tx (read-string (slurp "resources/miaomfood-schema.edn")))
(def data-tx (read-string (slurp "resources/miaomfood-data.edn")))
(def db (init-db uri schema-tx data-tx))

(defn init-db [db-uri schema seed-data]
  (let [conn (do (d/delete-database db-uri)
                 (d/create-database db-uri)
                 (d/connect db-uri))]
    @(d/transact conn schema)
    @(d/transact conn seed-data)
    (d/db conn)))

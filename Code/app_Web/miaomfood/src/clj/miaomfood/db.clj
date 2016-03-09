(ns miaomfood.db
    (:require
      [mount.core :refer [defstate start]]
      [environ.core :refer [env]]
      [datomic.api :as d]
      [miaomfood.conf :refer [config]]
      [miaomfood.datomic.data :refer [schema-tx data-tx]]))

(defn- get-uri [conf]
  (if-let [env-uri (env :datomic-uri)]
    env-uri
    (get-in conf [:datomic :uri])))

(def uri (get-uri config))
; (defstate conn :start (d/connect uri))
; (defstate db :start (d/db conn))

;; Be cautious, just for development !!!
;; #######################################

(defn- init-conn [uri schema seed-data]
  (let [conn (do (d/delete-database uri)
                 (d/create-database uri)
                 (d/connect uri))]
    @(d/transact conn schema)
    @(d/transact conn seed-data)
    conn))

(defstate conn :start (init-conn uri schema-tx data-tx))
(def db (d/db conn))

; hook for mount to initiate from
(defn init []
  (start))

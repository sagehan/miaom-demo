(ns dev.db
  (:require
   [mount.core :as mount :refer [defstate]]
   [environ.core :refer [env]]
   [clojure.tools.logging :refer [info]]
   [datomic.api :as d]
   [miaomfood.conf :refer [config]]
   [miaomfood.utils.io :refer [resource transact-all]]))

(defn- get-uri [conf]
  (if-let [env-uri (env :datomic-dev-uri)]
    (do
      (info "Get uri settings from environ")
      env-uri)
    (do
      (info "Get uri settings from config file")
      (get-in conf [:datomic :dev-uri]))))

;; Be cautious, just for development !!!
;; #######################################

(defn- init-conn [conf]
  (let [uri (get-uri conf)
        conn (do
               (info "creating a connection to datomic:" uri)
               (d/delete-database uri)
               (d/create-database uri)
               (d/connect uri))]
    (transact-all conn (resource "miaomfood-schema.edn"))
    (transact-all conn (resource "miaomfood-data.edn"))
    conn))

(defstate conn-dev
  :start (init-conn config))

(ns miaomfood.db
  (:require
   [boot.core :refer [load-data-readers!]]
   [mount.core :as mount :refer [defstate start]]
   [environ.core :refer [env]]
   [clojure.tools.logging :refer [info]]
   [datomic.api :as d]
   [miaomfood.conf :refer [config]]
   [miaomfood.util.io :refer [resource transact-all]]))

(defn- get-uri [conf]
  (if-let [env-uri (env :datomic-uri)]
    (do
      (info "Get uri settings from environ")
      env-uri)
    (do
      (info "Get uri settings from config file")
      (get-in conf [:datomic :uri]))))

(defstate uri :start (get-uri config))
; (defstate conn :start (d/connect uri))
; (defstate db :start (d/db conn))

;; Be cautious, just for development !!!
;; #######################################

(defn- init-conn [uri]
  (let [conn (do
               (info "creating a connection to datomic:" uri)
               (d/delete-database uri)
               (d/create-database uri)
               (d/connect uri))]
    (transact-all conn (resource "miaomfood-schema.edn"))
    (transact-all conn (resource "miaomfood-data.edn"))
    conn))

(defstate conn :start (init-conn uri))

(defstate db :start (d/db conn))

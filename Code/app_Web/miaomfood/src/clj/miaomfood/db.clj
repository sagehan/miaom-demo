(ns miaomfood.db
  (:require
   [mount.core :as mount :refer [defstate start]]
   [environ.core :refer [env]]
   [clojure.tools.logging :refer [info]]
   [datomic.api :as d]
   [miaomfood.conf :refer [config]]
   [miaomfood.utils.io :refer [resource transact-all]]))

(defn- get-uri [conf]
  (if-let [env-uri (env :datomic-uri)]
    (do
      (info "Get uri settings from environ")
      env-uri)
    (do
      (info "Get uri settings from config file")
      (get-in conf [:datomic :uri]))))

(defn disconnect [conf conn]
  (let [uri (get-uri conf)]
    (info "disconnecting from " uri)
    (d/release conn)))

(defstate conn
  :start (let [uri (get-uri config)]
           (d/connect uri))
  :stop (disconnect config conn))

(defn- init
  []
  (mount/start))

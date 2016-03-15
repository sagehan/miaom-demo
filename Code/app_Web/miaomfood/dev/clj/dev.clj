(ns dev
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :as tn]
            [boot.core :refer [load-data-readers!]]
            [mount.core :as m :refer [defstate]]
            [miaomfood.db]
            [dev.db]
            [miaomfood.app]))

(defn start []
  (comment (m/start #'miaomfood.conf/config))
  (comment (-> (m/only #{#'miaomfood.conf/config
                         #'miaomfood.db/conn
                         #'miaomfood.db/db
                         #'dev.db/conn-dev})
               (m/swap-states {#'miaomfood.db/conn #'dev.db/conn-dev})))
  (m/start-with-states {#'miaomfood.db/conn #'dev.db/conn-dev}))

(defn stop []
  (m/stop))

(defn refresh []
  (m/stop)
  (tn/refresh))

(defn refresh-all []
  (m/stop)
  (tn/refresh-all))

(defn go
  "starts all states defined by defstate"
  []
  (m/start)
  :ready)

(defn reset
  "stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (m/stop)
  (tn/refresh :after 'dev/go))

(m/in-clj-mode)
(load-data-readers!)

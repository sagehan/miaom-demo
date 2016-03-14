(ns dev
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :as tn]
            [boot.core :refer [load-data-readers!]]
            [mount.core :as mount :refer [defstate]]
            ))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh-all []
  (stop)
  (tn/refresh-all))

(defn go
  "starts all states defined by defstate"
  []
  (start)
  :ready)

(defn reset
  "stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (stop)
  (tn/refresh :after 'dev/go))

(mount/in-clj-mode)
(load-data-readers!)

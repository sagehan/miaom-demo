(ns miaomfood.app
  (:require [clojure.tools.nrepl.server :refer [start-server stop-server]]
            [mount.core :as mount :refer [defstate]]
            [miaomfood.conf :refer [config]]
            [miaomfood.core])
  (:gen-class))

(defn- start-nrepl [{:keys [host port]}]
  (start-server :bind host :port port))

(defstate nrepl
  :start (start-nrepl (:nrepl config))
  :stop (stop-server nrepl))

;; here is app main entry point for uberjar
(defn -main [& args]
  (mount/start))

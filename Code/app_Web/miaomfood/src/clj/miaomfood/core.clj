;; Copyright (c) Sage Han. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns miaomfood.core
  (:require
    [compojure.core :as c]
    [compojure.route :as route]
    [ring.middleware.defaults :as d]
    [ring.util.response :as response]
    [castra.middleware :as castra]
    [ring.adapter.jetty  :refer [run-jetty]]
    [mount.core :refer [defstate]]
    [miaomfood.conf :refer [config]]))

(def server (atom nil))

(c/defroutes app-routes
  (c/GET "/" req (response/content-type (response/resource-response "index.html") "text/html"))
  (c/GET "/love/" [] "Love Miaom!")
  (route/resources "/" {:root ""}))

(defn app [{:keys [http]}]
  (-> app-routes
      (d/wrap-defaults d/api-defaults)
      (castra/wrap-castra-session "a 16-byte secret")
      (castra/wrap-castra 'miaomfood.api)
      (run-jetty {:join? false
                  :port (:port http)})))

(defstate miaom-app
  :start (app config)
  :stop  (.stop miaom-app))


(defn start-server
  "Start castra server (port 33333)."
  [port public-path]
  (swap! server #(or % (app port ))))

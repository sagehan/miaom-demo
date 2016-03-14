#!/usr/bin/env boot

(set-env!
 :dependencies '[
                 [org.clojure/clojure       "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [compojure                 "1.4.0"]
                 [ring                      "1.4.0"]
                 [ring/ring-defaults        "0.1.5"]
                 [datascript                "0.15.0"]
                 [org.clojure/algo.generic  "0.1.0"]
                 [org.clojure/tools.logging "0.3.1"        :scope "provided"]
                 [environ                   "1.0.2"]
                 [mount                     "0.1.7"]
                 [lein-light-nrepl          "0.3.2"]

                 ;; locate the datomic-pro installation , execute  bin/maven-install
                 ;; , that will install the datomic-pro jar to your local maven repo
                 [com.datomic/datomic-pro  "0.9.5350"]

                 ;; boot clj
                 [boot/core                 "2.5.1"      :scope "provided"]

                 ;; boot cljs
                 [pandeiro/boot-http        "0.7.3"]
                 [adzerk/boot-cljs          "1.7.228-1"]
                 [adzerk/boot-reload        "0.4.5"]
                 [hoplon/castra             "3.0.0-SNAPSHOT"]
                 [hoplon/boot-hoplon        "0.1.10"]
                 [hoplon/hoplon             "6.0.0-alpha13"]])

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[pandeiro.boot-http       :refer [serve]]
  '[environ.core             :refer [env]]
  '[lighttable.nrepl.handler :as light]
  '[clojure.tools.nrepl.server :as nrepl]
  '[clojure.tools.namespace.repl :refer [set-refresh-dirs]])

(deftask repl-light []
  (let [nrepl-server (atom nil)]
    (fn [continue]
      (fn [event]
        (swap! nrepl-server
               #(or % (nrepl/start-server
                       :port 0
                       :handler (nrepl/default-handler #'light/lighttable-ops))))
        (println "nrepl running on " (:port @nrepl-server))
        (continue event)
        @(promise)))))

(deftask dev
  "Build miaomfood for local development."
  []
  (set-env! :source-paths #{"src/clj" "src/cljs" "src/hl"})
  (set-env! :resource-paths #{"resources"})
  (apply set-refresh-dirs (get-env :directories))

  (require 'dev)
  (in-ns 'dev))

(deftask cljs-dev
  (set-env! :source-paths #{"src/clj" "src/cljs" "src/hl"})
  (set-env! :resource-paths #{"resources"})
  (set-env! :target-path    "target/public")

  (comp
   (repl-light)
   (watch)
   (speak)
   (hoplon :pretty-print true)
   (reload)
   (cljs :optimizations :none :source-map true)
   (serve
    :init 'miaomfood.db/init
    :handler 'miaomfood.core/handler
    :resource-root "target/public"
    :reload true
    :port 8000)))

(deftask prod
  "Build miaomfood for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)))

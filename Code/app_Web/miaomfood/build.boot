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
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/tools.nrepl   "0.2.12"]
                 [com.cemerick/piggieback   "0.2.1"  :scope "test"]
                 [weasel                    "0.7.0"  :scope "test"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [environ                   "1.0.2"]
                 [mount                     "0.1.10"]
                 [hoplon/castra             "3.0.0-alpha3"]

                 ;; locate the datomic-pro installation , execute  bin/maven-install
                 ;; , that will install the datomic-pro jar to your local maven repo
                 [com.datomic/datomic-pro  "0.9.5350"]

                 ;; boot clj
                 [boot/core                 "2.5.5"      :scope "provided"]
                 [adzerk/boot-logservice    "1.0.1"      :scope "test"]
                 [boot-environ              "1.0.2"]

                 ;; boot cljs
                 [pandeiro/boot-http        "0.7.3"]
                 [adzerk/boot-cljs          "1.7.228-1"]
                 [adzerk/boot-cljs-repl     "0.3.0"]
                 [adzerk/boot-reload        "0.4.5"]
                 [hoplon/boot-hoplon        "0.1.10"]
                 [hoplon/hoplon             "6.0.0-alpha13"]])

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[adzerk.boot-cljs-repl    :refer [cljs-repl start-repl]]
  '[adzerk.boot-logservice   :as    log-service]
  '[clojure.tools.logging    :as    log]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[pandeiro.boot-http       :refer [serve]]
  '[environ.boot             :refer [environ]]
  '[clojure.tools.nrepl.server :as  nrepl]
  '[clojure.tools.namespace.repl :refer [set-refresh-dirs]])

(def log4b
  [:configuration
   [:appender {:name "STDOUT" :class "ch.qos.logback.core.ConsoleAppender"}
    [:encoder [:pattern "%-5level %logger{36} - %msg%n"]]]
   [:root {:level "TRACE"}
    [:appender-ref {:ref "STDOUT"}]]])

(deftask dev
  "Build miaomfood for local development."
  []
  (set-env! :source-paths #{"dev/clj" "src/clj"})
  (set-env! :resource-paths #(conj % "dev/resources"))

  (alter-var-root #'log/*logger-factory*
                  (constantly (log-service/make-factory log4b)))

  (apply set-refresh-dirs (get-env :directories))
  (load-data-readers!)

  (require 'dev)
  (in-ns 'dev))

(deftask cljs-dev
  []
  (set-env! :source-paths #{"src/cljs" "src/hl" "src/clj"})
  (set-env! :resource-paths #{"resources/assets"})

  (comp
   (environ :env
            {:datomic-uri "datomic:dev://localhost:4334/miaomfood-dev"})
   (serve
    :init 'miaomfood.db/init
    :handler 'miaomfood.core/handler
    :port 8000
    :reload true)
   (watch)
   (speak)
   (reload)
   (hoplon :pretty-print true)
   (cljs)))

(deftask prod
  "Build miaomfood for production deployment."
  []
  (set-env! :source-paths #{"src/cljs" "src/hl"})
  (set-env! :resource-paths #{"resources/assets"})
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (target :dir #{"target"})))

(deftask play
  []
  (set-env! :source-paths #{"src/cljs" "src/hl" "src/clj"})
  (set-env! :resource-paths #{"resources/assets"})

  (comp
    (environ :env
            {:datomic-uri "datomic:dev://localhost:4334/miaomfood-dev"})
    (serve
     :init 'miaomfood.db/init
     :handler 'miaomfood.core/handler
     :port 8000
     :reload true)
    (hoplon)
    (cljs :optimizations :advanced)
    (wait)))

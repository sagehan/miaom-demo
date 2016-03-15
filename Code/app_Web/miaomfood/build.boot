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
                 [org.clojure/tools.logging "0.3.1"]
                 [environ                   "1.0.2"]
                 [mount                     "0.1.10"]

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

  '[clojure.tools.namespace.repl :refer [set-refresh-dirs]])

(deftask dev
  "Build miaomfood for local development."
  []
  (set-env! :source-paths #{"dev/clj" "src/clj"})
  (set-env! :resource-paths #{"dev/resources"})
  (apply set-refresh-dirs (get-env :directories))
  (load-data-readers!)

  (require 'dev)
  (in-ns 'dev))

(deftask cljs-dev
  []
  (set-env! :source-paths #{"src/clj" "src/cljs" "src/hl"})
  (set-env! :resource-paths #{"resources/assets"})
  (set-env! :target-path    "resources/public")

  (comp
   (watch)
   (speak)
   (hoplon :pretty-print true)
   (reload)
   (cljs :optimizations :none :source-map true)
   (serve
    :dir "resources/public"
;    :reload true
    :port 8000)))

(deftask prod
  "Build miaomfood for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)))

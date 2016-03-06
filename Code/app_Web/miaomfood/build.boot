(set-env!
 :dependencies '[
                 [org.clojure/clojure       "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [compojure                 "1.4.0"]
                 [ring                      "1.4.0"]
                 [ring/ring-defaults        "0.1.5"]
                 [com.datomic/datomic-free  "0.9.4766"]
                 [datascript                "0.15.0"]
                 [org.clojure/algo.generic  "0.1.0"]
                 [environ                   "1.0.1"]
                 [mount                     "0.1.7"]

                 ;; boot cljs
                 [pandeiro/boot-http        "0.7.3"]
                 [adzerk/boot-cljs          "1.7.228-1"]
                 [adzerk/boot-reload        "0.4.5"]
                 [hoplon/castra             "3.0.0-SNAPSHOT"]
                 [hoplon/boot-hoplon        "0.1.10"]
                 [hoplon/hoplon             "6.0.0-alpha13"]]

  :target-path    "target/public"
  :source-paths   #{"src/hl" "src/cljs" "src/clj"}
  :resource-paths #{"resources/assets"})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[pandeiro.boot-http       :refer [serve]])

(deftask dev
  "Build miaomfood for local development."
  []
  (comp
   (serve
    :init 'miaomfood.db/init
    :handler 'miaomfood.core/handler
    :resource-root "target/public"
    :reload true
    :port 8000)
   (watch)
   (speak)
   (hoplon :pretty-print true)
   (reload)
   (cljs :optimizations :none :source-map true)))

(deftask prod
  "Build miaomfood for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)))

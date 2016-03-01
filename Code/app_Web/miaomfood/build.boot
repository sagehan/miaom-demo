(set-env!
  :dependencies '[[adzerk/boot-cljs          "1.7.228-1"]
                  [adzerk/boot-reload        "0.4.5"]
                  [compojure                 "1.4.0"]
                  [hoplon/castra             "3.0.0-SNAPSHOT"]
                  [hoplon/boot-hoplon        "0.1.10"]
                  [hoplon/hoplon             "6.0.0-alpha13"]
                  [org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [tailrecursion/boot-jetty  "0.1.3"]
                  [ring                      "1.4.0"]
                  [ring/ring-defaults        "0.1.5"]
                  [com.datomic/datomic-free  "0.9.4766"]
                  [datascript                "0.15.0"]
                  [org.clojure/algo.generic  "0.1.0"]]
  :source-paths #{"src/hl" "src/cljs"}
  :asset-paths  #{"assets"})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[tailrecursion.boot-jetty :refer [serve]])

(deftask dev
  "Build miaomfood for local development."
  []
  (comp
    (watch)
    (speak)
    (hoplon)
    (reload)
    (cljs)
    (serve :port 8000)))

(deftask prod
  "Build miaomfood for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)))

(ns miaomfood.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(set! cljs.core/*print-fn* #(.log js/console %))

(defc raw-db [])
(defc user-db {})
(defc order {})
(defc error nil)
(defc loading [])

(def fetch-raw-db
  (mkremote 'miaomfood.api/customer-db raw-db error loading))

(def fetch-user-db
  (mkremote 'miaomfood.api/user-db user-db error loading))

(def submit-order! (mkremote 'miaomfood.api/submit-order! order error loading))

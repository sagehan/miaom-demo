(ns miaomfood.api
  (:require
   [castra.core :refer [defrpc]]
   [miaomfood.datomic.qpi :as q]))

(defrpc customer-db []  (q/customer-db))

(defrpc user-db [uid] (q/user-db uid))

(defrpc submit-order! [order-raw] (q/submit-order! order-raw))

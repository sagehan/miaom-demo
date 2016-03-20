(ns miaomfood.api
  (:require
   [castra.core :refer [defrpc]]
   [miaomfood.datomic.qpi :as q]))

(defrpc customer-db []  (q/customer-db))

(defrpc user-db [uid] (q/user-db uid))

(defrpc like! [uid cuisine]
  (q/like! uid cuisine)
  (q/user-db uid))

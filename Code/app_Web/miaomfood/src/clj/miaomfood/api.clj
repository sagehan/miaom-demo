(ns miaomfood.api
  (:require
   [castra.core :refer [defrpc]]
   [miaomfood.datomic.qpi :refer :all]))

(defrpc get-state []
  {:random (rand-int 100)})

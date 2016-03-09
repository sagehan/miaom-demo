(ns miaomfood.api
  (:require
   [castra.core :refer [defrpc]]
   [miaomfood.datomic.qpi :as q :refer :all]))

(defrpc website-metadata [] (q/website-metadata))
(defrpc cuisines-metadata [] (q/cuisines-metadata))
(defrpc user-metadata [] (q/user-metadata))

(defrpc like! [username cuisine]
  (q/like! username cuisine)
  (user-metadata username))

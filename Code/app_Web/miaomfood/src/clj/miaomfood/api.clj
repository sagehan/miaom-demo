(ns miaomfood.api
  (:require
   [castra.core :refer [defrpc]]
   [miaomfood.datomic.qpi :as q]))

(defrpc website-metadata [] (q/website))
(defrpc menus-metadata [] (q/menus))
(defrpc cuisines-metadata [] (q/cuisines))
(defrpc user-metadata [uid] (q/user uid))

(defrpc like! [uid cuisine]
  (q/like! uid cuisine)
  (q/user uid))

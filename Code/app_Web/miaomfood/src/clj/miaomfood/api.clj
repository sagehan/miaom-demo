(ns miaomfood.api
  (:require
   [castra.core :refer [defrpc]]
   [miaomfood.datomic.qpi :as q]))

(defrpc website-metadata [] (q/website))
(defrpc menus-metadata [] (q/menus))
(defrpc cuisines-metadata [] (q/cuisines))
(defrpc user-metadata [user-ident] (q/user user-ident))

(defrpc like! [user-ident cuisine]
  (q/like! user-ident cuisine)
  (q/user user-ident))

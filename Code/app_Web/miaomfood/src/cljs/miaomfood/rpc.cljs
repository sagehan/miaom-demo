(ns miaomfood.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc website {:title "喵姆餐厅"})
(defc user {})
(defc error nil)
(defc loading [])

(defc= random-number (get state :random))

(def website-metadata
  (mkremote 'miaomfood.api/website-metadata website error loading))

(def like! (mkremote 'miaomfood.api/like! user error loading))

(defn init []
  (website-metadata)
  (js/setInterval website-metadata 1000))

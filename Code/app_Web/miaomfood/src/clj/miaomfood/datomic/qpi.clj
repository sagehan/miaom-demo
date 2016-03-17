(ns miaomfood.datomic.qpi
  (:require
    [miaomfood.db :refer [conn]]
    [datomic.api :as d]
    [clojure.algo.generic.functor :refer [fmap]]))

;; Copyright (c) https://github.com/danielneal/  ==== start =========================

(defn- load-entity
  "Loads an entity and its attributes. Keep in the db/id
  and replace references with ids (for use by DataScript)"
  [db entity]
  (as->
   (d/entity db entity) e
   (d/touch e)
   (into {:db/id (:db/id e)} e) ; needs to be a hash-map, not an entity map
   (fmap (fn [v]
           (cond (set? v) (mapv #(if (instance? datomic.query.EntityMap %) (:db/id %) %) v)
                 (instance? datomic.query.EntityMap v) (:db/id v)
                 :else v)) e)))

;; Copyright (c) https://github.com/danielneal/  ==== end =========================

(defn- e->website
  "Get the entities related to website itself"
  [conn]
  (d/q '[:find [(pull
                 ?e
                 [:website/title
                  {:website/notices [:notice/content
                                     :notice/duration
                                     :notice/priority]}])]
         :where [?e :website/title]]
       (d/db conn)))


(defn-  e->menus
  "Get the original structure of the menus"
  [conn]
  (d/q '[:find [(pull
                 ?e
                 [{:group/menus [:menu/name
                                 {:menu/categories [:category/name
                                                    {:category/cuisines [:cuisine/name]}]}]}])]
         :where [?e :group/menus]]
       (d/db conn)))

(defn- e->cuisines
  "Get all cuisines entities"
  [conn]
  (d/q '[:find [(pull
                 ?e
                 [:cuisine/name
                  :cuisine/id
                  :cuisine/doc
                  :cuisine/depict
                  {:cuisine/species [:spec/price
                                     :spec/inventory
                                     {:spec/name [:db/ident]}]}])
                ...]
         :where [?e :cuisine/name]]
       (d/db conn)))

(defn- e->user
  "Get a user's entities"
  [user-ident conn]
  (d/pull (d/db conn) '[:customer/id
                        :customer/username
                        :customer/phone
                        :customer/gender
                        :customer/streetAddress
                        :customer/email
                        :customer/wechat
                        :customer/qq
                        :customer/totalConsume
                        :customer/totalPurchase
                        {:customer/likes [:cuisine/name]}]
          user-ident))

(defn website [] (e->website conn))
(defn menus [] (e->menus conn))
(defn cuisines [] (e->cuisines conn))
(defn user [uid] (e->user uid conn))

(defn like!
  "A customer up-vote an cuisine"
  [user-ident cuisine-id]
  @(d/transact-async conn [{:db/id user-ident  :customer/likes cuisine-id}]))

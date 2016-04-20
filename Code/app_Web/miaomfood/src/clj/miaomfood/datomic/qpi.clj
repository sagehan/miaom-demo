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

(defn- e->customer
  "Get all entIDs that could be accessible to a customer"
  [db]
  (d/q '[:find ?e
         :in $ %
         :where (visible? ?e)]
       db
       '[[(visible? ?wt) [?wt :website/title]]
         [(visible? ?wn) [?wn :website/notices]]
         [(visible? ?rn) [?rn :restaurant/name]]
         [(visible? ?nc) [?nc :notice/content]]
         [(visible? ?gm) [?gm :group/menus]]
         [(visible? ?mc) [?mc :menu/categories]]
         [(visible? ?cc) [?cc :category/cuisines]]
         [(visible? ?cs) [?cs :cuisine/species]]
         [(visible? ?sn) [?sn :spec/name]]
         ]))

(defn- e->user
  "Get a user's own entID"
  [user-ident conn]
  (d/pull (d/db conn) '[:customer/id] user-ident))

(defn customer-db []
  (let [db (d/db conn)]
    (map (comp (partial load-entity db) first)
          (e->customer db))))

(defn user-db [uid] (e->user uid conn))

(defn submit-order! [ order-raw ]
  (let [filter #(-> %
                    (update :db/id (fn [n] (d/tempid :db.part/user (- n))) )
                    (update :cuisineItem/ID (fn [ID] (d/entid (d/db conn) [:cuisine/id ID])) )
                    (select-keys [:db/id
                                  :cuisineItem/ID
                                  :cuisineItem/spec
                                  :cuisineItem/qty]) )
        items  (vec (map filter (:cart/cuisineItems order-raw)))
        id     (d/tempid :db.part/user)
        tx     [(-> order-raw
                    (select-keys [:order/customerName
                                  :order/customerPhone
                                  :order/streetAddress
                                  :order/comment
                                  :order/schedule-day
                                  :order/schedule-time ])
                    (assoc :db/id id :order/cuisineItems items))] ]
    @(d/transact conn tx) ))

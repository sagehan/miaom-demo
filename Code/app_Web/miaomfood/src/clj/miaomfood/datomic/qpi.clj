(ns miaomfood.datomic.qpi
  (:require
    [miaomfood.db :refer [conn db]]
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

(defn- e->website [db]
  (d/q '[:find ?e
         :in $ %
         :where (e__website ?e)]
    db
    '[[(e__website ?t)
       [?t :website/title _ ]]
      [(e__website ?n)
       [?n :website/notices _]]
      [(e__website ?c)
       [?c :notice/content _]]
      [(e__webstie ?d)
       [?d :notice/duration _ ]]
      [(e__website ?p)
       [?p :notice/priority _]]]))

(defn- e->user [db]
  (d/q '[:find ?e
         :in $ %
         :where ]))

(defn website-metadata []
  {:entities (map (comp (partial load-entity db) first) (e->website db))})

(defn user-metadata []
  {})

(defn like!
  "A customer up-vote an cuisine"
  [username cuisine]
  (d/transact conn [{:db/id (d/tempid :db.part/user) :customer/username username :customer/favorites cuisine}])
  (user-metadata))

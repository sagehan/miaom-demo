;   Copyright (c) Metadata Partners, LLC. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns miaomfood.utils.io
  (:use [datomic.api :as d])
  (:require [clojure.java.io :as io]
            [mount.core :as mount :refer [defstate]])
  (:import datomic.Util))

(def resource io/resource)

(defn read-all
  "Read all forms in f, where f is any resource that can
   be opened by io/reader"
  [f]
  (Util/readAll (io/reader f)))

(defn transact-all
  "Load and run all transactions from f, where f is any
   resource that can be opened by io/reader."
  [conn f]
  (loop [n 0
         [tx & more] (read-all f)]
    (if tx
      (recur (+ n (count (:tx-data  @(d/transact conn tx))))
             more)
      {:datoms n})))

(ns clojure-purchases.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk])
  (:gen-class))
  

(defn -main []
  (let [purchase (slurp "purchases.csv")
        purchase (str/split-lines purchase)
        purchase (map (fn [line]
                       (str/split line #","))
                     purchase)
        header (first purchase)
        purchase (rest purchase)
        purchase (map (fn [line]
                        (apply hash-map (interleave header line)))
                   purchase)
        purchase (walk/keywordize-keys purchase)
        purchase (filter (fn [line]
                           (=(:category line) "Furniture"))
                   purchase)]
    (spit "filtered_purchase.edn" (pr-str purchase))
    purchase))
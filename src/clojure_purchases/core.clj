(ns clojure-purchases.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk])
  (:gen-class))
  

(defn -main []
  (println "Please type a category")
  (println "i.e. Alcohol, Furniture, Food, Shoes, Toiletries")
  (let [purchase (slurp "purchases.csv")
        text (read-line)
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
                           (=(:category line) text))
                         purchase)]
    (spit "filtered_purchase.edn" (pr-str purchase))
    purchase))
    

   
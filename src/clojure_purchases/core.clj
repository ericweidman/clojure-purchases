(ns clojure-purchases.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [ring.middleware.params :as p]
            [hiccup.core :as h])
  (:gen-class))
  

(defn read-purchase []
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
        purchase (walk/keywordize-keys purchase)]
    ;(spit "filtered_purchase.edn" (pr-str purchase))
    purchase))
    
(defn category-html [purchase]
    (let [all-category (map :category purchase)
           unique-category (set all-category)
           sorted-category (sort unique-category)]
     [:div
      (map (fn [category]
            [:span
             [:a {:href (str "/?category=" category)} category]
             " "])
         sorted-category)]))

(defn purchase-html [purchase]
  [:ul
   (map (fn [purchase]
          [:li (str (:customer_id purchase) " " (:date purchase) " " (:credit_card purchase) " " (:cvv purchase))])
        purchase)])


(c/defroutes app
  (c/GET "/" request
    (let [params (:params request)
          category (get params "category")
          category (or category "Food")
          purchase (read-purchase)
          filtered-purchase (filter (fn [purchase]
                                     (= (:category purchase) category))
                              purchase)]
     (h/html [:html
              [:body 
               (category-html purchase)
               (purchase-html filtered-purchase)]]))))


(defn -main []
  (j/run-jetty (p/wrap-params app) {:port 3000}))
   
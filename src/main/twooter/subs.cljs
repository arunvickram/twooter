(ns twooter.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :get-counter
 (fn [db _]
   (:counter db)))

(rf/reg-sub
 :counter-tappable?
 (fn [db _]
   (:counter-tappable? db)))

(rf/reg-sub
 :navigation/root-state
 (fn [db _]
   (get-in db [:navigation :root-state])))

(rf/reg-sub
 :get-in
 (fn [db [_ path default-value]]
   (get-in db path default-value)))

(rf/reg-sub
 :tweets
 (fn [db _]
   (if (:tweets-failure db)
     (:tweets-failure db)
     (or (:tweets db) []))))

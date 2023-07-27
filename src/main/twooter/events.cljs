(ns twooter.events
  (:require
   [re-frame.core :as rf]
   [superstructor.re-frame.fetch-fx]
   [twooter.db :as db :refer [app-db]]))

(def standard-interceptors  [(when ^boolean goog.DEBUG rf/debug)])

(rf/reg-event-fx
 :initialize-db
 standard-interceptors
 (fn [_ _]
   {:db app-db
    :dispatch [:fetch-tweets]}))

(rf/reg-event-db
 :inc-counter
 (fn [db [_ _]]
   (update db :counter inc)))

(rf/reg-event-db
 :navigation/set-root-state
 (fn [db [_ navigation-root-state]]
   (assoc-in db [:navigation :root-state] navigation-root-state)))

(rf/reg-event-db :assoc-in
  (fn [app-db [_ app-db-path v]]
    (assoc-in app-db app-db-path v)))

(rf/reg-event-db :dissoc-in
  (fn [app-db [_ app-db-path ks]]
    (assert (seqable? ks))
    (update-in app-db app-db-path
      (fn [v]
        (apply dissoc v ks)))))

(rf/reg-event-fx
 :fetch-tweets
 standard-interceptors
 (fn [{:keys [db]}]
  {:fetch {:method :get
           :url "https://jsonplaceholder.typicode.com/posts"
           :response-content-types {#"application/.*json" :json}
           :on-success [:fetch-tweets/success]
           :on-failure [:fetch-tweets/failure]}}))

(rf/reg-event-db
 :fetch-tweets/success
 standard-interceptors
 (fn [db [_ result]]
   (assoc db :tweets (:body result))))

(rf/reg-event-db
 :fetch-tweets/failure
 standard-interceptors
 (fn [db [_ result]]
   (assoc db :tweets-failure result)))

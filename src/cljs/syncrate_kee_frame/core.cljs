(ns syncrate-kee-frame.core
  (:require
    [kee-frame.core :as kf]
    [re-frame.core :as rf]
    [ajax.core :as http]
    [syncrate-kee-frame.ajax :as ajax]
    [syncrate-kee-frame.routing :as routing]
    [syncrate-kee-frame.view :as view]
    [syncrate-kee-frame.db :refer [initial-db]]
    ;; -- auth --
    [syncrate-kee-frame.auth.subs]
    [syncrate-kee-frame.auth.events]
    ;; -- posts --
    [syncrate-kee-frame.posts.subs]
    [syncrate-kee-frame.posts.events]
    [syncrate-kee-frame.posts.controllers]
    ;; -- errors --
    [syncrate-kee-frame.errors.subs]
    [syncrate-kee-frame.errors.events]))


(rf/reg-event-fx
  ::load-about-page
  (constantly nil))

(kf/reg-controller
  ::about-controller
  {:params (constantly true)
   :start  [::load-about-page]})

(rf/reg-sub
  :docs
  (fn [db _]
    (:docs db)))

;(kf/reg-chain
;  ::load-home-page
;  (fn [_ _]
;    {:http-xhrio {:method          :get
;                  :uri             "/docs"
;                  :response-format (http/raw-response-format)
;                  :on-failure      [:common/set-error]}})
;  (fn [{:keys [db]} [_ docs]]
;    {:db (assoc db :docs docs)}))


(kf/reg-controller
  ::home-controller
  {:params (constantly true)
   :start  (fn [])})

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components
  ([] (mount-components true))
  ([debug?]
   (rf/clear-subscription-cache!)
   (kf/start! {:debug?         (boolean debug?)
                :routes         routing/routes
                :hash-routing?  false
                :initial-db     initial-db
                :root-component [view/root-component]})))

(defn init! [debug?]
  (ajax/load-interceptors!)
  (mount-components debug?))

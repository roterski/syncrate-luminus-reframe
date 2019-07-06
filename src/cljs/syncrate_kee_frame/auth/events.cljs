(ns syncrate-kee-frame.auth.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db reg-fx dispatch inject-cofx]]
            [ajax.core :as http]
            [clojure.walk :as w]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(def token-key "auth-token")

(reg-event-fx
  :log-out
  (fn-traced [{:keys [db]} _]
    {:db (assoc-in db [:auth] nil)
     :navigate-to [:log-in]
     :fb-log-out nil
     :remove-local-storage token-key}))

(reg-event-fx
  :fb-log-in
  (fn-traced []
    {:fb-log-in nil}))

(reg-fx
  :fb-log-in
  (fn []
    (js/window.FB.login #(dispatch [:handle-fb-auth %]))))

(reg-fx
  :fb-log-out
  (fn []
    (js/window.FB.logout)))

(reg-event-fx
  :handle-fb-auth
  (fn-traced [{:keys [db]} [_ response]]
    (let [parsed-response (w/keywordize-keys (js->clj response))
          next-event (if (= (:status parsed-response) "connected")
                       [:authenticate-backend (get-in parsed-response [:authResponse :accessToken])]
                       [:authentication-failed parsed-response])]
      {:dispatch next-event})))

(reg-event-fx
  :authenticate-backend
  (fn-traced [{:keys [db]} [_ fb-token]]
    (let [body {:fb-token fb-token}]
      {:http-xhrio {:method :post
                    :uri    "/api/authenticate_fb"
                    :headers {:content-type "application/json"}
                    :response-format (http/json-response-format)
                    :body (js/JSON.stringify (clj->js body))
                    :on-success [:authentication-succeeded]
                    :on-failure [:authentication-failed]}})))

(reg-event-fx
  :authentication-succeeded
  (fn-traced [{:keys [db]} [_ response]]
    (let [auth (w/keywordize-keys response)]
      (do
        {:db (update-in db [:auth] merge auth)
         :set-local-storage [token-key (:auth-token auth)]}))))

(reg-event-fx
  :authentication-failed
  (fn-traced [{:keys [db]} [_ response]]
    {:db (assoc-in db [:errors :auth] response)
     :dispatch [:log-out]}))

(reg-event-fx
  :restore-auth
  [(inject-cofx :get-local-storage token-key)]
  (fn-traced [{:keys [db get-local-storage]} [_]]
    {:db (assoc-in db [:auth :auth-token] get-local-storage)}))

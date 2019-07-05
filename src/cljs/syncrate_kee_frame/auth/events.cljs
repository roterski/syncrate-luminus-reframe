(ns syncrate-kee-frame.auth.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db dispatch]]
            [ajax.core :as http]
            [clojure.walk :as w]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(reg-event-fx
  :log-out
  (fn-traced [{:keys [db]} _]
    (do
      (js/window.FB.logout)
      {:db (assoc-in db [:auth] nil)
       :navigate-to [:log-in]})))

(reg-event-fx
  :fb-log-in
  (fn-traced []
    (do
      (js/window.FB.login #(dispatch [:handle-fb-auth %]))
      {})))

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

(reg-event-db
  :authentication-succeeded
  (fn-traced [db [_ response]]
    (-> db
      (update-in [:auth] merge (w/keywordize-keys response)))))

(reg-event-fx
  :authentication-failed
  (fn-traced [{:keys [db]} [_ response]]
    {:db (assoc-in db [:errors :auth] response)
     :dispatch [:log-out]}))

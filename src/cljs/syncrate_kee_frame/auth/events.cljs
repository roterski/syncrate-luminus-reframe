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
      {:db (assoc-in db [:auth] nil)})))

(reg-event-fx
  :fb-log-in
  (fn-traced []
    (do
      (js/window.FB.login #(dispatch [:set-fb-auth %]))
      {})))

(reg-event-fx
  :check-fb-auth
  (fn-traced []
    (do
      (js/window.FB.getLoginStatus #(dispatch [:set-fb-auth %]))
      {})))

(reg-event-fx
  :set-fb-auth
  (fn-traced [{:keys [db]} [_ response]]
    (let [parsed-response (w/keywordize-keys (js->clj response))
          next-event (if (= (:status parsed-response))
                       [:authenticate-backend]
                       [:authentication-failed parsed-response])]
      {:db (assoc-in db [:auth :fb-auth] parsed-response)
       :dispatch next-event})))

;(reg-event-fx
;  :set-fb-auth
;  (fn-traced [{:keys [db]} [_ response]]
;             (let [parsed-response (w/keywordize-keys (js->clj response))
;                   db-update {:db (assoc-in db [:auth :fb-auth] parsed-response)}]
;               (if (= (:status parsed-response) "connected")
;                 (merge db-update {:dispatch [:authenticate-backend]})
;                 db-update))))


(reg-event-fx
  :authenticate-backend
  (fn-traced [{:keys [db]} _]
    (let [body (-> db
                   (get-in [:auth :fb-auth :authResponse])
                   (select-keys [:accessToken :userID]))]
      {:http-xhrio {:method :post
                    :uri    "/api/authenticate_fb"
                    :headers {:content-type "application/json"}
                    :response-format (http/json-response-format)
                    :body (js/JSON.stringify (clj->js body))
                    :on-success [:authentication-succeeded]
                    :on-failure [:authentication-failed]}})))


;(reg-event-fx
;  :create-post
;  posts-interceptors
;  (fn-traced [db [_ post]]
;             {:http-xhrio {:method :post
;                           :uri "/api/posts"
;                           :body (js/JSON.stringify (clj->js post))
;                           :headers {:content-type "application/json"}
;                           :response-format (http/json-response-format)
;                           :on-success      [:post-created]
;                           :on-failure      [:common/set-error]}}))
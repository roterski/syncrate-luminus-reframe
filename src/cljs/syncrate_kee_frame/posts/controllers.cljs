(ns syncrate-kee-frame.posts.controllers
  (:require [kee-frame.core :as kf]
            [ajax.core :as http]))


(kf/reg-controller
  ::post-controller
  {:params (fn [{:keys [path-params]}]
             (:post-id path-params))
   :start  (fn [_ post-id]
             [:set-active-post [post-id]])})

(kf/reg-chain
  ::load-posts-page
  (fn [_ _]
    {:http-xhrio {:method          :get
                  :uri             "api/posts"
                  :response-format (http/json-response-format)
                  :on-success      [:posts-loaded-successfully]
                  :on-failure      [:common/set-error]}}))

(kf/reg-controller
  ::posts-controller
  {:params (constantly true)
   :start [::load-posts-page]})

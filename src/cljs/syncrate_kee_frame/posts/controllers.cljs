(ns syncrate-kee-frame.posts.controllers
  (:require [kee-frame.core :as kf]
            [ajax.core :as http]))

(kf/reg-controller
  ::posts-controller
  {:params (fn [{:keys [data]}]
             (when (= (:name data) :posts)
               true))
   :start [:load-posts]})

(kf/reg-controller
  ::post-controller
  {:params (fn [{:keys [path-params]}]
             (:post-id path-params))
   :start  (fn [_ post-id]
             [:load-post [post-id]])})

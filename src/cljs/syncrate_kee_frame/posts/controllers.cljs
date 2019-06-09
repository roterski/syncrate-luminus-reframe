(ns syncrate-kee-frame.posts.controllers
  (:require [kee-frame.core :as kf]))


(kf/reg-controller
  ::post-controller
  {:params (fn [{:keys [path-params]}]
             (:post-id path-params))
   :start  (fn [_ post-id]
             [:set-active-post [post-id]])})

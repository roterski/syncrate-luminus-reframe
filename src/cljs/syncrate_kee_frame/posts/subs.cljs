(ns syncrate-kee-frame.posts.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :posts
  (fn [db _]
    (:posts db)))

(reg-sub
  :active-post
  (fn [db _]
    (:active-post db)))

(reg-sub
  :post
  :<- [:posts]
  :<- [:active-post]
  (fn [[posts active-post] _]
    (get posts active-post)))

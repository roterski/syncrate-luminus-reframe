(ns syncrate-kee-frame.posts.views.post-page
  (:require [re-frame.core :as rf]
            [syncrate-kee-frame.components.page-nav :refer [page-nav]]
            ;[app.posts.views.comment-item :refer [comment-item]]
            ;[app.posts.views.comment-tree :refer [flat-comment-tree]]
            ;[breaking-point.core :as bp]
            ["@smooth-ui/core-sc" :refer [Typography Box]]))

(defn post-page
  []
  (let [{:keys [title body]} @(rf/subscribe [:post])]
        ;comments @(rf/subscribe [:post-comments])
        ;screen-width @(rf/subscribe [::bp/screen-width])]
    [:<>
     [page-nav {:left :posts
                :center "Post"}]
     [:> Box {:class "post-card"
              :mb 3
              :p 2}
      [:> Typography {:variant "h1"}
       title]
      [:> Box {:p 2}
       body]]]))
     ;(for [comment (flat-comment-tree comments)]
     ;  ^{:key (:id comment)}
     ;  [comment-item comment screen-width])]))

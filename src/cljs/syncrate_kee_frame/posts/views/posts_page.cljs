(ns syncrate-kee-frame.posts.views.posts-page
  (:require [re-frame.core :as rf]
            [kee-frame.core :as kf]
            [syncrate-kee-frame.components.page-nav :refer [page-nav]]
            [syncrate-kee-frame.posts.views.post-list :refer [post-list]]
            ["@smooth-ui/core-sc" :refer [Button Typography]]))

(defn posts-page
  []
  (let [posts @(rf/subscribe [:posts])]
    [:<>
     [page-nav {:center "Posts"
                :right [:> Button {:as "a"
                                   :color "white"
                                   :href (kf/path-for [:new-post])}
                        "New post"]}]
     (when (seq posts)
       [:<>
        [post-list posts]])]))

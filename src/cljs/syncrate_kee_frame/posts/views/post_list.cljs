(ns syncrate-kee-frame.posts.views.post-list
  (:require ["@smooth-ui/core-sc" :refer [Box]]
            [syncrate-kee-frame.posts.views.post-item :refer [post-item]]))


(defn post-list
  [items]
  [:> Box {:class "list"}
   (for [[key post] items]
     ^{:key (:id post)}
     [post-item post])])

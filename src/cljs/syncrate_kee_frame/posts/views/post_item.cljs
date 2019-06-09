(ns syncrate-kee-frame.posts.views.post-item
  (:require [kee-frame.core :as kf]
            ["@smooth-ui/core-sc" :refer [Button Box Typography]]))

(defn trunc
  [string max-length]
  (let [length (count string)
        exceeded? (> length max-length)]
    (if exceeded?
      (str (subs string 0 (min length max-length)) "...")
      string)))

(defn post-item
  [{:keys [id title body]}]
  [:> Box {:as "a"
           :href (kf/path-for [:post {:post-id id}])
           :class "recipe-card"}
   [:> Box {:p 2}
    [:> Typography {:variant "h6"
                    :font-weight 700}
     title]
    [:> Box {:pl 2
             :pb 2
             :display "flex"}
     (trunc body 50)]]])

(ns syncrate-kee-frame.nav.views.nav-item
  (:require ["@smooth-ui/core-sc" :refer [Box]]
            [re-frame.core :as rf]
            [kee-frame.core :as kf]))

(defn nav-item [{:keys [title page]}]
  (let [is-active (= page @(rf/subscribe [:nav/page]))]
    [:> Box {:href   (kf/path-for [page])
             :as "a"
             :ml 2
             :pb 10
             :border-bottom (when is-active "2px solid #102A43")
             :class (when is-active "is-active")}
     title]))
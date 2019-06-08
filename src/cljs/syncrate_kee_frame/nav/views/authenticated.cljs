(ns syncrate-kee-frame.nav.views.authenticated
  (:require [syncrate-kee-frame.nav.views.nav-item :refer [nav-item]]
            ["@smooth-ui/core-sc" :refer [Box]]))

(defn authenticated
  []
  (let [nav-items [{:page :posts
                    :title "Posts"}
                   {:page :about
                    :title "About"}]]
    [:> Box {:display "flex"
             :justify-content "flex-end"
             :py 1}
     (for [{:keys [title page]} nav-items]
       [nav-item {:key page
                  :page page
                  :title title}])]))

(ns syncrate-kee-frame.nav.views.public
  (:require [syncrate-kee-frame.nav.views.nav-item :refer [nav-item]]
            ["@smooth-ui/core-sc" :refer [Box]]))

(defn public
  []
  (let [nav-items [{:page :about
                    :title "About"}
                   {:page :log-in
                    :title "Login"}]]
    [:> Box {:display "flex"
             :justify-content "flex-end"
             :py 1}
     (for [{:keys [title page]} nav-items]
       [nav-item {:key page
                  :page page
                  :title title}])]))

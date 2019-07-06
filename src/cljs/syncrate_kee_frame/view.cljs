(ns syncrate-kee-frame.view
  (:require
    [kee-frame.core :as kf]
    [markdown.core :refer [md->html]]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [syncrate-kee-frame.theme :refer [syncrate-theme]]
    ["@smooth-ui/core-sc" :refer [Normalize ThemeProvider Grid Row Col Box Button]]
    ;; -- nav --
    [syncrate-kee-frame.nav.views.nav :refer [nav]]
    ;; -- auth --
    [syncrate-kee-frame.auth.views.log-in :refer [log-in]]
    ;; -- posts --
    [syncrate-kee-frame.posts.views.posts-page :refer [posts-page]]
    [syncrate-kee-frame.posts.views.post-page :refer [post-page]]
    [syncrate-kee-frame.posts.views.new-post :refer [new-post]]))

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.content
   (when-let [docs @(rf/subscribe [:docs])]
     [:div {:dangerouslySetInnerHTML {:__html (md->html docs)}}])])

(defn root-component []
  [:<>
   [:> Normalize]
   [:> ThemeProvider {:theme syncrate-theme}
    [:> Grid {:fluid false}
     [:> Row
      [:> Col
       [nav]
       [kf/switch-route (fn [route] (get-in route [:data :name]))
        :home home-page
        :about about-page
        :posts posts-page
        :post post-page
        :new-post new-post
        :log-in log-in
        nil [:div ""]]]]]]])

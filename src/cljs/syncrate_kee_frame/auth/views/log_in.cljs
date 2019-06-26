(ns syncrate-kee-frame.auth.views.log-in
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [kee-frame.core :as kf]
            [syncrate-kee-frame.components.page-nav :refer [page-nav]]
            [syncrate-kee-frame.components.form-group :refer [form-group]]
            ["@smooth-ui/core-sc" :refer [Row Col FormGroup Label Input Box Button]]))

(defn log-in
  []
  (let [initial-values {:email "" :password ""}
        values (r/atom initial-values)]
    (fn []
      [:> Row {:justify-content "center"}
       [:> Col {:xs 12 :sm 6}
        [page-nav {:center "Log in"}]
        [form-group {:id :email
                     :label "Email"
                     :type "email"
                     :values values}]
        [form-group {:id :password
                     :label "Password"
                     :type "password"
                     :values values}]
        [:> Box {:display "flex"
                 :justify-content "space-between"}
         [:> Box {:py 1
                  :pr 2}]
          ;[:a {:href (kf/path-for :sign-up)}
          ; "New to Syncrate? Create an account!"]]
         [:> Box
          [:> Button {:on-click #(rf/dispatch [:log-in @values])}
           "Log in"]]]]])))

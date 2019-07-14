(ns syncrate-kee-frame.posts.views.new-post
  (:require
            [syncrate-kee-frame.components.page-nav :refer [page-nav]]
            [syncrate-kee-frame.components.form-group :refer [form-group]]
            [syncrate-kee-frame.validation :refer [validate post-schema]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]
            ["@smooth-ui/core-sc" :refer [Row Col Box Typography Textarea Button]]
            ["styled-icons/fa-regular/CheckCircle" :refer [CheckCircle]]))

(defn new-post
  []
  (let [initial-values {:title "" :body ""}
        form-key :POST_api_posts
        values (r/atom initial-values)
        save (fn [event vals]
               (.preventDefault event)
               (let [[errors data] (validate vals post-schema)]
                 (if errors
                   (rf/dispatch [:common/set-validation-errors errors form-key])
                   (rf/dispatch [:create-post @values]))))]
    (fn []
      [:> Box
       [page-nav {:left :posts
                  :center "New Post"}]
       [:> Box {:py 5}
        [:> Row
         [:> Col {:xs 12 :md 6}
          [:form
           [:> Row
            [form-group {:id :title
                         :form-key form-key
                         :label "Title"
                         :type "text"
                         :values values}]]
           [:> Row
            [form-group {:id :body
                         :form-key form-key
                         :label "Body"
                         :type "textarea"
                         :element Textarea
                         :values values}]]
           [:> Row
            [:> Button {:on-click #(save % @values)}
             "Save"]]]]]]])))

(ns syncrate-kee-frame.posts.views.new-post
  (:require
            [syncrate-kee-frame.components.page-nav :refer [page-nav]]
            [syncrate-kee-frame.components.form-group :refer [form-group]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]
            ["@smooth-ui/core-sc" :refer [Row Col Box Typography Textarea Button]]
            ["styled-icons/fa-regular/CheckCircle" :refer [CheckCircle]]))

(defn new-post
  []
  (let [initial-values {:title "" :body ""}
        values (r/atom initial-values)
        save (fn [event {:keys [title body]}]
               (.preventDefault event)
               (when (and (not (str/blank? title))
                          (not (str/blank? body)))))]
                 ;(rf/dispatch [:upsert-post values]
                 ;               (reset! values initial-values)))
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
                         :label "Title"
                         :type "text"
                         :values values}]]
           [:> Row
            [form-group {:id :body
                         :label "Body"
                         :type "textarea"
                         :element Textarea
                         :values values}]]
           [:> Row
            [:> Button {:on-click #(save % @values)}
             "Save"]]]]]]])))

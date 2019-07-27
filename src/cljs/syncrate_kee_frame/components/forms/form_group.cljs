(ns syncrate-kee-frame.components.forms.form-group
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [syncrate-kee-frame.validation :refer [validate-field]]
            [clojure.string :as str]
            ["@smooth-ui/core-sc" :refer [FormGroup Label Input Textarea ControlFeedback]]))

(def skip [:__scTheme :theme :control :valid])

(def r-input
  (r/reactify-component
    (fn [props]
      [:input (apply dissoc props skip)])))

(def r-textarea
  (r/reactify-component
    (fn [props]
      [:textarea (apply dissoc props skip)])))

(defn form-group
  [{:keys [id form-key label type schema values element on-key-down] :or {element Input}}]
  (let [errors @(rf/subscribe [:forms/errors form-key])
        input-error (when-let [errs (get errors id)]
                      (clojure.string/join ", " errs))
        textarea (= element Textarea)
        input (= element Input)
        value (id @values)
        validate (fn []
                   (let [errors (first (validate-field value id schema))]
                     (if errors
                       (rf/dispatch [:errors/set-validation-errors errors form-key])
                       (rf/dispatch [:errors/set-validation-errors {id nil} form-key]))))]
    [:> FormGroup
     [:> Label {:html-for id} label]
     [:> element {:as (cond
                        input r-input
                        textarea r-textarea)
                  :control true
                  :valid (not input-error)
                  :on-blur validate
                  :rows (when textarea 6)
                  :id id
                  :type type
                  :value value
                  :on-change (fn [ev]
                               (let [val (.. ev -target -value)]
                                 (rf/dispatch [:forms/set-form-values form-key {id val}])))
                  :on-key-down on-key-down}]
                  ;:on-key-up (when-not (str/blank? (id @values)) validate)}]
     (when input-error
       [:> ControlFeedback {:valid false}
        input-error])]))

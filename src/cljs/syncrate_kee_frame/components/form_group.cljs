(ns syncrate-kee-frame.components.form-group
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
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
  [{:keys [id form-key label type values element on-key-down] :or {element Input}}]
  (let [errors @(rf/subscribe [:form-errors form-key])
        input-error (when-let [errs (get errors id)]
                      (clojure.string/join ", " errs))
        is-empty? (str/blank? (id @values))
        textarea (= element Textarea)
        input (= element Input)]
        ;validate (fn []
        ;           (if is-empty?
        ;             (rf/dispatch [:has-value? id])
        ;             (rf/dispatch [:clear-error id])))]
    [:> FormGroup
     [:> Label {:html-for id} label]
     [:> element {:as (cond
                        input r-input
                        textarea r-textarea)
                  :control true
                  :valid (not input-error)
                  ;:on-blur validate
                  :rows (when textarea 6)
                  :id id
                  :type type
                  :value (id @values)
                  :on-change (fn [ev]
                               (let [val (.. ev -target -value)]
                                 (rf/dispatch [:set-form-values form-key {id val}])))
                  :on-key-down on-key-down}]
                  ;:on-key-up (when-not (str/blank? (id @values)) validate)}]
     (when input-error
       [:> ControlFeedback {:valid false}
        input-error])]))

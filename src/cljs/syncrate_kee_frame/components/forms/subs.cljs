(ns syncrate-kee-frame.components.forms.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :forms/errors
  (fn [db [_ form-key]]
    (get-in db [:forms form-key :errors])))

(reg-sub
  :forms/values
  (fn [db [_ form-key]]
    (get-in db [:forms form-key :values])))

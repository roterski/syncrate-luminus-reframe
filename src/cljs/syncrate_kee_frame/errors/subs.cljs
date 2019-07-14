(ns syncrate-kee-frame.errors.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :errors
  (fn [db _]
    (:errors db)))

(reg-sub
  :form-errors
  :<- [:errors]
  (fn [errors [_ form-key]]
    (get-in errors [:validations form-key])))

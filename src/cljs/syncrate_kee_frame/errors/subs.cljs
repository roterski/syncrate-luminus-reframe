(ns syncrate-kee-frame.errors.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :errors
  (fn [db _]
    (:errors db)))

(reg-sub
  :form-errors
  (fn [db [_ form-key]]
    (get-in db [:forms form-key :errors])))

(reg-sub
  :form-values
  (fn [db [_ form-key]]
    (get-in db [:forms form-key :values])))

(reg-sub
  :db
  (fn [db [_]]
    db))
(ns syncrate-kee-frame.errors.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :errors
  (fn [db _]
    (:errors db)))

(reg-sub
  :db
  (fn [db [_]]
    db))
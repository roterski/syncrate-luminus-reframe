(ns syncrate-kee-frame.auth.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :uid
  (fn [db _]
    (get-in db [:auth :uid])))

(reg-sub
  :logged-in?
  :<- [:uid]
  (fn [uid _]
    (boolean uid)))

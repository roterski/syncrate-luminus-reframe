(ns syncrate-kee-frame.auth.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :current-user
  (fn [db _]
    (get-in db [:auth :current-user])))

(reg-sub
  :logged-in?
  :<- [:current-user]
  (fn [current-user _]
    (boolean current-user)))

(reg-sub
  :auth-token
 (fn [db _]
   (get-in db [:auth :auth-token])))

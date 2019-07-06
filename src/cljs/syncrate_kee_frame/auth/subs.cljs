(ns syncrate-kee-frame.auth.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :auth-token
 (fn [db _]
   (get-in db [:auth :auth-token])))

(reg-sub
  :logged-in?
  :<- [:auth-token]
  (fn [auth-token _]
    (boolean auth-token)))

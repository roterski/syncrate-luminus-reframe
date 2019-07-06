(ns syncrate-kee-frame.effects
  (:require [re-frame.core :refer [reg-cofx reg-fx]]))

(reg-cofx
  :get-local-storage
  (fn [coeffects key]
    (assoc coeffects
      :get-local-storage
      (js->clj (js/localStorage.getItem key)))))

(reg-fx
  :set-local-storage
  (fn [[key value]]
    (js/localStorage.setItem key value)))

(reg-fx
  :remove-local-storage
  (fn [key]
    (js/localStorage.removeItem key)))

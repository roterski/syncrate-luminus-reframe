(ns syncrate-kee-frame.components.forms.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [clojure.walk :as w]
            [syncrate-kee-frame.utils :refer [build-form-key]]))

(reg-event-db
  :forms/set-form-values
  (fn-traced [db [_ form-key values]]
             (update-in db [:forms form-key :values] #(merge % values))))

(reg-event-db
  :forms/clear-form
  (fn-traced [db [_ form-key]]
             (assoc-in db [:forms form-key] {})))

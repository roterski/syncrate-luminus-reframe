(ns syncrate-kee-frame.errors.events
  (:require [re-frame.core :refer [reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))
            ;[app.spec :refer [check-spec-interceptor]]))

;(def errors-interceptors [check-spec-interceptor])

(reg-event-db
  :has-value?
  ;errors-interceptors
  (fn-traced [db [_ id]]
    (assoc-in db [:errors id] "Can't be blank")))

(reg-event-db
  :clear-error
  ;errors-interceptors
  (fn-traced [db [_ id]]
    (update-in db [:errors] dissoc id)))

(reg-event-db
  :common/set-error
  (fn-traced [db [_ error]]
    (do
      (js/console.error error)
      (-> db
          (update-in [:errors] #(merge % {:error error}))))))


(reg-event-db
  :common/set-request-error
  (fn-traced [db [_ error]]
    (do
      (js/console.error error)
      (-> db
          (update-in [:errors] #(merge % {:request error}))))))

(ns syncrate-kee-frame.errors.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [syncrate-kee-frame.spec :refer [check-spec-interceptor]]))

(def errors-interceptors [check-spec-interceptor])

(reg-event-db
  :has-value?
  errors-interceptors
  (fn-traced [db [_ id]]
    (assoc-in db [:errors id] "Can't be blank")))

(reg-event-db
  :clear-error
  errors-interceptors
  (fn-traced [db [_ id]]
    (update-in db [:errors] dissoc id)))

(reg-event-db
  :common/set-error
  errors-interceptors
  (fn-traced [db [_ error]]
    (do
      (js/console.error error)
      (-> db
          (update-in [:errors] #(merge % {:error error}))))))

(reg-event-fx
  :common/set-request-error
  errors-interceptors
  (fn-traced [{:keys [db]} [_ error]]
    (do
      (js/console.error error)
      (merge {:db (assoc-in db [:errors :request] error)}
             (when (= (:status error) 403)
              {:navigate-to [:log-in]})))))

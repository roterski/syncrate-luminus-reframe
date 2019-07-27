(ns syncrate-kee-frame.errors.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [clojure.walk :as w]
            [syncrate-kee-frame.utils :refer [build-form-key]]
            [syncrate-kee-frame.spec :refer [check-spec-interceptor]]))

(def errors-interceptors [check-spec-interceptor])

(reg-event-db
  :errors/set-error
  errors-interceptors
  (fn-traced [db [_ error]]
    (do
      (js/console.error error)
      (-> db
          (update-in [:errors :latest-error] #(merge % error))))))

(reg-event-fx
  :errors/handle-server-validation-error
  errors-interceptors
  (fn-traced [db [_ response]]
    (let [parsed (w/keywordize-keys response)
          form-key (build-form-key parsed)
          validation-errors (get-in parsed [:response :errors])]
        (if (= (:status parsed) 400)
          {:dispatch [:common/set-validation-errors validation-errors form-key]}
          {:dispatch [:common/set-error validation-errors]}))))

(reg-event-db
  :errors/set-validation-errors
  (fn-traced [db [_ errors form-key]]
    (-> db
        (update-in [:forms form-key :errors] #(merge % errors)))))

(reg-event-fx
  :errors/set-request-error
  errors-interceptors
  (fn-traced [{:keys [db]} [_ error]]
    (do
      (js/console.error error)
      (merge {:db (assoc-in db [:errors :request] error)}
             (when (= (:status error) 403)
              {:navigate-to [:log-in]})))))

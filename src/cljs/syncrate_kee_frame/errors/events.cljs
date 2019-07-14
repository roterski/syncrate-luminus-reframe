(ns syncrate-kee-frame.errors.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [clojure.walk :as w]
            [clojure.string :as s]
            [syncrate-kee-frame.spec :refer [check-spec-interceptor]]))

(def errors-interceptors [check-spec-interceptor])

;(reg-event-db
;  :has-value?
;  errors-interceptors
;  (fn-traced [db [_ id]]
;    (assoc-in db [:errors id] "Can't be blank")))
;
;(reg-event-db
;  :clear-error
;  errors-interceptors
;  (fn-traced [db [_ id]]
;    (update-in db [:errors] dissoc id)))

(reg-event-db
  :common/set-error
  errors-interceptors
  (fn-traced [db [_ error]]
    (do
      (js/console.error error)
      (-> db
          (update-in [:errors :latest-error] #(merge % error))))))

(defn build-form-key
  [response]
  (-> (select-keys response [:last-method :uri])
      vals
      s/join
      (s/replace "/" "_")
      keyword))

(reg-event-fx
  :common/handle-server-validation-error
  errors-interceptors
  (fn-traced [db [_ response]]
    (let [parsed (w/keywordize-keys response)
          form-key (build-form-key parsed)
          validation-errors (get-in parsed [:response :errors])]
        (if (= (:status parsed) 400)
          {:dispatch [:common/set-validation-errors validation-errors form-key]}
          {:dispatch [:common/set-error validation-errors]}))))

(reg-event-db
  :common/set-validation-errors
  (fn-traced [db [_ errors form-key]]
    (-> db
        (update-in [:errors :validations form-key] #(merge % errors)))))

(reg-event-fx
  :common/set-request-error
  errors-interceptors
  (fn-traced [{:keys [db]} [_ error]]
    (do
      (js/console.error error)
      (merge {:db (assoc-in db [:errors :request] error)}
             (when (= (:status error) 403)
              {:navigate-to [:log-in]})))))

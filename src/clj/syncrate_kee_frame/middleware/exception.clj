(ns syncrate-kee-frame.middleware.exception
  (:require [reitit.ring.middleware.exception :as exception]
            [clojure.tools.logging :as log]
            [clojure.core.match :refer [match]]
            [syncrate-kee-frame.validation :refer [error-key]]
            [ring.util.http-response :as response]))

(def exception-middleware
  (exception/create-exception-middleware
   (merge
    exception/default-handlers
    {;; log stack-traces for all exceptions
     ::exception/wrap (fn [handler e request]
                        (log/error e (.getMessage e))
                        (handler e request))})))

(defn handle-exception [exception action]
  (let [error (Throwable->map exception)]
    (match (:cause error)
      error-key
      (response/bad-request {:errors (:data error)})
      ;; else
      (response/internal-server-error {:errors {:server-error [(str "Failed to " action "!")]}}))))

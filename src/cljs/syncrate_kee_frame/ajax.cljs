(ns syncrate-kee-frame.ajax
  (:require
    [ajax.core :as ajax]
    [luminus-transit.time :as time]
    [cognitect.transit :as transit]
    [re-frame.core :as rf]))

(defn local-uri? [{:keys [uri]}]
  (not (re-find #"^\w+?://" uri)))

(defn build-headers []
  (let [token @(rf/subscribe [:auth-token])]
    (merge {:Content-Type "application/json"}
      (when token
        {:Authorization (str "Token " token)
         "x-csrf-token" js/csrfToken}))))

(defn default-headers [request]
  (if (local-uri? request)
    (-> request
        (update :headers #(merge (build-headers) %)))
    request))


;; injects transit serialization config into request options
(defn as-transit [opts]
  (merge {:raw             false
          :format          :transit
          :response-format :transit
          :reader          (transit/reader :json time/time-deserialization-handlers)
          :writer          (transit/writer :json time/time-serialization-handlers)}
         opts))

(defn load-interceptors! []
  (swap! ajax/default-interceptors
         conj
         (ajax/to-interceptor {:name "default headers"
                               :request default-headers})))

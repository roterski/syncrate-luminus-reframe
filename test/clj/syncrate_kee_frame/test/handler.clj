(ns syncrate-kee-frame.test.handler
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [syncrate-kee-frame.handler :refer :all]
    [syncrate-kee-frame.auth.auth :refer [sign-token]]
    [syncrate-kee-frame.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'syncrate-kee-frame.config/env
                 #'syncrate-kee-frame.handler/app)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      ;; returns home page because of hash-less frontend navigation
      (is (= 200 (:status response))))))

  ; PENDING
  ;(testing "not-found api route"
  ;  (let [response (app (request :get "/api/invalid"))]
  ;    (is (= 404 (:status response))))))


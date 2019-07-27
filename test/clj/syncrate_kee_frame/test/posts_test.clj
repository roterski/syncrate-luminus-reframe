(ns syncrate-kee-frame.test.posts-test
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [syncrate-kee-frame.handler :refer :all]
    [syncrate-kee-frame.auth.auth :refer [sign-token]]
    [syncrate-kee-frame.middleware.formats :as formats]
    [syncrate-kee-frame.db.core :refer [*db*] :as db]
    [luminus-migrations.core :as migrations]
    [clojure.java.jdbc :as jdbc]
    [syncrate-kee-frame.config :refer [env]]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(def token
  (str "Token " (sign-token {:id 1 :facebook_id "1"})))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'syncrate-kee-frame.config/env
                 ;#'syncrate-kee-frame.db.core/*db*
                 #'syncrate-kee-frame.handler/app)
    ;(migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-app
  ;(jdbc/with-db-transaction [t-conn *db*]
  ;  (jdbc/db-set-rollback-only! t-conn)
    (testing "create-post"
      (testing "unauthorized"
        (let [response (app (-> (request :post "/api/posts")
                                (json-body {:title "T" :body "Body"})
                                (content-type "application/json")))]
          (is (= 403 (:status response)))
          (is (= {:errors {:access "Access to /api/posts is not authorized"}} (m/decode-response-body response)))))
      (testing "valid"
        (let [body {:title "Title" :body "Body"}
              response (app (-> (request :post "/api/posts")
                                (json-body body)
                                (content-type "application/json")
                                (header "Authorization" token)))]
          (is (= 200 (:status response)))
          (is (= body (-> response
                          m/decode-response-body
                          (select-keys [:title :body]))))))
      (testing "invalid"
        (let [body {:title "T" :body "Body"}
              response (app (-> (request :post "/api/posts")
                                (json-body body)
                                (content-type "application/json")
                                (header "Authorization" token)))]
          (is (= 400 (:status response)))
          (is (= {:errors {:title "less than the minimum 3"}} (m/decode-response-body response)))))))

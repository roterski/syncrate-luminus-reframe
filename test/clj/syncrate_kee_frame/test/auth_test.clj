(ns syncrate-kee-frame.test.auth-test
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [syncrate-kee-frame.handler :refer :all]
    [syncrate-kee-frame.auth.auth :refer [sign-token get-or-create-user]]
    [syncrate-kee-frame.middleware.formats :as formats]
    [syncrate-kee-frame.db.core :refer [*db*] :as db]
    [luminus-migrations.core :as migrations]
    [clojure.java.jdbc :as jdbc]
    [cheshire.core :as ch]
    [syncrate-kee-frame.config :refer [env]]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(def token
  (str "Token " (sign-token {:id 1 :facebook_id "1"})))

(defn fb-data [user-data]
  {:status 200,
   :headers {"Content-Type" "text/javascript; charset=UTF-8",
             "Access-Control-Allow-Origin" "*",
             "Content-Length" "69",
             "X-FB-Debug" "3uAZF4ULJqYBiXDxIWma7CFzaQYuJA2GBKRemdaQzv5RfM4+nbZcsIsybEghTmI9kqiUKLvpYCUNO04vYLQvXg==",
             "facebook-api-version" "v3.3",
             "Strict-Transport-Security" "max-age=15552000; preload",
             "Connection" "close",
             "Pragma" "no-cache",
             "Expires" "Sat, 01 Jan 2000 00:00:00 GMT",
             "x-fb-rev" "1000915067",
             "ETag" "\"672c2fdee9812dd467d3060c2473aca1b028acf5\"",
             "x-fb-trace-id" "A4TlxULgr+p",
             "Date" "Sat, 06 Jul 2019 14:26:02 GMT",
             "x-fb-request-id" "AMvEqTLkhcyamXpmC5TMgMS",
             "Cache-Control" "private, no-cache, no-store, must-revalidate",
             "x-app-usage" "{\"call_count\":0,\"total_cputime\":0,\"total_time\":0}"},
   :body (ch/generate-string user-data)
   :request-time 136,
   :trace-redirects ["https://graph.facebook.com/v3.3/me"],
   :orig-content-encoding nil})

(use-fixtures
  :once
  (fn [f]
    (mount/start #'syncrate-kee-frame.config/env
                 #'syncrate-kee-frame.db.core/*db*
                 #'syncrate-kee-frame.handler/app)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-app
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (testing "get-or-create-user"
      (testing "when user exists"
        (let [user-data {:facebook_id "12345" :first_name "Sam" :last_name "Smith"}
              existing-user (db/create-user!
                              t-conn
                              (merge {:email nil} user-data))]
          (is (= existing-user (get-or-create-user (fb-data user-data)))))))))

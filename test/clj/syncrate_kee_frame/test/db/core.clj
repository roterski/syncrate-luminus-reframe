(ns syncrate-kee-frame.test.db.core
  (:require
    [syncrate-kee-frame.db.core :refer [*db*] :as db]
    [luminus-migrations.core :as migrations]
    [clojure.test :refer :all]
    [clojure.java.jdbc :as jdbc]
    [syncrate-kee-frame.config :refer [env]]
    [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'syncrate-kee-frame.config/env
      #'syncrate-kee-frame.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= {:first_name "Sam"
            :last_name  "Smith"
            :facebook_id "1234"
            :email      "sam.smith@example.com"
            :admin      false}
           (dissoc (db/create-user!
                     t-conn
                     {:first_name "Sam"
                      :last_name  "Smith"
                      :facebook_id "1234"
                      :email      "sam.smith@example.com"})
             :id :last_login :created_at)))
    (is (= {:first_name "Sam"
            :last_name  "Smith"
            :facebook_id "1234"
            :email      "sam.smith@example.com"
            :admin      false}
           (dissoc (db/get-user t-conn {:facebook_id "1234"}) :id :last_login :created_at)))))

(ns syncrate-kee-frame.auth.auth
  (:require [syncrate-kee-frame.db.core :as db]
            [clojure.pprint :refer [pprint]]
            [clj-http.client :as client]
            [cheshire.core :refer [parse-string]]
            [clojure.walk :refer [keywordize-keys]]
            [buddy.sign.jwt :as jwt]
            [syncrate-kee-frame.config :refer [env]]
            [syncrate-kee-frame.validation :refer [validate! user-schema]]
            [syncrate-kee-frame.middleware.exception :refer [handle-exception]]
            [ring.util.http-response :as response]))

(defn get-fb-data
  [access-token]
  (client/get "https://graph.facebook.com/v3.3/me"
              {:query-params {:access_token access-token
                              :fields "id,first_name,last_name"}}))
(def default-user-attrs
  {:email nil
   :first_name nil
   :last_name nil})

(defn parse-fb-data
  [response]
  (merge default-user-attrs
    (-> response
        :body
        parse-string
        keywordize-keys
        (clojure.set/rename-keys {:id :facebook_id}))))

(defn sign-token [user]
  (-> user
      (select-keys [:id :facebook_id])
      (jwt/sign (:auth-secret env))))

(defn get-or-create-user [fb-data]
  (let [user-data (parse-fb-data fb-data)
        user-found (-> user-data
                       (select-keys [:facebook_id])
                       (db/get-user))]
    (or user-found
        (db/create-user! (validate! user-data user-schema)))))

(defn authenticate-fb [{:keys [body-params]}]
  (try
   (let [fb-data (get-fb-data (:fb-token body-params))
         user (get-or-create-user fb-data)]
     {:status 200
      :body {:auth-token (sign-token user)}})
   (catch Exception e
     (handle-exception e "authenticate with Facebook"))))

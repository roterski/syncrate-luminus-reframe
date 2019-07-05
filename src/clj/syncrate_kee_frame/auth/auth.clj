(ns syncrate-kee-frame.auth.auth
  (:require [syncrate-kee-frame.db.core :as db]
            [clojure.pprint :refer [pprint]]
            [clj-http.client :as client]
            [cheshire.core :refer [parse-string]]
            [clojure.walk :refer [keywordize-keys]]
            [buddy.sign.jwt :as jwt]
            [syncrate-kee-frame.config :refer [env]]
            [ring.util.http-response :as response]))

(defn fetch-fb-data
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

(defn authenticate-fb [{:keys [body-params]}]
  (try
   (let [fb-response (fetch-fb-data (:fb-token body-params))
         user-data (parse-fb-data fb-response)
         user-found (-> user-data
                        (select-keys [:facebook_id])
                        (db/get-user))
         user (or user-found
                (db/create-user! user-data))
         auth-token (-> user
                        (select-keys [:id :facebook_id])
                        (jwt/sign (:auth-secret env)))]

     {:status 200
      :body {:auth-token auth-token
             :current-user (select-keys user [:id :first_name])}})
   (catch Exception e
     (let [{id :syncrate-kee-frame/error-id
            errors :errors} (ex-data e)]
       (case id
         :validation
         (response/bad-request {:errors errors})
         ;;else
         (response/internal-server-error
           {:errors {:server-error ["Failed to authenticate with facebook"]}}))))))

(ns syncrate-kee-frame.auth.routes
  (:require
    [syncrate-kee-frame.auth.auth :refer [authenticate-fb]]))

(defn auth-routes []
  ["/authenticate_fb" {:post {:summary "authenticate with facebook"
                              :parameters {:body {:fb-token string?}}
                              :responses {200 {:body map?}
                                          500 {:errors map?}}
                              :handler authenticate-fb}}])

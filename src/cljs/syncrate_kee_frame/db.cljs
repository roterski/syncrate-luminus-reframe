(ns syncrate-kee-frame.db)

(def initial-db
  {:errors {}
   :active-post :1
   :posts
     {:1 {:id 1
          :title "Hello world"
          :profile-id :1
          :body  "Nice to be here"}
      :2 {:id 2
          :title "Second post"
          :profile-id :2
          :body "Feeling good, la la la lala"}
      :3 {:id 3
          :title "Third post"
          :profile-id :3
          :body "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"}}})
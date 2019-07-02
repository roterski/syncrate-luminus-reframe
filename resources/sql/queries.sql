-- :name create-user! :insert :raw
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, facebook_id, email)
VALUES (:first_name, :last_name, :facebook_id, :email)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE facebook_id = :facebook_id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name create-post! :insert :raw
-- :doc creates a new post record
INSERT INTO posts
(title, body)
VALUES (:title, :body)


-- :name get-posts :? :*
-- :doc retrieves all post records
SELECT * FROM posts
LIMIT 1000;

-- :name get-post :? :1
-- :doc retrieves a post record given the id
SELECT * FROM posts
WHERE id = :id

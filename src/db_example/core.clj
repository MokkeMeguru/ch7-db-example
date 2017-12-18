(ns db-example.core
  (:require [clojure.java.jdbc :as sql]
            [clojure.java.io :as io]))

(def db {:subprotocol "postgresql"
         :subname "//localhost/reporting"
         :user "admin"
         :password "admin"})

(defn create-users-table! []
  (sql/db-do-commands
   db
   (sql/create-table-ddl
    :users
    [:id "varchar(32) PRIMARY KEY"]
    [:pass "varchar(100)"])))

(defn get-user [id]
  (first (sql/query db ["select * from users where id = ?" id])))

(defn add-user! [user]
  (sql/insert! db :users user))

(add-user! {:id "foo" :pass "bar"})

(get-user "foo")

(defn add-users! [& users]
  (apply sql/insert! db :users users))

(add-users!
 {:id "foo1" :pass "bar"}
 {:id "foo2" :pass "bar"}
 {:id "foo3" :pass "bar"})

(sql/insert! db :users [:id] ["bar"] ["baz"])

(get-user "bar")
(get-user "baz")

(defn set-pass! [id pass]
  (sql/update!
   db
   :users
   {:pass pass}
   ["id=?" id]))

(set-pass! "foo" "bar")

(get-user "foo")

(defn remove-user! [id]
  (sql/delete! db :users ["id=?" id]))

(remove-user! "foo")


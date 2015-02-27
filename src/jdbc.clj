(ns jdbc
  (:require [clojure.java.jdbc :as sql]
            [clojure.string :as string]))

(def db-spec {:classname "com.mysql.jdbc.Driver"
               :subprotocol "mysql"
               :subname "//127.0.0.1:3306/test"
               :user "root"
               :password "tsd8pass"})

(sql/query db-spec
           ["SELECT * FROM users"])

(defn- snake-prn
  [{:keys [email name]}]
    {name email})

(sql/query db-spec
           ["SELECT * FROM users"]
           :as-arrays? false
           :identifiers string/lower-case
           :result-set-fn vec
           :row-fn snake-prn)

;; 在users表中插入三条记录
(sql/insert! db-spec :users
             {:name "bill" :email "bill@hello.com"}
             {:name "ken" :email "ken@hello.com"}
             {:name "lili" :email "lili@hello.com"})

;; 更新users表中插入的记录
(sql/update! db-spec :users
             {:name "snake" :email "snake@hello.com"}
             ["id = ?" 2])

(sql/db-do-commands db-spec
                    (sql/create-table-ddl :frust
                                          [:name "varchar(32)"]
                                          [:appearance "varchar(32)"]
                                          [:cost :int]
                                          [:grade :real]))

(sql/db-do-commands db-spec
                    (sql/drop-table-ddl :frust))

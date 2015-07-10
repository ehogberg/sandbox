(ns sandbox.multimethods
  (:require [clojure.string :as str]))


(defmulti my-print class)

(defmethod my-print String [s]
  (.write *out* s))

(defmethod my-print nil [s]
  (.write *out* "<nil>"))

(defmethod my-print Number [n]
  (.write *out* (.toString n)))

(defmethod my-print :default [s]
  (.write *out* "#<")
  (.write *out* (.toString s))
  (.write *out* ">"))

(defmethod my-print java.util.Collection [c]
  (.write *out* "(")
  (.write *out* (str/join " " c))
  (.write *out* ")"))

(my-print (take 6 (cycle [1 2 3])))

(defmethod my-print clojure.lang.IPersistentVector [c]
  (.write *out* "[")
  (.write *out* (str/join " " c))
  (.write *out* "]"))

(prefer-method my-print clojure.lang.IPersistentVector
               java.util.Collection)

(my-println [1 2 3])

(my-print (java.util.Date.))



(defstruct account :id :tag :balance)

(alias 'acc 'sandbox.multimethods)

(def test-savings (struct account 1 ::acc/Savings 100M))
(def test-checking (struct account 2 ::acc/Checking 250M))

(my-print test-savings)

(defmulti interest-rate :tag)
(defmethod interest-rate ::acc/Checking [_] 0M)
(defmethod interest-rate ::acc/Savings [_] 0.05M)


(defmulti account-level :tag)
(defmethod account-level ::acc/Checking [acct]
  (if (>= (:balance acct) 5000) ::acc/Premium ::acc/Basic))
(defmethod account-level ::acc/Savings [acct]
  (if (>= (:balance acct) 1000) ::acc/Premium ::acc/Basic))

(derive ::acc/Savings ::acc/Account)
(derive ::acc/Checking ::acc/Account)


(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))
(defmethod service-charge [::acc/Basic ::acc/Checking] [_] 25)
(defmethod service-charge [::acc/Basic ::acc/Savings] [_] 10)
(defmethod service-charge [::acc/Premium ::acc/Account] [_] 0)


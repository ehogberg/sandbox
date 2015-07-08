(ns sandbox.protocol
  (:import (java.io FileInputStream InputStreamReader BufferedReader
                    FileOutputStream OutputStreamWriter BufferedWriter)))

(definterface IOFactory
  (^java.io.BufferedReader make_reader [this])
  (^java.io.BufferedWriter make_writer [this]))

(defn make-reader [src]
  (-> src FileInputStream. InputStreamReader. BufferedReader.))

(defn make-writer [dst]
  (-> dst FileOutputStream. OutputStreamWriter. BufferedWriter.))

(defn gulp [src]
  (let [sb (StringBuilder.)]
    (with-open [reader (make-reader src)]
      (loop [c (.read reader)]
        (if (neg? c)
          (str sb)
          (do
            (.append sb (char c))
            (recur (.read reader)))))))) 

(defn expectorate [dst content]
  (with-open [writer (make-writer dst)]
    (.write writer (str content))))







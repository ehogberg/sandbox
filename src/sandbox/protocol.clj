(ns sandbox.protocol
  (:import (java.io InputStream OutputStream File
                    FileInputStream InputStreamReader BufferedReader
                    FileOutputStream OutputStreamWriter BufferedWriter)
           (java.net Socket URL )))

(defprotocol IOFactory
  "A protocol for things that can be read from and written to."
  (make-reader [this] "Creates a BufferedReader")
  (make-writer [this] "Creates a BufferedWriter"))


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


(extend-protocol IOFactory
  File
  (make-reader [src]
    (make-reader (FileInputStream. src)))
  (make-writer [dst]
    (make-writer (FileOutputStream. dst)))
  Socket
  (make-reader [src] (.getInputStream src))
  (make-writer [dst] (.getOutputStream dst))
  URL
  (make-reader [src]
    (if (= "file" (.getProtocol src))
      (-> src .getPath FileInputStream.)
      (.openStream src)))
  (make-writer [dst]
    (if (= "file" (.getProtocol dst))
      (-> dst .getPath FileInputStream.)
      (throw (IllegalArgumentException.
              "Can't write to non-file URL")))))

(extend InputStream
  IOFactory
  {:make-reader (fn [src]
                  (-> src InputStreamReader. BufferedReader.))
   :make-writer (fn [dst]
                  (throw (IllegalArgumentException.
                          "Can't open as an InputStream")))})

(extend OutputStream
  IOFactory
  {:make-reader (fn [src] (throw (IllegalArgumentException.
                                  "Can't open as an OutputStream.")))
   :make-writer (fn [dst]
                  (-> dst OutputStreamWriter. BufferedWriter.))})







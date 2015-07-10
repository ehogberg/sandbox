(ns sandbox.macros)


(defmacro unless [expr form]
  (list 'if expr nil form))

(unless false (println "this should print."))
(unless true (println "this hsould print"))

(macroexpand-1 '(unless (= 2 3) (println "this hsould print")))

(macroexpand '(.. arm getHand GetFinger))

(use 'clojure.repl)

(source when-not)

(macroexpand-1
 '(when-not false
   (println "hello")
   (println "goodbye")))

(defmacro chain
  ([x form] `(. ~x ~form))
  ([x form & more] `(chain (. ~x ~form) ~@more)))


(macroexpand '(chain arm getHand))
(macroexpand '(chain arm getHand getFinger getThumb))

                                        ;(bendh (str "a" "b"))

(defmacro bench [expr]
  `(let [start# (System/nanoTime)
         result# ~expr]
     {:result result# :elapsed (- (System/nanoTime) start#)}))

(bench (for [x [1 2 3 4]] (+ x x)))
(bench (map + [1 2 3 4]))
(bench (str "a" "b"))


(def slow-calc (delay (Thread/sleep 5000) "done!"))

(force slow-calc)

(with-out-str (print "hello, ") (print "World"))

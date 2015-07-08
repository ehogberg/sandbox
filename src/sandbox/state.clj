(ns sandbox.state)

(def current-track (ref "Mars,the Bringer of War"))
(def current-composer (ref "Holst"))

@current-track

(dosync
 (ref-set current-track "Credo")
 (ref-set current-composer "Byrd"))

(defrecord Message [sender text])

(->Message "Aaron" "Hello")

(def messages (ref ()))

(defn add-message [msg]
  (dosync (commute messages conj msg)))

(add-message (->Message "Eric" "Hello"))
(add-message (->Message "David" "Goodbye"))
(add-message (->Message "Gregory" "Howdy!"))

(def validate-messsage-list
  (partial every? #(and (:sender %) (:text %))))

(def messages (ref () :validator validate-messsage-list))

@messages

(def current-track (atom "Venus, the Bringer of Peace"))

@current-track
(reset! current-track "Credo")

(def current-track (atom {:title "Credo" :composer "Byrd"}))

@current-track

(swap! current-track assoc :title "Sancte Deus")

(def counter (agent 0))

(send counter inc)
@counter

@messages

(def counter (agent 0 :validator number?))

(def backup-agent (agent "/tmp/messages-backup.clj"))

(defn add-message-with-backup [msg]
  (dosync
   (let [snapshot (commute messages conj msg)]
     (send-off backup-agent (fn [filename]
                              (spit filename snapshot)
                              filename))
     snapshot)))

(add-message-with-backup (->Message "David" "G'day!"))


(def ^:dynamic foo 10)

(.start (Thread. (fn [] (println foo))))

(defn print-foo [] (println foo))

(print-foo)

(let [foo "let foo"] (print-foo))
(binding [foo "bound foo"] (print-foo))

(defn ^:dynamic slow-double [n]
  (Thread/sleep 100)
  (* n 2))

(defn calls-slow-double []
  (map slow-double [1 2 1 2 1 2]))

(time (dorun (calls-slow-double)))

(defn demo-memoize []
  (time
   (dorun
    (binding [slow-double (memoize slow-double)]
      (calls-slow-double)))))

(demo-memoize)

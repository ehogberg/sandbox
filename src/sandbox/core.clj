(ns sandbox.core)

(def flips [:h :t :h :t :t :h :h :h :t :t :t :h])

(def by-twos (partial partition 2 1))

(by-twos flips)

(def count-if (comp count filter))

(count (filter (fn [pair] (every? #(= :h %) pair))
               (by-twos flips)))

(count-if (fn [pair] (every? #(= :h %) pair))
          (by-twos flips))

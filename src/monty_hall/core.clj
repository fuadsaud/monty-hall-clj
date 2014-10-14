(ns monty-hall.core
  (:gen-class))

(declare monty-hall)

(defn -main
  [& args]
  (let [n-doors (Integer. (first *command-line-args*))
        n-times (Integer. (second *command-line-args*))
        results
        (for [_ (range n-times)]
          (((monty-hall n-doors) (rand-int n-doors)) true))]
    (println (map count ((juxt filter remove) identity results)))))

(declare empty-doors random-door-excluding)

(defn monty-hall [n-doors]
  (fn [chosen-door-idx]
    (let [prize-door-idx (rand-int n-doors)
          doors (assoc (empty-doors n-doors) prize-door-idx true)
          candidate-idx (if (= chosen-door-idx prize-door-idx)
                          (random-door-excluding n-doors chosen-door-idx)
                          prize-door-idx)]

      (println "prize: " prize-door-idx " chosen: " chosen-door-idx " candidate: " candidate-idx)

      (fn [switch?]
        (= prize-door-idx
           (if switch? candidate-idx chosen-door-idx))))))

(defn- empty-doors [n-doors]
  (vec (take n-doors (repeat false))))

(defn- random-door-excluding [top idx]
  (first
    (drop-while #(= % idx) (repeatedly #(rand-int top)))))

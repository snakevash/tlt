(ns main
  (:use [joy.ch2]))

(defstruct vehicle-struct :make :model :year :color)
(def vehicle (struct vehicle-struct "Toyota" "Prius" 2009))
(def make (accessor vehicle-struct :make))

(defn power [base & exponents]
  (reduce #(Math/pow %1 %2) base exponents))
(power 2 3)

(defn parting
  "returns a String parting in a given language"
  ([] (parting "World"))
  ([name] (parting name "en"))
  ([name language]
   (condp = language
     "en" (str "GoodBye, " name)
     "es" (str "Adios, " name)
     (throw (IllegalArgumentException.
             (str "unsupported language " language))))))
(parting "Mark")
(parting "Mark" "es")
(parting)

(def years [1940 1944 1961 1985 1987])
(filter (fn [year] (even? year)) years)
(filter #(even? %) years)
(defn pair-test [test-fn n1 n2]
  (if (test-fn n1 n2) "pass" "fail"))
(pair-test #(even? (+ %1 %2)) 3 5)

(defmulti what-am-i class)
(defmethod what-am-i Number [arg] (str arg " is a Number"))
(defmethod what-am-i String [arg] (str arg " is a String"))
(defmethod what-am-i :default [arg] (str arg " is something"))
(what-am-i 19)
(what-am-i "hello")
(what-am-i true)

(defn teenage? [age]
  (and (>= age 13) (< age 20)))
(def non-teen? (complement teenage?))
(non-teen? 47)
(defn times2 [n] (* n 2))
(defn minus3 [n] (- n 3))
(def my-composition (comp minus3 times2))
(my-composition 4)

(import '(java.util Calendar GregorianCalendar))
(let [gc (GregorianCalendar.)
      day-of-week (.get gc Calendar/DAY_OF_WEEK)
      is-weekend (or (= day-of-week Calendar/SATURDAY) (= day-of-week Calendar/SUNDAY))]
  (if is-weekend
    (str "play")
    (do (str "work" "sleep"))))
(defn process-next [waiting-line]
  (if-let [name (first waiting-line)]
    (str name " is next")
    (str "no waiting")))
(process-next '("Jeremy" "Amanda" "Tami"))
(process-next '())
'()
'("Jeremy" "Amanda" "Tami")

(defn summarize
  "prints the first item in a collection
  followed by a period  for each remaining item"
  [coll]
  (when-let [head (first coll)]
    (print head)
    (dotimes [_ (dec (count coll))] (print \.))
    (println)))
(summarize ["Moe" "Larry" "Curly"])

(import '(java.io BufferedReader))
(print "Enter a number: ")
(flush)
(let [value "1"]
  (println
   (condp = value
     1 "one"
     2 "two"
     3 "three"
     (str "unexpected value, \"" value \")))
  (println
   (condp instance? value
     Number (* value 2)
     String (* (count value) 2))))

(let [temperature 10]
  (println
   (cond
     (instance? String temperature) "invalid temperature"
     (<= temperature 0) "freezing"
     (>= temperature 100) "boiling"
     true "neither")))

(dotimes [card-number 3]
  (println "deal card number" (inc card-number)))

(def cols "ABCD")
(def rows (range 1 4))

(println "for demo")
(dorun
 (for [col cols :when (not= col \B)
       row rows :when (< row 3)]
   #_(println (str col row))))

(defn factorial-1 [number]
  "computes the factorial of a positive integer
  in a way that doesn't consume statck space"
  )


(defstruct card-struct :rank :suit)

(def card1 (struct card-struct :king :club))
(def card2 (struct card-struct :king :club))

(def card2 #^{:bent true} card2)
(meta (var card2))

(def a-to-j (vec (map char (range 65 75))))
(nth [] 9 :whoops)
(get [] 9 :whoops)
(seq a-to-j)
(rseq a-to-j)
(assoc a-to-j 4 "no longer E")
(replace {2 :a 4 :b} [1 2 3 2 3 4])
(def matrix
  [[1 2 3]
   [4 5 6]
   [7 8 9]])
(get-in matrix [1 2])
(assoc-in matrix [1 2] 'x)
(update-in matrix [1 2] * 100)

(defn neighbors
  "在2D矩阵查找某点邻接位置的函数"
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]] size yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(map + yx %) deltas))))
(map #(get-in matrix %) (neighbors 3 [0 1]))
(def my-stack [1 2 3])
(peek my-stack)
(pop my-stack)
(conj my-stack 4)
(+ (peek my-stack) (peek (pop my-stack)))

(defn strict-map1 [f coll]
  (loop [coll coll acc nil]
    (if (empty? coll)
      (reverse acc)
      (recur (next coll) (cons (f (first coll)) acc)))))
(strict-map1 - (range 5))
(next (range 5))
(first (range 5))

(defn strict-map2 [f coll]
  (loop [coll coll acc []]
    (if (empty? coll)
      acc
      (recur (next coll) (conj acc (f (first coll)))))))
(strict-map2 - (range 5))

;; 输出绑定改变
(binding [*out* *err*]
  (println "Blew up!"))

(def foo-file (clojure.java.io/writer "my.log"))
(binding [*out* foo-file]
  (println "Foo,bar."))
(.close foo-file)

;; 组合字符串

;; 从一个vector 组合成一个shell命令是不错的
;; str是调用的java's的.toString()方法
(def lines ["#! /bin/bash\n" "du -a ./ | sort -n -r\n"])
(apply println lines)

;; str 的几个特别的地方
(= (str) (str nil))
(= (str 1 'symbol :keyword) "1symbol:keyword")
(apply str [1 2 3])
(str [1 2 3])
(not= (apply str [1 2 3]) (str [1 2 3]))

;; 字符串是可以被解构的
(= (let [[f-c s-c] "abcdef"]
     (str f-c s-c)) "ab")

(require '[clojure.java.io :refer (reader)])
(def props (java.util.Properties.))
(.load props (reader "colors.properties"))
props
(class props)

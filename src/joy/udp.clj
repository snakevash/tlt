(ns joy.udp
  (:refer-clojure :exclude [get]))

(defn beget [o p]
  (assoc o ::prototype p))

(beget {:sub 0} {:suger 1})

(def put assoc)

(defn get [m k]
  (when m
    (if-let [[_ v] (find m k)]
      v
      (recur (::prototype m) k))))
(get (beget {:sub 0} {:super 1}) :super)

(defn sum-even-numbers [nums]
  (if-let [nums (seq (filter even? nums))]
    (reduce + nums)
    "No even numbers found."))
(sum-even-numbers [1 3 5 7 9])
(sum-even-numbers [1 3 5 7 9 10 12])
(if-let [y true]
  "then"
  "else")

(def cat {:likes-dogs true :ocd-bathing true})
(def morris (beget {:likes-9lives true} cat))
morris
(def post-traumatic-morris (beget {:likes-dogs nil} morris))
morris
post-traumatic-morris
(get cat :likes-dogs)
(get morris :likes-dogs)
(get post-traumatic-morris :likes-dogs)

(defmulti compile :os)

(doseq [x [1 2 3]
        y [1 2 3]]
  (prn (* x y)))

(identity [1 2 3 nil 4 false true 1234])

(defn say-hello
  [name]
  (str "hello" name))

(def say-hello-with-defaults (fnil say-hello " snake"))
(say-hello-with-defaults nil)

(defn say-hello
  [first other]
  (str "Hello " first " and " other))
(say-hello "nihao" "limin")
(def say-hello-with-defaults (fnil say-hello "World" "People"))
(say-hello-with-defaults nil nil)

(defprotocol Matrix
  "测试协议"
  (lookup [martix i j])
  (update [martix i j value])
  ())

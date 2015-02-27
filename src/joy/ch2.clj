(ns joy.ch2)

(defn hello []
  (println "Hello Cleveland!"))
(defn report-ns [name]
  (str "The current namespace is " name))
;; (report-ns)
;; (report-ns "aa")

(defn as-sql-name
  "提供一个函数和关键字或者字符串，返回字符串的命名策略。
  一个叫做x.y的形式会被看作是多个名称的组合 x，y，等等。
  "
  ([f]
   (fn [x]
     (as-sql-name f x)))
  ([f x]
   (let [n (name x)
         i (.indexOf n (int \.))]
     (if (= -1 i)
       (f n)
       ;; 参数x用.来分割成序列
       ;; 遍历所有序列元素 应用函数f
       ;; 把所有的结果再用.来接连
       (clojure.string/join "." (map f (.split n "\\.")))))))
(as-sql-name #(.concat % "2") "3")
(.indexOf "3.2" (int \.))
(map #(.concat % ".x") (vec (.split "3.2" "\\.")))

(defprotocol FIXO
  (fixo-push [fixo value])
  (fixo-pop [fixo])
  (fixo-peek [fixo]))

(defprotocol my-protocol
  (foo [x]))

(defrecord constant-foo [value]
  my-protocol
  (foo [x] value))

(def a (constant-foo. 7))
(foo a)

(extend-protocol my-protocol
  java.lang.String
  (foo [x] (.length x)))

(foo "helloooooo")

(use 'clojure.string)
(defprotocol StringOps
  (rev [s])
  (upp [s]))
;; (extend-type String
;;   StringOps
;;   (rev [s]
;;        (clojure.string/reverse s)))
;; (extend-type String
;;   StringOps
;;   (upp [s]
;;        (clojure.string/upper-case s)))

(def rev-mixin {:rev clojure.string/reverse})
(def upp-mixin {:upp clojure.string/upper-case})
(def fully-mixed (merge rev-mixin upp-mixin))
(extend String
  StringOps
  fully-mixed)

(-> "Workers" upp rev)

(ns joy.ch4)

(defn get-document
  [id]
  {:url "http://www.mozilla.org/about/manifesto.en.html"
   :title "The Mozilla Manifesto"
   :mine "text/html"
   :content (delay (slurp "http://www.baidu.com"))})

(def d (get-document :url))
d
;; delay 的值只有我们解引用的时候才计算

(realized? (:content d))
(:content d)
@(:content d)
(realized? (:content d))

;; future

;; 通过解引用来引用future计算后的值
(def long-calculation (future (apply + (range 1e8))))
@long-calculation

;; future会阻塞当前线程
@(future (Thread/sleep 5000) :done!)

;; 超时值和超时时间
(deref (future (Thread/sleep 8000) :done!)
       1000
       :impatient!)

;; promise
;; 刚开始时一个空的容器
(def p (promise))
(realized? p)
(deliver p 42)
(realized? p)
@p


;; 模拟一个并发场景
(def a (promise))
(def b (promise))
(def c (promise))
(future
  (deliver c (+ @a @b))
  (println "Delivery complete!"))
(deliver a 15)
(deliver b 16)
(realized? c)
@c

;; 注意 promise 不会检查循环依赖
;; 基于回调的api以同步的方式执行
(defn sync-fn
  [async-fn]
  (fn [& args]
    ;; 定义一个promise局部变量
    (let [result (promise)]
      ;; 把结果传入异步方法
      (apply async-fn (conj (vec args) #(deliver result %&)))
      @result)))

;; 利用正则抽取电话号码
(defn phone-numbers
  [string]
  (re-seq #"(\d{3})[\.-]?(\d{3})[\.-]?(\d{4})" string))
(phone-numbers " Sunil: 617.555.2937, Betty: 508.555.2218")
;; 创建测试数据
(def files (repeat 100
                   (apply str
                          (concat (repeat 1000000 \space)
                                  " Sunil: 617.555.2937, Betty: 508.555.2218"))))
(time (dorun (pmap phone-numbers files)))
;; map 会产生惰性序列
;; doall 可以使惰性序列实例化
;; pmap 会并行运行方法 但是会产生一定额外的性能损耗 取舍

;; 对引用类型调用deref绝对不会阻塞
(def aa 88)
aa

;; 协调 操作里面的角色需要相互交互
;; 同步 线程需要做操作

;; 帮助函数
(defmacro futures
  [n & exprs]
  (vec (for [_ (range n)
             expr exprs]
         `(future ~expr))))

(defmacro wait-futures
  [& args]
  `(doseq [f# (futures ~@args)]
     @f#))

;; atom
(def sarah (atom {:name "Sarah" :age 25 :wear-glasses? false}))
(swap! sarah update-in [:age] + 3)

;; swap! 先比较旧的值是否匹配

(def xs (atom #{1 2 3}))
(wait-futures 1 (swap! xs (fn [v]
                            (Thread/sleep 250)
                            (println "trying 4")
                            (conj v 4)))
              (swap! xs (fn [v]
                          (Thread/sleep 500)
                          (println "tring 5")
                          (conj v 5))))

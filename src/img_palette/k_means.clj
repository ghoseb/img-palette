(ns ^{:doc "K-means Clustering"
      :author "Baishampayan Ghose <b.ghose@helpshift.com>"}
  img-palette.k-means
  (:use [img-palette.util :only [take-while-changing]]))


(defn closest
  "Find the mean to which x is closest."
  [distance means x]
  (first (sort-by (partial distance x) means)))


(defn group-by-distance
  "Group a bunch of points given a distance fn & means."
  [distance means xs]
  (group-by (partial closest distance means) xs))


(defn gen-means
  "Generate a new set of means."
  [avg means points]
  (for [m means]
    (if (contains? points m)
      (apply avg (points m))
      m)))


(defn means
  "Generate a sequence of new means."
  [distance average data init-means]
  (iterate #(gen-means average % (group-by-distance distance % data)) init-means))


(defn groups
  "Find the groups in data given distance fn & means."
  [distance data means]
  (keys (group-by-distance distance means data)))


(defn k-groups
  "Find K groups given distance, average fns & data."
  [distance average data]
  (fn [init-means]
    (last (take-while-changing
           (map (partial groups distance data)
                (means distance average data init-means))))))


(comment

(def data [2 3 5 6 10 11 50 60 70 100 101 102])

(defn distance
  "Distance between two points."
  [x y]
  (Math/abs (- x y)))

(defn avg
  "Average of xs."
  [& xs]
  (/ (apply + xs) (count xs)))

(defn rand-mean
  [xs]
  (->> xs
       shuffle
       (take 10)
       (apply avg)))

(defn gen-rand-means
  [n xs]
  (repeatedly n #(rand-mean xs)))

)

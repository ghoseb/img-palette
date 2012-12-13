(ns img-palette.core
  (:require [img-palette.util :as u]
            [img-palette.k-means :as k]))

(defn space-distance
  "Distance between two points in 3d space."
  [x y]
  {:pre [(every? #(= (count %) 3) [x y])]}
  (Math/sqrt (apply + (map (comp #(Math/pow % 2) -) x y))))

(defn space-average
  "Average of multiple points in a 3d space."
  [& xs]
  {:pre [(every? #(= (count %) 3) xs)]}
  (apply map (fn [& cs] (/ (apply + cs) (count cs))) xs))

(defn rand-centroid
  "Generate a centroid from a bunch of points by taking the avg of 10 random points."
  [xs]
  (->> xs
      shuffle
      (take 10)
      (apply space-average)))

(defn gen-centroids
  "Generate n random centroids."
  [n xs]
  (repeatedly n #(rand-centroid xs)))


(defn img->palette
  "Take the source image and spit out the 3 primary colors."
  [filename dest-file]
  (let [data (u/get-pixel-colors filename)
        init-means (gen-centroids 3 data)
        centroids ((k/k-groups space-distance space-average data) init-means)]
    (u/spit-image (map u/point->rgb centroids) dest-file)))


(comment
(img->palette "/Users/ghoseb/Desktop/nature.jpg" "/Users/ghoseb/Desktop/palette.png")
)

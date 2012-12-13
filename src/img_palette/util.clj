(ns ^{:doc "Util fns"
      :author "Baishampayan Ghose <b.ghose@helpshift.com>"}
  img-palette.util
  (:require [clojure.java.io :as io])
  (:import java.awt.Image
           java.awt.image.BufferedImage
           javax.imageio.ImageIO
           java.io.File
           java.math.RoundingMode
           java.text.DecimalFormat
           java.awt.Graphics2D
           java.awt.Color))

(declare take-while-changing)

(letfn [(take-while-changing* [s prev]
          (lazy-seq
            (when (and (seq s) (not= prev (first s)))
              (take-while-changing s))))]
  (defn take-while-changing
    "Keep taking items from a seq while the values are changing."
    [s]
    (lazy-seq
      (when (seq s)
        (cons (first s) (take-while-changing* (rest s) (first s)))))))


(defn resize-image
  [^BufferedImage src]
  (let [resized ^Image (.getScaledInstance src 50 50 Image/SCALE_SMOOTH)
        buf-img ^BufferedImage (BufferedImage. 50 50 BufferedImage/TYPE_INT_RGB)]
    (.. buf-img getGraphics (drawImage resized 0 0 nil))
    buf-img))


(defn explode-rgb
  [rgb]
  [(bit-and (bit-shift-right rgb 16) 0xff)
   (bit-and (bit-shift-right rgb 8) 0xff)
   (bit-and rgb 0xff)])

(defn get-pixel-colors
  [^String i]
  (let [^BufferedImage img (resize-image (ImageIO/read (io/as-file i)))
        w (.getWidth img)
        h (.getWidth img)]
    (for [x (range w)
          y (range h) :let [rgb (.getRGB img x y)]]
      (explode-rgb rgb))))

(defn round-float
  [f]
  (Long/parseLong
   (.format (doto (DecimalFormat. "#")
              (.setRoundingMode RoundingMode/HALF_UP)) f)))

(defn point->rgb
  [p]
  (map round-float p))


(defn spit-image
  [colors dest-file]
  (let [bi ^BufferedImage (BufferedImage. 300 50 BufferedImage/TYPE_INT_RGB)
        g2d ^Graphics2D (.getGraphics bi)
        coords [0 100 200]]
    (doseq [[[r g b] coord] (map vector colors coords)]
      (.setPaint g2d (Color. r g b))
      (.fillRect g2d coord 0 100 50))
    (ImageIO/write bi "PNG" (io/as-file dest-file))
    dest-file))

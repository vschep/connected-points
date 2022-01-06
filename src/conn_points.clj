(ns conn-points
  (:require [clojure2d.core :as c2d]
            [clojure2d.color :as color]
            [fastmath.core :as m]))

;; be sure everything is fast as possible
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(m/use-primitive-operators)

;; define canvas
(def my-canvas (c2d/canvas 300 550))

;; create window
(def window (c2d/show-window my-canvas "Hello World!"))

(def points
  (let [xs (map #(* 50 %) (range 1 6))
        ys (map #(* 50 %) (range 1 11))]
    (vec (mapcat
           (fn [x] (map (fn [y] [x y]) ys))
           xs))))

(def color-index (atom -1))

(defn next-color [palette cycle]
  (swap! color-index
         #(mod (inc %) cycle))
  (nth palette @color-index))

(c2d/with-canvas
  [c my-canvas]
  (let [point-color (color/color 210 210 210)
        line-colors (color/palette 90)
        cycle (count line-colors)]
    (c2d/set-background c 255 255 255)
    (doseq [[x1 y1] points
            [x2 y2] points]
      (let [line-color (next-color line-colors cycle)]
        (prn [x1 y1 x2 y2 line-color])
        (c2d/set-color c line-color)
        (c2d/line c x1 y1 x2 y2)))
    #_(doseq [[x1 y1] points
            [x2 y2] points]
      (c2d/set-color c point-color)
      (c2d/ellipse c x1 y1 3 3))))

(ns conn-points
  (:require [clojure2d.core :as c2d]
            [clojure2d.color :as color]
            [fastmath.core :as m]
            [fastmath.random :as rr]))

;; be sure everything is fast as possible
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(m/use-primitive-operators)

;; define canvas
(def my-canvas (c2d/canvas 300 550))

;; create window
(def window (c2d/show-window my-canvas "Connected Points"))

(def x-distance 50)
(def y-distance 50)

(def points
  (let [xs (map #(* 50 %) (range 1 6))
        ys (map #(* 50 %) (range 1 11))]
    (vec (mapcat
           (fn [x] (map (fn [y]
                          [(+ x
                              (* (if (rr/brand) 1 -1)
                                 (rr/irand 15)))
                           (+ y
                              (* (if (rr/brand) 1 -1))
                              (rr/irand 15))])
                        ys))
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
    (doseq [[x1 y1] (shuffle points)
            [x2 y2] (shuffle points)]
      (let [line-color (next-color line-colors cycle)]
        (prn [x1 y1 x2 y2 line-color])
        (c2d/set-color c line-color)
        (c2d/line c x1 y1 x2 y2)))
    (c2d/save c (c2d/next-filename "results/conn_points_" ".jpg"))))

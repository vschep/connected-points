(ns conn-points
  (:require [clojure2d.core :as c2d]
            [clojure2d.color :as color]
            [fastmath.core :as m]
            [fastmath.random :as rr]))

;; be sure everything is fast as possible
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(m/use-primitive-operators)

(def x-point-count 10)
(def y-point-count 5)
(def x-distance 100)
(def y-distance 100)
(def x-jitter 15)
(def y-jitter 15)

;; define canvas
(def my-canvas (c2d/canvas
                 (* (inc x-point-count) x-distance)
                 (* (inc y-point-count) y-distance)))

;; create window
(def window (c2d/show-window my-canvas "Connected Points"))

(defn points []
  (let [xs (map #(* x-distance %) (range 1 (inc x-point-count)))
        ys (map #(* y-distance %) (range 1 (inc y-point-count)))]
    (vec (mapcat
           (fn [x] (map (fn [y]
                          [(+ x
                              (* (if (rr/brand) 1 -1)
                                 (rr/irand x-jitter)))
                           (+ y
                              (* (if (rr/brand) 1 -1))
                              (rr/irand y-jitter))])
                        ys))
           xs))))

(def color-index (atom -1))

(defn next-color [palette cycle]
  (swap! color-index
         #(mod (inc %) cycle))
  (nth palette @color-index))

(c2d/with-canvas
  [c my-canvas]
  (let [line-colors (color/palette
                      42
                      ;:ghibli/MarnieLight1
                      ;90
                      ;46
                      ;:gnbu-8
                      ;:gacruxa_fib54/fib54-12
                      ;:wkp_country/wiki-albania
                      ;:wkp_country/wiki-florida
                      )
        cycle (count line-colors)
        generated-points (points)]
    (c2d/set-background c 255 255 255)
    (doseq [[x1 y1] (shuffle generated-points)
            [x2 y2] (shuffle generated-points)]
      (let [line-color (next-color line-colors cycle)]
        (prn [x1 y1 x2 y2 line-color])
        (c2d/set-color c line-color)
        (c2d/line c x1 y1 x2 y2)))
    (c2d/save c (c2d/next-filename "results/conn_points_" ".jpg"))))

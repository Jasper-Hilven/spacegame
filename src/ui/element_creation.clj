(ns ui.element-creation
  (:import [com.badlogic.gdx.graphics Pixmap$Format Pixmap Texture Color]
           [com.badlogic.gdx.scenes.scene2d.ui Image]
           (com.badlogic.gdx.graphics.g2d TextureRegion)))

;;000e00ff
(def rBackground 0)
(def gBackground (/ 14.0 255.0))
(def bBackground 0)
(def aBackground 1)
(def background (new Color rBackground gBackground bBackground aBackground))
(def corner-color (new Color (/ 45.0 255.0) (/ 50.0 255.0) (/ 42.0 255.0) 1))

(defn create-corners [pixmap size-x size-y]
  (doto pixmap
    (.setColor corner-color)
    (.drawRectangle 42 0 50 8)
    (.drawRectangle 0 42 8 50)))

(defn create-background []
  (let
    [formatType Pixmap$Format/RGBA8888
     pixmap (new Pixmap 1920 1080 formatType)
     background-pixmap (doto pixmap
                         (.setColor background)
                         (.fill))
     texture (new Texture background-pixmap)
     texture-region (new TextureRegion texture)
     disposed (.dispose pixmap)
     image (new Image texture-region)
     ] image))

(def corner-size 50)
(def corner-thickness 8)
(def corner-remainder (- corner-size corner-thickness))
(defn create-corner [left bottom]
  (let [pixmap (new Pixmap corner-size corner-size Pixmap$Format/RGBA8888)
        setAll (doto pixmap
                 (.setColor corner-color)
                 (.fillRectangle 0 (if bottom 0 corner-remainder) corner-size corner-thickness)
                 (.fillRectangle (if left 0 corner-remainder) 0 corner-thickness corner-size))]
    pixmap))
(defn create-panel [width height x y]
  (let [pixmap (new Pixmap width height Pixmap$Format/RGBA8888)
        x-corner (- width corner-size)
        y-corner (- height corner-size)
        lb (create-corner true true)
        rb (create-corner false true)
        lu (create-corner true false)
        ru (create-corner false false)
        setAll (doto pixmap
                 (.drawPixmap lb 0 0)
                 (.drawPixmap rb x-corner 0)
                 (.drawPixmap lu 0 y-corner)
                 (.drawPixmap ru x-corner y-corner))
        texture (new Texture pixmap)
        texture-region (new TextureRegion texture)
        disposed [(.dispose pixmap) (.dispose lb) (.dispose rb) (.dispose lu) (.dispose ru)]
        image (new Image texture-region)
        positioned (.setPosition image x y)
        ] image)
  )

(defn create-panels [screenx screeny]
  (let [margin 20
        height-big-panel (- screeny (* 2 margin))
        height-small-panel (/ (- height-big-panel margin) 2)
        width-after-margins (- screenx (* 4 margin))
        width-big-panel (/ width-after-margins 2)
        width-small-panel (/ width-big-panel 2)
        x-start-right-panels (+ (* 3 margin) width-big-panel width-small-panel)
        y-start-up-panels (+ height-small-panel (* 2 margin))
        ] [
           (create-panel width-small-panel height-small-panel margin margin)
           (create-panel width-small-panel height-small-panel margin y-start-up-panels)
           (create-panel width-big-panel height-big-panel (+ (* 2 margin) width-small-panel) margin)
           (create-panel width-small-panel height-small-panel x-start-right-panels margin)
           (create-panel width-small-panel height-small-panel x-start-right-panels y-start-up-panels)
           ]
          ))

;;(create-panels 1920 1080 (fn [w h x y] [w h x y]))
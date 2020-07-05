(ns ui.element-creation
  (:import [com.badlogic.gdx.graphics Pixmap$Format Pixmap Texture]
           [com.badlogic.gdx.scenes.scene2d.ui Image]
           (com.badlogic.gdx.graphics.g2d TextureRegion)))

;;000e00ff
(def rBackground 0)
(def gBackground (/ 14.0 255.0))
(def bBackground 0)
(def aBackground 1)

(defn create-background []
  (let
    [formatType Pixmap$Format/RGBA8888
     pixmap (new Pixmap 1920 1080 formatType)
     background-pixmap (doto pixmap
                         (.setColor rBackground gBackground bBackground aBackground)
                         (.fill))
     texture (new Texture background-pixmap)
     texture-region (new TextureRegion texture)
     disposed (.dispose pixmap)
     image (new Image texture-region)
     ] image))
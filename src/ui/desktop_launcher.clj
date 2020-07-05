(ns ui.desktop-launcher
  (:require [ui.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication LwjglApplicationConfiguration]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main []

  (let [config (LwjglApplicationConfiguration.)]
    (set! (. config fullscreen) true)
    (set! (. config width) 1920)
    (set! (. config height) 1080)
    (LwjglApplication. (ui.core.Game.) config))
  (Keyboard/enableRepeatEvents true))

(-main)
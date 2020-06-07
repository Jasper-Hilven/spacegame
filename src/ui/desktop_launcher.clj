(ns ui.desktop-launcher
  (:require [ui.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication LwjglApplicationConfiguration]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main []

  (let [config (LwjglApplicationConfiguration.)]
    (set! (. config fullscreen) true)
    (LwjglApplication. (ui.core.Game.) config))
  (Keyboard/enableRepeatEvents true))

(-main)
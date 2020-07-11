(ns ui.core
  (:import [com.badlogic.gdx Game Gdx Graphics Screen]
           [com.badlogic.gdx.graphics Color GL20]
           [com.badlogic.gdx.graphics.g2d BitmapFont]
           [com.badlogic.gdx.scenes.scene2d Stage]
           [com.badlogic.gdx.scenes.scene2d.ui Label Label$LabelStyle]
           )
  (:require [ui.element-creation :as c]))

(gen-class
  :name ui.core.Game
  :extends com.badlogic.gdx.Game)

(def main-screen
  (let [stage (atom nil)]
    (proxy [Screen] []
      (show []
        (reset! stage (Stage.))
        (let [style (Label$LabelStyle. (BitmapFont.) (Color. 1 1 1 1))
              label (Label. "Hello worldos!" style)
              panels (c/create-panels 1920 1080)]
          (.addActor @stage (c/create-background))
          (.addActor @stage (get panels 0))
          (.addActor @stage (get panels 1))
          (.addActor @stage (get panels 2))
          (.addActor @stage (get panels 3))
          (.addActor @stage (get panels 4))
          ;;(.addActor @stage label)
          ))
      (render [delta]
        (.glClearColor (Gdx/gl) 0 0 0 0)
        (.glClear (Gdx/gl) GL20/GL_COLOR_BUFFER_BIT)
        (doto @stage
          (.act delta)
          (.draw)))
      (dispose [])
      (hide [])
      (pause [])
      (resize [w h])
      (resume []))))

(defn -create [^Game this]
  (.setScreen this main-screen))
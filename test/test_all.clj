(ns test-all
  (:require [clojure.test :refer :all]
            [actor.test-all :refer :all]
            [world.core-test :refer :all]))

(run-all-tests)
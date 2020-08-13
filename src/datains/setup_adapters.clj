(ns datains.setup-adapters
  (:require [datains.adapters.dingtalk :as dingtalk]
            [datains.adapters.fs.core :as fs]
            [clojure.tools.logging :as log]
            [datains.adapters.app-store.core :as app-store]
            [datains.adapters.data-commons.core :as dc]
            [datains.config :refer [env]]))

(defn setup-fs-service
  []
  (fs/setup-connection))

(defn reset-fs-service
  []
  (comment (log/info "TODO: reset fs service.")))

(defn setup-data-commons
  []
  (dc/setup-connection!))

(defn reset-data-commons
  []
  (dc/stop-connection!))

(defn setup-dingtalk
  []
  (let [access-token (get-in env [:dingtalk-access-token])
        secret       [get-in env [:dingtalk-secret]]]
    ; Don't setup dingtalk, if user don't set access-token and secret
    (when (and access-token secret)
      (log/info "Setup dingtalk adapter.")
      (dingtalk/setup-access-token access-token)
      (dingtalk/setup-secret secret))))

(defn reset-dingtalk
  []
  (log/info "Reset dingtalk adapter.")
  (dingtalk/setup-access-token "")
  (dingtalk/setup-secret ""))

(defn setup-app-store
  "Setup the configuration of choppy store from environment variables."
  []
  (log/info "Setup appstore adapter.")
  (app-store/setup-config {:access-token       (:app-store-access-token env)
                           :host               (:app-store-host env "choppy.3steps.cn")
                           :port               (:app-store-port env 80)
                           :scheme             (:app-store-scheme env "http")
                           :default-cover      (:app-default-cover env "")
                           :datains-workdir    (:datains-workdir env "~/.datains")
                           :app-utility-bin    (:app-utility-bin env nil)
                           :app-store-username (:app-store-username env)
                           :app-store-password (:app-store-password env)}))

(defn reset-app-store
  []
  (log/info "Reset appstore adapter.")
  (app-store/setup-config {:access-token       nil
                           :ping               "/api/v1/version"
                           :api-prefix         "/api/v1"
                           :host               nil
                           :port               nil
                           :scheme             nil
                           :default-cover      ""
                           :datains-workdir    "~/.datains/"
                           :app-store-username nil
                           :app-store-password nil}))
(ns datains.api.notification
  (:require
   [ring.util.http-response :refer [ok created no-content]]
   [datains.db.handler :as db-handler]
   [datains.api.spec :as db-spec]
   [clojure.tools.logging :as log]
   [datains.api.response :as response]
   [datains.events :as events]
   [datains.util :as util]))

(def notification
  [""
   {:swagger {:tags ["Notification Management"]}}

   ["/notifications"
    {:get  {:summary    "Get notifications."
            :parameters {:query db-spec/notification-params-query}
            :responses  {200 {:body {:total    nat-int?
                                     :page     pos-int?
                                     :per-page pos-int?
                                     :data     any?}}}
            :handler    (fn [{{{:keys [page per-page nstatus notification-type]} :query} :parameters}]
                          (let [query-map {:status            nstatus
                                           :notification-type notification-type}]
                            (log/debug "page: " page, "per-page: " per-page, "query-map: " query-map)
                            (ok (db-handler/search-notifications query-map
                                                                 page
                                                                 per-page))))}

     :post {:summary    "Create a notification."
            :parameters {:body db-spec/notification-body}
            :responses  {201 {:body {:message {:id pos-int?}}}}
            :handler    (fn [{{:keys [body]} :parameters}]
                          (log/debug "Create a notification: " body)
                          (created (str "/notifications/" (:id body))
                                   {:message (db-handler/create-notification! body)}))}}]

   ["/notifications/::uuid"
    {:get    {:summary    "Get a notification by id."
              :parameters {:path {:id pos-int?}}
              :responses  {200 {:body map?}}
              :handler    (fn [{{{:keys [id]} :path} :parameters}]
                            (log/debug "Get notification: " id)
                            (ok (db-handler/search-notifications id)))}

     :delete {:summary    "Delete a notification."
              :parameters {:path {:id pos-int?}}
              :responses  {204 nil}
              :handler    (fn [{{{:keys [id]} :path} :parameters}]
                            (db-handler/delete-notification! id)
                            (no-content))}}]])
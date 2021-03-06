(ns datains.api.workflow-spec
  (:require [clojure.spec.alpha :as s]
            [spec-tools.core :as st]))

(s/def ::page
  (st/spec
   {:spec                nat-int?
    :type                :long
    :description         "Page, From 1."
    :swagger/default     1
    :reason              "The page parameter can't be none."}))

(s/def ::per_page
  (st/spec
   {:spec                nat-int?
    :type                :long
    :description         "Num of items per page."
    :swagger/default     10
    :reason              "The per-page parameter can't be none."}))

(s/def ::id
  (st/spec
   {:spec                #(some? (re-matches #"[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}" %))
    :type                :string
    :description         "workflow-id"
    :swagger/default     "40644dec-1abd-489f-a7a8-1011a86f40b0"
    :reason              "Not valid a workflow-id."}))

;; -------------------------------- Workflow Spec --------------------------------
(s/def ::project_id
  (st/spec
   {:spec                #(some? (re-matches #"[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}" %))
    :type                :string
    :description         "project-id"
    :swagger/default     "40644dec-1abd-489f-a7a8-1011a86f40b0"
    :reason              "Not valid a project-id."}))

(s/def ::sample_id
  (st/spec
   {:spec            #(some? (re-matches #"[a-zA-Z0-9-]+" %))
    :type            :string
    :description     "sample-id"
    :swagger/default "sample_id"
    :reason          "Not valid a sample-id"}))

(s/def ::submitted_time
  (st/spec
   {:spec            nat-int?
    :type            :integer
    :description     "The submitted-time of the workflow."
    :swagger/default 0
    :reason          "Not a valid submitted-time"}))

(s/def ::started_time
  (st/spec
   {:spec            nat-int?
    :type            :integer
    :description     "The started-time of the workflow."
    :swagger/default 0
    :reason          "Not a valid started-time"}))

(s/def ::finished_time
  (st/spec
   {:spec            nat-int?
    :type            :integer
    :description     "The finished-time of the workflow."
    :swagger/default 0
    :reason          "Not a valid finished-time"}))

(s/def ::job_params
  (st/spec
   {:spec            map?
    :type            :map
    :description     "The job-params for the workflow."
    :swagger/default {}
    :reason          "Not a valid job-params."}))

(s/def ::labels
  (st/spec
   {:spec            map?
    :type            :map
    :description     "The labels for the workflow."
    :swagger/default {}
    :reason          "Not a valid labels."}))

(s/def ::percentage
  (st/spec
   {:spec            (s/and nat-int? #(< % 100))
    :type            :long
    :description     "Percentage."
    :swagger/default 0
    :reason          "The percentage parameter can't be negative integer."}))

(s/def ::status
  (st/spec
   {:spec                #(#{"Submitted" "Running" "Failed" "Aborting" "Aborted" "Succeeded" "On Hold"} %)
    :type                :string
    :description         "The status of the workflow."
    :swagger/default     "Submitted"
    :reason              "Only support the one of Submitted, Running, Failed, Aborting, Aborted, Succeeded, On Hold"}))

(s/def ::workflow_id
  (st/spec
   {:spec                (s/or :undefined nil?
                               :id #(re-matches #"[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}" %))
    :type                :string
    :description         "workflow-id"
    :swagger/default     "40644dec-1abd-489f-a7a8-1011a86f40b0"
    :reason              "Not valid a workflow-id."}))

(def workflow-id
  "A spec for the query parameters."
  (s/keys :req-un [::id]
          :opt-un []))

(def workflow-params-query
  "A spec for the query parameters."
  (s/keys :req-un []
          :opt-un [::page ::per_page ::project_id ::status]))

(def workflow-body
  {:project_id     ::project_id
   :sample_id      ::sample_id
   :submitted_time ::submitted_time
   :started_time   ::started_time
   :finished_time  ::finished_time
   :job_params     ::job_params
   :labels         ::labels
   :status         ::status})

(def workflow-put-body
  (s/keys :req-un []
          :opt-un [::status ::percentage ::finished_time ::workflow_id]))

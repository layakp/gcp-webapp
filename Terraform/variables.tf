variable "project_id" {
  description = "The ID of the project in which to create the resources."
}

variable "region" {
  description = "The region in which to create the resources."
  default     = "us-central1"
}

variable "cluster_name" {
  description = "The name of the cluster to be created."
}

variable "credentials_file" {
  description = "The path to the GCP credentials JSON file."
}

variable "kubeconfig_path" {
  description = "The path to the kubeconfig file."
  default     = "~/.kube/config"
}

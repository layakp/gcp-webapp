provider "google" {
  project = var.project_id
  region  = var.region
  credentials = file(var.credentials_file)
}

provider "kubernetes" {
  config_path = var.kubeconfig_path
}

resource "google_container_cluster" "primary" {
  name     = var.cluster_name
  location = var.region

  initial_node_count = 1

}


resource "kubernetes_deployment" "demo-app" {
  metadata {
    name      = "demo-app-deployment"
    namespace = "default"
  }
  spec {
    replicas = 2
    selector {
      match_labels = {
        app = "demo-app"
      }
    }
    template {
      metadata {
        labels = {
          app = "demo-app"
        }
      }
      spec {
        container {
          image = "layakp5/demo-app-release:1.0"
          name  = "demo-app-container"
          port {
            container_port = 8080
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "demo-app" {
  metadata {
    name      = "demo-app-loadbalancer"
    namespace = "default"
  }
  spec {
    selector = {
      app = "demo-app"
    }
    port {
      protocol = "TCP"
      port     = 80
      target_port = 8080
    }
    type = "LoadBalancer"
  }
}


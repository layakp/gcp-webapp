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
    replicas = 3

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

        annotations = {
          "prometheus.io/scrape" = "true"
          "prometheus.io/path"   = "/prometheus"
          "prometheus.io/port"   = "8080"
        }
      }

      spec {
        container {
          name  = "demo-app-container"
          image = "layakp5/demo-app-release:1.2"

          port {
            container_port = 8080
          }

        }
      }
    }
  }
}

resource "kubernetes_service" "demo-app-svc" {
  metadata {
    name      = "demo-app-svc"
    namespace = "default"
    labels = {
      app = "demo-app"
    }
  }

  spec {
    selector = {
      app = "demo-app"
    }

    port {
      protocol    = "TCP"
      port        = 80
      target_port = 8080
    }

  }
}

resource "kubernetes_service" "grafana_loadbalancer" {
  metadata {
    name      = "grafana-loadbalancer"
  }

  spec {
    selector = {
      app.kubernetes.io/name = "grafana"
    }

    port {
      protocol    = "TCP"
      port        = 3000
      target_port = 3000
    }

    type = "LoadBalancer"
  }
}


resource "kubernetes_service" "demo-app" {
  metadata {
    name      = "demo-app-loadbalancer"
    namespace = "default"
    labels = {
      app = "demo-app"
    }
  }

  spec {
    selector = {
      app = "demo-app"
    }

    port {
      protocol    = "TCP"
      port        = 80
      target_port = 8080
    }

    type = "LoadBalancer"
  }
}

resource "kubernetes_ingress_v1" "demo-app" {
  metadata {
    name      = "demo-app-ingress"
    namespace = "default"
    annotations = {
      "kubernetes.io/ingress.class" = "gce" # For GKE Ingress controller
      # "kubernetes.io/ingress.allow-http" = "true"
    }
  }

  spec {
    rule {
      http {
        path {
          path = "/"
          path_type = "Prefix"
          backend {
            service {
              name = "demo-app-loadbalancer" 
              port {
                number = 80 
              }
            }
          }
        }
        path {
          path = "/grafana"
          path_type = "Prefix"
          backend {
            service {
              name = "grafana-svc" 
              port {
                number = 3000
              }
            }
          }
        }
      }
    }
  }
}


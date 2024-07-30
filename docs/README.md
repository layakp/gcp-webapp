# HelloWorld Application on GCP with GKE, Terraform, Prometheus, and Grafana

## Project Overview
- **Application Name:** HelloWorld
- **Purpose:** A simple web application to demonstrate deployment and monitoring on GCP.
- **Tech Stack:**
  - **Cloud Provider:** Google Cloud Platform (GCP)
  - **Container Orchestration:** Google Kubernetes Engine (GKE)
  - **Infrastructure as Code:** Terraform
  - **Monitoring:** Prometheus and Grafana
  - **Application:** Java and SpringBoot

## Project Access
- **Application url:** http://34.121.35.181/api/v1/message
- **Monitoring:** http://34.123.237.119:3000/dashboards
- **Documentatiotn** https://github.com/layakp/gcp-webapp/doc
- **Java Source** https://github.com/layakp/gcp-webapp/demo
- **Terraform Config** https://github.com/layakp/gcp-webapp/Terraform

## highlevel architecture
![](Cloud%20Architecture.png)

## Application details
   - Java springboot based simple application. 
   - Currently uses embedded H2 data base for storing data. The interfaces are created in a way it is easy to switch to other databases if required
   - Java code is test driven and unit-test cases are writtern junit witht mockito.

## Application Deployment Details
   Application follows microservice architecture and is deployed as microservices orchestrated with K8s in GKE and GCP. Application is currently deployed as a 3 pod deployment with 3 different zones in GCP for high-availability and fault-tolerance. 

   Primary infrastrustures used are 
   - Docker 
   - Kubernetes
   - Terraform
   - Helm
   - Prometheus
   - Grafana
   - AlertManager

   More deployment details are as below

### Dockerization

1. **Create a Dockerfile:**
   - Write a Dockerfile for the HelloWorld application.

2. **Build and Push the Docker Image:**
   - Build the Docker image:
     ```sh
     docker build -t demo-app-release .
     ```
   - Push the image to Google Container Registry (GCR):
     ```sh
     docker tag demo-app-release layakp5/demo-app-release:1.2
     docker push layakp5/demo-app-release:1.2
     ```

## Infrastructure Setup

### GCP Project Configuration

1. **Create GCP Free Tier Account:**
   - Sign up for a GCP Free Tier account at [GCP Free Tier](https://cloud.google.com/free).

2. **Create Project or Use Default:**
   - Use the default project provided or create a new project.

3. **Enable Kubernetes Engine API:**
   - Navigate to the GCP Console and enable the Kubernetes Engine API for your project.

4. **Download and Install gcloud:**
   - Install the Google Cloud SDK on your local machine.
   - For macOS, use Homebrew:
     ```sh
     brew install --cask google-cloud-sdk
     ```

5. **Initialize gcloud:**
   - Initialize the gcloud SDK and authenticate using your Google account:
     ```sh
     gcloud init
     ```
   - Follow the prompts to log in and set your project.

7. **Create a GKE Cluster:**
   - Create a GKE cluster using gcloud:
     ```sh
     gcloud container clusters create myappcluster --num-nodes=1 --zone=us-central1
     ```

8. **Install gke-gcloud-auth-plugin:**
   - If prompted, install the gke-gcloud-auth-plugin to enable kubectl interaction with GKE:
     ```sh
     gcloud components install gke-gcloud-auth-plugin
     ```
   - After installing, run the following command to set up kubectl:
     ```sh
     gcloud container clusters get-credentials myappcluster --zone us-central1-a --project [PROJECT_ID]
     ```

### Terraform Configuration

1. **Install Terraform:**
   - Install Terraform using Homebrew:
     ```sh
     brew tap hashicorp/tap
     brew install hashicorp/tap/terraform
     ```

2. **Create a Service Account:**
   - Navigate to the IAM & Admin Console.
   - Click on **Create Service Account**.
   - Provide a name and description for the service account.
   - Click **Create**.
   - Assign the **Editor** role to the service account.
   - Create a JSON key for this service account and download it.

3. **Prepare Terraform Files:**
   - Create a directory for Terraform files, e.g., `/Users/layakannothpirattiyal/myproject/Terraform`.
   - Create main.tf and variable.tf files.

4. **Initialize Terraform:**
   - Navigate to the directory containing the Terraform files:
     ```sh
     cd /Users/layakannothpirattiyal/myproject/Terraform
     terraform init
     ```

5. **Plan Terraform Deployment:**
   - Run a dry-run to see the planned actions:
     ```sh
     terraform plan -var="project_id=river-button-430606-q9" -var="credentials_file=/Users/layakannothpirattiyal/Downloads/river-button-430606-q9-a13aa0b91aed.json" -var="kubeconfig_path=~/.kube/config" -var="cluster_name=myappcluster"
     ```

6. **Apply Terraform Configuration:**
   - Deploy the infrastructure:
     ```sh
     terraform apply -var="project_id=river-button-430606-q9" -var="credentials_file=/Users/layakannothpirattiyal/Downloads/river-button-430606-q9-a13aa0b91aed.json" -var="kubeconfig_path=~/.kube/config" -var="cluster_name=myappcluster"
     ```
   - Note: Running the apply command again will add any new changes, maintaining state.

## Monitoring Setup

### Enable Google Cloud Monitoring and Prometheus

1. **Enable Google Cloud APIs:**
   - In your GCP Console, enable the following APIs:
     - Cloud Monitoring API
     - Managed Service for Prometheus API
     - Cloud Resource Manager API

2. **Update GKE Cluster for Managed Prometheus:**
   - Ensure your GKE cluster is version 1.21 or later. If not, upgrade the cluster:
     ```sh
     gcloud container clusters upgrade myappcluster --master --cluster-version 1.21
     ```
   - Enable Managed Prometheus on your cluster:
     ```sh
     gcloud container clusters update myappcluster --enable-managed-prometheus --region=us-central1
     ```

3. **Install Prometheus Operator:**
   - Use Helm to install the Prometheus Operator:
     ```sh
     helm repo add prometheus-community https://prometheus-community.github.io/helm-chart
     helm repo update
     helm install prometheus-operator prometheus-community/kube-prometheus-stack
     ```
   - For port forwarding - 
      ```sh
     kubectl port-forward svc/prometheus-operator-kube-p-prometheus -n default 9090:9090
     ``
4. **Create Grafana LoadBalancer:**
   - Create a load balancer service for Grafana to access the dashboard:
     ```sh
     kubectl apply -f grafanalb.yaml
     ```

5. **Access Grafana Dashboard:**
   - Default Grafana credentials:
     - **Username:** admin (or your custom username if set)
     - **Password:** prom-operator
   - Access Grafana at `http://<load_balancer_ip>:3000/dashboards`

6. **Application Metrics:**
   - Use Spring Boot Actuator and Micrometer for exposing application metrics at the `/prometheus` endpoint.

7. **Service Monitor:**
   - Add a service monitor for Prometheus Operator to scrape metrics from the `/prometheus` endpoint.

8. **Grafana Dashboard:**
   - Create a Grafana dashboard to visualize the metrics. Access the dashboard at `http://<load_balancer_ip>:3000/dashboards`.

## Observations and Decisions
1. **Terraform Configuration:**
   - Decided to use Terraform for its ability to maintain state and manage infrastructure changes effectively.
   
2. **GCP and GKE:**
   - Observed that specifying a single region in GCP resulted in nodes being created in multiple zones within that region. This ensures high availability but increases resource usage.
   - Chose GKE for its seamless integration with other GCP services.
   - Enabled Managed Prometheus for efficient monitoring without the overhead of managing Prometheus infrastructure.
etes:**
   - Dockerized the HelloWorld application to ensure consistency across environments.
   - Us
3. **Docker and Kuberned GKE to orchestrate the containerized application, leveraging Kubernetes' features like scaling and rolling updates.

4. **Monitoring with Prometheus and Grafana:**
   - Integrated Prometheus and Grafana for comprehensive monitoring and visualization.

5. **Challenges and Resolutions:**
   - Application metrics exposed using Spring Boot Actuator and Micrometer at /prometheus endpoint. Initially used managed prometheus , but it was not scraping these application metrics. So had to install prometheus operator. 
   - Encountered an issue with Ingress requiring services to be of type NodePort or LoadBalancer. Resolved by ensuring correct service types in Kubernetes manifests.
   
6. **Future Enhancements:** 
   - Plan to implement CI/CD pipeline for automated deployments.
   - Explore additional monitoring tools and integrations.
   - Migrate to a bigger and better db based on future requirement
   - Setup Horizontal pod Autoscaler to make the application scalable


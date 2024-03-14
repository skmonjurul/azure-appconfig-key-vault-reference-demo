## Integrate AKS with Azure Key Vault and Azure App Configuration using Workload Identity

### Create a Resource Group
Create a resource group with the `az group create` command. An Azure resource group is a logical container into which
Azure resources are deployed and managed.

```bash
az group create --name <resource-group-name> --location <location>
```
A resource group belongs to a single location. To see all the locations supported in your current subscription,
run the az account list-locations command:
```bash
az account list-locations
```

### Create an Azure Key Vault
Create an Azure Key Vault with the `az keyvault create` command. An Azure Key Vault is a cloud service that works as a
secure secrets store. You can use Key Vault to protect secrets such as API keys, passwords, and certificates.

```bash
az keyvault create --name <key-vault-name> --resource-group <resource-group-name> --location <location> --enable-rbac-authorization
```

### Get the Object id of yourself
Get the object id of the user with the `az ad user list` command. You can use the object id to grant access to the Key Vault.

```bash
az ad user list --display-name <your-account-display-name> --query "[].id" -o tsv
```
OR

```bash
az ad user list --query "[].id" -o tsv
```


### Grant access "Key Vault Administrator" role to the Key Vault
Grant access to the Key Vault with the `az role assignment create` command. You can grant access to a user, group,
or service principal at a particular scope.

Get the key vault id with the `az keyvault show` command.
```bash
az keyvault show --name <key-vault-name> --query "id" -o tsv
```

```bash
az role assignment create --role "Key Vault Administrator" --assignee <user-email>/<object_id> --scope <key-vault-id>
```

### Create a secret in the Key Vault
Create a secret in the Key Vault with the `az keyvault secret set` command. You can use the secret to store sensitive
information such as passwords, connection strings, and other secrets.

```bash
az keyvault secret set --vault-name <key-vault-name> --name <secret-name> --value <secret-value>
```

### Create an Azure App Configuration
Create an Azure App Configuration with the `az appconfig create` command. Azure App Configuration is a service that
enables you to centralize your application settings and feature flags.

```bash
az appconfig create --name <app-config-name> --resource-group <resource-group-name> --location <location> --sku Free
```

Set the environment variable `APP_CONFIG_ENDPOINT` on your local machine.
```bash
export APP_CONFIG_ENDPOINT="<app-config-endpoint>"
```

### Create a key value in the App Configuration
Create a key value in the App Configuration with the `az appconfig kv set` command.

```bash
az appconfig kv set --name <app-config-name> --key <key> --value <value>
```

### Create a key vault reference in the App Configuration
Create a key vault reference in the App Configuration with the `az appconfig kv set` command.

```bash
az appconfig kv set-keyvault --name <app-config-name> --key <key> --secret-identifier <secret-identifier>
```
you can get the secret-identifier from the Azure Key Vault using below command.
```bash
az keyvault secret list --vault-name <keyvault-name> --query "[].id" -o tsv
```

### Create container registry
Create a container registry with the `az acr create` command. An Azure Container Registry is a managed Docker registry
service based on the open-source Docker Registry 2.0. You can use it to store and manage your container images.

```bash
az acr create --resource-group <resource-group-name> --name <container-registry-name> --sku Basic
```
If LocationNotAvailableForResourceType error occurs, you can use the `az acr check-name --name <location>` command to check the availability of the name.
Then you can use the `az acr create --resource-group <resource-group-name> --name <container-registry-name> --sku Basic --location <location>` command to create the container registry.

```bash
az acr create --resource-group <resource-group-name> --name <container-registry-name> --sku Basic --location <location>
```

**_Please note that the container registry name must be unique within Azure, and contain 5-50 alphanumeric characters only
and cannot contain special characters. And also registry name must use only lowercase letters._**

### Login to the container registry
Login to the container registry with the `az acr login` command. You can use the container registry to store and manage your container images.

```bash
az acr login --name <container-registry-name>
```

### Build the project using Maven
Build the project using Maven with the `mvn clean package` command.

```bash
mvn clean package
```

### Create a Docker image
Create a Docker image with the `docker build` command. You can use the Docker image to run your application in a container.

```bash
docker build -t <container-registry-name>.azurecr.io/<image-name>:<tag> .
```

### Push the Docker image
Push the Docker image to the container registry with the `docker push` command. You can use the container registry to
store and manage your container images.

```bash
docker push <container-registry-name>.azurecr.io/<image-name>:<tag>
```

### Create AKS cluster
Create an Azure Kubernetes Service (AKS) cluster with the `az aks create` command. Azure Kubernetes Service (AKS)
manages your hosted Kubernetes environment, making it quick and easy to deploy and manage containerized applications
without container orchestration expertise.

```bash
az aks create --resource-group <resource-group-name> --name <aks-cluster-name> --node-count 1 \
--enable-addons azure-keyvault-secrets-provider --enable-oidc-issuer --enable-workload-identity \
--attach-acr <container-registry-name> --location <location> --generate-ssh-keys
```

### Get access credentials for your AKS cluster
Get access credentials for your AKS cluster with the `az aks get-credentials` command. You can use the access credentials
to connect to your AKS cluster using kubectl.

```bash
az aks get-credentials --resource-group <resource-group-name> --name <aks-cluster-name>
```

### Create a Managed Identity
Create a managed identity with the `az identity create` command. You can use the managed identity to authenticate and
authorize users and services to access your resources.

```bash
az identity create --resource-group <resource-group-name> --name <managed-identity-name>
```

### Grant access "Key Vault Secrets User" role to the Managed Identity
Grant access to the Key Vault with the `az role assignment create` command. You can grant access to a user, group,
service principal or a managed identity at a particular scope.
```bash
az role assignment create --role "Key Vault Secrets User" --assignee <managed-identity-client-id> --scope <key-vault-id>
```
Get the managed identity client id with the `az identity show` command.
```bash
az identity show --resource-group <resource-group-name> --name <managed-identity-name> --query "clientId" -o tsv
```
Get the key vault id with the `az keyvault show` command.
```bash
az keyvault show --name <key-vault-name> --query "id" -o tsv
```

### Grant access "App Configuration Data Reader" role to the Managed Identity
Grant access to the App Configuration with the `az role assignment create` command. You can grant access to a user, group,
service principal or a managed identity at a particular scope.
```bash
az role assignment create --role "App Configuration Data Reader" --assignee <managed-identity-client-id> --scope <app-config-id>
```
Get the managed identity client id with the `az identity show` command.
```bash
az identity show --resource-group <resource-group-name> --name <managed-identity-name> --query "clientId" -o tsv
```
Get the app configuration id with the `az appconfig show` command.
```bash
az appconfig show --name <app-config-name> --resource-group <resource-group-name> --query "id" -o tsv
```

### Get the AKS cluster OIDC issuer URL
Get the AKS cluster OIDC issuer URL with the `az aks show` command. You can use the OIDC issuer URL to create a Kubernetes
Service Account.

```bash
az aks show --resource-group <resource-group-name> --name <aks-cluster-name> --query "oidcIssuerProfile.issuerUrl" -o tsv
```

### Create a Kubernetes Service Account
Create a yaml file with below content,

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  annotations:
    azure.workload.identity/client-id: <USER_ASSIGNED_MANAGED-IDENTITY-CLIENT_ID>
  name: kvr-sa
  namespace: default
```
Deploy the service account to the AKS cluster with the `kubectl apply` command.

```bash
kubectl apply -f <file-name>.yaml
```

You can also use the below command to create the service account.
```bash
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: ServiceAccount
metadata:
  annotations:
    azure.workload.identity/client-id: <USER_ASSIGNED_MANAGED-IDENTITY-CLIENT_ID>
  name: kvr-sa
  namespace: default
EOF
```

You can check the created service account using the below command.
```bash
kubectl get serviceaccount
```

### Create a Federated credentials for the Managed Identity
Create a federated credentials for the managed identity with the `az identity federated-credential create` command.
You can use the federated credentials to authenticate and authorize users and services to access your resources.

```bash
az identity federated-credential create --name <federated-identity-name> --identity-name <managed-identity-name> \
--resource-group <resource-group> --issuer <aks-oidc-issuer-url> --subject system:serviceaccount:<serviceaccount-namespace>:<serviceaccount-name>
```


### Deploy the application to the AKS cluster using WorkloadIdentity
Create a deployment yaml file with below content,

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kvr-service
  labels:
    app: kvr-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kvr-service
  template:
    metadata:
      name: kvr-service
      namespace: default
      labels:
        azure.workload.identity/use: "true"
        app: kvr-service
    spec:
      serviceAccountName: kvr-sa
      containers:
        - name: kvr-service
          image: <container-registry-name>.azurecr.io/<image-name>:<tag>
          ports:
            - containerPort: 8080
          env:
            - name: APP_CONFIG_ENDPOINT
              value: <APP_CONFIGURATION_ENDPOINT>
```

Deploy the application to the AKS cluster with the `kubectl apply` command.

```bash
kubectl apply -f <file-name>.yaml
```

You can also use the below command to create the deployment.
```bash
cat <<EOF | kubectl apply -f -
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kvr-service
  labels:
    app: kvr-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kvr-service
  template:
    metadata:
      name: kvr-service
      namespace: default
      labels:
        azure.workload.identity/use: "true"
        app: kvr-service
    spec:
      serviceAccountName: kvr-sa
      containers:
        - name: kvr-service
          image: <container-registry-name>.azurecr.io/<image-name>:<tag>
          ports:
            - containerPort: 8080
          env:
            - name: APP_CONFIG_ENDPOINT
              value: <APP_CONFIGURATION_ENDPOINT>
EOF
```

Verify that `AZURE_CLIENT_ID`, `AZURE_TENANT_ID` and `AZURE_FEDERATED_TOKEN_FILE` environment variables are set in the pod using the below command.
```bash
kubectl exec -it <pod-name> -- env
```
```bash
kubectl describe pod <pod-name>
```


You can check the application logs using the below command.
```bash
kubectl logs <pod-name>
```
To get the pod name, you can use the below command.
```bash
kubectl get pods
```


### Create a service for the application
Create a service yaml file with below content,

```yaml
apiVersion: v1
kind: Service
metadata:
  name: kvr-service
spec:
  type: LoadBalancer
  ports:
    - port: 8080
  selector:
    app: kvr-service
```
Deploy the service to the AKS cluster with the `kubectl apply` command.

```bash
kubectl apply -f <file-name>.yaml
```

You can also use the below command to create the service.
```bash
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: Service
metadata:
  name: kvr-service
spec:
  type: LoadBalancer
  ports:
  - port: 8080
  selector:
    app: kvr-service
EOF
```
Get the external IP of the service using the below command.
```bash
kubectl get service
```
To get service details, you can use the below command.
```bash
kubectl describe service kvr-service
```
Note the external IP of the service and use it to access the application using below the url
* `http://<external-ip>:8080/secrets`
* `http://<external-ip>:8080/appConfigs`
* `http://<external-ip>:8080/secretsAndAppConfigs`
* `http://<external-ip>:8080/actuator`


### Delete the resource group
Delete the resource group with the `az group delete` command. When you delete a resource group, all resources in the resource
group are also deleted.

```bash
az group delete --name <resource-group-name> --yes --no-wait
```
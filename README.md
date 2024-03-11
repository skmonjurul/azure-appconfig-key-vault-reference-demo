# How to use Azure App configuration key vault reference 

This sample demonstrates how to use Azure App Configuration key vault reference to store secrets in Azure Key Vault and 
access them in your application through Azure App Configuration.

# In this article
* [prerequisites](#prerequisites)
* [Quickstart](#quickstart)
* [How to run java spring boot application using key vault reference in your local machine.](#how-to-run-java-spring-boot-application-using-key-vault-reference-in-your-local-machine)
* How to run java spring boot application using key vault reference in Azure AKS using AppConfigurationProvider.
* How to run java spring boot application using key vault reference in Azure AKS using WorkloadIdentity.

## Prerequisites
* An Azure subscription; if you don't already have an Azure subscription, you can activate your [MSDN subscriber benefits](https://azure.microsoft.com/en-us/pricing/member-offers/msdn-benefits-details/) or sign up for a [free account](https://azure.microsoft.com/en-us/free/).
* Azure CLI. If you don't have Azure CLI, install the [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli).
* Java 17 or later. If you don't have Java, install the [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
* Maven 3.0 and above. If you don't have Maven, install [Maven](https://maven.apache.org/download.cgi).
* Docker. If you don't have Docker, install [Docker](https://www.docker.com/products/docker-desktop).


## Quickstart
* Set env variable `APP_SECRET` and `APP_CONFIG` on your local machine.
  ```bash
  export APP_SECRET="123456789"
  export APP_CONFIG="Message_From_Local_Machine"
  ```
* Build the project using Maven.
  ```bash
  mvn clean package
  ```
* Run the application using the following command:
  ```bash
  mvn spring-boot:run
  ```
* Open a browser and navigate to `http://localhost:8080/secret` to see the secret value.
* Open a browser and navigate to `http://localhost:8080/appConfigs` to see the app configuration value.
* Open a browser and navigate to `http://localhost:8080/secretAndAppConfigs` to see the secret and app configuration value.

## How to run java spring boot application using key vault reference in your local machine.

Please note: before running the application remove the env variables `APP_SECRET` and `APP_CONFIG` from your local machine.
* [create a resource group](#create-a-resource-group)
* [create an Azure Key Vault](#create-an-azure-key-vault)
* [get the Object id of yourself](#get-the-object-id-of-yourself)
* [grant access "Key Vault Administrator" role to the Key Vault](#grant-access-key-vault-administrator-role-to-the-key-vault)
* [create a secret in the Key Vault](#create-a-secret-in-the-key-vault)
* [create an Azure App Configuration](#create-an-azure-app-configuration)
* [create a key value in the App Configuration](#create-a-key-value-in-the-app-configuration)
* [create a key vault reference in the App Configuration](#create-a-key-vault-reference-in-the-app-configuration)
* [create a Service Principal](#create-a-service-principal)
* [grant access "App Configuration Data Reader" role to the Service Principal](#grant-access-app-configuration-data-reader-role-to-the-service-principal)
* [build the project using Maven](#build-the-project-using-maven)
* [run the application](#run-the-application)




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

### Create a Service Principal
Create a service principal with the `az ad sp create-for-rbac` command. You can use the service principal to authenticate
and authorize users and services to access your resources.

```bash
az ad sp create-for-rbac --name <service-principal-name>  --role "Key Vault Secrets User" --scopes <key-vault-id>
```

Take a note `appId` from the output of the above command. And set an env variable `AZURE_CLIENT_ID` on your local machine.
```bash
export AZURE_CLIENT_ID="<appId>"
```

Take a note `tenant` from the output of the above command. And set an env variable `AZURE_TENANT_ID` on your local machine.
```bash
export AZURE_TENANT_ID="<tenant>"
```

Take a note `password` from the output of the above command. And set an env variable `AZURE_CLIENT_SECRET` on your local machine.
Please note that you will not be able to retrieve the password again, so make sure to save it in a secure location.
```bash
export AZURE_CLIENT_SECRET="<password>"
```

### Grant access "App Configuration Data Reader" role to the Service Principal
Grant access to the App Configuration with the `az role assignment create` command. You can grant access to a user, group,
or service principal at a particular scope.
```bash
az role assignment create --role "App Configuration Data Reader" --assignee <service-principal-appId> --scope <app-config-id>
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

### Create a Docker image
Create a Docker image with the `docker build` command. You can use the Docker image to run your application in a container.

```bash
docker build -t <container-registry-name>.azurecr.io/<image-name>:<tag> .
```

### Push the Docker image
Push the Docker image to the container registry with the `docker push` command. You can use the container registry to store and manage your container images.

```bash
docker push <container-registry-name>.azurecr.io/<image-name>:<tag>
```

### Build the project using Maven
Build the project using Maven with the `mvn clean package` command.

```bash
mvn clean package
```

### Run the application
Run the application using the following command:

```bash
java -jar target/key-vault-reference-demo-0.0.1-SNAPSHOT.jar
```
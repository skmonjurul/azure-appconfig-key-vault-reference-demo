# How to use Azure App configuration key vault reference 

This sample demonstrates how to use Azure App Configuration key vault reference to store secrets in Azure Key Vault and 
access them in your application through Azure App Configuration.

# In this article
* [prerequisites](#prerequisites)
* [Quickstart](#quickstart)
* How to run java spring boot application using key vault reference in your local machine.
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


## Create a Resource Group
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

## Create an Azure Key Vault
Create an Azure Key Vault with the `az keyvault create` command. An Azure Key Vault is a cloud service that works as a 
secure secrets store. You can use Key Vault to protect secrets such as API keys, passwords, and certificates.

```bash
az keyvault create --name <key-vault-name> --resource-group <resource-group-name> --location <location> --enable-rbac-authorization
```

## Get the Object id of yourself
Get the object id of the user with the `az ad user list` command. You can use the object id to grant access to the Key Vault.

```bash
az ad user list --display-name <your-account-display-name> --query "[].id" -o tsv
```
OR

```bash
az ad user list --query "[].id" -o tsv
```


## Grant access "Key Vault Administrator" role to the Key Vault
Grant access to the Key Vault with the `az role assignment create` command. You can grant access to a user, group,
or service principal at a particular scope.

Get the key vault id with the `az keyvault show` command.
```bash
az keyvault show --name <key-vault-name> --query "id" -o tsv
```

```bash
az role assignment create --role "Key Vault Administrator" --assignee <user-email>/<object_id> --scope <key-vault-id>
```

## Create a secret in the Key Vault
Create a secret in the Key Vault with the `az keyvault secret set` command. You can use the secret to store sensitive
information such as passwords, connection strings, and other secrets.

```bash
az keyvault secret set --vault-name <key-vault-name> --name <secret-name> --value <secret-value>
```


## Delete key vault
Delete the key vault with the `az keyvault delete` command.

```bash
az keyvault delete --name <key-vault-name> --resource-group <resource-group-name>
```

## Recover soft-deleted key-vault
Recover the soft-deleted key vault with the `az keyvault recover` command.

```bash
az keyvault recover --subscription <subscription-id> -n <key-vault-name> 
```

## Purge soft-deleted key vault (WARNING! THIS OPERATION WILL PERMANENTLY DELETE YOUR KEY VAULT)
Purge the soft-deleted key vault with the `az keyvault purge` command.

```bash
az keyvault purge --subscription <subscription-id> -n <keyvault-name> 
```

## Enable purge-protection on key-vault
Enable purge-protection on the key vault with the `az keyvault update` command.

```bash
az keyvault update --subscription <subscription-id> -g <resource-group> -n <keyvault-name> --enable-purge-protection true
```
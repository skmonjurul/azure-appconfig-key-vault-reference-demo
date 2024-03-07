# How to use Azure App configuration key vault reference 

This sample demonstrates how to use Azure App Configuration key vault reference to store secrets in Azure Key Vault and 
access them in your application through Azure App Configuration.

# In this article

## Prerequisites
* An Azure subscription; if you don't already have an Azure subscription, you can activate your [MSDN subscriber benefits](https://azure.microsoft.com/en-us/pricing/member-offers/msdn-benefits-details/) or sign up for a [free account](https://azure.microsoft.com/en-us/free/).
* Azure CLI. If you don't have Azure CLI, install the [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli).
* Java 17 or later. If you don't have Java, install the [Java Development Kit (JDK)](https://adoptopenjdk.net/).
* Maven 3.0 and above. If you don't have Maven, install [Maven](https://maven.apache.org/download.cgi).


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
az keyvault create --name <key-vault-name> --resource-group <resource-group-name> --location <location>
```


# How to use Azure App configuration key vault reference 

This sample demonstrates how to use Azure App Configuration key vault reference to store secrets in Azure Key Vault and 
access them in your application through Azure App Configuration.

# In this article

## Prerequisites
* An Azure subscription; if you don't already have an Azure subscription, you can activate your [MSDN subscriber benefits](https://azure.microsoft.com/en-us/pricing/member-offers/msdn-benefits-details/) or sign up for a [free account](https://azure.microsoft.com/en-us/free/).
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



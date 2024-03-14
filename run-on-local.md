## Use Key Vault references in a Java Spring app

Please note: before running the application remove the env variables `APP_SECRET` and `APP_CONFIG` from your local machine.

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
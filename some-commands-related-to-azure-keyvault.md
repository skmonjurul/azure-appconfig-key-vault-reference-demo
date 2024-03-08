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

## Grant access "Key Vault Secrets User" role to the Service Principal
Grant access to the Key Vault with the `az role assignment create` command. You can grant access to a user, group,
or service principal at a particular scope. In this case, we are granting access to the service principal.
```bash
az role assignment create --role "Key Vault Secrets User" --scope <key-vault-id> --assignee-principal-type ServicePrincipal \
--assignee-object-id <service-principal-object-id>
```
Below is the command to get the service principal object id.
```bash
az ad sp show --id <service-principal-appId> --query "id" -o tsv 
```

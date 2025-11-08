
#  Mottooth - Projeto Challenge 2TDSPW 2025

<img width="1363" height="648" alt="image" src="https://github.com/user-attachments/assets/01d3d49e-acc3-4b57-89e8-50956a66d1b1" />
<img width="1366" height="696" alt="image" src="https://github.com/user-attachments/assets/f6102f46-53f1-43fd-abe0-3c80e52d4970" />



Este repositório contém a aplicação **Mottooth**, desenvolvida como parte do **Challenge DevOps da FIAP (2º Semestre de 2025)**.  
A aplicação foi criada utilizando **Spring Boot**, **Docker** e **Azure DevOps**, com **deploy automatizado** em um container na nuvem via **Azure Container Instances (ACI)**.

---
🧠 Descrição da Solução

O Mottooth é um sistema inteligente para rastreamento e gerenciamento de motocicletas em pátios, utilizando tecnologia Bluetooth Low Energy (BLE) por meio de beacons.
A solução foi desenvolvida para permitir que administradores acompanhem, em tempo real, a localização e o status de cada motocicleta cadastrada, otimizando o controle de entrada, saída e permanência nos pátios.

A aplicação backend foi construída com Spring Boot (Java 17), integrando-se a um banco de dados Azure SQL e executando dentro de containers Docker, publicados no Azure Container Registry (ACR) e implantados automaticamente em Azure Container Instances (ACI) via Azure DevOps Pipelines (CI/CD).
---

## ✅ Integrantes do Grupo

| Nome | RM |
|------|----|
| [Arthur Ramos Dos Santos] | RM558798 |
| [Felipe Melo de Sousa] | RM556099 |
| [Robert Daniel da Silva Coimbra] | RM555881 |


---

## 🚀 Tecnologias Utilizadas

- ☕ **Java 17 + Spring Boot**
- 🧰 **Maven**
- 🐳 **Docker**
- 🗄️ **Azure Container Registry (ACR)**
- ☁️ **Azure Container Instances (ACI)**
- 🧩 **Azure SQL Database**
- 🔁 **Azure DevOps Pipelines (CI/CD)**

---

## 🌐 Link da Aplicação em Produção

> **URL:** [http://mottooth-app-arthur.brazilsouth.azurecontainer.io:8080/](http://mottooth-app-arthur.brazilsouth.azurecontainer.io:8080/)

---

## ⚙️ Pipeline DevOps

### 🧱 CI (Integração Contínua)

**Responsável por:**
- Build do projeto com Maven  
- Criação da imagem Docker  
- Push da imagem para o ACR (`acrmottooth.azurecr.io`)

---

### 🚀 CD (Entrega Contínua)

**Responsável por:**
- Deploy da imagem para o **Azure Container Instance (ACI)**  
- Utilizando **Azure CLI** diretamente no pipeline  

---

### 📸 Prints das Execuções

#### ✅ CI Pipeline Sucesso:
![CI pipeline]
<img width="1097" height="252" alt="image" src="https://github.com/user-attachments/assets/1e4fb877-623d-466a-8814-ce979d10924b" />


#### ✅ CD Pipeline Sucesso:
![CD pipeline]
<img width="717" height="499" alt="image" src="https://github.com/user-attachments/assets/488cc241-aea3-4a09-a900-60df102a9196" />


---

## 🐳 Docker

### Dockerfile
```dockerfile
FROM openjdk:17
COPY target/mottooth.jar mottooth.jar
ENTRYPOINT ["java", "-jar", "mottooth.jar"]
````

---

## 🗃️ Banco de Dados (Azure SQL)

| Configuração | Valor                                          |
| ------------ | ---------------------------------------------- |
| **Servidor** | `mottooth-sql-1449.database.windows.net`       |
| **Database** | `mottoothdb`                                   |
| **Usuário**  | `adminmottooth`                                |
| **Driver**   | `com.microsoft.sqlserver.jdbc.SQLServerDriver` |
| **Dialect**  | `org.hibernate.dialect.SQLServerDialect`       |

As credenciais foram configuradas como **variáveis seguras** no pipeline e no container.

---

## ☁️ Container

### Azure Container Registry (ACR)

| Propriedade      | Valor                                           |
| ---------------- | ----------------------------------------------- |
| **Nome**         | `acrmottooth`                                   |
| **Login Server** | `acrmottooth.azurecr.io`                        |
| **Imagem**       | `acrmottooth.azurecr.io/mottooth-app-devops:20` |

### Azure Container Instance (ACI)

| Propriedade           | Valor                                                                                                                          |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| **Nome do container** | `mottooth-app`                                                                                                                 |
| **Grupo de recurso**  | `mottooth-devops`                                                                                                              |
| **DNS público**       | [http://mottooth-app-arthur.brazilsouth.azurecontainer.io:8080](http://mottooth-app-arthur.brazilsouth.azurecontainer.io:8080) |

---

## 🔐 Variáveis de Ambiente

Configuração no container via **Azure CLI**:

```bash
--environment-variables \
  SPRING_DATASOURCE_URL='jdbc:sqlserver://mottooth-sql-1449.database.windows.net:1433;database=mottoothdb;encrypt=true;trustServerCertificate=false;' \
  SPRING_DATASOURCE_USERNAME='adminmottooth' \
  SPRING_DATASOURCE_DRIVER_CLASS_NAME='com.microsoft.sqlserver.jdbc.SQLServerDriver' \
  SPRING_JPA_DATABASE_PLATFORM='org.hibernate.dialect.SQLServerDialect' \
  SPRING_JPA_HIBERNATE_DDL_AUTO='update' \
--secure-environment-variables \
  SPRING_DATASOURCE_PASSWORD='Prs417272@'
```

---

## 🧪 Teste da Aplicação

1. Acesse a URL pública da aplicação.
2. Verifique o funcionamento dos endpoints principais (`/`, `/health`, `/api/...`).
3. Confirme no banco de dados a execução de inserções e consultas.

---

## 📄 Comandos Azure CLI Utilizados

```bash
# Criar resource group
az group create --name mottooth-devops --location brazilsouth

# Criar Container Registry
az acr create --resource-group mottooth-devops --name acrmottooth --sku Basic --admin-enabled true

# Obter credenciais do ACR
az acr credential show --name acrmottooth

# Push da imagem Docker
docker tag mottooth-app-devops acrmottooth.azurecr.io/mottooth-app-devops:20
docker push acrmottooth.azurecr.io/mottooth-app-devops:20

# Criar container no ACI
az container create \
  --resource-group mottooth-devops \
  --name mottooth-app \
  --image acrmottooth.azurecr.io/mottooth-app-devops:20 \
  --cpu 1 \
  --memory 1.5 \
  --registry-login-server acrmottooth.azurecr.io \
  --registry-username acrmottooth \
  --registry-password 'SENHA_ACR' \
  --dns-name-label mottooth-app-arthur \
  --ports 8080 \
  --environment-variables \
    SPRING_DATASOURCE_URL=... \
    SPRING_DATASOURCE_USERNAME=... \
  --secure-environment-variables \
    SPRING_DATASOURCE_PASSWORD=...
```

---

## 📦 Estrutura do Projeto

```plaintext
📦 mottooth
├── 📁 src
│   └── 📁 main
│       ├── 📁 java
│       │   └── br.com.fiap.mottooth
│       └── 📁 resources
│           ├── application.properties
│           └── static/
├── Dockerfile
├── pom.xml
├── azure-pipelines.yml
└── README.md
```

---

## ✅ Status Final

| Etapa                | Status      |
| -------------------- | ----------- |
| CI com Docker        | ✅ Concluído |
| CD com deploy no ACI | ✅ Concluído |
| Banco de Dados (SQL) | ✅ Conectado |
| Acesso público       | ✅ Online    |
| Documentação         | ✅ Completa  |

---



---

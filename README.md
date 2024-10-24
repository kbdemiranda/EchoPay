# EchoPay

EchoPay is a system for processing financial transactions through a REST API. It simulates sending and processing transactions using technologies such as Kafka, PostgreSQL, and Spring Boot.

## Requirements

- **Java 21**
- **Docker** and **Docker Compose**
- **Kafka**
- **PostgreSQL**

## Setup and Execution

### 1. Clone the repository
```bash
git clone https://github.com/kbdemiranda/EchoPay.git
cd EchoPay
```

### 2. Configure Docker Compose
The project includes a `docker-compose.yml` file to run the required services (EchoPay, EchoPay-Processor, PostgreSQL, Kafka, and Zookeeper).

Run the following command to start the containers:
```bash
docker-compose up
```

### 3. Main Endpoints

- `POST /api/transactions` - Create a new transaction.
- `GET /api/transactions/{uuid}` - Retrieve a transaction by UUID.
- `PUT /api/transactions/{uuid}/status` - Update the status of a transaction.

## Environment Variables

The environment variables configured in `docker-compose.yml` include:

- `SPRING_DATASOURCE_URL`: URL for the PostgreSQL database connection.
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Configured Kafka server.

## Project Structure

```bash
├── application/         # Contains controllers, DTOs, and services
├── domain/              # Contains domain models and enums
├── infrastructure/      # Database, Kafka configurations, and exceptions
├── Dockerfile           # Docker configuration file
└── docker-compose.yml   # File to orchestrate the containers
```

## Technologies Used

- **Spring Boot**: Framework for building the API.
- **Kafka**: For communication between EchoPay and the Transaction Processor.
- **PostgreSQL**: Relational database.
- **Docker**: For containerization and service orchestration.

## Author

Developed by **Kaique de Miranda**.


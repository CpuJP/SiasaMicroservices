# SiasaMicroservices
Repository of the "PROPOSAL FOR AN INFORMATION SYSTEM FOR THE AUTOMATION OF ACADEMIC SERVICES AT THE UNIVERSITY OF CUNDINAMARCA FACATATIVÁ EXTENSION" of the thesis, consists of several microservices to solve the problems raised in the thesis.

This microservice is a crucial component of the SIASA architecture, a project whose primary goal is to qualify for the title of Computer Systems Engineer.

## Architectural proposal
![Architecture](https://github.com/CpuJP/SiasaMicroservices/blob/main/Arquitectura%20SIAP%20(Back).png)

## SIASA Architecture Documentation

The API documentation is available in the following path, from the main repository:

- [SIASA Documentation](https://github.com/CpuJP/SiasaMicroservices)

## Available projects

This architecture handles different projects:

1. **[siasa-principal](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-principal) ->** This microservice is the main pillar of the SIASA architecture, and it does all the management and automation within the campus.

2. **[siasa-prestamos](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-prestamos) ->** This microservice is linked to a database completely independent of that of the main microservice, this microservice is in charge of the inventories and loans of university well-being (sports and audiovisual material).

3. **[siasa-reportes](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-reportes) ->** This microservice is linked to the Siasa-Principal database, this microservice is responsible for generating reports with statistical data of interest to the different administrators of each area.

4. **[siasa-principal-failover](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-principal-failover) ->** This microservice is responsible for taking over in case the main microservice fails or takes too long to respond.

5. **[siasa-eureka](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-eureka) ->** This microservice is a discovery service for other microservices among themselves for optimal, secure and crucial communication for the SIASA microservices infrastructure.

6. **[siasa-gateway](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-gateway) ->** This microservice is the gateway for the customer, and the unification of each microservice offered by the SIASA architecture, as well as being responsible for activating the circuitbreaker in the event of a failure and solving that failure with a failover.

7. **[siasa-auth](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-auth) ->** This microservice is the natural part where the JWT tokens generated will be generated and validated for use as security for the SIASA software.

8. **[zipkin](http://localhost:9411) ->** This microservice provided by a third party helps us centralize the traceability of the logs released by each microservice.

9. **[prometheus](http://localhost:9090) ->** This microservice provided by a third party helps us centralize the monitoring of the different metrics of the siasa architecture.


## Front-End proposal
The frontendof SIASA has been created by my colleague Cristhian Montejo, who, like me, aspires to the title of Systems Engineer. 
If you want to view the front repository of the project's microservices, you can do so through the following link: [Frontend SIASA](https://github.com/CrisMontejo23/SiasaFront)


## Docker Compose for SIASA Microservices

To deploy the backend infrastructure of the SIASA Microservices, you can use the provided Docker Compose configuration. This configuration includes all the necessary services and settings to run the microservices seamlessly.

### Prerequisites

Before using Docker Compose, make sure you have Docker and Docker Compose installed on your machine. You can download them from the official [Docker website](https://www.docker.com/get-started).

### Docker Compose Configuration

The Docker Compose file ([docker-compose.yml](https://github.com/CpuJP/SiasaMicroservices/blob/main/docker-compose.yml)) in this repository contains the necessary specifications to set up the SIASA Microservices infrastructure. It defines the services, networks, and configurations required for a smooth deployment.

#### Usage

1. Clone the repository: 
   ```bash
   git clone https://github.com/CpuJP/SiasaMicroservices.git
   ```
   
2. Navigate to the directory containing the docker-compose.yml file
   ```bash
   cd SiasaMicroservices
   ```

3. Run the following command to start the SIASA Microservices
   ```bash
   docker-compose up -d
   ```



     
     



## Contribution

If you wish to contribute to the development or improve this microservice, we invite you to do so! Please follow these guidelines:

1. Open an issue to discuss your ideas or problems.
2. Fork the repository.
3. Create a new branch with a descriptive name.
4. Make your changes and ensure high code quality.
5. Open a Pull Request and provide a concise description of your changes.

## Contact

If you have questions, comments, or concerns, please feel free to reach out to us via [email](cpujuanpis@gmail.com) or in the issues section of this repository.

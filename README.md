# SiasaMicroservices
Repository of the "PROPOSAL FOR AN INFORMATION SYSTEM FOR THE AUTOMATION OF ACADEMIC SERVICES AT THE UNIVERSITY OF CUNDINAMARCA FACATATIVÁ EXTENSION" of the thesis, consists of several microservices to solve the problems raised in the thesis.

This microservice is a crucial component of the SIASA architecture, a project whose primary goal is to qualify for the title of Computer Systems Engineer. The microservice provides an interface for accessing and managing data related to various schemas.

## Architectural proposal
![Architecture](https://github.com/CpuJP/SiasaMicroservices/blob/main/Arquitectura%20SIAP%20(Back).png)

## SIASA Architecture Documentation

The API documentation is available in the following path, from the main repository:

- [SIASA Documentation](https://github.com/CpuJP/SiasaMicroservices)

## Available projects

This architecture handles different projects:

1. **[siasa-principal](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-principal) ->** This microservice is the main pillar of the SIASA architecture, and it does all the management and automation within the campus.

2. **[siasa-prestamos](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-prestamos) ->** This microservice is linked to a database completely independent of that of the main microservice, this microservice is in charge of the inventories and loans of university well-being (sports and audiovisual material).

3.
**[siasa-reportes](https://github.com/CpuJP/SiasaMicroservices/tree/siasa-reportes) ->** This microservice is linked to the two different databases (Siasa-Principal and Siasa-Prestamos), this microservice is responsible for generating reports with statistical data of interest to the different administrators of each area.



## Contribution

If you wish to contribute to the development or improve this microservice, we invite you to do so! Please follow these guidelines:

1. Open an issue to discuss your ideas or problems.
2. Fork the repository.
3. Create a new branch with a descriptive name.
4. Make your changes and ensure high code quality.
5. Open a Pull Request and provide a concise description of your changes.

## Contact

If you have questions, comments, or concerns, please feel free to reach out to us via [email](cpujuanpis@gmail.com) or in the issues section of this repository.

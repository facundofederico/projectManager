# Project Manager

## Objective and use
The main objective behind this project is to get some hands-on practice using:
- Java
- Neo4j
- Docker

The solution itself provides a tool for users to create projects, define a hierarchy
of tasks for it, and use it to calculate the total time required for the project using
a critical path calculation (like in a Grantt chart).

For this version, I chose to keep it simple by sticking to a simple hierarchy, but it is
my idea for a future version to allow users to define directed graphs by manually linking
'needed' subtasks to a task.

## Development notes
The project is divided in 3 modules (domain, infrastructure and adapter), following a
hexagonal architecture:

Controller -> Domain <- Repository

This allows me to keep the domain logic where it belongs, and to define, if I wanted,
different implementations for the Controller and the Repository.

In this implementation, I'm using a Neo4j repository and a CLI controller.

For future versions, I will also provide:
- Full directed-graph support
- API REST controller
- User authentication

## Installation
This application is being containerized in a docker file so anyone can try it.

- Clone the repository
- Build the image with `docker build -t project-manager .`
- Create the neo4j container with `docker compose up -d neo4j`
- Run the app interactively with `docker compose run --rm app`
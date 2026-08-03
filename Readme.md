# 🚖 Ride Sharing Platform - Microservices Architecture

A production-style **Ride Sharing Platform** built using **Java, Spring Boot, Spring Cloud, Kafka, Redis, CockroachDB, Docker, and NGINX Load Balancer** following **Microservices Architecture**, **Event-Driven Design**, and **Hexagonal Architecture**.

The objective of this project is to demonstrate how modern distributed systems are built with scalability, resiliency, service discovery, asynchronous messaging, and horizontal scaling.

---

# Architecture

```mermaid
flowchart TD

    A[Client / UI]

    B[NGINX Load Balancer<br/>Round Robin]

    A --> B

    B --> G1[API Gateway<br/>Instance 1]
    B --> G2[API Gateway<br/>Instance 2]
    B --> G3[API Gateway<br/>Instance 3]

    G1 --> RS[Ride Service]
    G2 --> RS
    G3 --> RS

    G1 --> DLS[Driver Location Service]
    G2 --> DLS
    G3 --> DLS

    RS -- "RideRequestEvent (Kafka)" --> MS[Matching Service]

    MS -->|"Find Nearby Drivers"| DLS

    MS -- "RideMatchedEvent (Kafka)" --> RS

    RS --> DA[Driver Assigned]

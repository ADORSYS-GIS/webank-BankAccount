# Deposit Account Service (DAS) - ARC42 Architecture Documentation

## Table of Contents
1. [Introduction](#1-introduction)
2. [Architecture Constraints](#2-architecture-constraints)
3. [System Scope and Context](#3-system-scope-and-context)
4. [Solution Strategy](#4-solution-strategy)
5. [Building Block View](#5-building-block-view)
6. [Runtime View](#6-runtime-view)
7. [Deployment View](#7-deployment-view)
8. [Cross-cutting Concepts](#8-cross-cutting-concepts)
9. [Architecture Decisions](#9-architecture-decisions)
10. [Quality Requirements](#10-quality-requirements)
11. [Risks and Technical Debt](#11-risks-and-technical-debt)
12. [Product Management](#12-product-management)
13. [Glossary](#13-glossary)
14. [Appendix](#14-appendix)

---

## 1. Introduction
### Document Goals
This document provides a structured technical overview of the Deposit Account Service (DAS) using the ARC42 template. Its purpose is to support stakeholders, architects, and developers in understanding the architecture and design choices of the DAS module.

### Stakeholders
- **Product Managers**: Define functional and business requirements.
- **Development Teams**: Implement and maintain the service.
- **Security and Compliance Teams**: Ensure GDPR and regulatory compliance.
- **Operations**: Manage deployment, scalability, and monitoring.

## 2. Architecture Constraints
### Regulatory Constraints
- **GDPR Compliance**: Ensures data privacy and protection for users.
- **Financial Regulations**: Aligns with relevant financial regulatory standards for transaction handling and auditing.

### Technical Constraints
- **Data Encryption**: Sensitive data is encrypted both at rest and in transit.
- **Performance**: Must meet specific response time targets for balance inquiries and transaction history requests.

## 3. System Scope and Context
### Business Context
DAS is a backend module within a financial ecosystem, responsible for managing user accounts, balances, and transaction histories. It interfaces with the User Application, OBS (Online Banking Service), and AAS (Account Access Service).

### External Interfaces
- **User Interface**: Provides access for end-users to manage accounts and view balances.
- **Other Services**: Interfaces with OBS and AAS to handle tasks like balance inquiries and transaction retrievals.

## 4. Solution Strategy
DAS utilizes a layered architecture for clear separation of concerns, modularity, and scalability.

- **API Layer**: Exposes RESTful endpoints for external interactions.
- **Service Layer**: Processes business logic for account registration, balance inquiries, and transaction history.
- **Data Layer**: Manages data access, encryption, and storage.

## 5. Building Block View
### Overview
The DAS module is composed of three main components:
1. **Account Registration**: Manages account creation and data validation.
2. **Balance Inquiry**: Handles real-time balance retrieval.
3. **Transaction History**: Logs, retrieves, and filters transaction data.

### Detailed View
- **API Layer**: Communicates with users and other services.
- **Service Layer**: Contains business logic for account management.
- **Data Layer**: Manages interactions with the database for account and transaction data.

## 6. Runtime View
### Balance Inquiry and Account Registration Scenario

The following diagram illustrates the interaction between different services and components in the **DAS** system, particularly focusing on the balance inquiry, account registration, and OTP verification processes.

![Account Registration and Balance Inquiry Sequence](../../webbank-BankAccount/Docs/Images/Architecture_DAS2.png)

1. **Account Registration**: The **Account Holder** initiates the process by providing their phone number and public key.
    - **Step 1**: The Online Banking System BFF sends a request with the phone number and public key to initiate registration.
    - **Step 2**: The Pending Registration Service registers the account and sends an OTP to the account holder.
    - **Step 3**: The user submits the OTP, which the Online Banking System BFF sends to the Pending Registration Service for verification.
    - **Step 4**: Upon successful verification, the account is registered, and a status is returned.

2. **Balance Inquiry**:
    - **Step 5**: The Online Banking System BFF requests account creation from the Deposit Account Service, which returns the account ID and balance.
    - **Step 6**: An access is created within the Account Access Service, linking the account ID and public key, which returns the balance to the Online Banking System BFF.

3. **Transaction History Retrieval**: Once an account is active, the DAS can retrieve and filter transaction history upon request.

### Key Scenarios
- **Account Registration**: The user initiates account creation, and the system validates, stores, and confirms data.
- **Transaction History Retrieval**: The user requests a filtered transaction history, which DAS retrieves accordingly.

## 7. Deployment View
### Deployment Strategy

- **Cloud Infrastructure**
    - **Platform**: DAS is deployed on Amazon Web Services (AWS), which provides a reliable and scalable cloud environment with high availability and security.
    - **Load Balancing**: AWS Elastic Load Balancing (ELB) distributes incoming application traffic across multiple instances to ensure availability and resilience.
    - **Scaling**: AWS Auto Scaling monitors system performance and automatically adjusts capacity as needed.

- **Container Orchestration**
    - **Kubernetes (K8s)**: DAS is containerized and managed using Kubernetes for deployment and scalability.

- **CI/CD Pipelines**
    - Automated CI/CD pipelines are set up for building, testing, and deployment with tools like Jenkins or GitLab CI/CD, integrating with AWS and Kubernetes.

## 8. Cross-cutting Concepts
### Security
- **Authentication and Authorization**: Keycloak provides user authentication and authorization services.
- **Data Encryption**: All sensitive data is encrypted in transit and at rest.
- **Access Control**: Role-based access control (RBAC) is enforced for sensitive data.

### Performance Optimization
- **Caching and Indexing**: Implemented for commonly accessed data, such as balances and transaction histories.

### Error Handling
- **Logging and Alerts**: All errors are logged, and alerts are generated for critical issues in real-time.

## 9. Architecture Decisions
- **Layered Architecture**: For modularity and separation of concerns.
- **RESTful API**: Ensures broad compatibility with external services.
- **Data Encryption**: Mandatory for security and compliance with regulations.
- **GDPR Compliance**: User data is handled securely, respecting user privacy.

## 10. Quality Requirements
### Functional Requirements
- Account Registration, Balance Inquiry, and Transaction History retrieval.

### Non-Functional Requirements
- **Performance**: Low-latency responses.
- **Security**: Strong encryption and user data protection.
- **Usability**: User-friendly API design.

## 11. Risks and Technical Debt
- **Risks**: Includes data breach concerns and potential performance bottlenecks with high transaction volume.
- **Technical Debt**: Service module refactoring as DAS scales, and integration challenges with legacy systems.

## 12. Product Management
- **Technology Stack**: DAS is developed with Java, Spring Boot, ReactJS, TypeScript, TailwindCSS, AWS, Kubernetes, and Keycloak for a robust and secure environment.

## 13. Glossary
- **DAS**: Deposit Account Service
- **OBS**: Online Banking Service
- **AAS**: Account Access Service

---


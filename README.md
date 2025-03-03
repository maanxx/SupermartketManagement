# SupermartketManagement

SupermartketManagement is a distributed application for supermarket management that leverages Hibernate for object-relational mapping (ORM) and Java RMI for remote method invocation. This project is built using Maven and follows a modular design with separate modules for client, server, and shared code.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview

This project provides a robust framework for managing supermarket operations by integrating various subsystems such as employee management, account handling, customer orders, inventory, and invoicing. With a distributed architecture, the system is scalable and maintains a clean separation of concerns among the client, server, and shared components.

## Features

- **Distributed Architecture:** Utilizes Java RMI to allow remote interaction between client and server.
- **Robust Persistence Layer:** Uses Hibernate ORM for database management and mapping of Java objects to MySQL tables.
- **Modular Design:** Separates concerns into client, server, and shared modules for easier maintenance and scalability.
- **Automatic Schema Update:** The `hibernate.hbm2ddl.auto` property is set to `update` to automatically handle database schema changes.
- **Enhanced Debugging:** SQL logging and formatting are enabled to facilitate debugging and performance tuning.

## Technology Stack

- **Programming Language:** Java (version 21)
- **Build Tool:** Maven
- **ORM:** Hibernate 6.6.4.Final
- **Remote Communication:** Java RMI
- **Database:** MySQL
- **Additional Libraries:**
  - JavaFX (for client UI components)
  - Spring Context (for RMI support)
  - JavaFaker (for data generation)
  - Lombok (for cleaner code)
  - Apache Commons Lang
  - JUnit 5 (for testing)
- **Other Tools:** JAXB Runtime for XML binding

## Installation
 Prerequisites
- **Java:** 21 or higher
- **Maven:** 3.8.1 or higher
- **MySQL Server:** Ensure you have a MySQL Server running with a database named `SupermarketManagement`
- **IDE (Optional):** IntelliJ IDEA, Eclipse, or any other Java IDE

### Steps

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/maanxx/SupermartketManagement.git
   cd SupermartketManagement
Configure the Database:
Create a MySQL database named SupermarketManagement.
Open the persistence.xml file and update the database connection properties if needed (e.g., username, password, and URL).
Build the Project: mvn clean install
Run the Server: mvn exec:java -Dexec.mainClass="server.MainServerClass"
Run the Client:mvn exec:java -Dexec.mainClass="client.MainClientClass"



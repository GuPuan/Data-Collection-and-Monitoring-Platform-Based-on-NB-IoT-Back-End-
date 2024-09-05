# IoT Data Collection & Monitoring Platform (Back-End)

## Overview
The back-end of this IoT data collection and monitoring platform is developed using **Spring Boot**. It manages data collection from NB-IoT sensors, stores the data in a **MySQL** database, and provides APIs for data management and control. The back-end enables real-time data collection, storage, retrieval, and operations like starting/stopping data collection, issuing warnings, and performing CRUD operations.

## Features
- **API Integration**: Collects real-time data from NB-IoT sensors via APIs.
- **Data Storage**: Manages sensor data in a MySQL database.
- **Control Mechanisms**: APIs to start/stop data collection and issue data warnings.
- **CRUD Operations**: Create, read, update, and delete sensor data.
- **Export Functionality**: Export sensor data in various formats (e.g., Excel, CSV).

## Project Structure
- **src/**: Contains the Java source code.
  - **controller/**: Handles API requests and responses.
  - **service/**: Contains the business logic of the platform.
  - **repository/**: Manages database interactions.
  - **model/**: Represents the entities for the MySQL database.
  - **config/**: Configuration files for the database and other settings.

## Prerequisites
- Java 8 or higher
- Maven 3.6+
- MySQL 5.7 or higher

## Installation
1. Clone this repository:
   ```bash
   git clone <repository_url>

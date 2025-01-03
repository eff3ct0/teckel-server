# Teckel Server

Welcome to the Teckel Server project! This README provides an overview of the project, installation instructions, and
usage examples to help you get started.

## Overview

Teckel Server is a robust platform designed to facilitate efficient data processing workflows
using [Teckel](https://github.com/eff3ct0/teckel) and connecting with a remote Apache Spark Environment. It leverages
the capabilities of the Teckel framework to provide a scalable ETL (Extract, Transform, Load) environment suitable for a
wide variety of data processing tasks.

## Features

- **Scalable ETL Processing**: Built on Apache Spark, supporting high-performance large-scale data processing.
- **Flexible Configuration**: Define complex ETL workflows using YAML configuration files.
- **Dockerized Deployment**: Easy to deploy with Docker, allowing for both development and production use.
- **Integration with Popular Data Stores**: Supports connecting to various data sources, including CSV, JSON, and
  Parquet formats.

## Installation

To install and run Teckel Server, clone the repository and set up the environment as follows:

```bash
git clone https://github.com/eff3ct/teckel-server.git
cd teckel-server
```

## Prerequisites

Before running Teckel Server, ensure that you have the following prerequisites installed:

- **Java 11**: Make sure you have Java 11 installed.

- **Docker**: Optional, for containerized deployment.

## Usage

### Running the Server Locally

To run the Teckel Server, follow these steps:

1. Build the project using SBT:

```bash
sbt assembly
```

2. Run the server:

```bash
java -jar target/scala-2.13/teckel-server.jar
```

3. Access the server:

```bash
curl http://localhost:1443/health
```

### Running the Server with Docker

You can build and run the Teckel Server with Docker using the following steps:

0. [Optional] Build the Docker image:

```bash
sbt docker
```

1. Or, run the Pre-built Docker Image:

```bash
# Pull the latest image
docker run -p 1443:1443 eff3ct/teckel-server:latest
```

```bash
# Or, specify a specific version
docker run -p 1443:1443 eff3ct/teckel-server:0.1.0
```

## Contributing

We welcome contributions! If you'd like to contribute, please fork the repository, make your changes, and submit a pull
request. Ensure that your code adheres to our coding standards and passes all tests.

# License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.

# Support

For any questions or issues, please open an issue on the GitHub repository or contact the maintainers directly.
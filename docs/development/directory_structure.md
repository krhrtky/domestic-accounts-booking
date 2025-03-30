# Project Directory Structure

This document explains the directory structure of the project, including the purpose of each directory and its contents. It also provides examples of typical files found in each directory, the rationale behind the directory structure, and any conventions or best practices followed.

## Root Directory

The root directory contains the main configuration files and directories for the project.

- `.github/`: Contains GitHub-specific configuration files, such as workflows and issue templates.
- `backend/`: Contains the backend code and related files.
- `docs/`: Contains documentation files for the project.
- `frontend/`: Contains the frontend code and related files.
- `gradle/`: Contains Gradle wrapper files.
- `.gitignore`: Specifies files and directories to be ignored by Git.
- `build.gradle.kts`: The main Gradle build script for the project.
- `settings.gradle.kts`: The Gradle settings file for the project.

## Backend Directory

The `backend/` directory contains the backend code and related files.

- `api/`: Contains the API code and related files.
  - `build.gradle.kts`: The Gradle build script for the API module.
  - `src/`: Contains the source code for the API module.
- `applications/`: Contains the application code and related files.
  - `build.gradle.kts`: The Gradle build script for the applications module.
  - `src/`: Contains the source code for the applications module.
- `core/`: Contains the core code and related files.
  - `build.gradle.kts`: The Gradle build script for the core module.
  - `src/`: Contains the source code for the core module.
- `domains/`: Contains the domain code and related files.
  - `serviceAccount/`: Contains the service account domain code and related files.
    - `build.gradle.kts`: The Gradle build script for the service account domain module.
    - `src/`: Contains the source code for the service account domain module.
  - `transaction/`: Contains the transaction domain code and related files.
    - `build.gradle.kts`: The Gradle build script for the transaction domain module.
    - `src/`: Contains the source code for the transaction domain module.
- `infrastructure/`: Contains the infrastructure code and related files.
  - `build.gradle.kts`: The Gradle build script for the infrastructure module.
  - `src/`: Contains the source code for the infrastructure module.
- `shared/`: Contains shared code and related files.
  - `build.gradle.kts`: The Gradle build script for the shared module.
  - `src/`: Contains the source code for the shared module.
- `testCore/`: Contains the test core code and related files.
  - `build.gradle.kts`: The Gradle build script for the test core module.
  - `src/`: Contains the source code for the test core module.

## Docs Directory

The `docs/` directory contains documentation files for the project.

- `architecture/`: Contains architecture-related documentation.
  - `backend.md`: Describes the backend architecture of the project.
  - `dataflow.md`: Explains the data flow within the system.
  - `frontend.md`: Describes the frontend architecture of the project.
  - `system_overview.md`: Provides an overview of the entire system.
  - `tradeoffs.md`: Discusses the trade-offs made during the design and development of the system.
- `development/`: Contains development-related documentation.
  - `coding_guidelines.md`: Outlines the coding guidelines for the project.
  - `directory_structure.md`: Explains the directory structure of the project.
  - `glossary.md`: Contains a glossary of terms used in the project.
  - `setup.md`: Explains how to set up the development environment for the project.
  - `testing.md`: Outlines the testing strategy for the project.
  - `workflow.md`: Describes the development workflow for the project.
- `operations/`: Contains operations-related documentation.
  - `deployment.md`: Explains the deployment process for the project.
  - `incident_response.md`: Outlines the incident response plan for the project.
  - `monitoring.md`: Describes the monitoring strategy for the project.
- `product/`: Contains product-related documentation.
  - `journey_map/`: Contains user journey maps for various features.
    - `shared_credit_card_split.md`: Details the user journey for the shared credit card split feature.
  - `problems/`: Contains documents discussing the problems addressed by various features.
    - `shared_credit_card_split.md`: Discusses the problems addressed by the shared credit card split feature.
  - `user_stories/`: Contains user stories for various features.
    - `shared_credit_card_split.md`: Contains user stories for the shared credit card split feature.

## Frontend Directory

The `frontend/` directory contains the frontend code and related files.

- `package.json`: Specifies the dependencies and scripts for the frontend project.
- `src/`: Contains the source code for the frontend project.

## Gradle Directory

The `gradle/` directory contains Gradle wrapper files.

- `wrapper/`: Contains the Gradle wrapper JAR and properties files.
  - `gradle-wrapper.jar`: The Gradle wrapper JAR file.
  - `gradle-wrapper.properties`: The Gradle wrapper properties file.
- `libs.versions.toml`: Specifies the versions of the dependencies used in the project.

## Conventions and Best Practices

The directory structure follows a modular approach, with separate directories for different aspects of the project, such as backend, frontend, and documentation. This helps in organizing the code and related files in a logical manner, making it easier to navigate and maintain the project.

Each module within the backend directory has its own `build.gradle.kts` file, which allows for independent configuration and dependency management. This modular approach also facilitates easier testing and deployment of individual components.

The documentation is organized into separate directories based on the type of information, such as architecture, development, operations, and product. This helps in keeping the documentation well-structured and easy to find.

By following these conventions and best practices, the project aims to maintain a clean and organized codebase, which in turn helps in improving the overall development experience and reducing the chances of errors and inconsistencies.

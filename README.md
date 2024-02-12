# CaclTrain

#### Train Route Demand and Ticket Pricing Calculator

This project is a Java-based application designed to calculate the demand on various train routes and determine the optimal ticket prices. It's built for players of the "Railway Manager" game.

The application incorporates various factors such as the number of passengers, route distance, and peak travel times to generate passenger flows for each train route. These passenger flows form the basis for calculating the demand for each route, which is represented in a first matrix. In addition, the application computes the potential revenue a player can generate on each route, which is depicted in a secondary matrix.

Furthermore, users have the ability to create a custom tariff for their in-game company. This feature ensures that the projected earnings accurately reflect the user's pricing strategy.

The application is built with Maven. It features a user-friendly interface that allows users to input data and view the calculated demand and ticket prices.

## Prerequisites
This application is designed to run on Windows operating systems only and does not support cross-platform usage.

Before you can run this project, you need to have Java and Java SDK (Software Development Kit) installed on your machine. The minimum required versions are JDK 17 and JRE 1.8.

* Java: You can download it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html). Make sure to choose the version that matches your operating system and architecture.
* Java SDK: If you don't have it installed, you can download it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html). Again, choose the version that matches your operating system and architecture.

After installing Java and Java SDK, you should be able to run the application. If you encounter issues, you may need to set up the JAVA_HOME system variable:

On Windows:

* Right-click on 'My Computer' and select 'Properties'.
* Click on 'Advanced System Settings'.
* Click on 'Environment Variables'.
* Click on 'New' under System Variables.
* For 'Variable name', enter JAVA_HOME.
* For 'Variable value', enter the path to your Java SDK installation directory (for example, C:\Program Files\Java\jdk-11.0.1).
* Click 'OK' and restart your machine to apply the changes.

## Run Locally

Clone the project

```bash
git clone https://github.com/EagleBlood/CalcTrain
```

Go to the project directory

```bash
cd my-project
```

Install dependencies

```bash
mvn install
```

Start the application

```bash
mvn javafx:run
```

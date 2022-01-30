# Data-Warehouse-for-METRO-Shopping-Store
## Description:
This project contains the implementation of building a sample Data Warehouse for METRO Shopping Store in Pakistan. 
The dataset used in this project was used just as a sample and does not contain the real data of METRO Pakistan. 

## External JARs used:
-  commons-collections4.4.1.jar
-  mysql-connector-java-8.0.27.jar

## Instructions on running the project:
1. Create a schema in your database and populate it with "Transaction_and_MasterData_Generator.sql" 
2. Now run the "CreationDW.sql" file so that the schema for warehouse can be created along with appropriate tables their constraints. 
2. Just open the project in any IDE of your choice and run the project. 
3. It will ask you for your username and password for the mysql database as well as name of the schema 
   which you have populated in the 1st step. 
5. After giving all the required info, your warehouse will be automatically build in a schema "i180589_Project". You can change the 
   name of schema as well but for that you will need to change the name in many different places in the code as well as in the sql script files so its better to stick with the default credentials. 

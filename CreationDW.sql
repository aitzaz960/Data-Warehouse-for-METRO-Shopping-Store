drop schema if exists i180589_Project;
create schema i180589_Project;

drop table if exists i180589_Project.Product;
CREATE TABLE i180589_Project.Product (
  Product_id VARCHAR(10) NOT NULL,
  Product_name VARCHAR(45) NOT NULL,
  Price DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (Product_id),
  UNIQUE INDEX Product_id_UNIQUE (Product_id ASC) VISIBLE);

drop table if exists i180589_Project.Customer;
CREATE TABLE i180589_Project.Customer (
  Customer_id VARCHAR(10) NOT NULL,
  Customer_name VARCHAR(45) NOT NULL,
  PRIMARY KEY (Customer_id),
  UNIQUE INDEX Customer_id_UNIQUE (Customer_id ASC) VISIBLE);

drop table if exists i180589_Project.Store;
CREATE TABLE i180589_Project.Store (
  Store_id VARCHAR(10) NOT NULL,
  Store_name VARCHAR(45) NOT NULL,
  PRIMARY KEY (Store_id),
  UNIQUE INDEX Store_id_UNIQUE (Store_id ASC) VISIBLE);

drop table if exists i180589_Project.Supplier;
CREATE TABLE i180589_Project.Supplier (
  Supplier_id VARCHAR(10) NOT NULL,
  Supplier_name VARCHAR(45) NOT NULL,
  PRIMARY KEY (Supplier_id),
  UNIQUE INDEX Supplier_id_UNIQUE (Supplier_id ASC) VISIBLE);

drop table if exists i180589_Project.Transaction_Date;
CREATE TABLE i180589_Project.Transaction_Date (
  Date_id VARCHAR(12) NOT NULL,
  Date_Year INT NOT NULL,
  Year_Half INT NOT NULL,
  Year_Quarter INT NOT NULL,
  Date_Month INT NOT NULL,
  Week_of_Month INT NOT NULL,
  Week_of_Year INT NOT NULL,
  Day_of_Week INT NOT NULL,
  Day_of_Month INT NOT NULL,
  PRIMARY KEY (Date_id));
  
drop table if exists i180589_Project.Revenue;
CREATE TABLE i180589_Project.Revenue (
	Fact_id INT NOT NULL AUTO_INCREMENT,
	Product_id VARCHAR(10),
    Customer_id VARCHAR(10),
    Store_id VARCHAR(10),
    Supplier_id VARCHAR(10),
    Date_id VARCHAR(12),
    Quantity INT,
    Sale DECIMAL(5,2),
    PRIMARY KEY (Fact_id),
    FOREIGN KEY (Product_id) REFERENCES Product(Product_id),
    FOREIGN KEY (Customer_id) REFERENCES Customer(Customer_id),
    FOREIGN KEY (Store_id) REFERENCES Store(Store_id),
    FOREIGN KEY (Supplier_id) REFERENCES Supplier(Supplier_id),
    FOREIGN KEY (Date_id) REFERENCES Transaction_Date(Date_id)
);
-- IF COL_LENGTH('tableName', 'columnName') IS NOT NULL
IF NOT EXISTS (SELECT *FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME = 'OBJECT_METADATA')
  BEGIN
      EXECUTE ('CREATE TABLE OBJECT_METADATA
      ( OBJECT_UID INT,
        OBJECT_TYPE VARCHAR(10),
        OBJECT_NAME VARCHAR(30) PRIMARY KEY)')
  END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'USERS' AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE USERS
      ( USER_UID INT,
        USER_ID VARCHAR(30) PRIMARY KEY,
        USER_FIRST_NAME VARCHAR(30) NOT NULL,
        USER_LAST_NAME VARCHAR(30),
        LANGUAGE_PREFERENCE VARCHAR(30) NOT NULL,
        USER_TYPE VARCHAR(30) NOT NULL,
        ENABLED BIT NOT NULL,
        PASSWORD VARCHAR(30) NOT NULL,
        NOTE VARCHAR(MAX)
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'USERS' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(1,''TABLE'',''USERS'')')
END

IF NOT EXISTS (SELECT *FROM USERS  WHERE USER_ID = 'Administrator')
  BEGIN
    EXECUTE('INSERT INTO USERS (USER_ID,USER_FIRST_NAME,USER_LAST_NAME,LANGUAGE_PREFERENCE,USER_TYPE,ENABLED,PASSWORD) VALUES(''Administrator'',''Chanky'',''Mallick'',''English'',''ADMIN'',1,''520759'')')
END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'PERMISSIONS' AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE PERMISSIONS
      ( PERMISSION_UID INT,
        PERMISSION_KEY VARCHAR(30) PRIMARY KEY,
        PERMISSION_NAME VARCHAR(15),
        PERMISSION_DESCRIPTION VARCHAR(MAX)    
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'PERMISSIONS' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(2,''TABLE'',''PERMISSIONS'')')
END
-- Permission List --
-- BEGIN
--     INSERT INTO PERMISSIONS (PERMISSION_KEY,PERMISSION_NAME,PERMISSION_DESCRIPTION) VALUES ('com.ordermanager.users','')
-- END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDERS'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE ORDERS
      ( ORDER_UID INT,
        LOCATION VARCHAR(10),
        BILL_NO VARCHAR(10) PRIMARY KEY,
        ORDER_DATE DATETIME,
        DELIVERY_DATE DATETIME,        
        CUSTOMER_NAME VARCHAR(30),
        CONTACT_NO VARCHAR(10),
        PIECE_VENDOR VARCHAR(30),
        ORDER_TYPE VARCHAR(20),       
        PRODUCT_TYPE VARCHAR(20),
        QUANTITY INT,
        PRICE INT,
        DISCOUNT INT,
        MEASURED_BY VARCHAR(20),
        CURRENT_STATUS VARCHAR(30),--LATER ADDED,
        CUSTOM_PRICE_ENABLED BIT,
        CUSTOM_PRICE_MASTER INT DEFAULT 0,
        CUSTOM_PRICE_TAILOR INT DEFAULT 0,
        NOTE VARCHAR(MAX)
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDERS' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(3,''TABLE'',''ORDERS'')')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_MOBILITY'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE ORDER_MOBILITY
      ( MOBILITY_UID INT PRIMARY KEY,
        BILL_NO VARCHAR(10),
        PROCESS_DATE DATETIME,    
        MAIN_STATUS VARCHAR(30),
        SUB_STATUS  VARCHAR(30),
        CURRENT_LOCATION  VARCHAR(30),   
        NOTE VARCHAR(MAX),
        CONSTRAINT FK_BILL_NO FOREIGN KEY (BILL_NO) REFERENCES ORDERS(BILL_NO)
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_MOBILITY' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(4,''TABLE'',''ORDER_MOBILITY'')')
END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'PAYMENT_TRANSACTIONS'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE PAYMENT_TRANSACTIONS 
                        (
                        TRANSACTION_UID INT  PRIMARY KEY,
                        ORDER_BILL_NO VARCHAR(10),                     
                        PAYMENT_TYPE VARCHAR(20),
                        AMOUNT INT,    
                        TRANSACTION_DATE DATETIME DEFAULT GETDATE(),
                        CONSTRAINT FK_BILLNO FOREIGN KEY (ORDER_BILL_NO) REFERENCES ORDERS(BILL_NO)
                      )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'PAYMENT_TRANSACTIONS' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(5,''TABLE'',''PAYMENT_TRANSACTIONS'')')
END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'AUDIT'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE AUDIT 
                        (
                        AUDIT_UID INT PRIMARY KEY,
                        AUDIT_TYPE VARCHAR(20),                     
                        AUDIT_MODULE VARCHAR(20),
                        AUDIT_DATETIME DATETIME,    
                        AUDITED_BY VARCHAR(30),
                        AUDIT_KEY INT, 
                        AUDIT_HISTORY VARCHAR(MAX),
                        NOTE VARCHAR(MAX),
                        CONSTRAINT FK_AUDITUSER FOREIGN KEY (AUDITED_BY) REFERENCES USERS(USER_ID)
                      )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'AUDIT' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(6,''TABLE'',''AUDIT'')')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ITEMS'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE ITEMS
                ( ITEM_UID INT,
                  ITEM_NAME VARCHAR(20) PRIMARY KEY,
                  ITEM_TYPE VARCHAR(20),--MAIN,EXTRAS
                  PARENT_ITEM VARCHAR(20),-- EXTRA ITEM UNDER A MAIN ITEM
                  ITEM_SUB_TYPE VARCHAR(20),--CUSTOM LIST 
                  MASTER_PRICE INT,
       	   TAILOR_PRICE INT,
       	    FINISHER_PRICE INT,
                  ITEM_ORDER VARCHAR(10),--Need to change into double data type later  
       	    ACTIVE BIT,
                  NOTE VARCHAR(MAX)
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ITEMS' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(7,''TABLE'',''ITEMS'')')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_ITEMS'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE ORDER_ITEMS
      (          ORDER_ITEMS_UID INT ,
                  BILL_NO VARCHAR(10),
       	    ITEM_NAME VARCHAR(20),
                  NOTE VARCHAR(MAX),
                  CONSTRAINT PK_ORDER_ITEMS PRIMARY KEY NONCLUSTERED ([BILL_NO], [ITEM_NAME]),
                  CONSTRAINT FK_ITEMNAME FOREIGN KEY (ITEM_NAME) REFERENCES ITEMS(ITEM_NAME),
                  CONSTRAINT FK_BILLNO2 FOREIGN KEY (BILL_NO) REFERENCES ORDERS(BILL_NO),
                
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_ITEMS' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(8,''TABLE'',''ORDER_ITEMS'')')
END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'APP_DATA'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE APP_DATA
      (           APP_DATA_UID INT ,
                  APP_DATA_MODULE VARCHAR(50),
                  APP_DATA_KEY VARCHAR(50),         	
                  APP_DATA_VALUE VARCHAR(MAX),
                  CONSTRAINT PK_APP_DATA PRIMARY KEY NONCLUSTERED ([APP_DATA_MODULE], [APP_DATA_KEY]), 
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'APP_DATA' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(9,''TABLE'',''APP_DATA'')')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_STATUS_TYPES'  AND OBJECT_TYPE='TABLE')
  
  BEGIN
      EXECUTE ('  CREATE TABLE ORDER_STATUS_TYPES
      (           STATUS_TYPE_UID INT ,
                  STATUS_TYPE VARCHAR(20),
                  STATUS_NAME VARCHAR(50),
                  STATUS_PARENT_NAME VARCHAR(50),
                  STATUS_ORDER INT,
	    NOTE VARCHAR(MAX),
                  ACTIVE BIT DEFAULT 0,  
                  CONSTRAINT PK_ORDER_STATUS_TYPES PRIMARY KEY NONCLUSTERED ([STATUS_NAME], [STATUS_PARENT_NAME]), 
        )')              
  END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_STATUS_TYPES' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(10,''TABLE'',''ORDER_STATUS_TYPES'')')
END

IF (SELECT COUNT(*) FROM ORDER_STATUS_TYPES) < 1
BEGIN
INSERT INTO ORDER_STATUS_TYPES(STATUS_TYPE_UID,STATUS_TYPE,STATUS_NAME,STATUS_PARENT_NAME,STATUS_ORDER,NOTE,ACTIVE)
        SELECT 1,'MAIN_STATUS','NEW_ORDER','',1,'',1
        UNION ALL
        SELECT 2,'MAIN_STATUS','IN_PROCESS','',2,'',1
        UNION ALL
        SELECT 3,'MAIN_STATUS','READY_TO_DELIVER','',3,'',1
        UNION ALL
        SELECT 4,'MAIN_STATUS','DELIVERY_COMPLETED','',4,'',1
        UNION ALL
        SELECT 5,'SUB_STATUS','NEW_ORDER','NEW_ORDER',1,'',1
        UNION ALL
        SELECT 6,'SUB_STATUS','PROBLEM_HALT','NEW_ORDER',1,'',1
        UNION ALL
        SELECT 7,'SUB_STATUS','REQUIREMENT_HALT','NEW_ORDER',1,'',1
        UNION ALL
        SELECT 8,'SUB_STATUS','ADVANCE_LOW','NEW_ORDER',1,'',1
        UNION ALL
        SELECT 9,'SUB_STATUS','CUTTING_IN_PROGRESS','IN_PROCESS',1,'' ,1 -- Need to add PROCESS_ONHOLD
        UNION ALL
        SELECT 10,'SUB_STATUS','CUTTING_COMPLETED','IN_PROCESS',2,'' ,1 
        UNION ALL
        SELECT 11,'SUB_STATUS','STICHING_IN_PROGRESS','IN_PROCESS',3,'' ,1  
        UNION ALL
        SELECT 12,'SUB_STATUS','STICHING_COMPLETED','IN_PROCESS',4,'',1   
        UNION ALL
        SELECT 13,'SUB_STATUS','FINISHING_IN_PROGRESS','IN_PROCESS',5,'' ,1  
        UNION ALL
        SELECT 14,'SUB_STATUS','FINISHING_COMPLETED','IN_PROCESS',6,'',1   
        UNION ALL
        SELECT 15,'SUB_STATUS','IRON_IN_PROGRESS','IN_PROCESS',7,'',1   
        UNION ALL
        SELECT 16,'SUB_STATUS','IRON_COMPLETED','IN_PROCESS',8,'' ,1  
END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'CURRENT_LOCATIONS'  AND OBJECT_TYPE='TABLE')
  
  BEGIN
      EXECUTE ('  CREATE TABLE CURRENT_LOCATIONS
      (           CURRENT_LOCATIONS_UID INT ,
                  LOCATION_NAME VARCHAR(40),
                  PARENT_STATUS VARCHAR(50), 
	    NOTE VARCHAR(MAX),
                  ACTIVE BIT,
                  CONSTRAINT PK_LOCATION_NAME PRIMARY KEY NONCLUSTERED ([LOCATION_NAME],[PARENT_STATUS])                 
        )')              
  END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'CURRENT_LOCATIONS' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(11,''TABLE'',''CURRENT_LOCATIONS'')')
END

IF (SELECT COUNT(*) FROM CURRENT_LOCATIONS) < 1
BEGIN
INSERT INTO CURRENT_LOCATIONS(CURRENT_LOCATIONS_UID,LOCATION_NAME,PARENT_STATUS,NOTE,ACTIVE)
        SELECT 1,'1_NO_SHOP','NEW_ORDER','',1
        UNION ALL
        SELECT 2,'2_NO_SHOP','NEW_ORDER','',1
        UNION ALL
        SELECT 3,'3_NO_SHOP','NEW_ORDER','',1
        UNION ALL        
        SELECT 4,'LOW_ADVANCE_RACK','NEW_ORDER','',1
        UNION ALL        
        SELECT 5,'REQUIREMENT_RACK','NEW_ORDER','',1
        UNION ALL        
        SELECT 6,'WORKSHOP','IN_PROCESS','',1
        UNION ALL        
        SELECT 7,'DELIVERY_RACKS','READY_TO_DELIVER','' ,1      
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'EMPLOYEES'  AND OBJECT_TYPE='TABLE')  
BEGIN
EXECUTE ('CREATE TABLE "EMPLOYEES" (
	"EMPLOYEE_UID" INT NOT NULL,
	"EMP_NAME" VARCHAR(50) NOT NULL,
	"EMP_MOBILE1" VARCHAR(10),
	"EMP_MOBILE2" VARCHAR(10),
	"EMP_ADDRESS" VARCHAR(100),
	"EMP_ROLE" VARCHAR(10) NOT NULL,
              "EMP_SPECIALITY" VARCHAR(20),
	"PAY_TYPE" VARCHAR(25) NOT NULL,
              "EMP_HOLIDAY" VARCHAR(20),
	"IN_SERVICE" INT NOT NULL DEFAULT 1,
              "NOTE"  VARCHAR(MAX),
              CONSTRAINT PK_EMPLOYEES PRIMARY KEY NONCLUSTERED ([EMP_NAME]) 
)')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'EMPLOYEES' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(12,''TABLE'',''EMPLOYEES'')')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_ASSIGNMENTS'  AND OBJECT_TYPE='TABLE')  
BEGIN
EXECUTE ('CREATE TABLE ORDER_ASSIGNMENTS (
	ASSIGNMENT_UID INT NOT NULL,
	BILL_NO VARCHAR(10) NOT NULL,
	ASSIGNMENT_DATE DATETIME NOT NULL,
	ASSIGNMENT_TYPE VARCHAR(20) NOT NULL,
	EMPLOYEE_NAME VARCHAR(50) NOT NULL,
	WAGE_AMOUNT INT NOT NULL,
	WAGE_STATUS VARCHAR(20) NOT NULL,
	PAYMENT_DATE DATETIME,
	NOTE VARCHAR(4000) NOT NULL,
              CONSTRAINT PK_ASSIGNMENT PRIMARY KEY NONCLUSTERED ([BILL_NO],[ASSIGNMENT_TYPE]) ,
              CONSTRAINT FK_ORDER_ASSIGNMENT_BILLNO FOREIGN KEY (BILL_NO) REFERENCES ORDERS(BILL_NO),
              CONSTRAINT FK_EMPLOYEE_NAME FOREIGN KEY (EMPLOYEE_NAME) REFERENCES EMPLOYEES(EMP_NAME)
)')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_ASSIGNMENTS' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(13,''TABLE'',''ORDER_ASSIGNMENTS'')')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ACCOUNTS_BOOK'  AND OBJECT_TYPE='TABLE')  
BEGIN
EXECUTE ('CREATE TABLE ACCOUNTS_BOOK (
	TRANSATION_UID INT NOT NULL,
	TR_DATE DATETIME NOT NULL,
	TR_MODULE VARCHAR(20) NOT NULL,
	TR_TYPE VARCHAR(20) NOT NULL,
	ACCOUNT_NAME VARCHAR(50) NOT NULL,
	TR_DETAILS VARCHAR(50) ,--Optional details
	MODULE_TRANSACTION_TYPE VARCHAR(30) NOT NULL,
	AMOUNT INT NOT NULL,
	NOTE  VARCHAR(50) NOT NULL,
              CONSTRAINT PK_ACCOUNTS PRIMARY KEY NONCLUSTERED (TRANSATION_UID)
)')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ACCOUNTS_BOOK' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(15,''TABLE'',''ACCOUNTS_BOOK'')')
END



IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ACCOUNT_REGISTER' AND OBJECT_TYPE='TABLE')  
BEGIN
EXECUTE ('CREATE TABLE ACCOUNT_REGISTER (
	TRANSACTION_UID INT NOT NULL,
                  ACCOUNT_MODULE VARCHAR(20) NOT NULL,
                  ACCOUNT_NAME VARCHAR(50) NOT NULL,
                  MOBILE VARCHAR(10),
                  ADDRESS VARCHAR(100),
                  BANK_DETAILS VARCHAR(200),  
	ACCOUNT_CREATION_DATE DATETIME NOT NULL DEFAULT GETDATE(),
                  VIEW_ORDER INT,
                  ACTIVE BIT DEFAULT 1,
	NOTE  VARCHAR(50) NOT NULL,
              CONSTRAINT PK_ACCOUNTS_REGISTER PRIMARY KEY NONCLUSTERED (TRANSACTION_UID)
)')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ACCOUNT_REGISTER' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_UID,OBJECT_TYPE,OBJECT_NAME) VALUES(16,''TABLE'',''ACCOUNT_REGISTER'')')
END

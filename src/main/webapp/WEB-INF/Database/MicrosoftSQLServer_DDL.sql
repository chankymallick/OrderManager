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
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''USERS'')')
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
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''PERMISSIONS'')')
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
        ORDER_STATUS VARCHAR(30),
        CUSTOMER_NAME VARCHAR(30),
        CONTACT_NO VARCHAR(10),
        ORDER_TYPE VARCHAR(20),
        PRODUCT_TYPE VARCHAR(20),
        QUANTITY INT,
        PRICE INT,
        DISCOUNT INT,
        MEASURED_BY VARCHAR(20),
        NOTE VARCHAR(MAX)
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDERS' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''ORDERS'')')
END

IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'PAYMENT_TRANSACTIONS'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE PAYMENT_TRANSACTIONS 
                        (
                        TRANSACTION_UID INT  PRIMARY KEY,
                        ORDER_BILL_NO VARCHAR(10),                     
                        PAYMENT_TYPE VARCHAR(20),
                        AMOUNT INT,    
                        CONSTRAINT FK_BILLNO FOREIGN KEY (ORDER_BILL_NO) REFERENCES ORDERS(BILL_NO)
                      )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'PAYMENT_TRANSACTIONS' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''PAYMENT_TRANSACTIONS'')')
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
                        AUDIT_HISTORY VARCHAR(MAX),
                        NOTE VARCHAR(MAX),
                        CONSTRAINT FK_AUDITUSER FOREIGN KEY (AUDITED_BY) REFERENCES USERS(USER_ID)
                      )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'AUDIT' AND OBJECT_TYPE='TABLE')
  BEGIN
    EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''AUDIT'')')
END
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ITEMS'  AND OBJECT_TYPE='TABLE')
  BEGIN
      EXECUTE ('CREATE TABLE ITEMS
                ( ITEM_UID INT,
       	ITEM_NAME VARCHAR(20) PRIMARY KEY,
                  ITEM_TYPE VARCHAR(20),--MAIN,EXTRAS
       	MASTER_PRICE INT,
       	TAILOR_PRICE INT,
       	FINISHER_PRICE INT,
       	ACTIVE BIT,
                  NOTE VARCHAR(MAX)
        )')        
  END  
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ITEMS' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''ITEMS'')')
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
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''ORDER_ITEMS'')')
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
IF NOT EXISTS (SELECT *FROM OBJECT_METADATA  WHERE OBJECT_NAME = 'ORDER_ITEMS' AND OBJECT_TYPE='TABLE')
  BEGIN
  EXECUTE('INSERT INTO OBJECT_METADATA (OBJECT_TYPE,OBJECT_NAME) VALUES(''TABLE'',''APP_DATA'')')
END

ALTER SESSION SET CURRENT_SCHEMA=DATUP/

DROP TABLE FDB_SITE_UPDATE/
DROP TABLE SITE/
DROP TABLE FDB_VERSION/
DROP SEQUENCE FDB_ID_SEQ/
DROP SEQUENCE FDB_SITE_UPDATE_SEQ/

CREATE TABLE SITE (
       SITE_ID       NUMERIC(30) NOT NULL,
       SITE_NAME     VARCHAR(256)NOT NULL,
       VISN 		 VARCHAR(256) NOT NULL)/

ALTER TABLE SITE
 ADD PRIMARY KEY (SITE_ID)/

CREATE TABLE FDB_VERSION (
       FDB_ID       	NUMERIC(30) NOT NULL,
       IS_CUSTOM 		VARCHAR(1) NOT NULL,
       VERSION_NUMBER   VARCHAR(8) NOT NULL,
       WHEN_CREATED		TIMESTAMP NOT NULL)/

ALTER TABLE FDB_VERSION 
       ADD PRIMARY KEY (FDB_ID)/
       
ALTER TABLE fdb_version 
	add   constraint fdb_version_uni unique (is_custom, version_number)/

CREATE TABLE FDB_SITE_UPDATE (
    FDB_SITE_UPDATE_ID		NUMERIC(30) NOT NULL,
	SITE_ID					NUMERIC(30) NOT NULL,
	FDB_ID					NUMERIC(30) NOT NULL,
	REGION_NAME				VARCHAR(256) NULL,
	IS_SUCCESSFUL 			VARCHAR(1) not null,
	MESSAGE					VARCHAR(256) not null,
	LAST_UPDATE_TIME		timestamp not null,
	LAST_UPDATE_TIMEZONE	VARCHAR(60) not null)/



ALTER TABLE FDB_SITE_UPDATE 
       ADD PRIMARY KEY (FDB_SITE_UPDATE_ID)/


 ALTER TABLE FDB_SITE_UPDATE
    ADD FOREIGN KEY (SITE_ID)
             REFERENCES SITE  (SITE_ID)/

ALTER TABLE FDB_SITE_UPDATE
     ADD FOREIGN KEY (FDB_ID)
                      REFERENCES FDB_VERSION  (FDB_ID)/

 
                        
CREATE SEQUENCE FDB_ID_SEQ START WITH 1 increment by 1/ 
CREATE SEQUENCE FDB_SITE_UPDATE_SEQ START WITH 1 increment by 1/ 

CREATE OR REPLACE TRIGGER fdb_version_tri 
BEFORE INSERT ON fdb_version 
FOR EACH ROW
BEGIN
  SELECT FDB_ID_SEQ.NEXTVAL
  INTO   :new.fdb_id
  FROM   dual;
END;
/


CREATE OR REPLACE TRIGGER fdb_site_update_tri 
BEFORE INSERT ON FDB_SITE_UPDATE 
FOR EACH ROW
BEGIN
  SELECT FDB_SITE_UPDATE_SEQ.NEXTVAL
  INTO   :new.fdb_site_update_id
  FROM   dual;
END;
/







                             
                 

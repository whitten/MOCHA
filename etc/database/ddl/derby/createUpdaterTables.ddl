DROP TABLE DATUP.FDB_SITE_UPDATE;
DROP TABLE DATUP.SITE;
DROP TABLE DATUP.FDB_VERSION;
DROP SCHEMA DATUP RESTRICT;

CREATE SCHEMA DATUP;
SET SCHEMA DATUP;

CREATE TABLE SITE (
       SITE_ID       NUMERIC(30) NOT NULL,
       SITE_NAME     VARCHAR(256)NOT NULL,
       VISN 		 VARCHAR(256) NOT NULL);

ALTER TABLE SITE
 ADD PRIMARY KEY (SITE_ID);

CREATE TABLE FDB_VERSION (
       FDB_ID       	INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
       IS_CUSTOM 		VARCHAR(1) NOT NULL,
       VERSION_NUMBER   VARCHAR(8) NOT NULL,
       WHEN_CREATED		TIMESTAMP NOT NULL);


CREATE TABLE FDB_SITE_UPDATE (
	FDB_SITE_UPDATE_ID      INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	SITE_ID					NUMERIC(30) NOT NULL,
	FDB_ID					INTEGER NOT NULL,
	REGION_NAME				VARCHAR(256),
	IS_SUCCESSFUL 			VARCHAR(1) not null,
	MESSAGE					VARCHAR(256) not null,
	LAST_UPDATE_TIME		TIMESTAMP not null,
	LAST_UPDATE_TIMEZONE	VARCHAR(60) not null);

ALTER TABLE FDB_SITE_UPDATE
    ADD FOREIGN KEY (SITE_ID)
             REFERENCES SITE  (SITE_ID);

ALTER TABLE FDB_SITE_UPDATE
     ADD FOREIGN KEY (FDB_ID)
                    REFERENCES FDB_VERSION  (FDB_ID);
                    
                    
                    alter table fdb_version add   constraint fdb_version_uni unique (is_custom, version_number);

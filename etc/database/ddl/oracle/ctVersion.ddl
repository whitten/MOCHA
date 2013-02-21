DROP TABLE CT_VERSION/

-- VA table required in FDB DIF database for custom data 
CREATE TABLE CT_VERSION
(
	VERSIONKEY SMALLINT NOT NULL,
	DBVERSION VARCHAR(5),
	BUILDVERSION VARCHAR(5),
	FREQUENCY VARCHAR(1),
	ISSUEDATE VARCHAR(8),
	VERSIONCOMMENT VARCHAR(80),
	DBTYPE VARCHAR(10)
)/

INSERT INTO CT_VERSION
(
  VERSIONKEY,
  DBVERSION,
  BUILDVERSION,
  FREQUENCY,
  ISSUEDATE,
  VERSIONCOMMENT,
  DBTYPE
)  
  
SELECT VERSIONKEY,
       DBVERSION,
       BUILDVERSION,
       FREQUENCY,
       ISSUEDATE,
       VERSIONCOMMENT,
       DBTYPE
FROM FDB_VERSION/
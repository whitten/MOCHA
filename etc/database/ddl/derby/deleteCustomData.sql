-- Delete all data from custom tables (including VA ct_vesion)

DELETE FROM fdb_custom_allergenpicklist;
DELETE FROM fdb_custom_allergenpicklstsrch;
DELETE FROM fdb_custom_attributenames;
DELETE FROM fdb_custom_attributevalues;
DELETE FROM fdb_custom_class;
DELETE FROM fdb_custom_class_drugconcept;
DELETE FROM fdb_custom_class_search;
DELETE FROM fdb_custom_ddim;
DELETE FROM fdb_custom_ddimdruglinkcat;
DELETE FROM fdb_custom_ddiminteraction;
DELETE FROM fdb_custom_ddimstrings;
DELETE FROM fdb_custom_doserangecheck;
DELETE FROM fdb_custom_dosing;
DELETE FROM fdb_custom_dosingneo;
DELETE FROM fdb_custom_drugmapping;
DELETE FROM fdb_custom_dtcat;
DELETE FROM fdb_custom_indication;
DELETE FROM fdb_custom_interactcategory;
DELETE FROM fdb_custom_monograph;
DELETE FROM fdb_custom_msg_catdef;
DELETE FROM fdb_custom_msg_def;
DELETE FROM fdb_custom_msg_link;
DELETE FROM fdb_custom_msg_text;
DELETE FROM fdb_custom_packageddrugpricing;
DELETE FROM ct_version;

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
FROM FDB_VERSION;
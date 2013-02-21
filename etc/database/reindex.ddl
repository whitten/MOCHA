-- Derby v10.2.1.6 tuning documentation states you must drop all indices and recreate them to reindex
-- First dropping all indices defined in FDB DIF's 'Standard SQL DB\createTablesDIF3.2US.sql' DDL
drop index ixfdbahfsclassdrugconcept1;
drop index ixfdballergenhicseqno1;
drop index ixfdballergengroupsearch1;
drop index ixfdballergengroupsearch2;
drop index ixfdballergengroupsearchii1;
drop index ixfdballergengroupsearchii2;
drop index ixfdballergenpicklistsearch1;
drop index ixfdballergenpicklistsearch2;
drop index ixfdbclassahfssearch1;
drop index ixfdbclassahfssearch2;
drop index ixfdbclassfdbsearch1;
drop index ixfdbclassfdbsearch2;
drop index ixfdbclassificationahfs1;
drop index ixfdbclassificationahfs2;
drop index ixfdbclassificationahfssear1;
drop index ixfdbclassificationahfssear2;
drop index ixfdbclassificationetc1;
drop index ixfdbclassificationetc2;
drop index ixfdbclassificationetcsearc1;
drop index ixfdbclassificationetcsearc2;
drop index ixfdbclassificationext1;
drop index ixfdbclassificationext2;
drop index ixfdbclassificationextsearc1;
drop index ixfdbclassificationextsearc2;
drop index ixfdbclassificationfdb1;
drop index ixfdbclassificationfdb2;
drop index ixfdbclassificationfdbsearc1;
drop index ixfdbclassificationfdbsearc2;
drop index ixfdbcstallergenpicklstsrch1;
drop index ixfdbcstallergenpicklstsrch2;
drop index ixfdbcustomattributevalues1;
drop index ixfdbcustomattributevalues2;
drop index ixfdbcustomclassdrugconcept1;
drop index ixfdbcustomclasssearch1;
drop index ixfdbcustomclasssearch2;
drop index ixfdbcustomddimstrings1;
drop index ixfdbcustomdrugmapping1;
drop index ixfdbcustomdrugmapping2;
drop index ixfdbcustomdtcat1;
drop index ixfdbcustommsglink1;
drop index ixfdbcustommsglink2;
drop index ixfdbdamscreeninfo1;
drop index ixfdbdamscreeninfo2;
drop index ixfdbdamscreeninfoii1;
drop index ixfdbdamscreeninfoii2;
drop index ixfdbddimcatseverities1;
drop index ixfdbddimdruglink1;
drop index ixfdbdispensable1;
drop index ixfdbdispensable2;
drop index ixfdbdispensable3;
drop index ixfdbdispensable4;
drop index ixfdbdosing1;
drop index ixfdbdrugrtgenid1;
drop index ixfdbdrugname1;
drop index ixfdbdrugname2;
drop index ixfdbdrugvalidation1;
drop index ixfdbdxidrelateddxid1;
drop index ixfdbdxidrelateddxid2;
drop index ixfdbextvocabrelateddxid1;
drop index ixfdbextvocabrelateddxid2;
drop index ixfdbextvocabrelateddxid3;
drop index ixfdbfdbclassdrugconcept1;
drop index ixfdbgenericdispsearch1;
drop index ixfdbgenericdispsearch2;
drop index ixfdbgenericdrugnamesearch1;
drop index ixfdbgenericdrugnamesearch2;
drop index ixfdbgenericrouteddfdrugsea1;
drop index ixfdbgenericrouteddfdrugsea2;
drop index ixfdbgenericrouteddrugsearc1;
drop index ixfdbgenericrouteddrugsearc2;
drop index ixfdbhicseqnobaseingredient1;
drop index ixfdbimprint1;
drop index ixfdbinddruglink;
drop index ixfdbingredientsearch1;
drop index ixfdbingredientsearch2;
drop index ixfdbivmremarklink1;
drop index ixfdbivmremarklink2;
drop index ixfdbmanufdrugsearch1;
drop index ixfdbmanufdrugsearch2;
drop index ixfdbmedcond1;
drop index ixfdbmedcondextvocab1;
drop index ixfdbmedcondextvocab2;
drop index ixfdbmedcondextvocabsearch1;
drop index ixfdbmedcondextvocabsearch2;
drop index ixfdbmedcondsearch1;
drop index ixfdbmedcondsearch2;
drop index ixfdbmedcondsearchfml1;
drop index ixfdbmedcondsearchfml2;
drop index ixfdbmedcondxref1;
drop index ixfdbmedcondxref2;
drop index ixfdbmnidhicl1;
drop index ixfdbpackageddrug1;
drop index ixfdbpackageddrug2;
drop index ixfdbpackageddrug3;
drop index ixfdbpackageddrug4;
drop index ixfdbpackageddrug5;
drop index ixfdbprecgeridruglink1;
drop index ixfdbpreclactdruglink1;
drop index ixfdbprecpedidruglink1;
drop index ixfdbprecpregdruglink1;
drop index ixfdbrdfmidrtdfgenid1;
drop index ixfdbrefitemsearch1;
drop index ixfdbrefitemsearch2;
drop index ixfdbregpackaged1;
drop index ixfdbregpackagedsearch1;
drop index ixfdbregpackagedsearch2;
drop index ixfdbreplacementdrugs1;
drop index ixfdbreplacementdrugs2;
drop index ixfdbrmidrtgenid1;
drop index ixfdbrouteddfdrug1;
drop index ixfdbrouteddfdrug2;
drop index ixfdbrouteddfdrug3;
drop index ixfdbrouteddrug1;
drop index ixfdbrouteddrug2;
drop index ixfdbrouteddrug3;
drop index ixfdbtgenidgenericrmid;
drop index ixfdbsubsetsite1;
drop index ixfdbsubsetsite2;

-- Creating all indices defined in FDB DIF's 'Standard SQL DB\createTablesDIF3.2US.sql' DDL to effectively reindex
create index ixfdbahfsclassdrugconcept1 on fdb_ahfsclass_drugconcept (conceptid);
create index ixfdballergenhicseqno1 on fdb_allergen_hicseqno (hicseqno);
create index ixfdballergengroupsearch1 on fdb_allergengroupsearch (descsearch);
create index ixfdballergengroupsearch2 on fdb_allergengroupsearch (descaltsearch);
create index ixfdballergengroupsearchii1 on fdb_allergengroupsearch_ii (descsearch);
create index ixfdballergengroupsearchii2 on fdb_allergengroupsearch_ii (descaltsearch);
create index ixfdballergenpicklistsearch1 on fdb_allergenpicklistsearch (descsearch);
create index ixfdballergenpicklistsearch2 on fdb_allergenpicklistsearch (descaltsearch);
create index ixfdbclassahfssearch1 on fdb_class_ahfssearch (descsearch);
create index ixfdbclassahfssearch2 on fdb_class_ahfssearch (descaltsearch);
create index ixfdbclassfdbsearch1 on fdb_class_fdbsearch (descsearch);
create index ixfdbclassfdbsearch2 on fdb_class_fdbsearch (descaltsearch);
create index ixfdbclassificationahfs1 on fdb_classification_ahfs (parentid);
create index ixfdbclassificationahfs2 on fdb_classification_ahfs (ultiparentid);
create index ixfdbclassificationahfssear1 on fdb_classification_ahfssearch (descsearch);
create index ixfdbclassificationahfssear2 on fdb_classification_ahfssearch (descaltsearch);
create index ixfdbclassificationetc1 on fdb_classification_etc (parentid);
create index ixfdbclassificationetc2 on fdb_classification_etc (ultiparentid);
create index ixfdbclassificationetcsearc1 on fdb_classification_etcsearch (descsearch);
create index ixfdbclassificationetcsearc2 on fdb_classification_etcsearch (descaltsearch);
create index ixfdbclassificationext1 on fdb_classification_ext (parentid);
create index ixfdbclassificationext2 on fdb_classification_ext (ultiparentid);
create index ixfdbclassificationextsearc1 on fdb_classification_extsearch (descsearch);
create index ixfdbclassificationextsearc2 on fdb_classification_extsearch (descaltsearch);
create index ixfdbclassificationfdb1 on fdb_classification_fdb (parentid);
create index ixfdbclassificationfdb2 on fdb_classification_fdb (ultiparentid);
create index ixfdbclassificationfdbsearc1 on fdb_classification_fdbsearch (descsearch);
create index ixfdbclassificationfdbsearc2 on fdb_classification_fdbsearch (descaltsearch);
create index ixfdbcstallergenpicklstsrch1 on fdb_custom_allergenpicklstsrch (descsearch);
create index ixfdbcstallergenpicklstsrch2 on fdb_custom_allergenpicklstsrch (descaltsearch);
create index ixfdbcustomattributevalues1 on fdb_custom_attributevalues (conceptidstring);
create index ixfdbcustomattributevalues2 on fdb_custom_attributevalues (conceptidnumeric);
create index ixfdbcustomclassdrugconcept1 on fdb_custom_class_drugconcept (conceptid);
create index ixfdbcustomclasssearch1 on fdb_custom_class_search (descsearch);
create index ixfdbcustomclasssearch2 on fdb_custom_class_search (descaltsearch);
create index ixfdbcustomddimstrings1 on fdb_custom_ddimstrings (interactionid);
create index ixfdbcustomdrugmapping1 on fdb_custom_drugmapping (conceptidstring);
create index ixfdbcustomdrugmapping2 on fdb_custom_drugmapping (conceptidnumeric);
create index ixfdbcustomdtcat1 on fdb_custom_dtcat (dtcid);
create index ixfdbcustommsglink1 on fdb_custom_msg_link (conceptidstring);
create index ixfdbcustommsglink2 on fdb_custom_msg_link (conceptidnumeric);
create index ixfdbdamscreeninfo1 on fdb_damscreeninfo (hittype);
create index ixfdbdamscreeninfo2 on fdb_damscreeninfo (hitid);
create index ixfdbdamscreeninfoii1 on fdb_damscreeninfo_ii (hittype);
create index ixfdbdamscreeninfoii2 on fdb_damscreeninfo_ii (hitid);
create index ixfdbddimcatseverities1 on fdb_ddim_catseverities (interactionid);
create index ixfdbddimdruglink1 on fdb_ddimdruglink (rtgenid2);
create index ixfdbdispensable1 on fdb_dispensable (gcnseqno);
create index ixfdbdispensable2 on fdb_dispensable (descaltsearch);
create index ixfdbdispensable3 on fdb_dispensable (rmid);
create index ixfdbdispensable4 on fdb_dispensable (descsearch);
create index ixfdbdosing1 on fdb_dosing (hittype);
create index ixfdbdrugrtgenid1 on fdb_drug_rtgenid (rtgenid);
create index ixfdbdrugname1 on fdb_drugname (descaltsearch);
create index ixfdbdrugname2 on fdb_drugname (descsearch);
create index ixfdbdrugvalidation1 on fdb_drugvalidation (rtdfgenid);
create index ixfdbdxidrelateddxid1 on fdb_dxid_relateddxid (dxid);
create index ixfdbdxidrelateddxid2 on fdb_dxid_relateddxid (relateddxid);
create index ixfdbextvocabrelateddxid1 on fdb_extvocab_relateddxid (relateddxid);
create index ixfdbextvocabrelateddxid2 on fdb_extvocab_relateddxid (vocabtypecode);
create index ixfdbextvocabrelateddxid3 on fdb_extvocab_relateddxid (prefhexid, relateddxid, clinicalcode, navcode);
create index ixfdbfdbclassdrugconcept1 on fdb_fdbclass_drugconcept (conceptid);
create index ixfdbgenericdispsearch1 on fdb_generic_dispsearch (descsearch);
create index ixfdbgenericdispsearch2 on fdb_generic_dispsearch (descaltsearch);
create index ixfdbgenericdrugnamesearch1 on fdb_generic_drugnamesearch (descsearch);
create index ixfdbgenericdrugnamesearch2 on fdb_generic_drugnamesearch (descaltsearch);
create index ixfdbgenericrouteddfdrugsea1 on fdb_generic_routeddfdrugsearch (descsearch);
create index ixfdbgenericrouteddfdrugsea2 on fdb_generic_routeddfdrugsearch (descaltsearch);
create index ixfdbgenericrouteddrugsearc1 on fdb_generic_routeddrugsearch (descsearch);
create index ixfdbgenericrouteddrugsearc2 on fdb_generic_routeddrugsearch (descaltsearch);
create index ixfdbhicseqnobaseingredient1 on fdb_hicseqno_baseingredient (baseingredientid);
create index ixfdbimprint1 on fdb_imprint (searchdescription);
create index ixfdbinddruglink on fdb_inddruglink (gcnseqno);
create index ixfdbingredientsearch1 on fdb_ingredientsearch (descsearch);
create index ixfdbingredientsearch2 on fdb_ingredientsearch (descaltsearch);
create index ixfdbivmremarklink1 on fdb_ivm_remarklink (grouptestid);
create index ixfdbivmremarklink2 on fdb_ivm_remarklink (remarkid);
create index ixfdbmanufdrugsearch1 on fdb_manufdrugsearch (descsearch);
create index ixfdbmanufdrugsearch2 on fdb_manufdrugsearch (descaltsearch);
create index ixfdbmedcond1 on fdb_medcond (dxid);
create index ixfdbmedcondextvocab1 on fdb_medcondextvocab (extvocabid);
create index ixfdbmedcondextvocab2 on fdb_medcondextvocab (hexid);
create index ixfdbmedcondextvocabsearch1 on fdb_medcondextvocabsearch (descsearch);
create index ixfdbmedcondextvocabsearch2 on fdb_medcondextvocabsearch (descaltsearch);
create index ixfdbmedcondsearch1 on fdb_medcondsearch (descsearch);
create index ixfdbmedcondsearch2 on fdb_medcondsearch (descaltsearch);
create index ixfdbmedcondsearchfml1 on fdb_medcondsearchfml (descsearch);
create index ixfdbmedcondsearchfml2 on fdb_medcondsearchfml (descaltsearch);
create index ixfdbmedcondxref1 on fdb_medcondxref (hexid);
create index ixfdbmedcondxref2 on fdb_medcondxref (hexid2);
create index ixfdbmnidhicl1 on fdb_mnid_hicl (hicl);
create index ixfdbpackageddrug1 on fdb_packageddrug (medid);
create index ixfdbpackageddrug2 on fdb_packageddrug (descsearch);
create index ixfdbpackageddrug3 on fdb_packageddrug (descaltsearch);
create index ixfdbpackageddrug4 on fdb_packageddrug (safedescsearch);
create index ixfdbpackageddrug5 on fdb_packageddrug (safedescaltsearch);
create index ixfdbprecgeridruglink1 on fdb_precgeri_druglink (conceptid);
create index ixfdbpreclactdruglink1 on fdb_preclact_druglink (conceptid);
create index ixfdbprecpedidruglink1 on fdb_precpedi_druglink (conceptid);
create index ixfdbprecpregdruglink1 on fdb_precpreg_druglink (conceptid);
create index ixfdbrdfmidrtdfgenid1 on fdb_rdfmid_rtdfgenid (rtdfgenid);
create index ixfdbrefitemsearch1 on fdb_refitem_search (descsearch);
create index ixfdbrefitemsearch2 on fdb_refitem_search (descaltsearch);
create index ixfdbregpackaged1 on fdb_regpackaged (medid);
create index ixfdbregpackagedsearch1 on fdb_regpackagedsearch (descsearch);
create index ixfdbregpackagedsearch2 on fdb_regpackagedsearch (descaltsearch);
create index ixfdbreplacementdrugs1 on fdb_replacementdrugs (replacedid);
create index ixfdbreplacementdrugs2 on fdb_replacementdrugs (replacementid);
create index ixfdbrmidrtgenid1 on fdb_rmid_rtgenid (rtgenid);
create index ixfdbrouteddfdrug1 on fdb_routeddfdrug (mnid);
create index ixfdbrouteddfdrug2 on fdb_routeddfdrug (descaltsearch);
create index ixfdbrouteddfdrug3 on fdb_routeddfdrug (descsearch);
create index ixfdbrouteddrug1 on fdb_routeddrug (mnid);
create index ixfdbrouteddrug2 on fdb_routeddrug (descaltsearch);
create index ixfdbrouteddrug3 on fdb_routeddrug (descsearch);
create index ixfdbtgenidgenericrmid on fdb_rtgenid_genericrmid (rmid);
create index ixfdbsubsetsite1 on fdb_subset_site (descsearch);
create index ixfdbsubsetsite2 on fdb_subset_site (descaltsearch);
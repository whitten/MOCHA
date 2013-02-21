-- RouteUtility.properties, replace spaces (' ') in property names with escaped space ('\ ')
SELECT DESCRIPTION1 || '=' || DOSEROUTEID
FROM FDB_DOSEROUTE
ORDER BY DESCRIPTION1;

-- Schema routeType
SELECT '<xsd:enumeration value="' || DESCRIPTION1 || '" />'
FROM FDB_DOSEROUTE
ORDER BY DESCRIPTION1;

-- DoseTypeUtility.properties, replace spaces (' ') in property names with escaped space ('\ ')
SELECT DESCRIPTION1 || '=' || DOSETYPEID
FROM FDB_DOSETYPE
ORDER BY DESCRIPTION1;

-- Schema doseTypeType
SELECT '<xsd:enumeration value="' || DESCRIPTION1 || '" />'
FROM FDB_DOSETYPE
ORDER BY DESCRIPTION1;

-- Schema doseUnitType
SELECT '<xsd:enumeration value="' || UNITS || '" />'
FROM FDB_DOSEUNITSTYPE
ORDER BY UNITS;
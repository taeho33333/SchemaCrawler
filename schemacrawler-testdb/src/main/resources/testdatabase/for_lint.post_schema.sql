-- Remarks
COMMENT ON TABLE PUBLIC.FOR_LINT.EXTRA_PK IS 'Extra table with just a primary key'
;
COMMENT ON COLUMN PUBLIC.FOR_LINT.PUBLICATIONS.TITLE IS 'Publication title'
;

-- Domains
CREATE DOMAIN VALID_STRING AS VARCHAR(20) DEFAULT 'NO VALUE' CHECK (VALUE IS NOT NULL AND CHARACTER_LENGTH(VALUE) > 2);

-- Temporary table
CREATE GLOBAL TEMPORARY TABLE PUBLIC.FOR_LINT.TEMP1
(
  ID INT PRIMARY KEY, 
  SCORES INT ARRAY DEFAULT ARRAY[], 
  NAMES VARCHAR(20) ARRAY[10], 
  DATA VALID_STRING
)
ON COMMIT DELETE ROWS;

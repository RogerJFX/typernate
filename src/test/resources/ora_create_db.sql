--------------------------------------------------------
--  Datei erstellt -Donnerstag-August-21-2014   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Type TYPE_TEST
--------------------------------------------------------

  CREATE OR REPLACE TYPE "TYPE_TEST" AS object
   (id integer,
    iname character varying(256),
    test_data type_test_int_string_varray);

/
--------------------------------------------------------
--  DDL for Type TYPE_TEST_INT_STRING
--------------------------------------------------------

  CREATE OR REPLACE TYPE "TYPE_TEST_INT_STRING" as object (
  id integer,
  str varchar(256)
);

/
--------------------------------------------------------
--  DDL for Type TYPE_TEST_INT_STRING_VARRAY
--------------------------------------------------------

  CREATE OR REPLACE TYPE "TYPE_TEST_INT_STRING_VARRAY" as varray(10) of type_test_int_string;

--CREATE TYPE type_test AS object
--   (id integer,
--    iname character varying(256),
--    test_data type_test_int_string_varray);

/
--------------------------------------------------------
--  DDL for Type TYPE_TEST_SINGLE
--------------------------------------------------------

  CREATE OR REPLACE TYPE "TYPE_TEST_SINGLE" AS object
   (id integer,
    iname character varying(256),
    test_data type_test_int_string);

/
--------------------------------------------------------
--  DDL for Type TYPE_TEST_VARRAY
--------------------------------------------------------

  CREATE OR REPLACE TYPE "TYPE_TEST_VARRAY" as varray(10) of type_test;

/
--------------------------------------------------------
--  DDL for Sequence TBL_TYPETEST_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "TBL_TYPETEST_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 121 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence TBL_TYPETEST_SINGLE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "TBL_TYPETEST_SINGLE_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence TBL_TYPETEST_WITH_LIST_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "TBL_TYPETEST_WITH_LIST_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table TBL_TYPETEST
--------------------------------------------------------

  CREATE TABLE "TBL_TYPETEST" 
   (	"ID" NUMBER(*,0), 
	"ITYPE" "TYPE_TEST" 
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table TBL_TYPETEST_SINGLE
--------------------------------------------------------

  CREATE TABLE "TBL_TYPETEST_SINGLE" 
   (	"ID" NUMBER(*,0), 
	"ITYPE" "TYPE_TEST_SINGLE" 
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table TBL_TYPETEST_WITH_LIST
--------------------------------------------------------

  CREATE TABLE "TBL_TYPETEST_WITH_LIST" 
   (	"ID" NUMBER(*,0), 
	"ITYPE" "TYPE_TEST_VARRAY" 
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for View VIEW_TYPETEST_SINGLE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_TYPETEST_SINGLE" ("INAME", "STR") AS 
  SELECT (itype).iname as iname, (itype).test_data.str as str
    
FROM tbl_typetest_single;
--------------------------------------------------------
--  DDL for Index TABLE1_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TABLE1_PK" ON "TBL_TYPETEST" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index TABLE2_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TABLE2_PK" ON "TBL_TYPETEST_SINGLE" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index TBL_TYPETEST_WITH_LIST_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TBL_TYPETEST_WITH_LIST_PK" ON "TBL_TYPETEST_WITH_LIST" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table TBL_TYPETEST
--------------------------------------------------------

  ALTER TABLE "TBL_TYPETEST" ADD CONSTRAINT "TABLE1_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "TBL_TYPETEST" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_TYPETEST_SINGLE
--------------------------------------------------------

  ALTER TABLE "TBL_TYPETEST_SINGLE" ADD CONSTRAINT "TABLE2_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "TBL_TYPETEST_SINGLE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBL_TYPETEST_WITH_LIST
--------------------------------------------------------

  ALTER TABLE "TBL_TYPETEST_WITH_LIST" ADD CONSTRAINT "TBL_TYPETEST_WITH_LIST_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "TBL_TYPETEST_WITH_LIST" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  DDL for Trigger TBL_TYPETEST_SINGLE_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TBL_TYPETEST_SINGLE_TRG" 
BEFORE INSERT ON TBL_TYPETEST_SINGLE 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT TBL_TYPETEST_SINGLE_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "TBL_TYPETEST_SINGLE_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TBL_TYPETEST_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TBL_TYPETEST_TRG" 
BEFORE INSERT ON TBL_TYPETEST 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT TBL_TYPETEST_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "TBL_TYPETEST_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TBL_TYPETEST_WITH_LIST_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TBL_TYPETEST_WITH_LIST_TRG" 
BEFORE INSERT ON TBL_TYPETEST_WITH_LIST 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT TBL_TYPETEST_WITH_LIST_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "TBL_TYPETEST_WITH_LIST_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Function FUNC_TYPEINSERT
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "FUNC_TYPEINSERT" 
(
  melement IN TYPE_TEST 
) RETURN NUMBER IS 
--DECLARE
  PRAGMA AUTONOMOUS_TRANSACTION; 
  seqval integer;
BEGIN
  select tbl_typetest_seq.nextval into seqval from dual ;
  insert into tbl_typetest (id, itype) values(seqval, melement);
  commit;
  RETURN seqval;
END FUNC_TYPEINSERT;

/
--------------------------------------------------------
--  DDL for Function FUNC_TYPEINSERTSINGLE
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "FUNC_TYPEINSERTSINGLE" 
(
  melement IN TYPE_TEST_SINGLE
) RETURN NUMBER IS 
-- declare 
  PRAGMA AUTONOMOUS_TRANSACTION; 
  seqval integer;
BEGIN
  select tbl_typetest_single_seq.nextval into seqval from dual ;
  insert into tbl_typetest_single (id, itype) values(seqval, melement);
  commit;
  RETURN seqval;
END FUNC_TYPEINSERTSINGLE;

/

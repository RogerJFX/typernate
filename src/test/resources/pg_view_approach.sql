-- ##########################################################################################
-- A view may ease things without any problems having only simple columns.
-- However with an array type it's of course impossible to roll out an array of unknown size 
-- to a grid view.
-- So e.g. for an update there only remains something like this in this case:
--
-- update tbl_typetest set itype.test_data[5].str = 'candle light' where id = 1;
--
-- Note: nearly no editor supports editing views in grid mode. At least I don't know any.
-- ##########################################################################################

CREATE OR REPLACE VIEW view_typetest_single AS 
 SELECT tbl_typetest_single.id AS o_id, 
 		(tbl_typetest_single.itype).id AS id, 
 		(tbl_typetest_single.itype).iname AS iname, 
 		(tbl_typetest_single.itype).test_data.id AS i_id, 
 		(tbl_typetest_single.itype).test_data.str AS i_str
   FROM tbl_typetest_single;


-- Rule: "_INSERT" ON view_typetest_single

-- DROP RULE "_INSERT" ON view_typetest_single;

CREATE OR REPLACE RULE "_INSERT" AS
    ON INSERT TO view_typetest_single DO INSTEAD  
    	INSERT INTO tbl_typetest_single (itype) 
  			VALUES (ROW(new.id, new.iname, ROW(new.i_id, new.i_str)::type_test_int_string)::type_test_single);

-- Rule: "_UPDATE" ON view_typetest_single

-- DROP RULE "_UPDATE" ON view_typetest_single;

CREATE OR REPLACE RULE "_UPDATE" AS
    ON UPDATE TO view_typetest_single DO INSTEAD  
    	UPDATE tbl_typetest_single 
    		SET itype.id = new.id, itype.iname = new.iname, itype.test_data.id = new.i_id, itype.test_data.str = new.i_str
  WHERE tbl_typetest_single.id = new.o_id;
  
-- Running some tests:
update view_typetest_single set i_str = 'foo bar' where o_id = 1;
update view_typetest_single set i_id = 777 where i_str = 'foo bar';
insert into view_typetest_single (id, iname, i_id, i_str) values (-1, 'Dagobert', 1234, 'inner Dagobert');
  
  
  
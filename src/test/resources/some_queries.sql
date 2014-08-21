-- ################################################## -- 
-- PostgreSQL only!
-- After running the unit tests, any of the following queries should result in at least one row.
-- ################################################## -- 

-- Select all from tbl_typetest where field "iname" of itype (of type type_test) matches 'admin' 
select * from tbl_typetest where (itype).iname='admin'

-- Select all from tbl_typetest_single where field str of embedded type (type_test_int_string) matches 'foo'
select * from tbl_typetest_single where ((itype).test_data).str='foo'

-- Same with tbl_typetest, but only the first entry of array
select * from tbl_typetest where ((itype).test_data[1]).str='foo'

-- Search for a row in tbl_typetest where str in any array entry matches 'bar'
select * from
   (select id, (itype).test_data,
           generate_subscripts((itype).test_data, 1) as s
      from tbl_typetest) as foo
 where test_data[s].str = 'bar';
 
-- Better: Lets say, we have bar twice (e.g. at array index 1 AND 2). We would get one row twice.
-- Ok then:
select distinct id, test_data from
   (select id, (itype).test_data,
           generate_subscripts((itype).test_data, 1) as s
      from tbl_typetest) as foo
 where test_data[s].str = 'bar';

 -- Select str of first array entry (not index 0!!!)
 select ((itype).test_data[1]).str from tbl_typetest
 -- for more information have a look at the view provided in pg_view_approach.sql Works!
 
 
 
-- ################################################## -- 
-- ORACLE section.
-- After running the unit tests, any of the following queries should result in at least one row.
-- ################################################## -- 
-- Same as Postgre
select * from tbl_typetest where (itype).iname='admin';
select * from tbl_typetest_single where ((itype).test_data).str='foo';
-- Attention! We have to set an alias (here t)
update tbl_typetest t set t.itype.iname = 'Hulk' where id = 1;
update tbl_typetest_single t set t.itype.test_data.str = 'Hulk' where id = 1;
select t.id, t.itype.iname from tbl_typetest t;
select t.id, t.itype.iname, x.str from tbl_typetest t, table(t.itype.test_data) x;
-- for masochists or ORACLE experts (what's the difference???)
select * from  (select rownum as rn, str from the (select cast(t.itype.test_data as type_test_int_string_varray) as x from tbl_typetest t where id = 21 ))  where rn = 2 ;
 
-- Ok, giving it up..., have a look here, if you are unhappy (and want become even unhappier): 
-- http://docs.oracle.com/cd/B28359_01/appdev.111/b28371/adobjcol.htm



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
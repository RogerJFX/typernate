# Typernate Test Setup#

Only works with PostgreSQL at that very moment.

Ok, I'm wrong here. It already works with ORACLE.

Documentation comming soon.

You only should care for folder [typernate]/src/test


+ First have a look at folder resources. Make up your database using pg_create_db.sql. Should work with PostgreSQL from 8.4 on.
+ Then (very important, and sorry for that!) edit the File DatabaseTestImpl.java found in src/test/java/de/crazything/sql/typernate/conf. Edit values for: 
    + dbUser
    + dbPass
    + dbConnection (only if you have another database name as "typernate". Did you change pg_create_db.sql?)
+ If you have Postgres >= 9.2 installed, you may want to have a look at the dump /src/test/pg_json_sample.sql. You will see, that the json column accepts anything. So it is actually NOT typesafe.


Ok then: the tests are here: src/test/java/de/crazything/sql/typernate/TypeSerializerTest.java

You should have a look to the database from time to time. Don't you want to know what's happening, dude?

Tests are written as follows:

+ We have a TypeFactory creating Instances using Gson. So an object is created out of a Json String.  
+ Ok, we have an instance now. This instance now is persisted to database.
+ Next step: we fetch this very (not yet) instance back from database. 
+ Deserialize it and look, if it equals the original.

**Important: if you want to work out other tests, keep in mind, we are not using JPA here, but a very weak database interface for only showcase issues. So if you plan to have an entity with a String inside, forget about persisting it using the test setup.**




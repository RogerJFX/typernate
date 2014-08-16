# Typernate Test Setup#

Only works with PostgreSQL at that very moment.

You only should care for folder [typernate]/src/test

+ First have a look at folder resources. Make up your database using pg_create_db.sql. Should work with PostgreSQL from 8.4 on.
+ Then (very important, and sorry for that!) edit the File DatabaseTestImpl.java found in src/test/java/de/crazything/sql/typernate/conf. Edit values for: 
    + dbUser
    + dbPass
    + dbConnection (only if you have another database name as "typernate". Did you change pg_create_db.sql?)


Ok then: the tests are here: src/test/java/de/crazything/sql/typernate/TypeSerializerTest.java

You should have a look to the database from time to time. Don't you want to know what's happening, dude?

Tests are written as follows:

+ We have a TypeFactory creating Instances using Gson. So an object is created out of a Json Strings. 
+ Ok, we have an instance now. This instance now is persisted to database.
+ Next step: we fetch this very (not yet) instance back from database. 
+ Deserialize it and look, if it equals.





# Typernate #

#### Typernate is a typesafe OR mapper for user defined database types. 

##### Written in Java, currently works with PostgreSQL and ORACLE.

**Version: 0.1.1**

**Author: Roger F. Hösl**

**Date: 2014-08-17**

#### Why Typernate?
+ Typernate combines the benefits of NoSQL with those of a common relational database like Postgres or ORACLE.
+ Typernate is typesafe without having to validate Objects in a strange layer. Typesafety is guaranteed by the layer it belongs to: by the database itself.
+ Typernate should work with JPA.
+ Using Typernate may dramatically increase performance in comparison to JPA and common database modelling.

#### How did it happen? What's the idea?

Within a project I faced the problem having to read much data from database. 

+ First thought: why not using the good old file with a proprietary format. 
    + Ah, well... I would have done it 15 years ago.
+ Second: ok, lets use NoSQL. Not the badest idea in my particular case, but:  
    + Huge overhead. Too high level.
    + Querying NoSQL dbs is still a hard job, if sophisticated queries are needed.
    + More caveats, if one needs compatiblity to relational databases.
+ Third approach: man, we have a postgre database, let's use the new Json-Type, available since version 9.2. And we have 9.3!
    + The json type of postgres is not save. But to guarantee typesafety is one of the first obligations of a database in my very opinion. You can put anything to a json column, no matter what is in there.
    + My approaches of validating json types in the database failed. It's all text, I could not deal with that. Triggers? Failed. Functions? Failed. No chance as I still guess.
    + In the end the Postgre-Type "json" is weak. Not suitable for my issues.
+ **Solution:**  So why not using user defined types? In sophisticated databases it is possible to have a type referencing another type, that again references a third type a.s.f.. A stack of types or arrays of types then, just like JSON can have nested objects or arrays.

**Ah! So we can have structures like json, document based, but typesafe in the database as if using constraints? Yes, we can!** 


 
#### How does Typernate work?

Well, at least it is similar to Hibernate. Hibernate maps a database row to a Java object. Ok then, Typernate maps a database type to a Java object. But Typernate can deal with Entities obtained by Hibernate as well. The only thing, that has to be done: annotate some fields, so Typernate may know what to do.


####Conclusion

We can combine it: the strength of old fashioned databases with modern document based style. Yes, we can!



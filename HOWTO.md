# Further recommendations - JPA

Using Typernate with JPA should be very easy.

1. Annotate your entity correctly using typernate annotations
2. After fetching a List of entities, deserialize them using EntityDeserializer
3. Before persisting an entity, simply pass it to EntitySerializer.

That's all. 

# How to annotate

## Type annotating

Any user defined database type has to be represented in Java as Type-Entity. There are two Annotations:

1. @DbType - annotation for a class
2. @DbTypeField - annotation for a field in class (that is a member of the db type)

@DbType has one value telling us the name of db type. E.g. @DbType("type_test") makes sure, our class represents the db type "type_test".

@DbTypeField represents a member of the db type. There a two params:

1. index (mandatory): the index of member in the db type.
2. quote (optional, default false): only for serialization. If true, the value will be wrapped in single quotes.


## Entity annotating

If there is a db type in your entity, just have it representated by a Java Object annotated by

@DbTypeObject

There is one Parameter "target", that is mandatory. It should point to another member of your class representing a Type-Entity or a List of it. This entity never should be persisted, so don't annotate it with @Column (JPA). 

Typically we have something like this:

~~~~~~~~~~~
@Column(name="myTypeFieldInDB")
@DbTypeObject(target = "myTestType")
Object nameDoesNotMatter;
// Ah! So here is our member we can work with further on.
TestType myTestType;
~~~~~~~~~~~

Member myTestType must never contact the database. It's Object nameDoesNotMatter being responsible for communicating with the database. 

EntityDeserializer will translate *Object nameDoesNotMatter* to its target *TestType myTestType* after being fetched from database.

And the other way:

EntitySerializer will translate *TestType myTestType* to *Object nameDoesNotMatter* before persiting.




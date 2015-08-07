# Introduction #

# Details #

## Tools ##

  * Latest **JDK**
  * [Maven](http://maven.apache.org/)
  * [JavaFX](http://docs.oracle.com/javafx/)

## Steps ##

```
user@localhost:~/ svn checkout https://java-market-data.googlecode.com/svn/trunk user@localhost:~/ java-market-data
user@localhost:~/ mvn package
user@localhost:~/ cd target directory
user@localhost:~/ java -jar eod.X.X.X
```

## In NetBeans ##

  * Add **exec.classpathScope=compile** to the "Run Project" Action of the used Maven profile in NetBeans.
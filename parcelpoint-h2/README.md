# ParcelPoint with an H2 data store

Build and run from this folder.

```
mvn package
java -jar target/parcelpoint-h2-1.0.jar
```

Open the H2 Shell on the database while the program is not running. The file
h2shell.txt holds its options. Type SQL statements after `sql>`, such as
`SELECT * FROM PARCEL;`, and type `quit` to leave.

```
java "@h2shell.txt"
```

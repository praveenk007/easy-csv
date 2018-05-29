# easy-csv
is an **annotation** based CSV utility with support for **column names**, **column positions** and **nested objects**

## Usage
Let's say you have a `List<UserDetail>`, which needs to be converted into a CSV format data or written into a CSV file.
Add field level annotations on fields which needs to be considered for the operation in below fashion in your `UserDetail` object

```java
public class UserDetail {
  
  @CSVHeader(value = "First name")
  @CSVHeaderPosition(value = 1)
  private String fName;
  
  @CSVHeader(value = "Last name")
  @CSVHeaderPosition(value = 2)
  private String lName;
  
  
  @CSVHeaderPosition(value = 4)
  private Address address;
  
  @CSVHeaderPosition(value = 3)
  private List<Vehicle> vehicles;
  .
  .
  .
```
If you wish to ignore a field for CSV conversion, just don't add the annotations.  
If you wish to change column order, just change the value of `@CSVHeaderPosition` annotated over the field.  
Simple! Ain't it? :)  
Incase of nested object (in this case, `Address`), provide `@CSVHeaderPosition` on the field and `@CSVHeaderPosition` and `@CSVHeader` on the fields inside Address.
In-case of `List<Object>` (in this case `List<Vehicle>`), same thing as above, but only the first object inside the collection will be considered for CSV (the index to be considered won't be taken from user to avoid `ArrayIndexOutOfBoundException`).

Now, let's consider a scenario where-in your object has hundreds of fields and you need to add a new field with header position 2, but that position is already taken.
Here it's cumbersome to update the position values of hundreds of fields just because you added a new field. So, you can just add the new field with `@CSVHeaderPosition` as 2 just above the exisiting field with position 2.

Please note: I used `@CSVHeaderPosition` to sort fields as JAVA doesn't guarantee field order when using reflection.  However, when sorting fields in scenarios where we have same positions for mulitple fields, the field placement order will be considered for sorting.

### Supported data-types:
```java
String
Object
boolean, float, long, double, int, char, short
List<Object>
Object[], boolean[], float[], long[], double[], int[], char[], short[]
```

### V1.0.0.1
This version comes with a function to convert collection of data into compressed, Base64 encoded CSV data in bytes format.

#### Usage

```java
EasyCSV t = new EasyCSV(",");
List<UserDetail> userDetails = new ArraysList<>();
byte[] b = t.write(userDetails, true); //pass true in 2nd arg if you want to apply header in CSV
String data = new String(Base64.getDecoder().decode(GZip.decompress(b).toString()));
//Do anything with this data, maybe write to a file or send it in bytes somewhere.
```
As of now, it's upto the caller to manage memory by sending data in chunks.  
Memory management and file write will be supported in next version.

### Disclaimer
This version is still in testing phase (basic testing with small data has been done).
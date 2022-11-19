# TitanInventories
##### Create GUI's with ease!



## Features
- Clickable items
- Make a paginated GUI with a single method!
- open & close listeners.
- Make auto updating GUIs as simple as making a normal GUI!
- Custom title and size.
- Ability to fill borders, specific columns & rows
- A util class to make items super easy!


## Maven Repository
```XML
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
```XML
<dependency>
	<groupId>com.github.TitanDevX</groupId>
	<artifactId>TitanInventories</artifactId>
	<version>LATEST</version>
</dependency>
```

## Using the plugin
### Creating a simple inventory
All you need to do is make a class that extends `TitanInv`, override the constructor to set title and size, and the `init` function where you will be setting inventory content and pagination, as follows:
```java
public class UsersGUI extends TitanInv {

public UsersGUI() {
   super(Title, Size);
}

@Override
public void init(Player p, InventoryContents con, Object[] data) {

}
```
The InventoryContens is the management of the contents.


See Examples `UsersGUI` and `ExampleUpdatingGUI`.

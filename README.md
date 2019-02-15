# WebFrame Framework
It´s a easy simple framework that makes a server where you can publish resources and web apps.

## Installation

1. Download the WebFrame-1.0-SNAPSHOT.jar file in target/folder and put in your project
2. Put the following in the pom 

```pom
<dependency>
     <groupId>eci</groupId>
     <artifactId>WebFrame</artifactId>
     <version>1.0-SNAPSHOT</version>
     <scope>system</scope>
     <systemPath>${basedir}/<path to the jar>/WebFrame-1.0-SNAPSHOT.jar</systemPath>
</dependency>
```

## Usage
1.In the src/ folder there should be a public/ folder where are located all the resorces to be publish in the server.

2.Your main class should be like the following :

```java
public class YourClass {
    public static void main(String args[]){
        WebFrameApplication.run(YourClass.class, YourPort, args);
    }
```
3.The methods that use the server must be in the main class where the past code is from, they also use the annotation @WebGet.
The annotation @WebGet can be used in a method like:
```java
@WebGet("/<your path to app>")
public String example(String...){
    //...
    //Do something intersting
    //...
    return variable;
}
```
For example:
```java
@WebGet("/hello")
public String hola(String name){
    return "Hello" + nombre + "!";
}
```
Where, when you put in the browser http://your-ip:your-port/hello?name=David , it should appear "Hello David!" in the screan.

IMPORTANT NOTE: the methods that use the @WebGet Annotation MUST return String and all parameters Must be String

##Example
[Example](https://github.com/davd62133/Example-Web-Frame)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](LICENSE.txt)

## JSPI - Java Internet Printing Protocol Implementation

This library enables you to add remote printing capabilities to your Java applications running on servers and other client applications. Have made changes in IppHttpConnection to use org.apache.httpcomponents.httpclient:4.5.3


### Usage

#### Adding dependency using Apache Maven

1. Clone the project

	Execute `git clone https://github.com/peterwhitehead/jspi.git`

2. Install artifact on your Maven repository

	Change into `jspi-core` project and execute `mvn clean install` or `mvn deploy`. Note that you might need to skip the tests when building the project.

3. Add as a dependency

   Add the following on your `pom.xml`

    ```
	<dependency>
      <groupId>com.xinterium.jspi</groupId>
      <artifactId>jspi-core</artifactId>
      <version>2.0-SNAPSHOT</version>
    </dependency>
    ```


### API and Examples

Examples are available in the `jspi-core/src/test` directory along with the tests.

#### Notes from the History : 

##### jspi
This project is based on the original project exported from code.google.com/p/jspi (LGPL)

##### Description: 

We realize a pure java implementation of the internet printing protocol (ipp) implementing Java Print Service API (jsp). This implementation can be useful for both server and client side code, so this helps in high level printing services, virtual printers for document conversion and so on.

### Authors
- Peter Whitehead
- Bhagya Silva ([https://about.me/bhagyas](https://about.me/bhagyas))

Original library author information can be found on the Google Code project location.

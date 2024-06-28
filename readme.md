
# Codex  Naturalis <br> PSP13

## Members
Federico Introzzi<br>
Francesco Fregna<br>
Samuele Lo Piccolo<br>
Vitaliano Mandara<br>
<br>

| Implemented Functionalities |
|:----------------------------|
| Complete Rules              |
| TUI + GUI                   | 
| Socket                      | 
| Chat                        |
## How to run the JARs:

- In the deliverables/final/jar folder there are three jar files, one to set the Server up, and the other two to start the Client.
- To be able to run the JARs you must have [Java21](https://www.oracle.com/java/technologies/downloads/#java21) or a higher version installed on your device
- To check what version of java is installed you can open a command prompt and type:<br>

    ```
     java -version
    ```


- The JARs were created using the maven-shade plugin. <br>To create the file named PSP13-uber-client.jar we removed the following part from the maven-shade plugin's execution related to the Client

  ```
     
      <artifactSet>
         <excludes>
            <exclude>org.openjfx</exclude>
         </excludes>
      </artifactSet>
   ```
- To create your own jars you can execute, from maven lifecycle, the **clean** and **package** steps.
<br><br><br><br>
- The Server can be run with the following command:
    ```
     java -jar PSP13-server.jar
    ```
  The server will start and use port 4321.<br><br>
- We have created two client JARs because the one named PSP13-uber-client.jar, an uber-jar that contains the javafx dependency, gives a warning when it's launched with: <br>
    ```
     java -jar PSP13-uber-client.jar
    ```
  The warning doesn't seem to have any actual effect on the application's behaviour<br>
  **We strongly recommend following the instructions to run the PSP13-client.jar file as that is the way we intended the project to be run.<br><br><br>**
-  To run PSP13-client.jar you must have installed your system's [JavaFX](https://gluonhq.com/products/javafx/
   ) SDK version 21.0.3[LTS] or higher.
- If you install JavaFX 21.0.3 and put the JavaFX files in C:\Program Files\JavaFX\ then the Client can be run with the following command:

    ```
     java --module-path "C:\Program Files\JavaFX\javafx-sdk-21.0.3\lib" --add-modules javafx.controls,javafx.fxml  -jar PSP13-client.jar
    ```

  - If you have another version of JavaFX installed, or you have the JavaFX files in another directory you must change   ```"C:\Program Files\JavaFX\javafx-sdk-21.0.3\lib"  ```  to suit your settings
  - If you have problems running the Client JAR try adding the path to the \lib folder of your JavaFX installation to the environmental variable "Path".

- _To run the TUI we recommend using a terminal on which you can disable soft wrap_
- - -
_Note that every procedure shown here is for Windows as that is the only system available to the members of our group.<br>The dependencies for other systems have been included in the pom.xml, but the behaviour of the jar files in said systems could not be tested_

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

- In the deliverables/jar folder there are two jar files, one to set the Server up, and the other one to start the Client.
- To be able to run the JARs you must have [Java21](https://www.oracle.com/java/technologies/downloads/#java21) or an higher version installed on your device
- To check what version of java is installed you can open a command prompt and type:<br>

    ```
     java -version
    ```
<br>

- The Server can be run with the following command:
    ```
     java -jar PSP13-server.jar
    ```
  The server will start and use port 4321.
-  To run the client you must have installed [JavaFX](https://gluonhq.com/products/javafx/
   ) SDK version 21.0.3[LTS] or higher.
- If you install JavaFX 21.0.3 and position the JavaFX files in C:\Program Files\JavaFX\ then the Client can be run with the following command:

    ```
     java --module-path "C:\Program Files\JavaFX\javafx-sdk-21.0.3\lib" --add-modules javafx.controls,javafx.fxml  -jar PSP13-client.jar
    ```

    - If you have another version of JavaFX installed, or you have the JavaFX files in another directory you must change   ```"C:\Program Files\JavaFX\javafx-sdk-21.0.3\lib"  ```  to suit your settings
    - If you have problems running the Client JAR try adding the path to the \lib folder of your JavaFX installation to the environmental variable "Path".

###### Note that every procedure shown here is for Windows as that is the only system available to the members of our group.
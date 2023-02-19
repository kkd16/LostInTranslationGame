
## Run Locally

Install Maven
https://maven.apache.org/

Clone the project

```bash
  git clone https://github.com/kkd16/LostInTranslationGame
```

Go to the project directory

```bash
  cd LostInTranslationGame
```

Build the server

```bash
  cd chatserver
  mvn package
```

Run the server

```bash
  java -jar .\target\chatserver-1.0.jar
```

Go back to the project directory

```bash
  cd..
```

Build the client

```bash
  cd chatclient
  mvn package
```

Run the client x2

```bash
  java -jar .\target\chatclient-1.0.jar
```

NOTE: The jar files should be run in seperate terminal windows and the terminal should be left open while running. Do not double click the jar files from the file explorer or it won't run properly.



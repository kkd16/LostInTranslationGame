
# Huh?

A very inconvient messaging platform. Send your message and the person on the other side sees the message after it has gone through a circle of language translations with Google Translate. The messages are pretty sure to come out a mess, and you have to guess what the original was to be able to view it. The goal is to make sure the receiver has no idea what you are talking about!




## Authors

- Kyle Deliyannides [@kkd16](https://www.github.com/kkd16)

- Kia Zamani [@kza40](https://www.github.com/kza40)

- Sahba Hajihoseini [@Sahbahh](https://www.github.com/Sahbahh)

- Vedant Jain [@vedantj2](https://www.github.com/vedantj2)




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


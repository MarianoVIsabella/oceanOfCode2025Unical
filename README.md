# AI Competition - Ocean of Code
#### Unical 2024/25, CdS Informatica III anno

Shared Code of the AI Competition Project A.A. 2024-2025 based on the Game *Ocean of Codes* ([CodingGame - Ocean of Codes](https://www.codingame.com/contests/ocean-of-code)).
<br />
Link to the [Official Repository](https://github.com/CodinGameCommunity/ocean-of-code) of the Game, developed by CodingGame, and to the [Official Page](https://www.codingame.com/ide/puzzle/ocean-of-code) of the Puzzle Game.
<br />
To run this Game with ASP Players, an Unofficial Modified GameEngine is needed so as to alter the Maximum Game Time.
<br /><br />
*PS*. Sometimes, the ASP Solver Response Time is too high, which means the Game will end with an Error.
In this case, a reboot is usually enough to see a full Game.


## How to Run Locally

### Prerequisites:

* JDK 1.8 (or higher).
* Maven 3.6.x.

### Dependencies:

* Antlr 4.7 (Runtime) - Maven Central.
* Emb ASP (latest) - Complete Jar.
* DLV2 (or Other) - Executables
* CodingGame GameEngine (Unofficial, Personalized) - Complete Jar

## Add to VM Option:
* --add-opens java.base/java.lang=ALL-UNNAMED

### Follow the next steps respectively to run the game locally:

1. Run `mvn clean install`. This bundles the game dependencies and tests in one jar file inside the target directory.
1. Run `java -jar ./target/captainsonar-1.0-SNAPSHOT-fat-tests.jar`.

### Output:

Running the executable jar will output two things:

* A full dump of the game summary to the stdout.

* A web page to see the game in action (e.g. http://localhost:8888/test.html).

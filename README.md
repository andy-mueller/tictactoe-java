tictactoe-java
==============

A Tic Tac Toe game written in Java using TDD and following the [S.O.L.I.D.][solid] principles. The code can be compiled and executed using maven and JDK 7.

   [solid]: http://www.objectmentor.com/resources/articles/Principles_and_Patterns.pdf

A Swing application can be started using:

    $ mvn -pl delivery/swing exec:java


A text based application can be started using:

    $ mvn -pl delivery/text/cli exec:java

There is also a jcurses based text ui. However, it just  works on 32 bit platforms because of the needed native code. It can be invoked by typing:

    $ mvn -pl delivery/text/jcurses exec:java
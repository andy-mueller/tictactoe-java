tictactoe-java
==============

A Tic Tac Toe game written in Java using TDD and following the [S.O.L.I.D.][solid] principles. The code can be compiled and executed using maven.

   [solid]: http://www.objectmentor.com/resources/articles/Principles_and_Patterns.pdf

A Swing application can be started using:

    $ mvn -pl tictacttoe-client/tictactoe-client-swing exec:java


A text based application can be started using:

    $ mvn -pl tictacttoe-client/tictacttoe-client-jcurses exec:java
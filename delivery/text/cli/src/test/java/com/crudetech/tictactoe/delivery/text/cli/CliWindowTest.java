package com.crudetech.tictactoe.delivery.text.cli;


import org.junit.Test;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CliWindowTest {
    private static final String nl = System.getProperty("line.separator");
    @Test
    public void givenCliWindow_startingQueriesUser() throws Exception {
        StringWriter actualOutput = new StringWriter();
        InputStream stopAfterMoveInput = new ByteArrayInputStream("quit\n".getBytes());
        CliWindow window = new CliWindow(new PrintWriter(actualOutput), stopAfterMoveInput);

        try {
            window.start();
        } catch (TextGridLocationInput.BadFormatException e) { }
        String expected =
                "Play a game of tic tac toe. Make a move by specifying the grids cell by \n" +
                        "zero based, comma separated coordinates. I.e, you want to place your mark \n" +
                        "in the bottom left corner, you will enter 2,0. The first coordinate \n" +
                        "specifies the row, the second the column." + nl +
                        nl +
                        "   |   |   \n" +
                        "---+---+---\n" +
                        "   |   |   \n" +
                        "---+---+---\n" +
                        "   |   |   " + nl +
                        "Make your move" +
                        nl;

        assertThat(actualOutput.toString(), is(expected));
    }

}

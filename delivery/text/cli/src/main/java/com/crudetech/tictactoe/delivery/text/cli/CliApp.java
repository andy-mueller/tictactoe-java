package com.crudetech.tictactoe.delivery.text.cli;


import com.crudetech.tictactoe.game.AlphaBetaPruningPlayer;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.HumanPlayer;
import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CliApp {
    public static void main(String[] args) throws Exception {

        final ComputerPlayer aiPlayer = AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).asSecondPlayer();

        final PrintWriter out = createSystemOutWriter();

        printWelcomeToUser(out);

        TextGridWidget widget = new TextGridWidget();
        TextGridWidgetUiView textGridWidgetUiView = new TextGridWidgetUiView(widget, out);
        TextUiFeedbackChannel textUiFeedbackChannel = new TextUiFeedbackChannel(out);
        TextUserInput input = new TextUserInput(System.in);
        HumanPlayer humanPlayer = new TextHumanPlayer(textGridWidgetUiView, textUiFeedbackChannel, out, input, widget);
        HumanVsComputerPlayerInteractor interactor =
                HumanVsComputerPlayerInteractor.builder()
                        .setComputerPlayer(aiPlayer)
                        .setHumanPlayer(humanPlayer)
                        .build();

        interactor.startWithHumanPlayer(Grid.Mark.Cross);
    }

    private static void printWelcomeToUser(PrintWriter out) {
        out.println("Play a game of tic tac toe. Make a move by specifying the grids cell " +
                "by zero based, comma separated coordinates. I.e, you want to place your mark " +
                "in the bottom left corner, you will enter 2,0. The first coordinate specifies the row, " +
                "the second the column.");
    }

    private static PrintWriter createSystemOutWriter() {
        return new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
    }

}

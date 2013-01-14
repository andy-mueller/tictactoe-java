package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.AlphaBetaPruningPlayer;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;
import com.crudetech.tictactoe.ui.UiPlayer;

import java.io.InputStream;
import java.io.PrintWriter;


class CliWindow {
    private final PrintWriter out;
    private final InputStream in;

    public CliWindow(PrintWriter out, InputStream in) {
        this.out = out;
        this.in = in;
    }

    public void start() {
        final ComputerPlayer aiPlayer = AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).asSecondPlayer();

        printWelcomeToUser(out);

        TextGridWidget widget = new TextGridWidget();
        TextGridWidgetUiView textGridWidgetUiView = new TextGridWidgetUiView(widget, out);
        TextUiFeedbackChannel textUiFeedbackChannel = new TextUiFeedbackChannel(out);
        TextGridLocationInput input = new TextGridLocationInput(in);
        UiPlayer humanPlayer = new TextUiPlayer(textGridWidgetUiView, textUiFeedbackChannel, out, input);
        HumanVsComputerPlayerInteractor interactor =
                HumanVsComputerPlayerInteractor.builder()
                        .setComputerPlayer(aiPlayer)
                        .setHumanPlayer(humanPlayer)
                        .build();

        interactor.startWithHumanPlayer(Grid.Mark.Cross);
    }

    private static void printWelcomeToUser(PrintWriter out) {
        out.println(
                "Play a game of tic tac toe. Make a move by specifying the grids cell by " + "\n" +
                        "zero based, comma separated coordinates. I.e, you want to place your mark " + "\n" +
                        "in the bottom left corner, you will enter 2,0. The first coordinate " + "\n" +
                        "specifies the row, the second the column."
        );
        out.println();
    }
}

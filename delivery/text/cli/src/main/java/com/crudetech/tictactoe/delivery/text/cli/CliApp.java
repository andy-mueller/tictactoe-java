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

        final ComputerPlayer aiPlayer = AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).withStartPlayerMark(Grid.Mark.Cross).asMin();

        final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
        final TextGridWidget widget = new TextGridWidget();

        final TextUserInput input = new TextUserInput(System.in);

        TextGridWidgetUiView textGridWidgetUiView = new TextGridWidgetUiView(widget, out);
        TextUiFeedbackChannel textUiFeedbackChannel = new TextUiFeedbackChannel(out);
        final HumanPlayer humanPlayer = new TextHumanPlayer(textGridWidgetUiView, textUiFeedbackChannel, out, input, widget);
        HumanVsComputerPlayerInteractor interactor =
                HumanVsComputerPlayerInteractor.builder()
                        .setComputerPlayer(aiPlayer)
                        .setHumanPlayer(humanPlayer)
                        .build();

        interactor.startWithHumanPlayer(Grid.Mark.Cross);
    }

}

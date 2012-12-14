package com.crudetech.tictactoe.client.cli;


import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.AlphaBetaPruningPlayer;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.CellEventObject;
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

        final EventSupport<CellEventObject<TextGridWidget>> cellEnterEvent = new EventSupport<CellEventObject<TextGridWidget>>();
        TextGridWidgetUiView textGridWidgetUiView = new TextGridWidgetUiView(widget, out);
        TextUiFeedbackChannel textUiFeedbackChannel = new TextUiFeedbackChannel(out);
        final HumanPlayer humanPlayer = new HumanPlayer(textGridWidgetUiView, textUiFeedbackChannel, cellEnterEvent){
            @Override
            public void yourTurn(Grid actualGrid) {
                super.yourTurn(actualGrid);
                out.println("Make your move");
                Grid.Location location = input.nextLocation();
                cellEnterEvent.fireEvent(new CellEventObject<TextGridWidget>(widget, location));
            }
        };
        HumanVsComputerPlayerInteractor interactor = new HumanVsComputerPlayerInteractor(
                aiPlayer,
                humanPlayer);

        interactor.startWithHumanPlayer(Grid.Mark.Cross);
    }

}

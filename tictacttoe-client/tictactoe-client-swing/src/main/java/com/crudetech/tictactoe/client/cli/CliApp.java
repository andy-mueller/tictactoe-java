package com.crudetech.tictactoe.client.cli;


import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.AlphaBetaPruningPlayer;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.CellEventObject;
import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;
import com.crudetech.tictactoe.ui.UiFeedbackChannel;
import com.crudetech.tictactoe.ui.UiView;

import java.io.PrintWriter;

public class CliApp {
    public static void main(String[] args) throws Exception{

        ComputerPlayer aiPlayer = AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).withStartPlayerMark(Grid.Mark.Cross).asMin();

        final PrintWriter out = new PrintWriter(System.out);
        final TextGridWidget widget = new TextGridWidget();

        EventSupport<CellEventObject<TextGridWidget>> cellEnterEvent = new EventSupport<CellEventObject<TextGridWidget>>();
        HumanVsComputerPlayerInteractor interactor = new HumanVsComputerPlayerInteractor(aiPlayer, cellEnterEvent){
            @Override
            protected UiView createUiView() {
                return new TextGridWidgetUiView(widget, out);
            }

            @Override
            protected UiFeedbackChannel createUiFeedback() {
                return new TextUiFeedbackChannel(out);
            }
        };

        interactor.startWithHumanPlayer(Grid.Mark.Cross);
    }

}

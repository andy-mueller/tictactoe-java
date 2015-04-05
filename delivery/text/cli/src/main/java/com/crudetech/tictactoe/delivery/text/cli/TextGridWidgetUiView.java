package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.UiView;

import java.io.PrintWriter;

public class TextGridWidgetUiView implements UiView{
    private final TextGridWidget widget;
    private final PrintWriter pipe;

    public TextGridWidgetUiView(TextGridWidget widget, PrintWriter pipe) {
        this.widget = widget;
        this.pipe = pipe;
    }

    @Override
    public void setModel(Grid grid) {
        widget.setModel(grid);
        widget.render(pipe);
        lineBreak();
        lineBreak();
    }

    private void lineBreak() {
        pipe.println();
    }

    @Override
    public void highlight(Grid.ThreeInARow triple) {
        widget.highlight(triple);
        widget.render(pipe);
        lineBreak();
    }
}

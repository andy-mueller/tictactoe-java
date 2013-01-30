package com.crudetech.tictactoe.delivery.text.jcurses;


import com.crudetech.event.Event;
import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.delivery.text.cli.TextGridWidget;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.CellEventObject;
import com.crudetech.tictactoe.ui.UiView;
import jcurses.system.InputChar;
import jcurses.widgets.TextComponent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import static com.crudetech.matcher.Verify.verifyThat;
import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


class GridWidget extends TextComponent implements UiView {
    private final TextGridWidget textGridWidget;
    private final TextGridWidget.Cursor cursor;

    GridWidget() {
        this(new TextGridWidget.Cursor());
    }
    GridWidget(TextGridWidget.Cursor cursor) {
        this(cursor, new TextGridWidget());
    }
    GridWidget(TextGridWidget.Cursor cursor, TextGridWidget textGridWidget) {
        this.textGridWidget = textGridWidget;
        this.cursor = cursor;
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
    }

    @Override
    public void setModel(Grid grid) {
        verifyThat(grid, is(notNullValue()));

        textGridWidget.setModel(grid);
        textGridWidget.highlight(Grid.ThreeInARow.Empty);
        String gridAsText = buildTextRepresentation();
        setText(gridAsText);
        doRepaint();
    }

    private String buildTextRepresentation() {
        StringWriter textPipe = new StringWriter();
        textGridWidget.render(new PrintWriter(textPipe));
        return textPipe.toString();
    }


    void moveCursorToFirstMarkedCell(Grid grid) {
        if (currentCursorPositionIsOnMarkedCell(grid)) {
            return;
        }

        Grid.Location cursorLocation =
                from(grid.getCells())
                        .where(markIsEqualTo(Grid.Mark.None))
                        .select(location())
                        .firstOr(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        cursor.setLocation(cursorLocation);
    }

    private boolean currentCursorPositionIsOnMarkedCell(Grid grid) {
        return grid.getAt(cursor.getLocation()).equals(Grid.Mark.None);
    }

    @Override
    public void highlight(Grid.ThreeInARow triple) {
        textGridWidget.highlight(triple);
        setText(buildTextRepresentation());
        repaint();
    }

    static class KeyDownEventObject extends CellEventObject<GridWidget> {
        private final char character;

        public KeyDownEventObject(GridWidget gridWidget, char character, Grid.Location location) {
            super(gridWidget, location);

            this.character = character;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            KeyDownEventObject that = (KeyDownEventObject) o;

            return character == that.character
                && super.equals(that);

        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), (int) character);
        }

        @Override
        public String toString() {
            return "KeyDownEventObject{" +
                    "source=" + getSource() +
                    "character=" + character +
                    ", location=" + getCellLocation() +
                    '}';
        }
    }

    private final EventSupport<KeyDownEventObject> keyDownEvent = new EventSupport<>();

    Event<KeyDownEventObject> keyDownEvent() {
        return keyDownEvent;
    }


    @Override
    protected boolean handleInput(InputChar ch) {
        if (ch.isSpecialCode()) {
            dispatchSpecialKeyInput(ch.getCode());
        } else {
            keyDownEvent.fireEvent(new KeyDownEventObject(this, ch.getCharacter(), cursor.getLocation()));
        }
        invalidate();
        return true;
    }

    private void invalidate() {
        doRepaint();
    }

    private void dispatchSpecialKeyInput(int keyCode) {
        if (keyCode == InputChar.KEY_DOWN) {
            cursor.moveDown();
        } else if (keyCode == InputChar.KEY_UP) {
            cursor.moveUp();
        } else if (keyCode == InputChar.KEY_LEFT) {
            cursor.moveLeft();
        } else if (keyCode == InputChar.KEY_RIGHT) {
            cursor.moveRight();
        }
    }

    @Override
    protected void doPaint() {
        setCursorLocation(cursor.getTextPositionX(), cursor.getTextPositionY());
        super.doPaint();
    }
}

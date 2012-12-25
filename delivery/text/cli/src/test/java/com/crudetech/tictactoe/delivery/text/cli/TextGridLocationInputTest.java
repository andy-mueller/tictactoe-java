package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TextGridLocationInputTest {
    @Test
    public void givenUserSpecifiesValidCoordinates_gridLocationIsComputed() throws Exception {
        InputStream in = createTextStream("2,1\n");
        TextGridLocationInput input = new TextGridLocationInput(in);

        Grid.Location location = input.nextLocation();

        assertThat(location, is(Grid.Location.of(Grid.Row.Third, Grid.Column.Second)));
    }

    @Test
    public void givenUserSpecifiesInvalidCoordinateFormat_BadFormatIsThrown() throws Exception {
        InputStream in = createTextStream("2:1\n");
        final TextGridLocationInput input = new TextGridLocationInput(in);

        Runnable inputWithBadFormat = new Runnable() {
            @Override
            public void run() {
                input.nextLocation();
            }
        };

        assertThat(inputWithBadFormat, doesThrow(TextGridLocationInput.BadFormatException.class));
    }
    @Test
    public void givenUserSpecifiesInvalidCoordinates_BadFormatIsThrown() throws Exception {
        InputStream in = createTextStream("2,NaN\n");
        final TextGridLocationInput input = new TextGridLocationInput(in);

        Runnable inputWithBadFormat = new Runnable() {
            @Override
            public void run() {
                input.nextLocation();
            }
        };

        assertThat(inputWithBadFormat, doesThrow(TextGridLocationInput.BadFormatException.class));
    }

    private InputStream createTextStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }
}

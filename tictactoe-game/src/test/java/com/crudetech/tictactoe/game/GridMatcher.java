package com.crudetech.tictactoe.game;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class GridMatcher {
    static Matcher<Grid> isEmpty() {
        return new TypeSafeDiagnosingMatcher<Grid>() {
            @Override
            protected boolean matchesSafely(Grid grid, Description mismatchDescription) {
                for (Grid.Cell cell : grid) {
                    if (cell.getMark() != Grid.Mark.None) {
                        mismatchDescription.appendText("not empty at").appendValue(cell.getLocation());
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is empty");
            }
        };
    }

}

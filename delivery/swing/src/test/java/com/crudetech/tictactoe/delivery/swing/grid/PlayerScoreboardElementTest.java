package com.crudetech.tictactoe.delivery.swing.grid;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PlayerScoreboardElementTest {

    private static class PlayerScoreboardElement {
    }

    @Test
    public void creates() throws Exception {
        PlayerScoreboardElement p = new PlayerScoreboardElement();
        assertThat(p, is(notNullValue()));
    }
    // givenPlayersName_NameIsPainted
}

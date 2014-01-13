package com.crudetech.tictactoe.delivery.swing.grid;

import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class JScoreboardTest {
    @Test
    public void uiClassHasPublicCreateUIMethod() throws Exception {
        Method m = ScoreboardUI.class.getMethod("createUI", JComponent.class);
        assertMethodIsPublicStatic(m);
    }

    private void assertMethodIsPublicStatic(Method m) {
        assertThat(m, is(notNullValue()));
        assertThat(Modifier.isStatic(m.getModifiers()), is(true));
        assertThat(Modifier.isPublic(m.getModifiers()), is(true));
    }

    @Test
    public void defaultCtorCreatesUIInstance() throws Exception {
        JScoreboard sc = new JScoreboard();
        assertThat(sc.getUI(), is(instanceOf(ScoreboardUI.class)));
    }

}

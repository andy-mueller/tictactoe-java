package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Image;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class StylesTest {
    private final Styles style;

    public StylesTest(Styles style) {
        this.style = style;
    }

    @Parameters
    public static Collection<Styles[]> createData() {
        Collection<Styles[]> data = new ArrayList<>();
        for (Styles s : Styles.values()) {
            data.add(new Styles[]{s});
        }
        return data;
    }

    @Test
    public void backGroundImageLoads() {
        assertThat(style.getBackgroundImage(), is(notNullValue()));
    }

    @Test
    public void backGroundImageIsCorrect() throws Exception {
        Image expected = loadBackGroundImageFromResources();
        assertThat(style.getBackgroundImage(), is(expected));
    }

    private AwtImage loadBackGroundImageFromResources() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/com/crudetech/tictactoe/delivery/swing/grid/" + style.name().toLowerCase() + "style/tic-tac-toe-grid.jpg")) {
            return new AwtImage(ImageIO.read(in));
        }
    }

    @Test
    public void backgroundImageIsCached() {
        assertThat(style.getBackgroundImage(), is(sameInstance(style.getBackgroundImage())));
    }

    @Test
    public void crossImageLoads() {
        assertThat(style.getCrossImage(), is(notNullValue()));
    }

    @Test
    public void crossImageIsCached() {
        assertThat(style.getCrossImage(), is(sameInstance(style.getCrossImage())));
    }

    @Test
    public void noughtImageLoads() {
        assertThat(style.getNoughtImage(), is(notNullValue()));
    }

    @Test
    public void noughtImageIsCached() {
        assertThat(style.getNoughtImage(), is(sameInstance(style.getNoughtImage())));
    }

    @Test
    public void markImagesHaveSameDimensions() {
        assertThat(style.getCrossImage().getWidth(), is(style.getNoughtImage().getWidth()));
        assertThat(style.getCrossImage().getHeight(), is(style.getNoughtImage().getHeight()));

        for (Grid.Location location : Grid.Location.allLocations()) {
            Rectangle rectangle = style.getGridMarkLocations(location);
            assertThat(rectangle.width, is(style.getCrossImage().getWidth()));
            assertThat(rectangle.height, is(style.getCrossImage().getHeight()));
        }
    }

}

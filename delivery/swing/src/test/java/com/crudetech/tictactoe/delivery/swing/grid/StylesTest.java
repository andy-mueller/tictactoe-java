package com.crudetech.tictactoe.delivery.swing.grid;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
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
        BufferedImage expected = loadBackGroundImageFromResources();
        assertImagesAreEqual(style.getBackgroundImage(), expected);
    }

    private BufferedImage loadBackGroundImageFromResources() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/com/crudetech/tictactoe/delivery/swing/grid/" + style.name().toLowerCase() + "style/tic-tac-toe-grid.jpg")) {
            return ImageIO.read(in);
        }
    }

    private static void assertImagesAreEqual(BufferedImage actual, BufferedImage expected) {
        DataBuffer expectedDataBuffer = expected.getData().getDataBuffer();
        DataBuffer actualDataBuffer = actual.getData().getDataBuffer();

        assertThat(actualDataBuffer.getSize(), is(expectedDataBuffer.getSize()));
        int size = expectedDataBuffer.getSize();

        for (int i = 0; i < size; i++) {
            assertThat(actualDataBuffer.getElem(i), is(expectedDataBuffer.getElem(i)));
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
    public void markImagesHaveSameDimensions(){
        assertThat(style.getCrossImage().getWidth(), is(style.getNoughtImage().getWidth()));
        assertThat(style.getCrossImage().getHeight(), is(style.getNoughtImage().getHeight()));

        for (Rectangle[] rectangles : style.getGridMarkLocations()) {
            for (Rectangle rectangle : rectangles) {
                assertThat(rectangle.width, is(style.getCrossImage().getWidth()));
                assertThat(rectangle.height, is(style.getCrossImage().getHeight()));
            }
        }
    }
    @Test
    public void markBounds(){
        assertThat(style.getGridMarkLocations().length, is(3));
        assertThat(style.getGridMarkLocations()[0].length, is(3));
        assertThat(style.getGridMarkLocations()[1].length, is(3));
        assertThat(style.getGridMarkLocations()[2].length, is(3));
    }
}

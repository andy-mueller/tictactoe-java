package com.crudetech.tictactoe.client.swing.grid;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.imageio.ImageIO;
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
        try (InputStream in = getClass().getResourceAsStream("/com/crudetech/tictactoe/client/swing/grid/" + style.name().toLowerCase() + "/tic-tac-toe-grid.jpg")) {
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
}

package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AwtImage implements Image {
    final BufferedImage image;

    AwtImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }


    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwtImage awtImage = (AwtImage) o;

        return equalsByBuffer(this, awtImage);
    }

    private boolean equalsByBuffer(AwtImage lhs, AwtImage rhs) {
        DataBuffer expectedDataBuffer = lhs.image.getData().getDataBuffer();
        DataBuffer actualDataBuffer = rhs.image.getData().getDataBuffer();

        assertThat(actualDataBuffer.getSize(), is(expectedDataBuffer.getSize()));
        int size = expectedDataBuffer.getSize();

        for (int i = 0; i < size; i++) {
            if(actualDataBuffer.getElem(i) != expectedDataBuffer.getElem(i)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return image.hashCode();
    }
}

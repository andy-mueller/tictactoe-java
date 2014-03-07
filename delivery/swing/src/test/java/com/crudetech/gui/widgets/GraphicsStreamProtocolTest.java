package com.crudetech.gui.widgets;


import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.runner.RunWith;

@RunWith(Features.class)
public class GraphicsStreamProtocolTest {
    static class ExampleWidget implements Widget {
        @Override
        public void paint(GraphicsStream pipe) {
            try (GraphicsStream.Context ctx = pipe.newContext()) {
                ctx.drawImage(null);
            }
        }

        @Override
        public CoordinateSystem coordinateSystem() {
            return CoordinateSystem.identity();
        }

        @Override
        public void setCoordinateSystem(CoordinateSystem coordinates) {
        }
    }

    @Feature(AdheresToGraphicsStreamProtocol.class)
    public static AdheresToGraphicsStreamProtocol.Factory adheresToProtocol() {
        return new AdheresToGraphicsStreamProtocol.Factory() {
            @Override
            public Widget createWidget() {
                return new ExampleWidget();
            }
        };
    }
}

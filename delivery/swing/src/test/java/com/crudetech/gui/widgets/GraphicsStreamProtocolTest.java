package com.crudetech.gui.widgets;


import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import org.junit.runner.RunWith;

@RunWith(Features.class)
public class GraphicsStreamProtocolTest {
    static abstract class AWidget implements Widget {
        static final Image AnyImage = null;

        @Override
        public CoordinateSystem coordinateSystem() {
            return CoordinateSystem.identity();
        }

        @Override
        public void setCoordinateSystem(CoordinateSystem coordinates) {
        }
    }

    static class ExampleWidget extends AWidget {
        private final ExampleSubWidget subWidget = new ExampleSubWidget();

        @Override
        public void paint(GraphicsStream pipe) {
            try (GraphicsStream.Context ctx = pipe.newContext()) {
                subWidget.paint(ctx);
                ctx.drawImage(AnyImage);
            }
        }
    }

    static class ExampleSubWidget extends AWidget {
        @Override
        public void paint(GraphicsStream pipe) {
            GraphicsStream.Context ctx = pipe.newContext();
            ctx.drawImage(AnyImage);
            ctx.close();
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

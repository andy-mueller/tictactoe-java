package com.crudetech.gui.widgets;

import com.crudetech.collections.Iterables;
import com.crudetech.junit.feature.FeatureFixture;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.StringJoiner.join;


public class AdheresToGraphicsStreamProtocol implements FeatureFixture {
    public static interface Factory {
        Widget createWidget();
    }

    private AdheresToGraphicsStreamProtocol.RecursiveSubContextCreator recursiveSubContextCreator;
    private GraphicsStream stream;
    private List<GraphicsStream.Context> subContexts;
    private final Factory factory;

    public AdheresToGraphicsStreamProtocol(Factory factory) {
        this.factory = factory;
    }

    @Before
    public void createGraphicStreamMock() throws Exception {
        recursiveSubContextCreator = new RecursiveSubContextCreator();
        stream = mock(GraphicsStream.class);
        subContexts = new ArrayList<>();
        recursivelySetupNewContextMocks(stream);
    }

    private void recursivelySetupNewContextMocks(GraphicsStream stream) {
        when(stream.newContext()).thenAnswer(recursiveSubContextCreator);
    }

    private class RecursiveSubContextCreator implements Answer<GraphicsStream.Context> {
        @Override
        public GraphicsStream.Context answer(InvocationOnMock invocation) throws Throwable {
            GraphicsStream.Context subMock = createMock(invocation);
            subContexts.add(subMock);
            recursivelySetupNewContextMocks(subMock);
            return subMock;
        }

        GraphicsStream.Context createMock(InvocationOnMock unused) {
            return mock(GraphicsStream.Context.class);
        }
    }

    @Test
    public void widgetTakesOnlyANewContextFromStream() throws Exception {
        Widget w = factory.createWidget();

        w.paint(stream);

        verify(stream, only()).newContext();
    }

    @Test
    public void newContextIsClosed() throws Exception {
        Widget w = factory.createWidget();

        w.paint(stream);

        verify(subContexts.get(0), new LastCall()).close();
    }

    private static class LastCall implements VerificationMode {
        @Override
        public void verify(VerificationData data) {
            List<Invocation> allInvocationOnMock = data.getAllInvocations();
            InvocationMatcher expectedInvocation = data.getWanted();
            Invocation lastInvocationOnMock = Iterables.lastOf(allInvocationOnMock);
            if (!expectedInvocation.matches(lastInvocationOnMock)) {
                reportWrongLastInteraction(expectedInvocation, lastInvocationOnMock);
            }
        }

        private void reportWrongLastInteraction(InvocationMatcher expectedInvocation, Invocation lastInvocationOnMock) {
            throw new NoInteractionsWanted(join(
                    "Expected last interaction: " + expectedInvocation,
                    "But actual last interaction was:",
                    lastInvocationOnMock.getLocation(),
                    "No interactions wanted here:",
                    new Location()
            ));
        }
    }

    @Test
    public void givenAnExceptionIsThrown_contextIsClosed() throws Exception {
        recursiveSubContextCreator = new ThrowingRecursiveSubContextCreator();
        final Widget w = factory.createWidget();

        assertThat(new Runnable() {
            @Override
            public void run() {
                w.paint(stream);
            }
        }, doesThrow(RuntimeException.class));
        verify(subContexts.get(0), new LastCall()).close();
    }

    private class ThrowingRecursiveSubContextCreator extends RecursiveSubContextCreator {
        private final int instanceThatWillThrow;
        private int instanceCounter = 0;

        public ThrowingRecursiveSubContextCreator(int instanceThatWillThrow) {
            this.instanceThatWillThrow = instanceThatWillThrow;
        }

        public ThrowingRecursiveSubContextCreator() {
            this(0);
        }

        @Override
        GraphicsStream.Context createMock(InvocationOnMock invocation) {

            if (instanceCounter++ != instanceThatWillThrow) {
                return super.createMock(invocation);
            }
            return mock(GraphicsStream.Context.class, new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    if (isCloseMethod(invocation)) {
                        return null;
                    }
                    throw new RuntimeException();
                }


                private boolean isCloseMethod(InvocationOnMock invocation) {
                    return invocation.getMethod().getName().equals("close");
                }
            });
        }
    }

    @Test
    public void allSubContextsAreClosedInReversedOrder() throws Exception {
        Widget w = factory.createWidget();

        w.paint(stream);

        GraphicsStream.Context[] reversedSubContexts = reversedSubContexts();
        InOrder reverseSubContexts = inOrder(reversedSubContexts);
        for (GraphicsStream.Context ctx : reversedSubContexts) {
            reverseSubContexts.verify(ctx).close();
            verify(ctx, new LastCall()).close();
        }
    }

    private GraphicsStream.Context[] reversedSubContexts() {
        List<GraphicsStream.Context> copy = new ArrayList<>(subContexts);
        Collections.reverse(copy);
        return copy.toArray(new GraphicsStream.Context[copy.size()]);
    }

    @Ignore
    @Test
    public void givenAnExceptionIsThrownAnyWhere_allContextsAreClosedInCorrectOrder() throws Exception {
        int recursiveContextCounts = computeSubContextCount();

        while (recursiveContextCounts >= 0) {
            recursiveSubContextCreator = new ThrowingRecursiveSubContextCreator(--recursiveContextCounts);
            final Widget w = factory.createWidget();

            assertThat(new Runnable() {
                @Override
                public void run() {
                    w.paint(stream);
                }
            }, doesThrow(RuntimeException.class));
            for (GraphicsStream.Context subContext : subContexts) {
                verify(subContext, new LastCall()).close();
            }
        }
    }

    private int computeSubContextCount() {
        Widget w = factory.createWidget();
        w.paint(stream);
        final int count = subContexts.size();
        subContexts.clear();
        return count;
    }

    static abstract class GraphicsStreamDouble implements GraphicsStream {

        @Override
        public void pushCoordinateSystem(CoordinateSystem coos) {
        }

        @Override
        public void popCoordinateSystem() {
        }

        @Override
        public void pushColor(Color color) {
        }

        @Override
        public void popColor() {
        }

        @Override
        public void drawRectangle(Rectangle rectangle) {
        }

        @Override
        public void fillRectangle(Rectangle boundary) {
        }

        @Override
        public void drawLine(Point start, Point end) {
        }

        @Override
        public void drawImage(Image image) {
        }

        @Override
        public void pushAlpha(AlphaValue alpha) {
        }

        @Override
        public void popAlpha() {
        }

        @Override
        public void drawText(int x, int y, String text) {
        }

        @Override
        public void pushFont(Font font) {
        }
    }


    static class GraphicsStreamMock extends GraphicsStreamDouble {
        private List<Context> allCreatedSubContexts = new ArrayList<>();

        @Override
        public Context newContext() {
            GraphicsContextMock contextMock = new GraphicsContextMock();
            allCreatedSubContexts.add(contextMock);
            return contextMock;
        }

        class GraphicsContextMock extends GraphicsStreamDouble implements GraphicsStream.Context {
            @Override
            public Context newContext() {
                return GraphicsStreamMock.this.newContext();
            }

            @Override
            public void close() {
            }
        }
    }
}

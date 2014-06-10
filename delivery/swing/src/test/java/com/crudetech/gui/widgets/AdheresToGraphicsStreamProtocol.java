package com.crudetech.gui.widgets;

import com.crudetech.collections.Iterables;
import com.crudetech.junit.feature.FeatureFixture;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.crudetech.gui.widgets.AdheresToGraphicsStreamProtocol.NothingExcept.nothingExcept;
import static com.crudetech.matcher.ThrowsException.doesThrow;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;


public class AdheresToGraphicsStreamProtocol implements FeatureFixture {
    public static interface Factory {
        Widget createWidget();
    }

    private final Factory factory;

    public AdheresToGraphicsStreamProtocol(Factory factory) {
        this.factory = factory;
    }

    @Test
    public void widgetTakesOnlyANewContextFromStream() throws Exception {
        Widget w = factory.createWidget();
        GraphicsStreamMock stream = new GraphicsStreamMock();

        w.paint(stream);

        stream.verifyOnlyNewContextWasCalled();
    }

    @Test
    public void newContextIsClosed() throws Exception {
        Widget w = factory.createWidget();
        GraphicsStreamMock stream = new GraphicsStreamMock();

        w.paint(stream);

        stream.verifyAllNewContextsAreClosedOnLastCall();
    }

    @Test
    public void givenAnExceptionIsThrown_contextIsClosed() throws Exception {
        final Widget w = factory.createWidget();
        final GraphicsStreamMock stream = new GraphicsStreamMock();
        stream.setSubInstanceToThrow(0);

        assertThat("Expecting the stream mock to throw", new Runnable() {
            @Override
            public void run() {
                w.paint(stream);
            }
        }, doesThrow(RuntimeException.class));
        stream.verifyAllNewContextsAreClosedOnLastCall();
    }

    @Test
    public void allSubContextsAreClosedInReversedOrder() throws Exception {
        Widget w = factory.createWidget();
        final GraphicsStreamMock stream = new GraphicsStreamMock();

        w.paint(stream);

        stream.verifyAllSubContextAreClosedInReverseOrder();
    }

    @Test
    public void givenAnExceptionIsThrownAnyWhere_allContextsAreClosedInCorrectOrder() throws Exception {
        int recursiveContextCounts = computeSubContextCount();

        while (recursiveContextCounts > 0) {
            final Widget widget = factory.createWidget();
            final GraphicsStreamMock stream = new GraphicsStreamMock();
            stream.setSubInstanceToThrow(--recursiveContextCounts);

            executeThrowingCall(new Runnable() {
                @Override
                public void run() {
                    widget.paint(stream);
                }
            });

            stream.verifyAllNewContextsAreClosedOnLastCall();
            stream.verifyAllSubContextAreClosedInReverseOrder();
        }
    }

    private void executeThrowingCall(Runnable runnable) {
        assertThat("Expecting the runnable to throw", runnable, doesThrow(RuntimeException.class));
    }

    private int computeSubContextCount() {
        Widget w = factory.createWidget();
        GraphicsStreamMock pipe = new GraphicsStreamMock();
        w.paint(pipe);
        return pipe.countOfAllCreatedSubContexts();
    }


    static class NothingExcept<T> extends TypeSafeDiagnosingMatcher<Collection<T>> {
        private final List<T> except;

        @SafeVarargs
        NothingExcept(T... except) {
            this.except = asList(except);
        }

        @SafeVarargs
        static <T> Matcher<Collection<T>> nothingExcept(final T... except) {
            return new NothingExcept<>(except);
        }


        @Override
        protected boolean matchesSafely(Collection<T> item, Description mismatchDescription) {
            HashSet<? super T> copy = new HashSet<>(item);
            copy.removeAll(except);
            if (!copy.isEmpty()) {
                mismatchDescription.appendText("contains also ").appendValue(Iterables.toString(copy));
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Contains only items: " + Iterables.toString(except));
        }
    }

    private static abstract class GraphicsStreamDouble implements GraphicsStream {
        static final String CloseMethod = "close";
        static final String NewContextMethod = "newContext";

        static class MethodCallRecorder {
            private final List<String> calledMethods = new ArrayList<>();

            String lastMethodCall() {
                return calledMethods.get(calledMethods.size() - 1);
            }

            public void recordMethodCall(int methodsOnStackToIgnore) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                StackTraceElement e = stackTrace[2 + methodsOnStackToIgnore];
                String methodName = e.getMethodName();
                calledMethods.add(methodName);
            }

            public void recordMethodCall() {
                recordMethodCall(1);
            }

            private Matcher<Collection<String>> noMethodsExcept(final String[] methods) {
                return nothingExcept(methods);
            }

            public void verifyOnlyMethods(String... methods) {
                Assert.assertThat(calledMethods, noMethodsExcept(methods));
            }

            @Override
            public String toString() {
                return "MethodCallRecorder{" +
                        "calledMethods=" + calledMethods +
                        '}';
            }
        }

        MethodCallRecorder recorder = new MethodCallRecorder();

        enum Interaction {
            Strict {
                @Override
                void interact(String method) {
                    fail("Unwanted Interaction " + method);
                }
            },
            Weak {
                @Override
                void interact(String method) {
                }
            },
            Throw {
                @Override
                void interact(String method) {

                    if (isNonThrowingMethod(method))
                        return;
                    throw new RuntimeException();
                }

                private boolean isNonThrowingMethod(String method) {
                    return method.equals(CloseMethod) || method.equals(NewContextMethod);
                }
            };

            abstract void interact(String method);
        }

        final Interaction interaction;


        GraphicsStreamDouble(Interaction interaction) {
            this.interaction = interaction;
        }

        public void verifyLastCallWasClose() {
            assertThat("Expected last methods call", recorder.lastMethodCall(), is(CloseMethod));
        }

        @Override
        public void pushCoordinateSystem(CoordinateSystem coos) {
            onMethodCall();
        }

        void onMethodCall() {
            onMethodCall(interaction);
        }

        void onMethodCall(Interaction override) {
            recorder.recordMethodCall(2);
            override.interact(recorder.lastMethodCall());
        }

        @Override
        public void popCoordinateSystem() {
            onMethodCall();
        }

        @Override
        public void pushColor(Color color) {
            onMethodCall();
        }

        @Override
        public void popColor() {
            onMethodCall();
        }

        @Override
        public void drawRectangle(Rectangle rectangle) {
            onMethodCall();
        }

        @Override
        public void fillRectangle(Rectangle boundary) {
            onMethodCall();
        }

        @Override
        public void drawLine(Point start, Point end) {
            onMethodCall();
        }

        @Override
        public void drawImage(Image image) {
            onMethodCall();
        }

        @Override
        public void pushAlpha(AlphaValue alpha) {
            onMethodCall();
        }

        @Override
        public void popAlpha() {
            onMethodCall();
        }

        @Override
        public void drawText(int x, int y, String text) {
            onMethodCall();
        }

        @Override
        public void pushFont(Font font) {
            onMethodCall();
        }
    }


    private static class GraphicsStreamMock extends GraphicsStreamDouble {
        private List<GraphicsContextMock> allCreatedSubContexts = new ArrayList<>();
        private int subContextInstanceThatThrows = -1;

        GraphicsStreamMock() {
            super(Interaction.Strict);
        }

        void setSubInstanceToThrow(int subContextInstanceThatThrows) {
            this.subContextInstanceThatThrows = subContextInstanceThatThrows;
        }

        @Override
        public Context newContext() {
            recorder.recordMethodCall();
            Interaction interaction = willBeThrowingInstance() ? Interaction.Throw : Interaction.Weak;
            GraphicsContextMock contextMock = new GraphicsContextMock(interaction, allCreatedSubContexts.size());
            allCreatedSubContexts.add(contextMock);
            return contextMock;
        }

        private boolean willBeThrowingInstance() {
            return subContextInstanceThatThrows == allCreatedSubContexts.size();
        }

        public void verifyOnlyNewContextWasCalled() {
            recorder.verifyOnlyMethods(CloseMethod, NewContextMethod);
            assertThat(allCreatedSubContexts.size(), is(greaterThanOrEqualTo(0)));
        }

        public void verifyAllNewContextsAreClosedOnLastCall() {
            allCreatedSubContexts.get(0).verifyLastCallWasClose();
        }

        public void verifyAllSubContextAreClosedInReverseOrder() {
            for (int i = allCreatedSubContexts.size() - 1; i >= 0; --i) {
                allCreatedSubContexts.get(i).verifyLastCallWasClose();
            }
        }

        public int countOfAllCreatedSubContexts() {
            return allCreatedSubContexts.size();
        }


        class GraphicsContextMock extends GraphicsStreamDouble implements GraphicsStream.Context {
            private int id;

            GraphicsContextMock(Interaction interaction, int id) {
                super(interaction);
                this.id = id;
            }

            @Override
            public Context newContext() {
                return GraphicsStreamMock.this.newContext();
            }

            @Override
            public void close() {
                onMethodCall();
            }

            @Override
            public String toString() {
                return "GraphicsContextMock{" +
                        "id=" + id +
                        '}';
            }
        }
    }
}

package com.crudetech.gui.widgets;

import com.crudetech.collections.Iterables;
import com.crudetech.junit.feature.FeatureFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.StringJoiner.join;


public class AdheresToGraphicsStreamProtocol implements FeatureFixture {

    private GraphicsStream stream;
    private GraphicsStream.Context ctx;

    public static interface Factory {
        Widget createWidget();
    }

    private final Factory factory;

    public AdheresToGraphicsStreamProtocol(Factory factory) {
        this.factory = factory;
    }

    @Before
    public void createGraphicStreamMock() throws Exception {
        stream = mock(GraphicsStream.class);
        ctx = mock(GraphicsStream.Context.class);
        when(stream.newContext()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return ctx;
            }
        });
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

        verify(ctx, new LastCall()).close();
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
                    "But found this interaction:",
                    lastInvocationOnMock.getLocation(),
                    "No interactions wanted here:",
                    new Location()
            ));
        }
    }

    @Test
    public void givenAnExceptionIsThrown_contextIsClosed() throws Exception {
        final Widget w = factory.createWidget();
        ctx = mock(GraphicsStream.Context.class, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (invocation.getMethod().getName().equals("close")) {
                    return null;
                }
                throw new RuntimeException();
            }
        });

        assertThat(new Runnable() {
            @Override
            public void run() {
                w.paint(stream);
            }
        }, doesThrow(RuntimeException.class));
        verify(ctx, new LastCall()).close();
    }
    // closes are called in reverse order
    // *all* sub streams are closed
    // al sub streams throw exceptions??
}

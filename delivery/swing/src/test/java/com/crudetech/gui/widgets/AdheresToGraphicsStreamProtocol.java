package com.crudetech.gui.widgets;

import com.crudetech.collections.Iterables;
import com.crudetech.junit.feature.FeatureFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.mockito.Mockito.*;


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
        when(stream.newContext()).thenReturn(ctx);
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
        private final Reporter reporter = new Reporter();

        public void verify(VerificationData data) {
            List<Invocation> invocations = data.getAllInvocations();
            InvocationMatcher lastInvocation = data.getWanted();
            Invocation invocation = Iterables.lastOf(invocations);
            if (!lastInvocation.matches(invocation)) {
                reporter.noMoreInteractionsWanted(invocation);
            }
        }
    }
    // closes are called in reverse order
    // all sub streams are closed
}

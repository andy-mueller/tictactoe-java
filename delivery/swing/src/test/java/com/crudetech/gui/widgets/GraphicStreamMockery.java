package com.crudetech.gui.widgets;

import org.hamcrest.Matcher;
import org.junit.rules.ExternalResource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class GraphicStreamMockery extends ExternalResource {
    private GraphicsStream stream;
    private List<GraphicsStream.Context> subContexts;
    private final Matcher<Integer> subContextCountMatcher;

    public static GraphicStreamMockery withOnlyOneSubContext() {
        return new GraphicStreamMockery();
    }

    public static GraphicStreamMockery withMoreThanOneContexts() {
        return new GraphicStreamMockery(greaterThanOrEqualTo(0));
    }

    private GraphicStreamMockery() {
        this(is(1));
    }

    private GraphicStreamMockery(Matcher<Integer> subContextCountMatcher) {
        this.subContextCountMatcher = subContextCountMatcher;
        setupSubContextCreation();
    }

    @Override
    protected void before() throws Throwable {
        setupSubContextCreation();
    }

    @Override
    protected void after() {
        stream = null;
        subContexts = null;
    }

    private void setupSubContextCreation() {
        stream = mock(GraphicsStream.class);
        subContexts = new ArrayList<>();
        recursivelySetupNewContextOnMock(stream);
    }

    private void recursivelySetupNewContextOnMock(GraphicsStream streamMock) {

        when(streamMock.newContext()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                GraphicsStream.Context contextStream = mock(GraphicsStream.Context.class);
                recursivelySetupNewContextOnMock(contextStream);
                subContexts.add(contextStream);
                return contextStream;
            }
        });
    }

    GraphicsStream stream() {
        return stream;
    }

    public void verifyOnlyOneSubContextCreated() {
        assertThat("Only one sub context was created", subContexts.size(), subContextCountMatcher);
    }

    public GraphicsStream verifyOnOnlySubContext() {
        verifyOnlyOneSubContextCreated();
        return verifyOnLastSubContext();
    }

    public GraphicsStream lastContextMock() {
        return subContexts.get(subContexts.size() - 1);
    }

    public GraphicsStream verifyOnLastSubContext() {
        return verify(lastContextMock(), times(1));
    }

    public GraphicsStream verifyOnLastSubContext(VerificationMode verificationMode) {
        return verify(lastContextMock(), verificationMode);
    }
}
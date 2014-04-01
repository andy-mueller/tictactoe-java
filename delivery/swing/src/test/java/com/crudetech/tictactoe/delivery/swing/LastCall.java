package com.crudetech.tictactoe.delivery.swing;

import com.crudetech.collections.Iterables;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.mockito.internal.util.StringJoiner.join;

public class LastCall implements VerificationMode {
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

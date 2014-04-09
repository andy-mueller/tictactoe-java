package com.crudetech.tictactoe.delivery.swing;

import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.mockito.internal.util.StringJoiner.join;

public class LastCallOnMethod implements VerificationMode {
    @Override
    public void verify(VerificationData data) {
        InvocationMatcher expectedInvocation = data.getWanted();
        Invocation lastMethod = findLastMethod(data.getAllInvocations(), expectedInvocation);

        if (!expectedInvocation.matches(lastMethod)) {
            reportWrongLastInteraction(expectedInvocation, lastMethod);
        }
    }

    private Invocation findLastMethod(List<Invocation> allInvocations, InvocationMatcher expectedInvocation) {
        for (int i = allInvocations.size() - 1; i >= 0; --i) {
            if (expectedInvocation.hasSameMethod(allInvocations.get(i))) {
                return allInvocations.get(i);
            }
        }
        reportNoLastInteractionOnMethod(expectedInvocation);
        return null;
    }

    private void reportNoLastInteractionOnMethod(InvocationMatcher expectedInvocation) {
        throw new TooLittleActualInvocations(join(
                "Expected last interaction: " + expectedInvocation,
                "But found no interaction"
        ));
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

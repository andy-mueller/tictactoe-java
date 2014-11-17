package com.crudetech.tictactoe.usecases;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 */
abstract class MapRequestBuilder implements UseCase.Request.Builder {
    private final HashMap<String, Object> parameters = new HashMap<>();

    @Override
    public void withParameter(String name, Object value) {
        parameters.put(name, value);
    }

    protected Object valueFor(String name) {
        return Objects.requireNonNull(parameters.get(name));
    }
}

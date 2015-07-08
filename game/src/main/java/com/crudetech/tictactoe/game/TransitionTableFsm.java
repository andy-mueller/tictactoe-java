package com.crudetech.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TransitionTableFsm<TEvent, TState> {

    TransitionTableFsm(TState initialState) {
        this.state = initialState;
        this.previousState = initialState;
    }


    class Transition<TEvent, TState> {
        private final TState givenState;
        private final TEvent event;
        private final TState nextState;
        private final Runnable entryAction;

        Transition(TState givenState, TEvent event, TState nextState, Runnable action) {
            this.givenState = givenState;
            this.event = event;
            this.nextState = nextState;
            this.entryAction = action;
        }
    }

    private TState state;
    private TState previousState;

    public static class TransitionNotAllowedForThisStateException extends IllegalStateException{
        private TransitionNotAllowedForThisStateException(Object event, Object state) {
            super(String.format("This event [%s] is not allowed for the state [%s]!", event, state));
        }
        private static <TEvent, TState> TransitionNotAllowedForThisStateException create(TEvent event, TState state){
            return new TransitionNotAllowedForThisStateException(event, state);
        }
    }
    private class TransitionTable {
        private final List<Transition<TEvent, TState>> transitions = new ArrayList<>();

        void add(TState givenState, TEvent event, TState nextState, Runnable action) {
            transitions.add(new Transition(givenState, event, nextState, action));
        }

        Transition transitionForEvent(TEvent event) {
            for (Transition t : transitions) {
                if (Objects.equals(t.givenState, state) && Objects.equals(t.event, event)) {
                    return t;
                }
            }
            throw TransitionNotAllowedForThisStateException.create(event, state);
        }
    }

    private final TransitionTable transitions = new TransitionTable();


    public void handleEvent(TEvent event) {
        Transition<TEvent, TState> t = transitions.transitionForEvent(event);
        this.previousState = this.state;
        this.state = t.nextState;
        t.entryAction.run();
    }

    public TState currentState() {
        return state;
    }

    public TState previousState() {
        return previousState;
    }

    protected void addTransition(TState givenState, TEvent event, TState nextState, Runnable entryAction) {
        transitions.add(givenState, event, nextState, entryAction);
    }
}
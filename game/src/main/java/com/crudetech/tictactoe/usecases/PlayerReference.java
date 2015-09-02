package com.crudetech.tictactoe.usecases;


import java.util.Objects;

public class PlayerReference {
    private Object id = "__no_id_set_yet__";

    public boolean hasId(Object id) {
        return Objects.equals(this.id, id);
    }

    public void setId(Object id) {
        this.id = id;
    }
    public Object getId() {
        return this.id;
    }
}

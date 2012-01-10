package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.Event;
import com.crudetech.event.EventListener;
import com.crudetech.event.EventObject;
import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class JTicTacToeGrid extends JComponent {
    private TicTacToeGridModel model;
    private EventSupport<CellClickedEventObject> clickedEvent = new EventSupport<>();
    private EventListener<Model.ChangedEventObject<Model<Grid>>> modelChangedListener = new EventListener<Model.ChangedEventObject<Model<Grid>>>() {
        @Override
        public void onEvent(Model.ChangedEventObject<Model<Grid>> e) {
            throw new UnsupportedOperationException("Implement me!");
        }
    };

    static {
        UIManager.getDefaults().put(TicTacToeGridUI.class.getSimpleName(), TicTacToeGridUI.class.getName());
    }

    public JTicTacToeGrid() {
        this(new TicTacToeGridModel());
        updateUI();
    }

    public JTicTacToeGrid(TicTacToeGridUI gridUi) {
        this(new TicTacToeGridModel());
        setUI(gridUi);
    }

    public JTicTacToeGrid(TicTacToeGridModel model) {
        this.model = model;
        updateUI();

        model.changed().addListener(new EventListener<Model.ChangedEventObject<Model<Grid>>>() {
            @Override
            public void onEvent(Model.ChangedEventObject<Model<Grid>> e) {
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onMouseClicked(e);
            }
      });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                 onMouseMoved(e);
            }
        });
    }

    private void onMouseMoved(MouseEvent e) {
        GridCellHit hit = cellHitFromMouseEvent(e);
        if (hit.hasHit()) {
            getModel().highlight(hit.getHit());
        }  else{
            getModel().unHighlight();
        }
        repaint();
    }

    private void onMouseClicked(MouseEvent e) {
        GridCellHit hit = cellHitFromMouseEvent(e);
        if (hit.hasHit()) {
            clickedEvent.fireEvent(new CellClickedEventObject(JTicTacToeGrid.this, hit.getHit()));
        }
    }

    private GridCellHit cellHitFromMouseEvent(MouseEvent e) {
        return getUI().checkGridCellHit(e.getX(), e.getY());
    }

    public void setModel(TicTacToeGridModel model) {
        getModel().changed().removeListener(modelChangedListener);
        model.changed().addListener(modelChangedListener);
        this.model = model;
    }

    @Override
    public String getUIClassID() {
        return TicTacToeGridUI.class.getSimpleName();
    }

    TicTacToeGridUI getUI() {
        return (TicTacToeGridUI) this.ui;
    }

    @Override
    public void setUI(ComponentUI newUI) {
        super.setUI(newUI);
    }

    @Override
    public void updateUI() {
        setUI(UIManager.getUI(this));
    }

    public TicTacToeGridModel getModel() {
        return model;
    }

    void raiseMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        super.processMouseMotionEvent(e);
    }

    public static class CellClickedEventObject extends EventObject<JTicTacToeGrid> {
        private final Grid.Location expectedCell;

        CellClickedEventObject(JTicTacToeGrid jTicTacToeGrid, Grid.Location expectedCell) {
            super(jTicTacToeGrid);
            this.expectedCell = expectedCell;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            CellClickedEventObject that = (CellClickedEventObject) o;

            return Objects.equals(expectedCell, that.expectedCell);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + Objects.hashCode(expectedCell);
            return result;
        }

        @Override
        public String toString() {
            return "CellClickedEventObject{" +
                    "source=" + getSource() +
                    "expectedCell=" + expectedCell +
                    '}';
        }
    }

    public Event<CellClickedEventObject> cellClicked() {
        return clickedEvent;
    }
}
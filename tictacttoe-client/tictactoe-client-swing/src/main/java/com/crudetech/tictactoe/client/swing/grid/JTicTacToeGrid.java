package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.Event;
import com.crudetech.event.EventListener;
import com.crudetech.event.EventObject;
import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class JTicTacToeGrid extends JComponent {
    private TicTacToeGridModel model;
    private EventSupport<CellClickedEventObject> clickedEvent = new EventSupport<>();
    private EventListener<TicTacToeGridModel.ChangedEventObject> modelChangedListener = new EventListener<TicTacToeGridModel.ChangedEventObject>() {
        @Override
        public void onEvent(TicTacToeGridModel.ChangedEventObject e) {
            repaint();
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
        setModel(model);
        updateUI();

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

    public JTicTacToeGrid(TicTacToeGridModel model, TicTacToeGridUI ui) {
        setModel(model);
        setUI(ui);
    }

    private void onMouseMoved(MouseEvent e) {
        GridCellHit hit = cellHitFromMouseEvent(e);
        if (hit.hasHit()) {
            getModel().highlightCell(hit.getHit());
        } else {
            getModel().unHighlightCell();
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
        if (hasModel()) {
            getModel().changed().removeListener(modelChangedListener);
        }
        model.changed().addListener(modelChangedListener);
        this.model = model;
    }

    private boolean hasModel() {
        return getModel() != null;
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
        private final Grid.Location clickedCellLocation;

        CellClickedEventObject(JTicTacToeGrid jTicTacToeGrid, Grid.Location clickedCellLocation) {
            super(jTicTacToeGrid);
            this.clickedCellLocation = clickedCellLocation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            CellClickedEventObject that = (CellClickedEventObject) o;

            return Objects.equals(clickedCellLocation, that.clickedCellLocation);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + Objects.hashCode(clickedCellLocation);
            return result;
        }

        @Override
        public String toString() {
            return "CellClickedEventObject{" +
                    "source=" + getSource() +
                    "clickedCellLocation=" + clickedCellLocation +
                    '}';
        }

        public Grid.Location getClickedCellLocation() {
            return clickedCellLocation;
        }
    }

    public Event<CellClickedEventObject> cellClicked() {
        return clickedEvent;
    }
}

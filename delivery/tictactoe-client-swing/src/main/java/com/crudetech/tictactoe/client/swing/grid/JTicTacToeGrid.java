package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.Event;
import com.crudetech.event.EventListener;
import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.ui.CellEventObject;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JTicTacToeGrid extends JComponent {
    private TicTacToeGridModel model;
    private EventSupport<CellEventObject<JTicTacToeGrid>> clickedEvent = new EventSupport<>();
    private EventListener<TicTacToeGridModel.CellsChangedEventObject> modelCellChangedListener;
    private EventListener<TicTacToeGridModel.ChangedEventObject> modelChangedListener;

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
        initializeListeners();
        setModel(model);
        updateUI();
    }

    public JTicTacToeGrid(TicTacToeGridModel model, TicTacToeGridUI ui) {
        initializeListeners();
        setModel(model);
        setUI(ui);
    }

    private void initializeListeners() {
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
        modelCellChangedListener = new EventListener<TicTacToeGridModel.CellsChangedEventObject>() {
            @Override
            public void onEvent(TicTacToeGridModel.CellsChangedEventObject e) {
                getUI().repaintCells(e.getChangedCells());
            }
        };
        modelChangedListener = new EventListener<TicTacToeGridModel.ChangedEventObject>() {
            @Override
            public void onEvent(TicTacToeGridModel.ChangedEventObject e) {
                getUI().repaintAll();
            }
        };
    }

    private void onMouseMoved(MouseEvent e) {
        GridCellHit hit = cellHitFromMouseEvent(e);
        if (hit.hasHit()) {
            getModel().highlightCell(hit.getHit());
        } else {
            getModel().unHighlightCell();
        }
    }

    private void onMouseClicked(MouseEvent e) {
        GridCellHit hit = cellHitFromMouseEvent(e);
        if (hit.hasHit()) {
            clickedEvent.fireEvent(new CellEventObject<JTicTacToeGrid>(this, hit.getHit()));
        }
    }

    private GridCellHit cellHitFromMouseEvent(MouseEvent e) {
        return getUI().checkGridCellHit(e.getX(), e.getY());
    }

    public void setModel(TicTacToeGridModel model) {
        if (hasModel()) {
            getModel().cellsChanged().removeListener(modelCellChangedListener);
            getModel().changed().removeListener(modelChangedListener);
        }
        model.cellsChanged().addListener(modelCellChangedListener);
        model.changed().addListener(modelChangedListener);
        this.model = model;
        repaint();
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

    public Event<CellEventObject<JTicTacToeGrid>> cellClicked() {
        return clickedEvent;
    }
}

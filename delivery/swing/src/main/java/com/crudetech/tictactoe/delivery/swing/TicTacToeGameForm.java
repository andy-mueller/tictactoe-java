package com.crudetech.tictactoe.delivery.swing;

import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridModel;
import com.crudetech.tictactoe.delivery.swing.grid.JTicTacToeGrid;
import com.crudetech.tictactoe.game.AlphaBetaPruningPlayer;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.NaiveTryAndErrorPlayer;
import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;
import com.crudetech.tictactoe.ui.UiPlayer;

/**
 *
 */
public class TicTacToeGameForm extends javax.swing.JFrame {
    private HumanVsComputerPlayerInteractor humanPlayerVsComputerInteractor;
    private static ComputerPlayer NullPlayer = new ComputerPlayer() {
        @Override
        protected Grid.Location computeNextMove(Grid actualGrid) {
            throw new IllegalStateException();
        }
    };

    /**
     * Creates new form TicTacToeGameForm
     */
    public TicTacToeGameForm() {
        initComponents();
        setSize(950, 1000);
        humanPlayerVsComputerInteractor = createInteractor(NullPlayer);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ticTacToesGridPane = new javax.swing.JScrollPane();
        ticTacToeGrid = new JTicTacToeGrid();
        mainMenu = new javax.swing.JMenuBar();
        javax.swing.JMenu gameMenu = new javax.swing.JMenu();
        newSimpleGameMenu = new javax.swing.JMenuItem();
        newAdvancedGameMenu = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/crudetech/tictactoe/delivery/swing/Bundle"); // NOI18N
        setTitle(bundle.getString("TicTacToeGameForm.title")); // NOI18N

        javax.swing.GroupLayout ticTacToeGridLayout = new javax.swing.GroupLayout(ticTacToeGrid);
        ticTacToeGrid.setLayout(ticTacToeGridLayout);
        ticTacToeGridLayout.setHorizontalGroup(
                ticTacToeGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 900, Short.MAX_VALUE)
        );
        ticTacToeGridLayout.setVerticalGroup(
                ticTacToeGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 800, Short.MAX_VALUE)
        );

        ticTacToesGridPane.setViewportView(ticTacToeGrid);

        gameMenu.setText(bundle.getString("TicTacToeGameForm.gameMenu.text")); // NOI18N

        newSimpleGameMenu.setText(bundle.getString("TicTacToeGameForm.newSimpleGameMenu.text")); // NOI18N
        newSimpleGameMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSimpleGameMenuActionPerformed(evt);
            }
        });
        gameMenu.add(newSimpleGameMenu);

        newAdvancedGameMenu.setText(bundle.getString("TicTacToeGameForm.newAdvancedGameMenu.text")); // NOI18N
        newAdvancedGameMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAdvancedGameMenuActionPerformed(evt);
            }
        });
        gameMenu.add(newAdvancedGameMenu);

        mainMenu.add(gameMenu);

        editMenu.setText(bundle.getString("TicTacToeGameForm.editMenu.text")); // NOI18N
        mainMenu.add(editMenu);

        helpMenu.setText(bundle.getString("TicTacToeGameForm.helpMenu.text")); // NOI18N

        aboutMenuItem.setText(bundle.getString("TicTacToeGameForm.aboutMenuItem.text")); // NOI18N
        helpMenu.add(aboutMenuItem);

        mainMenu.add(helpMenu);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ticTacToesGridPane, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ticTacToesGridPane, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newSimpleGameMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSimpleGameMenuActionPerformed
        startNewGameWithComputerOpponent(new NaiveTryAndErrorPlayer());
    }//GEN-LAST:event_newSimpleGameMenuActionPerformed

    private void newAdvancedGameMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAdvancedGameMenuActionPerformed
        ComputerPlayer alphaBetaPlayer = AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).asSecondPlayer();
        startNewGameWithComputerOpponent(alphaBetaPlayer);
    }//GEN-LAST:event_newAdvancedGameMenuActionPerformed

    private void startNewGameWithComputerOpponent(ComputerPlayer computerPlayer) {
        humanPlayerVsComputerInteractor.destroy();
        humanPlayerVsComputerInteractor = createInteractor(computerPlayer);

        humanPlayerVsComputerInteractor.startWithHumanPlayer(Grid.Mark.Cross);
    }

    private HumanVsComputerPlayerInteractor createInteractor(ComputerPlayer computerPlayer) {
        UiPlayer humanPlayer =
                new UiPlayer(new TicTacToeGridUiView(resetGridModel()),
                        new JOptionsPaneUiFeedback(this));

        return HumanVsComputerPlayerInteractor.builder()
                .setComputerPlayer(computerPlayer)
                .setHumanPlayer(humanPlayer)
                .setMadeMove(ticTacToeGrid.cellClicked())
                .build();
    }


    private TicTacToeGridModel resetGridModel() {
        TicTacToeGridModel newModel = new TicTacToeGridModel();
        ticTacToeGrid.setModel(newModel);
        return newModel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JMenuItem newAdvancedGameMenu;
    private javax.swing.JMenuItem newSimpleGameMenu;
    private JTicTacToeGrid ticTacToeGrid;
    private javax.swing.JScrollPane ticTacToesGridPane;
    // End of variables declaration//GEN-END:variables
}

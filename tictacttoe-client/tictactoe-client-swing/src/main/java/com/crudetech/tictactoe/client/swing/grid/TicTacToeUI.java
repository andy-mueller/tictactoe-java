package com.crudetech.tictactoe.client.swing.grid;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TicTacToeUI extends ComponentUI {
private BufferedImage grid;
private BufferedImage cross;
private BufferedImage nought;

    public TicTacToeUI() {
        grid = loadImage("/com/crudetech/tictactoe/client/swing/grid/brushstyle/tic-tac-toe-grid.jpg");
        cross = loadImage("/com/crudetech/tictactoe/client/swing/grid/brushstyle/tic-tac-toe-cross.jpg");
        nought = loadImage("/com/crudetech/tictactoe/client/swing/grid/brushstyle/tic-tac-toe-nought.jpg");
    }

    public static TicTacToeUI createUI(JComponent component) {
        return new TicTacToeUI();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setPaint(Color.ORANGE);
        g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
        g2d.drawImage(grid, null, 0,0);
        
        g2d.setPaint(Color.CYAN);
        
        
       // g2d.fillRect(55, 98, 215, 170);
        //g2d.fillRect(320, 98, 215, 170);
        //g2d.fillRect(600, 98, 215, 170);
        g2d.drawImage(cross, null, 55, 98);
        g2d.drawImage(nought, null, 320, 98);
        g2d.drawImage(cross, null, 600, 98);

        
//        g2d.fillRect(55, 310, 215, 170);
//        g2d.fillRect(320, 310, 215, 170);
//        g2d.fillRect(600, 310, 215, 170);
        g2d.drawImage(nought, null, 55, 310);
        g2d.drawImage(cross, null, 320, 310);
        g2d.drawImage(nought, null, 600, 310);

        
        
//        g2d.fillRect(55, 538, 215, 170);
//        g2d.fillRect(320, 538, 215, 170);
//        g2d.fillRect(600, 538, 215, 170);
        g2d.drawImage(cross, null, 55, 538);
        g2d.drawImage(nought, null, 320, 538);
        g2d.drawImage(cross, null, 600, 538);

        
        paint(g2d);
    }

    private void paint(Graphics2D g) {
    }

    private BufferedImage loadImage(String path) {
        try{
        try(InputStream imageStream = getClass().getResourceAsStream(path)){
         return ImageIO.read(imageStream);   
        }
        }
        catch(IOException e){
            throw new RuntimeException(e);
            
        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return new Dimension(this.grid.getWidth(), this.grid.getHeight());
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return getMinimumSize(c);
    }
    
}

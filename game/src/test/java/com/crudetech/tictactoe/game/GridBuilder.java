package com.crudetech.tictactoe.game;

public class GridBuilder {
    /**
     * Usage:
     * <pre>
     *     Grid expectedGridAfterInitialMove = gridOf("" +
     *                      "*|*|*" +
     *                      "*|*|*" +
     *                      "*|*|*");
     *
     * </pre>
     *
     * @param grid
     * @return
     */
    public static LinearRandomAccessGrid gridOf(String grid) {
        while (grid.contains("   ")) {
            grid = grid.replace("   ", " $ ");
        }
        while (grid.contains("|")) {
            grid = grid.replace("|", "");
        }
        while (grid.contains("---+---+---")) {
            grid = grid.replace("---+---+---", "");
        }
        while (grid.contains(" ")) {
            grid = grid.replace(" ", "");
        }
        Grid.Mark[] marks = new Grid.Mark[9];
        for (int i = 0; i < grid.length(); ++i) {
            marks[i] = markOf(grid.charAt(i));
        }
        return LinearRandomAccessGrid.of(marks);
    }

    private static Grid.Mark markOf(char c) {
        switch (Character.toLowerCase(c)) {
            case 'x':
                return Grid.Mark.Cross;
            case 'o':
                return Grid.Mark.Nought;
            default:
                return Grid.Mark.None;
        }
    }

}

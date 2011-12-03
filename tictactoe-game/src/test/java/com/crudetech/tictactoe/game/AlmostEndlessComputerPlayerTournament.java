package com.crudetech.tictactoe.game;

import java.io.PrintStream;
import java.util.Random;

public class AlmostEndlessComputerPlayerTournament {
    public static void main(String[] args) {
        AlmostEndlessComputerPlayerTournament app = new AlmostEndlessComputerPlayerTournament();
        app.battle(10, System.out);
    }

    private final static Random random = new Random();

    public void battle(int count, final PrintStream trace) {
        Statistic stat = new Statistic();
        while (count-- > 0) {
            AlphaBetaPruningPlayer firstPlayer = createTracingPlayer(trace);
            AlphaBetaPruningPlayer secondPlayer = new AlphaBetaPruningPlayer(Grid.Mark.Nought);


            TicTacToeGame game = new TicTacToeGame(firstPlayer, secondPlayer);
            firstPlayer.setGame(game);
            secondPlayer.setGame(game);

            Stopwatch sw = new Stopwatch();
            game.startWithPlayer(firstPlayer, Grid.Mark.Cross);
            stat.add(sw.elapsedMilis());
            trace.println(stat);
        }
    }

    private AlphaBetaPruningPlayer createTracingPlayer(final PrintStream trace) {
        return new AlphaBetaPruningPlayer(Grid.Mark.Cross) {
            boolean initialized = false;

            @Override
            public void yourTurn(Grid actualGrid) {
                if (initialized) {
                    super.yourTurn(actualGrid);
                    return;
                }
                Grid.Row row = Grid.Row.of(random.nextInt(3));
                Grid.Column col = Grid.Column.of(random.nextInt(3));
                initialized = true;
                game.addMark(this, Grid.Location.of(row, col));
            }

            @Override
            public void tie(Grid g) {
                StringBuilder b = new StringBuilder(50);
                b.append(cell(g, 0, 0)).append(" | ").append(cell(g, 0, 1)).append(" | ").append(cell(g, 0, 2)).append("\n");
                b.append("--+---+--").append("\n");
                b.append(cell(g, 1, 0)).append(" | ").append(cell(g, 1, 1)).append(" | ").append(cell(g, 1, 2)).append("\n");
                b.append("--+---+--").append("\n");
                b.append(cell(g, 2, 0)).append(" | ").append(cell(g, 2, 1)).append(" | ").append(cell(g, 2, 2)).append("\n");
                trace.println(b.toString());
            }

            private String cell(Grid g, int row, int col) {
                Grid.Mark m = g.getAt(Grid.Location.of(Grid.Row.of(row), Grid.Column.of(col)));
                if (m == Grid.Mark.Cross) return "x";
                if (m == Grid.Mark.Nought) return "o";
                return "-";
            }
        };
    }

    static class Stopwatch {

        private long start;

        Stopwatch() {
            start = System.currentTimeMillis();
        }

        long elapsedMilis() {
            return System.currentTimeMillis() - start;
        }
    }

    static class Statistic {
        private long sum;
        private long count;
        private long min = Long.MAX_VALUE;
        private long max = Long.MIN_VALUE;

        void add(long val) {
            sum += val;
            count++;
            max = Math.max(max, val);
            min = Math.min(min, val);
        }

        double average() {
            return sum / count;
        }

        double deviation() {
            return Math.sqrt(Math.pow((max - min), 2) / 12);
        }

        long min() {
            return min;
        }

        long max() {
            return max;
        }

        @Override
        public String toString() {
            return String.format("Statistic{count=%1$d, min=%2$d, max=%3$d, average=%4$5.2f, deviation=%5$5.2f}", count, min, max, average(), deviation());

        }
    }
}

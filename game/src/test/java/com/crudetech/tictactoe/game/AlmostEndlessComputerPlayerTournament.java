package com.crudetech.tictactoe.game;

import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.*;

public class AlmostEndlessComputerPlayerTournament {
    public static void main(String[] args) throws Exception {
        AlmostEndlessComputerPlayerTournament app = new AlmostEndlessComputerPlayerTournament();
        app.battle(5000, System.out);
    }

    private final static Random random = new Random();

    public void battle(int count, final PrintStream trace) throws Exception {
        Stopwatch all = new Stopwatch();
        final Statistic stat = new Statistic();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < count; ++i) {
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    AlphaBetaPruningPlayer firstPlayer = tracingAlphaBetaPruningPlayerBuilder(trace).withMark(Grid.Mark.Cross).asStartingPlayer();
                    AlphaBetaPruningPlayer secondPlayer = AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).asSecondPlayer();

                    HumanVsComputerPlayerInteractor interactor =
                            HumanVsComputerPlayerInteractor.builder()
                                    .setHumanPlayer(firstPlayer)
                                    .setComputerPlayer(secondPlayer)
                                    .build();

                    Stopwatch sw = new Stopwatch();
                    interactor.startWithHumanPlayer(Grid.Mark.Cross);
                    stat.add(sw.elapsedMilliseconds(), TimeUnit.MILLISECONDS);
                    trace.println(stat);
                }
            };

            completionService.submit(Executors.callable(runner, (Void) null));
        }

        for (int i = 0; i < count; ++i) {
            completionService.take().get();
        }

        trace.println("Finished in: " + all.elapsedMilliseconds() + " ms");
        executor.shutdown();
    }

    private AlphaBetaPruningPlayer.Builder tracingAlphaBetaPruningPlayerBuilder(final PrintStream trace) {
        return new AlphaBetaPruningPlayer.Builder() {
            @Override
            AlphaBetaPruningPlayer newPlayerInstance(Grid.Mark playersMark, GameTree.Player strategy, Grid.Mark startPlayersMark) {
                return new AlphaBetaPruningPlayer(Grid.Mark.Cross, GameTree.Player.Max, Grid.Mark.Cross) {
                    boolean notFirstMove = false;

                    @Override
                    public void yourTurn(Grid actualGrid) {
                        if (notFirstMove) {
                            super.yourTurn(actualGrid);
                        } else {
                            makeRandomFirstMove();
                        }
                    }

                    private void makeRandomFirstMove() {
                        notFirstMove = true;
                        makeMove(newRandomLocation());
                    }

                    private Grid.Location newRandomLocation() {
                        Grid.Row row = Grid.Row.of(random.nextInt(3));
                        Grid.Column col = Grid.Column.of(random.nextInt(3));
                        return Grid.Location.of(row, col);
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

                    @Override
                    public void youWin(Grid actualGrid, Grid.ThreeInARow triple) {
                        System.exit(100);
                        throw new UnsupportedOperationException("Win: Override not implemented!");
                    }

                    @Override
                    public void youLoose(Grid actualGrid, Grid.ThreeInARow triple) {
                        System.exit(100);
                        throw new UnsupportedOperationException("Override not implemented!");
                    }
                };
            }
        };
    }

    static class Stopwatch {

        private long start;

        Stopwatch() {
            start = System.currentTimeMillis();
        }

        long elapsedMilliseconds() {
            return System.currentTimeMillis() - start;
        }
    }

    static class Statistic {
        private long sum;
        private long count;
        private long min = Long.MAX_VALUE;
        private long max = Long.MIN_VALUE;

        synchronized void add(long time, TimeUnit unit) {
            time = unit.toMillis(time);
            sum += time;
            count++;
            max = Math.max(max, time);
            min = Math.min(min, time);
        }

        synchronized double average() {
            return sum / count;
        }

        synchronized double deviation() {
            return Math.sqrt(Math.pow((max - min), 2) / 12);
        }

        synchronized long min() {
            return min;
        }

        synchronized long max() {
            return max;
        }

        synchronized private long count() {
            return count;
        }

        @Override
        synchronized public String toString() {
            return String.format(
                    "Statistic{count=%1$d, min=%2$d, max=%3$d, average=%4$5.2f, deviation=%5$5.2f}",
                    count(), min(), max(), average(), deviation()
            );

        }
    }
}

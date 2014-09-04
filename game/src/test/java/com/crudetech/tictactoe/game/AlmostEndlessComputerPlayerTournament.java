package com.crudetech.tictactoe.game;

import com.crudetech.collections.Pair;
import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.*;

public class AlmostEndlessComputerPlayerTournament {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AlmostEndlessComputerPlayerTournament app = new AlmostEndlessComputerPlayerTournament(executor);
        app.battle(5000, System.out);
        executor.shutdown();
    }

    private final ExecutorService executor;

    public AlmostEndlessComputerPlayerTournament(ExecutorService executor) {
        this.executor = executor;
    }

    public void battle(int count, final PrintStream trace) throws Exception {
        Stopwatch overallElapsedTime = new Stopwatch();

        CompletionService<Pair<Grid, Duration>> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < count; ++i) {
            Callable<Pair<Grid, Duration>> oneMeasuredGame = measureTimeOf(playCompleteGame());
            completionService.submit(oneMeasuredGame);
        }

        final DurationOnlineStatistics stat = new DurationOnlineStatistics();
        for (int i = 0; i < count; ++i) {
            Pair<Grid, Duration> gameSample = completionService.take().get();
            //trace.println(gridToString(gameSample.getFirst()));
            stat.addSample(gameSample.getSecond());

            if (i % 50 == 0) {
                trace.println(i + " of " + count);
                trace.println(stat);
            }
        }

        trace.println("Finished in: " + overallElapsedTime.elapsed() + " ms");
        executor.shutdown();
    }

    private Callable<Grid> playCompleteGame() {
        final TracingAlphaBetaPruningPlayer firstPlayer = TracingAlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Cross).asStartingPlayer();
        AlphaBetaPruningPlayer secondPlayer = AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).asSecondPlayer();

        final HumanVsComputerPlayerInteractor interactor =
                HumanVsComputerPlayerInteractor.builder()
                        .setHumanPlayer(firstPlayer)
                        .setComputerPlayer(secondPlayer)
                        .build();

        return new Callable<Grid>() {
            @Override
            public Grid call() throws Exception {
                interactor.startWithHumanPlayer(Grid.Mark.Cross);
                return firstPlayer.grid;
            }
        };
    }

    private <T> Callable<Pair<T, Duration>> measureTimeOf(final Callable<T> play) {
        return new Callable<Pair<T, Duration>>() {
            @Override
            public Pair<T, Duration> call() throws Exception {
                Stopwatch sw = new Stopwatch();
                T result = play.call();
                return new Pair<>(result, sw.elapsed());
            }
        };
    }

    private String gridToString(Grid g) {
        StringBuilder b = new StringBuilder(50);
        b.append(cell(g, 0, 0)).append(" | ").append(cell(g, 0, 1)).append(" | ").append(cell(g, 0, 2)).append("\n");
        b.append("--+---+--").append("\n");
        b.append(cell(g, 1, 0)).append(" | ").append(cell(g, 1, 1)).append(" | ").append(cell(g, 1, 2)).append("\n");
        b.append("--+---+--").append("\n");
        b.append(cell(g, 2, 0)).append(" | ").append(cell(g, 2, 1)).append(" | ").append(cell(g, 2, 2)).append("\n");
        return b.toString();
    }

    private String cell(Grid g, int row, int col) {
        Grid.Mark m = g.getAt(Grid.Location.of(Grid.Row.of(row), Grid.Column.of(col)));
        if (m == Grid.Mark.Cross) return "x";
        if (m == Grid.Mark.Nought) return "o";
        return "-";
    }


    static class TracingAlphaBetaPruningPlayer extends AlphaBetaPruningPlayer {
        boolean notFirstMove = false;
        public Grid grid;
        private final static Random random = new Random();

        static class Builder extends AlphaBetaPruningPlayer.Builder {
            @Override
            TracingAlphaBetaPruningPlayer newPlayerInstance(Grid.Mark playersMark, GameTree.Player strategy, Grid.Mark startPlayersMark) {
                return new TracingAlphaBetaPruningPlayer(playersMark, strategy, startPlayersMark);
            }

            @Override
            public TracingAlphaBetaPruningPlayer.Builder withMark(Grid.Mark playersMark) {
                return (Builder) super.withMark(playersMark);
            }

            @Override
            public TracingAlphaBetaPruningPlayer asStartingPlayer() {
                return (TracingAlphaBetaPruningPlayer) super.asStartingPlayer();
            }
        }

        public static Builder builder() {
            return new Builder();
        }

        TracingAlphaBetaPruningPlayer(Grid.Mark playersMark, GameTree.Player playersStrategy, Grid.Mark startPlayersMark) {
            super(playersMark, playersStrategy, startPlayersMark);
        }

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
            this.grid = g;
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
    }

    static class Stopwatch {

        private final DateTime start;

        Stopwatch() {
            start = DateTime.now();
        }

        Duration elapsed() {
            return new Duration(start, DateTime.now().plus(Duration.millis(1)));
        }
    }

    static class OnlineStatistics {
        long n = 0;
        double mean = 0;
        double M2 = 0;
        private long min = Long.MAX_VALUE;
        private long max = Long.MIN_VALUE;

        synchronized void addSample(long x) {
            n = n + 1;
            final double delta = x - mean;
            mean = mean + delta / n;
            M2 = M2 + delta * (x - mean);

            min = Math.min(min, x);
            max = Math.max(max, x);
        }

        synchronized void removeSample(long x) {
            n = n - 1;
            final double delta = x - mean;
            mean = mean - delta / n;
            M2 = M2 - delta * (x - mean);
        }

        synchronized void updateSample(long oldX, long newX) {
            final double delta = newX - oldX;
            final double dold = oldX - mean;
            mean = mean + delta / n;
            final double dnew = newX - mean;
            M2 = M2 + delta * (dold + dnew);
        }

        synchronized double variance() {
            return M2 / (n - 1);
        }


        @Override
        synchronized public String toString() {
            return String.format(
                    "Statistic{count=%1$d, min=%2$d, max=%3$d, mean=%4$5.2f, deviation=%5$5.2f}",
                    count(), min(), max(), mean(), deviation()
            );
        }

        synchronized double deviation() {
            return Math.sqrt(variance());
        }

        synchronized double mean() {
            return mean;
        }

        synchronized long max() {
            return max;
        }

        synchronized long min() {
            return min;
        }

        synchronized long count() {
            return n;
        }
    }

    static class DurationOnlineStatistics {
        private final OnlineStatistics statistics = new OnlineStatistics();


        public void addSample(Duration duration) {
            statistics.addSample(duration.getMillis());
        }

        synchronized Duration variance() {
            return newRoundedDuration(statistics.variance());
        }


        @Override
        synchronized public String toString() {
            return String.format(
                    "DurationOnlineStatistics{count=%1$d, min=%2$s, max=%3$s, mean=%4$s, deviation=%5$s}",
                    count(), min(), max(), mean(), deviation()
            );
        }

        synchronized Duration deviation() {
            return newRoundedDuration(statistics.deviation());
        }

        synchronized Duration mean() {
            double mean = statistics.mean();
            return newRoundedDuration(mean);
        }

        private Duration newRoundedDuration(double duration) {
            return new Duration(Math.round(duration));
        }

        private Duration newRoundedDuration(long duration) {
            return new Duration(duration);
        }

        synchronized Duration max() {
            return newRoundedDuration(statistics.max());
        }

        synchronized Duration min() {
            return newRoundedDuration(statistics.min());
        }

        synchronized long count() {
            return statistics.count();
        }
    }
}

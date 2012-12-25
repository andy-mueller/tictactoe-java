package com.crudetech.tictactoe.delivery.text.cli;


import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CliApp {
    public static void main(String[] args) throws Exception {
        PrintWriter out = createSystemOutWriter();
        CliWindow window = new CliWindow(out, System.in);
        window.start();
    }

    private static PrintWriter createSystemOutWriter() {
        return new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
    }
}

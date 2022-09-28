package hk.ust.comp3021.tui;

import hk.ust.comp3021.actions.*;
import hk.ust.comp3021.game.InputEngine;
import hk.ust.comp3021.utils.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Scanner;

/**
 * An input engine that fetches actions from terminal input.
 */
public class TerminalInputEngine implements InputEngine {

    /**
     * The {@link Scanner} for reading input from the terminal.
     */
    private final Scanner terminalScanner;

    /**
     * @param terminalStream The stream to read terminal inputs.
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.terminalScanner = new Scanner(terminalStream);
    }

    /**
     * Fetch an action from user in terminal to process.
     *
     * @return the user action.
     */
    @Override
    public @NotNull Action fetchAction() {
        // This is an example showing how to read a line from the Scanner class.
        // Feel free to change it if you do not like it.
        final var inputLine = terminalScanner.nextLine();

        //
        // TODO
        switch (inputLine.toLowerCase()) {
            case "w": {
                return new Move.Up(0);
            }
            case "a": {
                return new Move.Left(0);
            }
            case "s": {
                return new Move.Down(0);
            }
            case "d": {
                return new Move.Right(0);
            }

            case "h": {
                return new Move.Up(1);
            }
            case "j": {
                return new Move.Left(1);
            }
            case "k": {
                return new Move.Down(1);
            }
            case "l": {
                return new Move.Right(1);
            }

            case "u": {
                return new Undo(0);
            }

            case "exit": {
                return new Exit(0);
            }
            case default: {
                return new InvalidInput(0, "Invalid Input");
            }
        }

    }
}


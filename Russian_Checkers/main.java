package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;
import static java.lang.Math.abs;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class Main {
    public static void main (String[] args) {
        /// Reading input:
        final Scanner scanner = new Scanner(System.in);

        final ArrayList<Position> whitePositions = new ArrayList<Position>();
        final ArrayList<Position> blackPositions = new ArrayList<Position>();
        final ArrayList<Move> moves = new ArrayList<Move>();

        try {
            for (final String pos : scanner.nextLine().split(" "))
                whitePositions.add(new Position(pos));

            for (final String pos : scanner.nextLine().split(" "))
                blackPositions.add(new Position(pos));

            while (scanner.hasNextLine())
                for (final String move : scanner.nextLine().split(" "))
                    moves.add(new Move(move));
        } catch (final CheckersException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        /// Preparing arrays for output:
        final ArrayList<Position> finalWhitePositions = new ArrayList<Position>();
        final ArrayList<Position> finalBlackPositions = new ArrayList<Position>();

        /// Creating an instance of engine and applying moves:
        try {
            Engine engine = new Engine(whitePositions, blackPositions);

            for (final Move move : moves)
                engine.makeMove(move);

            engine.getState(finalWhitePositions, finalBlackPositions);
        } catch (final CheckersException exception) {
            System.out.println(exception.getMessage());
            return;
        }
        /// Passed all possible exceptions here

        /// Writing output:
        final ArrayList<String> finalWhitePositionStrings = new ArrayList<String>();
        final ArrayList<String> finalBlackPositionStrings = new ArrayList<String>();

        for (final Position pos : finalWhitePositions)
            finalWhitePositionStrings.add(pos.toString());

        for (final Position pos : finalBlackPositions)
            finalBlackPositionStrings.add(pos.toString());

        Collections.sort(finalWhitePositionStrings);
        Collections.sort(finalBlackPositionStrings);
        System.out.println(String.join(" ", finalWhitePositionStrings));
        System.out.println(String.join(" ", finalBlackPositionStrings));
    }
}

class Engine {
    /**
     * 0 = empty
     * 1 = white man
     * 2 = black man
     * -1 = white king
     * -2 = black king
     */
    final private int[][] field;
    private boolean isWhiteMove;
    /**
     * 0 = game in progress
     * 1 = white won
     * -1 = black won
     */
    private int won;

    public Engine (final List<Position> whitePositions, final List<Position> blackPositions) throws CheckersException {
        isWhiteMove = true;
        field = new int[8][8];

        for (int i = 0; i < 8; i ++)
            for (int j = 0; j < 8; j ++)
                field[i][j] = 0;

        for (final Position whitePosition : whitePositions)
            field[whitePosition.getY()][whitePosition.getX()] = whitePosition.getIsCapital() ? -1 : 1;

        for (final Position blackPosition : blackPositions)
            field[blackPosition.getY()][blackPosition.getX()] = blackPosition.getIsCapital() ? -2 : 2;

        updateWonStatus();
    }

    public void getState (final List<Position> whitePositions, final List<Position> blackPositions) {
        for (int i = 0; i < 8; i ++)
            for (int j = 0; j < 8; j ++)
                switch (field[i][j]) {
                    case 0:
                        break;
                    case 1:
                        whitePositions.add(new Position(j, i, false));
                        break;
                    case 2:
                        blackPositions.add(new Position(j, i, false));
                        break;
                    case -1:
                        whitePositions.add(new Position(j, i, true));
                        break;
                    case -2:
                        blackPositions.add(new Position(j, i, true));
                        break;
                }
    }

    public void makeMove (final Move move) throws CheckersException {
        if (whiteWon() || blackWon())
            throw new CheckersException("error");

        for (int i = 0; i < move.getPositions().size() - 1; i ++) {
            final Position from = move.getPositions().get(i);
            final Position to = move.getPositions().get(i + 1);
            final int cellFrom = field[from.getY()][from.getX()];
            final int cellTo = field[to.getY()][to.getX()];

            if (
                /// Checking the order of players' moves
                    !(isWhiteMove ? isWhite(cellFrom) : isBlack(cellFrom)) ||
                            /// If the 'from' cell is capital, than we move king, man otherwise.
                            !(from.getIsCapital() ? isKing(cellFrom) : isMan(cellFrom))
            )
                throw new CheckersException("error");

            if (!isEmpty(cellTo))
                throw new CheckersException("busy cell");

            if (squareIsWhite(to))
                throw new CheckersException("white cell");

            if (!move.getIsCapture() && canMakeCapture())
                throw new CheckersException("invalid move");

            makeMiniMove(from, to, move.getIsCapture());
        }

        isWhiteMove = !isWhiteMove;
        updateWonStatus();
    }

    private boolean canMakeCapture () {
        for (int i = 0; i < 8; i ++)
            for (int j = 0; j < 8; j ++)
                if (
                        (isWhiteMove ? isWhite(field[i][j]) : isBlack(field[i][j])) &&
                                checkerAbleToCapture(i, j)
                )
                    return true;

        return false;
    }

    private boolean checkerAbleToCapture (final int i, final int j) {
        final int cellFrom = field[i][j];

        for (int dirX = -1; dirX <= 1; dirX += 2)
            for (int dirY = -1; dirY <= 1; dirY += 2) {
                int opponentY = i + dirY;
                int opponentX = j + dirX;

                if (isKing(cellFrom))
                    while (
                            isValidCell(opponentY, opponentX) &&
                                    isEmpty(field[opponentY][opponentX])
                    ) {
                        opponentY += dirY;
                        opponentX += dirX;
                    }

                final int emptyY = opponentY + dirY;
                final int emptyX = opponentX + dirX;

                if (
                        isValidCell(opponentY, opponentX) &&
                                isValidCell(emptyY, emptyX) &&
                                areOpponents(cellFrom, field[opponentY][opponentX]) &&
                                isEmpty(field[emptyY][emptyX])
                )
                    return true;
            }

        return false;
    }

    private boolean isValidCell (final int i, final int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

    private void makeMiniMove (
            final Position from,
            final Position to,
            final boolean isCapture
    ) throws CheckersException {
        final int dx = to.getX() - from.getX();
        final int dy = to.getY() - from.getY();
        final int xDir = dx / abs(dx);
        final int yDir = dy / abs(dy);
        final int cellFrom = field[from.getY()][from.getX()];
        final int minMoveDistance = isCapture ? 2 : 1;

        if (
                abs(dx) != abs(dy) ||
                        dx == 0 ||
                        !(isKing(cellFrom) ? abs(dx) >= minMoveDistance : abs(dx) == minMoveDistance)
        )
            throw new CheckersException("error");

        boolean captured = false;

        for (int i = 1; i < abs(dx); i ++) {
            final int currentCell = field[from.getY() + yDir * i][from.getX() + xDir * i];

            if (!isEmpty(currentCell)) {
                if (
                        !areOpponents(cellFrom, currentCell) ||
                                /// Invalid move, because we try to capture two pieces during only 1 mini move:
                                captured ||
                                /// Invalid move, because we are ready to capture, but the move isn't a capture
                                !isCapture
                )
                    throw new CheckersException("error");

                field[from.getY() + yDir * i][from.getX() + xDir * i] = 0;
                captured = true;
            }
        }

        field[from.getY()][from.getX()] = 0;
        field[to.getY()][to.getX()] =
                (to.getY() == (isWhite(cellFrom) ? 7 : 0)) ? /// Promoted
                        -abs(cellFrom) :
                        cellFrom;
    }

    private static boolean isEmpty (final int cell) { return cell == 0; }
    private static boolean isWhite (final int cell) { return abs(cell) == 1; }
    private static boolean isBlack (final int cell) { return abs(cell) == 2; }
    private static boolean isMan (final int cell) { return cell > 0; }
    private static boolean isKing (final int cell) { return cell < 0; }
    private static boolean squareIsWhite (final Position pos) { return (pos.getY() + pos.getX()) % 2 == 1; }
    private boolean areOpponents (final int firstCell, final int secondCell) {
        return isWhite(firstCell) ? isBlack(secondCell) : isWhite(secondCell);
    }

    private boolean whiteWon () { return won == 1; }
    private boolean blackWon () { return won == -1; }

    private void updateWonStatus () throws CheckersException {
        boolean foundWhite = false;
        boolean foundBlack = false;

        for (int i = 0; i < 8; i ++)
            for (int j = 0; j < 8; j ++) {
                if (isWhite(field[i][j]))
                    foundWhite = true;

                else if (isBlack(field[i][j]))
                    foundBlack = true;
            }

        if (!foundWhite && !foundBlack)
            throw new CheckersException("error");

        else if (!foundBlack)
            won = 1;

        else if (!foundWhite)
            won = -1;

        else
            won = 0;
    }
}

class Move {
    List<Position> positions;
    boolean isCapture;

    public Move (final String move) throws CheckersException {
        positions = new ArrayList<Position>();

        if (move.contains(":")) {
            for (final String pos : move.split(":"))
                positions.add(new Position(pos));

            isCapture = true;
        }

        else if (move.contains("-")) {
            for (final String pos : move.split("-"))
                positions.add(new Position(pos));

            if (positions.size() != 2)
                throw new CheckersException("error");

            isCapture = false;
        }

        else
            throw new CheckersException("error");
    }

    public List<Position> getPositions() { return positions; }
    public boolean getIsCapture () { return isCapture; }
}

class Position {
    private final int x;
    private final int y;
    private final boolean isCapital;

    public Position (final String str) throws CheckersException {
        if (str.length() != 2)
            throw new CheckersException("error");

        final String strLower = str.toLowerCase(Locale.ROOT);
        isCapital = !strLower.equals(str);
        x = strLower.charAt(0) - 'a';
        y = strLower.charAt(1) - '1';

        if (x < 0 || x >= 8 || y < 0 || y >= 8)
            throw new CheckersException("error");
    }

    public Position (final int x, final int y, final boolean isCapital) {
        this.x = x;
        this.y = y;
        this.isCapital = isCapital;
    }

    public String toString () {
        return String.format("%c%c", (isCapital ? 'A' : 'a') + x, '1' + y);
    }

    public int getX () { return x; }
    public int getY () { return y; }
    public boolean getIsCapital () { return isCapital; }
}

class CheckersException extends Exception {
    public CheckersException (final String message) {
        super(message);
    }
}

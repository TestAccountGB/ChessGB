package application;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import resourceBundle.exceptions.Exceptions;
import resourceBundle.messages.Messages;
import resourceBundle.pieces.Pieces;

public class Program {

	public static void main(String[] args) throws IOException {
		//I still have to organize everything, but the base is working
		Scanner scan = new Scanner(System.in);

		Path path = Paths.get(System.getProperty("user.home"), "Documents", "ChessGb", "ChessGbConfig.txt");

		Locale locale = UI.userLanguage(scan, path);

		Exceptions.bundle = ResourceBundle.getBundle("exceptionMessages", locale);
		Messages.bundle = ResourceBundle.getBundle("messages", locale);
		Pieces.bundle = ResourceBundle.getBundle("pieces", locale);

		while (true) {
			System.out.println("1) Play");
			System.out.println("2) Help (Not exists yet)");
			System.out.println("3) Language (Not exists yet)");
			System.out.println("4) Quit");
			String answer2 = scan.next().trim();

			switch (answer2) {
			case "1":
				game();
				break;
			case "2":

				break;
			case "3":

				break;
			case "4":
				System.exit(0);
			}
		}

	}

	private static void game() {
		Scanner scan = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();

		while (!chessMatch.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print(Messages.getString(Messages.source) + " ");
				ChessPosition source = UI.readChessPosition(scan);

				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				System.out.println();
				System.out.print(Messages.getString(Messages.target) + " ");
				ChessPosition target = UI.readChessPosition(scan);

				ChessPiece capturedPiece = chessMatch.performChessMove(scan, source, target);

				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}

			} catch (ChessException | InputMismatchException e) {
				System.out.println(e.getMessage());
				scan.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}
}

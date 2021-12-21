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
import resourceBundle.Exceptions;
import resourceBundle.Messages;
import resourceBundle.Pieces;

public class Program {

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);

		Path path = Paths.get(System.getProperty("user.home"), "Documents", "ChessGb", "ChessGbConfig.txt");
		Locale locale = UI.userLanguage(scan, path);

		Exceptions.bundle = ResourceBundle.getBundle("exceptionMessages", locale);
		Messages.bundle = ResourceBundle.getBundle("messages", locale);
		Pieces.bundle = ResourceBundle.getBundle("pieces", locale);
		
		while (true) {
			UI.clearScreen();
			System.out.println("1) " + UI.ANSI_WHITE + Messages.getString(Messages.play));
			System.out.println("2) " + Messages.getString(Messages.language));
			System.out.println("3) " + Messages.getString(Messages.quit) + UI.ANSI_RESET);
			String answer2 = scan.next().trim();
			UI.clearScreen();

			switch (answer2) {
			case "1":
				game();
				break;
			case "2":
				UI.changeLanguage(scan, path);
				break;
			case "3":
				System.exit(0);
				break;
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
			} catch (IllegalArgumentException e) {
				return;
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
		scan.nextLine();
	}
}

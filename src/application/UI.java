package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import resourceBundle.Exceptions;
import resourceBundle.Messages;
import resourceBundle.Pieces;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static ChessPosition readChessPosition(Scanner sc) throws IllegalArgumentException {
		String string = null;
		try {
			 string = sc.nextLine();
			char column = string.toLowerCase().charAt(0);
			int row = Integer.parseInt(string.substring(1));
			return new ChessPosition(column, row);
		} catch (RuntimeException e) {
			if(string.toUpperCase().equals("!SAIR"))
				throw new IllegalArgumentException();
			throw new InputMismatchException(Exceptions.getString(Exceptions.chessPositionError));
		}
	}

	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		printCapturedPieces(captured);
		System.out.println();
		System.out.println(Messages.getString(Messages.turn) + " " + chessMatch.getTurn());
		if (!chessMatch.getCheckMate()) {
			System.out.println(Messages.getString(Messages.waitingPlayer) + " " + chessMatch.getCurrentPlayerString());
			if (chessMatch.getCheck()) {
				System.out.println(Messages.getString(Messages.check));
			}
		} else {
			System.out.println(Messages.getString(Messages.checkmate));
			System.out.println(Messages.getString(Messages.winner) + " " + chessMatch.getCurrentPlayerString());
		}
	}

	public static void printBoard(ChessPiece[][] pieces) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	private static void printPiece(ChessPiece piece, boolean background) {
		if (background) {
			System.out.print(ANSI_BLUE_BACKGROUND);
		}
		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + piece + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}

	public static Locale userLanguage(Scanner scan, Path path) throws IOException {
		if (Files.notExists(path.getParent())) {
			Files.createDirectory(path.getParent());
		}
		if (Files.notExists(path)) {
			Files.createFile(path);
			return firstTime(scan, path);
		} else {
			return readUserLanguage(path);
		}
	}

	private static Locale readUserLanguage(Path path) throws IOException {
		String string;

		try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
			while ((string = reader.readLine()) != null) {
				if (string.matches("((?i)language=).*")) {
					String[] language = string.split("=");
					if (language.length == 2) {
						if (language[1].matches("(.+)_(.+)")) {
							String[] userLanguage = language[1].split("_");
							Locale userLocale = new Locale(userLanguage[0], userLanguage[1]);
							return userLocale;
						}
					}
				}
			}
			
			Files.setAttribute(path, "dos:readonly", false);
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
				System.out.println("Error reading previously chosen language. Setting the default language(en_US)...");
				writer.write("Language=en_US");
				Files.setAttribute(path, "dos:readonly", true);
				return new Locale("en", "US");
			}
		}
	}

	private static Locale firstTime(Scanner scan, Path path) throws IOException {
		System.out.println(UI.ANSI_WHITE + "Welcome :)");
		System.out.println("First, Let's choose a language" + UI.ANSI_RESET);
		
		return changeLanguage(scan, path);
	}
	
	private static Locale askLanguage(Scanner scan, BufferedWriter writer) throws IOException {
		
		while (true) {
			String answer = scan.next().trim();
			switch (answer) {
			case "1":
				writer.write("en_US");
				writer.flush();
				return new Locale("en", "US");
			case "2":
				writer.write("pt_BR");
				writer.flush();
				return new Locale("pt", "BR");
			default:
				System.out.println("Ivalid option. Try again");
			}
		}
	}
	
	public static Locale changeLanguage(Scanner scan, Path path) throws IOException {
		System.out.println(UI.ANSI_WHITE + "1- English");
		System.out.println("2- Portugues" + UI.ANSI_RESET);
		
		Files.setAttribute(path, "dos:readonly", false);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
			writer.write("Language=");
			
			Locale locale = askLanguage(scan, writer);
			
			Files.setAttribute(path, "dos:readonly", true);
			
			Exceptions.bundle = ResourceBundle.getBundle("exceptionMessages", locale);
			Messages.bundle = ResourceBundle.getBundle("messages", locale);
			Pieces.bundle = ResourceBundle.getBundle("pieces", locale);
			
			return locale;
		}
	}
	
	private static void printCapturedPieces(List<ChessPiece> captured) {
		List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE)
				.collect(Collectors.toList());
		List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK)
				.collect(Collectors.toList());
		System.out.println(Messages.getString(Messages.capturedPieces));
		System.out.print(Messages.getString(Messages.white) + ": ");
		System.out.print(ANSI_WHITE + white);
		System.out.println(ANSI_RESET);
		System.out.print(Messages.getString(Messages.black) + ": ");
		System.out.print(ANSI_YELLOW + black);
		System.out.println(ANSI_RESET);
	}

	public static String askPromotion(Scanner scan, ChessMatch match) {
		if (match.getPromoted() != null) {
			System.out.print(Messages.getString(Messages.piecePromotion) + " ");
			String type = scan.nextLine().toUpperCase();
			while (!type.equals(Pieces.getString(Pieces.bishop)) && !type.equals(Pieces.getString(Pieces.knight))
					&& !type.equals(Pieces.getString(Pieces.rook)) && !type.equals(Pieces.getString(Pieces.queen))) {
				System.out.print(Messages.getString(Messages.invalidValue) + " ");
				type = scan.nextLine().toUpperCase().trim();
			}
			return type;
		}
		throw new IllegalStateException(Exceptions.getString(Exceptions.typeError));
	}
	
}

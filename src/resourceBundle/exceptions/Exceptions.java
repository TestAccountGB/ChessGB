package resourceBundle.exceptions;

import java.util.ResourceBundle;

public class Exceptions {

	public static ResourceBundle bundle;

	public static String getString(String key) {
		return bundle.getString(key);
	}

	public static String chessPositionError = "chessPositionError";
	public static String typeError = "typeError";
	public static String boardError = "boardError";
	public static String positionError = "positionError";
	public static String existingPieceError = "existingPieceError";
	public static String instantiatingChessPositionError = "instantiatingChessPositionError";
	public static String yourselfInCheck = "yourselfInCheck";
	public static String castleError = "castleError";
	public static String prometedPieceError = "prometedPieceError";
	public static String noPieceInSourcePosition = "noPieceInSourcePosition";
	public static String chosenPieceError = "chosenPieceError";
	public static String possibleMovesError = "possibleMovesError";
	public static String targetPositionError = "targetPositionError";
	public static String kingError = "kingError";

}

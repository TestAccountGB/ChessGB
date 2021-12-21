package resourceBundle;

import java.util.ResourceBundle;

public class Messages {

	public static ResourceBundle bundle;

	public static String getString(String key) {
		return bundle.getString(key);
	}

	public static String turn = "turn";
	public static String waitingPlayer = "waitingPlayer";
	public static String winner = "winner";
	public static String check = "check";
	public static String checkmate = "checkmate";
	public static String capturedPieces = "capturedPieces";
	public static String white = "white";
	public static String black = "black";
	public static String piecePromotion = "piecePromotion";
	public static String invalidValue = "invalidValue";
	public static String source = "source";
	public static String target = "target";
	public static String play = "play";
	public static String language = "language";
	public static String quit = "quit";
	
}

package resourceBundle;

import java.util.ResourceBundle;

public class Pieces {

	public static ResourceBundle bundle;

	public static String getString(String key) {
		return bundle.getString(key);
	}

	public static String bishop = "bishop";
	public static String king = "king";
	public static String knight = "knight";
	public static String pawn = "pawn";
	public static String queen = "queen";
	public static String rook = "rook";

}

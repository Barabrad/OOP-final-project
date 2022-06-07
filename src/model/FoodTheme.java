/**
 * This enum will be used when the user is planning an event involving food
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

public enum FoodTheme {

	BREAKFAST("Breakfast"),
	LUNCH("Lunch"),
	DINNER("Dinner"),
	OTHER("Other"),
	NONE("None");

	private String printStr;
	
	private FoodTheme(String desc) {
		this.printStr = desc;
	}
	
	// Gets the number of enum options available
	public static int getNumOptions() {
		return FoodTheme.values().length;
	}
	
	// Gets the enum option associated with an index
	public static FoodTheme getOption(int num) {
		if (num < getNumOptions()) {
			return FoodTheme.values()[num];
		}
		else {
			return FoodTheme.NONE;
		}
	}
	
	// Returns the menu for this enum
	public static String getMenu() {
		String prompt = "What is the food theme?";

		for (FoodTheme ft : FoodTheme.values()) { // array from the enum
			prompt += "\n" + (ft.ordinal()) + ") " + ft;
		}
		
		return prompt;
	}

	@Override
	public String toString() {
		return this.printStr;
	}
}

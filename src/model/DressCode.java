/**
 * This enum will be used when the user decides the dress code for the event
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

public enum DressCode {

	FORMAL("Formal"),
	SEMI_FORMAL("Semi-Formal"),
	CASUAL("Casual"),
	OTHER("Other");

	private String printStr;
	
	private DressCode(String desc) {
		this.printStr = desc;
	}
	
	// Gets the number of enum options available
	public static int getNumOptions() {
		return DressCode.values().length;
	}

	// Gets the enum option associated with an index
	public static DressCode getOption(int num) {
		if (num < getNumOptions()) {
			return DressCode.values()[num];
		}
		else {
			return DressCode.OTHER;
		}
	}
	
	// Returns the menu for this enum
	public static String getMenu() {
		String prompt = "What is the dress code?";

		for (DressCode d : DressCode.values()) { // array from the enum
			prompt += "\n" + (d.ordinal()) + ") " + d;
		}
		
		return prompt;
	}

	@Override
	public String toString() {
		return this.printStr;
	}
}

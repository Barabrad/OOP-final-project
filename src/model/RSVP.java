/**
 * This enum will be used for keeping track of RSVPs
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

public enum RSVP {
	
	YES("Yes"),
	NO("No"),
	MAYBE("Maybe");

	private String printStr;
	
	private RSVP(String desc) {
		this.printStr = desc;
	}
	
	// Gets the number of enum options available
	public static int getNumOptions() {
		return RSVP.values().length;
	}
	
	// Gets the enum option associated with an index
	public static RSVP getOption(int num) {
		if (num < getNumOptions()) {
			return RSVP.values()[num];
		}
		else {
			return RSVP.MAYBE;
		}
	}
	
	// Gets the enum option associated with a String
	public static RSVP getOption(String s) {
		for (RSVP r : RSVP.values()) { // array from the enum
			if (s.equalsIgnoreCase(r.toString())) {
				return r;
			}
		}
		return MAYBE; // If none of the options matched
	}
	
	// Returns the menu for this enum
	public static String getMenu() {
		String prompt = "What is the dress code?";
		for (RSVP r : RSVP.values()) { // array from the enum
			prompt += "\n" + (r.ordinal()) + ") " + r;
		}
		return prompt;
	}

	@Override
	public String toString() {
		return this.printStr;
	}
}

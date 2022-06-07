/**
 * This class will create Celebration parties
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

import java.time.*;
import java.util.*;

public class Celebration extends Party implements Caterable {
	
	private String reason;
	private Collection<String> celebrants;
	private boolean isCatered;
	private String foodTheme;
	private boolean hasDessert;

	public Celebration(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS, LocalTime tE,
			String f, String r, Collection<String> c, boolean isC, String food, boolean hasD) {
		super(h, l, i, d, tS, tE, f);
		reason = r;
		celebrants = c;
		isCatered = isC;
		foodTheme = food;
		hasDessert = hasD;
	}

	public String getReason() {
		return reason;
	}
	
	public Collection<String> getCelebrants() {
		return celebrants;
	}
	
	// Gets the header (all or part of it) that will be used in the Party file
	public static String getHeader() {
		return Party.getHeader() + "reason" + D0 + "isCatered (true/false)" + D0 +
				"foodTheme" + D0 + "hasDessert (true/false)" + D0 + "celebrants (name_1" + D1 +
				"..." + D1 + "name_n" + D1 + ")" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString() + reason + D0 + isCatered + D0 + foodTheme + D0
				+ hasDessert + D0;
		if (!(celebrants.isEmpty())) {
			for (String c : celebrants) {
				info += c + D1;
			}
		}
		info += D0;
		if (this.getClass().getSimpleName().equals("Celebration")) {
			return "Celebration" + D0 + info;
		}
		else {
			return info;
		}
	}

	@Override
	public String toString() {
		String info = super.toString() + ", reason=" + reason + ", isCatered=" + isCatered;
		if (isCatered) {
			info += ", foodTheme=" + foodTheme;
		}
		info += ", hasDessert=" + hasDessert + ", celebrant(s)=" + celebrants;
		if (this.getClass().getSimpleName().equals("Celebration")) {
			return "Celebration [" + info + "]";
		}
		else {
			return info;
		}
	}

	@Override
	public boolean isCatered() {
		return isCatered;
	}

	@Override
	public String getFoodTheme() {
		return foodTheme;
	}

	@Override
	public boolean hasDessert() {
		return hasDessert;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(celebrants, reason, isCatered, foodTheme, hasDessert);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(super.equals(obj)))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Celebration other = (Celebration) obj;
		return Objects.equals(celebrants, other.celebrants) && Objects.equals(reason, other.reason)
				 && (isCatered == other.isCatered) && Objects.equals(foodTheme, other.foodTheme)
				 && (hasDessert == other.hasDessert);
	}

}

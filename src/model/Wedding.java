/**
 * This class will create Wedding parties
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

public class Wedding extends Celebration {
	
	private boolean hasEntertainment;
	private boolean childrenAllowed;

	public Wedding(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS, LocalTime tE,
			String f, String r, Collection<String> c, boolean isC, String food, boolean hasD,
			boolean hasE, boolean cAll) {
		super(h, l, i, d, tS, tE, f, r, c, isC, food, hasD);
		hasEntertainment = hasE;
		childrenAllowed = cAll;
	}
	
	// Since Celebration can be instantiated, we can use it to make a Wedding
	public Wedding(Celebration c, boolean hasE, boolean cAll) {
		this(c.getHostEmail(), c.getLocation(), c.getInvites(), c.getDate(), c.getTimeStart(),
				c.getTimeEnd(), c.getFormality(), c.getReason(), c.getCelebrants(), c.isCatered(),
				c.getFoodTheme(), c.hasDessert(), hasE, cAll);
	}
	
	// Getter for the entertainment boolean
	public boolean hasEntertainment() {
		return hasEntertainment;
	}
	
	// Getter for the children allowed boolean
	public boolean childrenAllowed() {
		return childrenAllowed;
	}

	// Gets the header that will be used in the Party file
	public static String getHeader() {
		return Celebration.getHeader() + "hasEntertainment (true/false)" + D0 +
				"childrenAllowed (true/false)";
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString() + hasEntertainment + D0 + childrenAllowed + D0;
		if (this.getClass().getSimpleName().equals("Wedding")) {
			return "Wedding" + D0 + info;
		}
		else { // Impossible for now, since nothing currently extends Wedding
			return info;
		}
	}
	
	@Override
	public String toString() {
		String info = super.toString() + ", hasEntertainment=" + hasEntertainment +
				", childrenAllowed=" + childrenAllowed;
		if (this.getClass().getSimpleName().equals("Wedding")) {
			return "Wedding [" + info + "]";
		}
		else { // Impossible for now, since nothing currently extends Wedding
			return info;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(hasEntertainment, childrenAllowed);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wedding other = (Wedding) obj;
		return (hasEntertainment == other.hasEntertainment) && (childrenAllowed == other.childrenAllowed);
	}
}

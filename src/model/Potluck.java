/**
 * This class will create Potluck parties
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

public class Potluck extends Gathering {
	
	private String theme;

	public Potluck(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS, LocalTime tE,
			String f, boolean n, String t) {
		super(h, l, i, d, tS, tE, f, n);
		theme = t;
	}
	
	// Since Gathering can be instantiated, we can use it to make a Potluck
	public Potluck(Gathering g, String t) {
		this(g.getHostEmail(), g.getLocation(), g.getInvites(), g.getDate(), g.getTimeStart(),
				g.getTimeEnd(), g.getFormality(), g.needsGifts(), t);
	}

	// Getter for theme
	public String getTheme() {
		return theme;
	}

	// Gets the header that will be used in the Party file
	public static String getHeader() {
		return Gathering.getHeader() + "theme" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString() + theme + D0;
		if (this.getClass().getSimpleName().equals("Potluck")) {
			return "Potluck" + D0 + info;
		}
		else { // Impossible for now, since nothing currently extends Potluck
			return info;
		}
	}

	@Override
	public String toString() {
		String info = super.toString() + ", theme=" + theme;
		if (this.getClass().getSimpleName().equals("Potluck")) {
			return "Potluck [" + info + "]";
		}
		else { // Impossible for now, since nothing currently extends Potluck
			return info;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(theme);
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
		Potluck other = (Potluck) obj;
		return theme.equals(other.theme);
	}
	
}

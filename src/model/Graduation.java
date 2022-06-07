/**
 * This class will create Graduation parties
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

public class Graduation extends Celebration {
	
	private String schoolPrev;
	private boolean willContSchool;
	private String schoolNext;

	public Graduation(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS, LocalTime tE,
			String f, String r, Collection<String> c, boolean isC, String food, boolean hasD, String prev,
			String next) {
		super(h, l, i, d, tS, tE, f, r, c, isC, food, hasD);
		schoolPrev = prev;
		schoolNext = next;
		willContSchool = !(schoolNext.equals("None"));
	}
	
	// Since Celebration can be instantiated, we can use it to make a Graduation
	public Graduation(Celebration c, String prev, String next) {
		this(c.getHostEmail(), c.getLocation(), c.getInvites(), c.getDate(), c.getTimeStart(),
				c.getTimeEnd(), c.getFormality(), c.getReason(), c.getCelebrants(), c.isCatered(),
				c.getFoodTheme(), c.hasDessert(), prev, next);
	}

	// Getter for previous school
	public String getSchoolPrev() {
		return schoolPrev;
	}

	// Getter for continuing boolean
	public boolean willContSchool() {
		return willContSchool;
	}

	// Getter for next school
	public String getSchoolNext() {
		return schoolNext;
	}

	// Gets the header that will be used in the Party file
	public static String getHeader() {
		return Celebration.getHeader() + "schoolPrev" + D0 + "schoolNext" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString() + schoolPrev + D0 + schoolNext + D0;
		if (this.getClass().getSimpleName().equals("Graduation")) {
			return "Graduation" + D0 + info;
		}
		else { // Impossible for now, since nothing currently extends Graduation
			return info;
		}
	}
	
	@Override
	public String toString() {
		String info = super.toString() + ", Graduating from " + schoolPrev;
		if (willContSchool) {
			info += ", Going to" + schoolNext;
		}
		if (this.getClass().getSimpleName().equals("Graduation")) {
			return "Graduation [" + info + "]";
		}
		else { // Impossible for now, since nothing currently extends Graduation
			return info;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(schoolNext, schoolPrev);
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
		Graduation other = (Graduation) obj;
		return Objects.equals(schoolNext, other.schoolNext) && Objects.equals(schoolPrev, other.schoolPrev);
	}
	
}

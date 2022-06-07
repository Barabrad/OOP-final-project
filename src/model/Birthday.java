/**
 * This class will create Birthday parties
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

public class Birthday extends Celebration {
	
	private String theme;
	private Collection<Integer> ages;

	public Birthday(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS, LocalTime tE,
			String f, String r, Collection<String> c, boolean isC, String food, boolean hasD, String t,
			Collection<Integer> a) {
		super(h, l, i, d, tS, tE, f, r, c, isC, food, hasD);
		theme = t;
		ages = a;
	}
	
	// Since Celebration can be instantiated, we can use it to make a Birthday
	public Birthday(Celebration c, String t, Collection<Integer> a) {
		this(c.getHostEmail(), c.getLocation(), c.getInvites(), c.getDate(), c.getTimeStart(),
				c.getTimeEnd(), c.getFormality(), c.getReason(), c.getCelebrants(), c.isCatered(),
				c.getFoodTheme(), c.hasDessert(), t, a);
	}

	// Getter for theme
	public String getTheme() {
		return theme;
	}

	// Getter for ages
	public Collection<Integer> getAges() {
		return ages;
	}
	
	// Gets the header that will be used in the Party file
	public static String getHeader() {
		return Celebration.getHeader() + "ages (int_1" + D1 + "..." + D1 + "int_n" + D1 + ")" + D0 +
				"theme" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString();
		if (!(ages.isEmpty())) {
			for (Integer a : ages) {
				info += a + D1;
			}
		}
		info += D0 + theme + D0;
		if (this.getClass().getSimpleName().equals("Birthday")) {
			return "Birthday" + D0 + info;
		}
		else { // Impossible for now, since nothing currently extends Birthday
			return info;
		}
	}

	@Override
	public String toString() {
		String info = super.toString() + ", ages=" + ages + ", theme=" + theme;
		if (this.getClass().getSimpleName().equals("Birthday")) {
			return "Birthday [" + info + "]";
		}
		else { // Impossible for now, since nothing currently extends Birthday
			return info;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(ages, theme);
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
		Birthday other = (Birthday) obj;
		return Objects.equals(ages, other.ages) && Objects.equals(theme, other.theme);
	}

}

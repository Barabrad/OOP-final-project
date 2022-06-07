/**
 * This class will create catered Gathering parties
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

public class CateredGathering extends Gathering implements Caterable {

	private boolean isCatered;
	private String foodTheme;
	private boolean hasDessert;
	
	public CateredGathering(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS,
			LocalTime tE, String f, boolean n, boolean isC, String food, boolean hasD) {
		super(h, l, i, d, tS, tE, f, n);
		isCatered = isC;
		foodTheme = food;
		hasDessert = hasD;
	}
	
	// Since Gathering can be instantiated, we can use it to make a CateredGathering
	public CateredGathering(Gathering g, boolean isC, String food, boolean hasD) {
		this(g.getHostEmail(), g.getLocation(), g.getInvites(), g.getDate(), g.getTimeStart(),
				g.getTimeEnd(), g.getFormality(), g.needsGifts(), isC, food, hasD);
	}
	
	// Gets the header that will be used in the Party file
	public static String getHeader() {
		return Gathering.getHeader() + "isCatered (true/false)" + D0 + "foodTheme" + D0 +
				"hasDessert (true/false)" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString() + isCatered + D0 + foodTheme + D0 + hasDessert + D0;
		if (this.getClass().getSimpleName().equals("CateredGathering")) {
			return "CateredGathering" + D0 + info;
		}
		else { // Impossible for now, since nothing currently extends CateredGathering
			return info;
		}
	}

	@Override
	public String toString() {
		String info = super.toString() + ", isCatered=" + isCatered;
		if (isCatered) {
			info += ", foodTheme=" + foodTheme;
		}
		info += ", hasDessert=" + hasDessert;
		if (this.getClass().getSimpleName().equals("CateredGathering")) {
			return "CateredGathering [" + info + "]";
		}
		else { // Impossible for now, since nothing currently extends CateredGathering
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
		result = prime * result + Objects.hash(foodTheme, hasDessert, isCatered);
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
		CateredGathering other = (CateredGathering) obj;
		return Objects.equals(foodTheme, other.foodTheme) && (hasDessert == other.hasDessert)
				&& (isCatered == other.isCatered);
	}

}

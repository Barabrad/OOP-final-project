/**
 * This class will create Gathering parties
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

public class Gathering extends Party {
	
	private boolean needsGifts;

	public Gathering(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS, LocalTime tE,
			String f, boolean n) {
		super(h, l, i, d, tS, tE, f);
		this.needsGifts = n;
	}

	// Getter for needsGifts
	public boolean needsGifts() {
		return needsGifts;
	}
	
	// Gets the header (all or part of it) that will be used in the Party file
	public static String getHeader() {
		return Party.getHeader() + "needsGifts (true/false)" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString() + needsGifts + D0;
		if (this.getClass().getSimpleName().equals("Gathering")) {
			return "Gathering" + D0 + info;
		}
		else {
			return info;
		}
	}

	@Override
	public String toString() {
		String info = super.toString() + ", needsGifts=" + needsGifts;
		if (this.getClass().getSimpleName().equals("Gathering")) {
			return "Gathering [" + info + "]";
		}
		else {
			return info;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(needsGifts);
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
		Gathering other = (Gathering) obj;
		return needsGifts == other.needsGifts;
	}

}

package model;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import viewController.DataReader;

public abstract class Party {
	
	public static final String D0 = DataReader.DELIM0;
	public static final String D1 = DataReader.DELIM1;
	public static final String DFORM = DataReader.DATE_FORM;
	public static final String TFORM = DataReader.TIME_FORM;
	
	private String hostEmail;
	private String location;
	private Map<String, RSVP> invites;
	private LocalDate date;
	private LocalTime timeStart;
	private LocalTime timeEnd;
	private String formality;

	public Party(String h, String l, Map<String, RSVP> i, LocalDate d, LocalTime tS, LocalTime tE,
			String f) {
		this.hostEmail = h;
		this.location = l;
		this.invites = i;
		this.date = d;
		this.timeStart = tS;
		this.timeEnd = tE;
		this.formality = f;
	}

	// Getter for hostEmail
	public String getHostEmail() {
		return hostEmail;
	}
	
	// Getter for location
	public String getLocation() {
		return location;
	}
	
	// Getter for invites
	public Map<String, RSVP> getInvites() {
		return invites;
	}
	
	// Getter for date
	public LocalDate getDate() {
		return date;
	}

	// Getter for the start time
	public LocalTime getTimeStart() {
		return timeStart;
	}

	// Getter for the end time
	public LocalTime getTimeEnd() {
		return timeEnd;
	}

	// Getter for formality
	public String getFormality() {
		return formality;
	}
	
	/**
	 * Sends an invitation to an email
	 * @param: an email
	 * @return: void
	 */
	public void invite(String email) {
		if (invites.containsKey(email)) {
			System.out.println("An invitation has already been sent to " + email);
		}
		else {
			this.invites.put(email, RSVP.MAYBE);
		}
	}
	
	/**
	 * Merges an invite Map with the Party's invite Map
	 * @param: an invite Map
	 * @return: void
	 */
	public void mergeInvites(Map<String, RSVP> invs) {
		for (String e : invs.keySet()) {
			if (invites.containsKey(e)) {
				invite(e);
			}
			else {
				updateRSVP(e, invs.get(e));
			}
		}
	}
	
	/**
	 * Updates the RSVP value for an email in the invite Map
	 * @param: an email and the new RSVP
	 * @return: void
	 */
	public void updateRSVP(String email, RSVP r) {
		invites.put(email, r);
	}
	
	/**
	 * Displays invitations sent for this Party by RSVP
	 * @param: a RSVP type
	 * @return: a boolean indicating whether or not there are any invitations in this RSVP category
	 */
	public boolean dispInvsByRSVP(RSVP r) {
		System.out.println("Printing emails with an RSVP of " + r + ":");
		Set<String> emails = invites.keySet();
		int i = 0;
		for (String e : emails) {
			if (invites.get(e) == r) {
				System.out.println(e);
				i++;
			}
		}
		return (i > 0);
	}
	
	// A wrapper method that runs the dispInvsByRSVP for all RSVP categories
	public void dispAllInvs() {
		dispInvsByRSVP(RSVP.YES);
		dispInvsByRSVP(RSVP.NO);
		dispInvsByRSVP(RSVP.MAYBE);
	}
	
	// Gets the header (all or part of it) that will be used in the Party file
	public static String getHeader() {
		return "hostEmail" + D0 + "location" + D0 + "invites (email_1" + D1 + "..." + D1 +
				"email_n" + D1 + ")" + D0 + "RSVPs (RSVP_1" + D1 + "..." + D1 + "RSVP_n" + D1 + 
				")" + D0 + "date (" + DFORM + ")" + D0 + "timeStart (" + TFORM + ")" + D0 + 
				"timeEnd (" + TFORM + ")" + D0 + "formality" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	public String toFileString() {
		DateTimeFormatter DF = DateTimeFormatter.ofPattern(DFORM);
		DateTimeFormatter TF = DateTimeFormatter.ofPattern(TFORM);
		String info = hostEmail + D0 + location + D0;
		if (!(invites.isEmpty())) {
			Set<String> guests = invites.keySet();
			for (String g : guests) {
				info += g + D1;
			}
			info += D0;
			Collection<RSVP> rsvps = invites.values();
			for (RSVP r : rsvps) {
				info += r.toString() + D1;
			}
		}
		else {
			info += D0;
		}
		info += D0 + DF.format(date) + D0 + TF.format(timeStart) + D0 + TF.format(timeEnd) + D0 +
				formality + D0;
		if (this.getClass().getSimpleName().equals("Party")) {
			return "Party" + D0 + info; // Should be impossible since Party is abstract
		}
		else {
			return info;
		}
	}

	@Override
	public String toString() {
		String info = "hostEmail=" + hostEmail + ", location=" + location + ", invites=" + invites + ", date=" + date
				+ ", timeStart=" + timeStart + ", timeEnd=" + timeEnd + ", formality=" + formality;
		if (this.getClass().getSimpleName().equals("Party")) {
			return "Party [" + info + "]"; // Should be impossible since Party is abstract
		}
		else {
			return info;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, formality, hostEmail, invites, location, timeEnd, timeStart);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Party))
			return false;
		Party other = (Party) obj;
		return Objects.equals(date, other.date) && (formality == other.formality)
				&& Objects.equals(hostEmail, other.hostEmail) && Objects.equals(location, other.location)
				&& Objects.equals(timeEnd, other.timeEnd) && Objects.equals(timeStart, other.timeStart);
		// the invites are not important in determining equality
	}
	
}
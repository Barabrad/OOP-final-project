/**
 * This abstract class will create the superclass of Member and NonMember objects
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

import java.util.*;

import viewController.DataReader;

public class User {
	
	public static final String D0 = DataReader.DELIM0;
	
	private String email;
	private Map<Party, RSVP> invites;
	
	public User(String e, Map<Party, RSVP> i) {
		this.email = e;
		this.invites = i;
	}

	// Getter for email
	public String getEmail() {
		return email;
	}
	
	// Getter for invites
	public Map<Party, RSVP> getInvites() {
		return invites;
	}
	
	/**
	 * Adds invitations that the user has received to the invitations Map
	 * @param: a Map of invitations (not the User's Map)
	 * @return: void
	 */
	public void addInvites(Map<Party, RSVP> invs) {
		for (Party p : invs.keySet()) {
			if (invites.containsKey(p)) {
				if (invites.get(p) != invs.get(p)) {
					invites.put(p, RSVP.MAYBE);
				}
			}
			else {
				invites.put(p, invs.get(p));
			}
		}
	}

	/**
	 * Displays invitations that the user has received by RSVP
	 * @param: a RSVP type
	 * @return: a boolean indicating whether or not there are any invitations in this RSVP category
	 */
	public boolean dispInvsByRSVP(RSVP r) {
		Set<Party> parties = invites.keySet();
		if (r == RSVP.YES) {
			System.out.println("Accepted invitations:\n");
		}
		else if (r == RSVP.NO) {
			System.out.println("Rejected invitations:\n");
		}
		else {
			System.out.println("Pending invitations:\n");
		}
		
		int i = 0; // Party index
		int j = 0; // Number in RSVP category
		for (Party p : parties) {
			if (invites.get(p) == r) {
				System.out.println(i + ": " + p + "\n");
				j++;
			}
			i++;
		}
		return (j > 0); // true if not blank
	}

	// A wrapper method that runs the dispInvsByRSVP for all RSVP categories
	public void dispAllInvs() {
		dispInvsByRSVP(RSVP.YES);
		dispInvsByRSVP(RSVP.NO);
		dispInvsByRSVP(RSVP.MAYBE);
	}
	
	/**
	 * Retrieves a Party from its index in the Party list
	 * @param: an index
	 * @return: the Party at that index (null if index is out of bounds)
	 */
	public Party getPartyFromIndex(int i) {
		Object[] parties = invites.keySet().toArray();
		if (i < parties.length) {
			return (Party)(parties[i]);
		}
		else {
			System.err.println("Your index was out of bounds.");
			return null;
		}
	}
	
	/**
	 * Updates a User's RSVP for a party
	 * @param: a Party and the RSVP for it
	 * @return: void
	 */
	public void rsvp(Party p, RSVP r) {
		invites.remove(p); // Clear out old party
		p.updateRSVP(email, r);
		invites.put(p, r);
		System.out.println("RSVP set to \"" + r + "\"");
	}

	// Gets the header (all or part of it) that will be used in the User subclass files
	public static String getHeader() {
		return "email" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	public String toFileString() {
		String info = email + D0;
		// Party info will be in another file
		if (this.getClass().getSimpleName().equals("User")) {
			return "User" + D0 + info; // Should be impossible since User is abstract
		}
		else {
			return info;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return this.email.equalsIgnoreCase(other.email); // email addresses are unique
	}
	
}

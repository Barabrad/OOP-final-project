/**
 * This class will create Member objects
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

import java.util.*;

public class Member extends User {

	private String name;
	private String password;
	private Set<Party> hostings;
	
	public Member(String email, Map<Party, RSVP> invs, String n, String p, Set<Party> h) {
		super(email, invs);
		name = n;
		password = p;
		hostings = h;
	}

	public Member(String n, String email, String p) {
		this(email, new HashMap<Party,RSVP>(), n, p, new HashSet<Party>());
	}

	// Getter for name
	public String getName() {
		return name;
	}

	// Getter for hostings
	public Set<Party> getHostings() {
		return hostings;
	}
	
	// Displays all hostings
	public void dispHostings() {
		System.out.println("Your hostings: ");
		int i = 0;
		for (Party p : hostings) {
			System.out.println(i + ": " + p + "\n");
			i++;
		}
	}
	
	/**
	 * Retrieves a hosting from its index in the Party list
	 * @param: an index
	 * @return: the Party at that index (null if index is out of bounds)
	 */
	public Party getHostingFromIndex(int i) {
		Object[] parties = hostings.toArray();
		if (i < parties.length) {
			return (Party)(parties[i]);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Retrieves a hosting from a Party that is considered equal
	 * This is used when a duplicate Party is being added
	 * @param: a Party
	 * @return: the Party in the User's list that is considered equal (null if not found)
	 */
	public Party getHostingFromParty(Party p) {
		for (Party p_i : hostings) {
			if (p_i.equals(p)) {
				return p_i;
			}
		}
		return null;
	}
	
	/**
	 * Acts as a getter for the password
	 * @param: a guess of the password (String)
	 * @return: true if the guess is correct
	 */
	public boolean checkPassword(String guess) {
		return password.equals(guess);
	}

	/**
	 * Allows the Member to change passwords
	 * @param: the old password, the new password
	 * @return: void
	 */
	public void changePassword(String pOld, String pNew) {
		if (pOld.equals(password)) {
			password = pNew;
			System.out.println("Password successfully changed.");
		}
		else {
			System.out.println("Current password was entered incorrectly. Password remains unchanged.");
		}
	}
	
	/**
	 * Acts a Party to the Member's hosting list
	 * @param: a Party
	 * @return: void
	 */
	public void addHosting(Party p1) {
		if (hostings.contains(p1)) {
			System.out.println("You are already hosting this party. Merging invitations...");
			Party p0 = getHostingFromParty(p1); // Since hostings contains p1, no need to check for null
			p0.mergeInvites(p1.getInvites());
			System.out.println("Merge successful.");
		}
		else {
			hostings.add(p1);
		}
	}
	
	// A wrapper method that calls addHosting multiple times to add a Set of parties to the list 
	public void addHostings(Set<Party> ps) {
		for (Party p : ps) {
			addHosting(p);
		}
	}
	
	/**
	 * Removes a party from the Member's hosting list
	 * @param: a Party
	 * @return: void
	 */
	public void cancelHosting(Party p1) {
		if (hostings.contains(p1)) {
			hostings.remove(p1);
		}
		else {
			System.out.println("You are not hosting this party.");
		}
	}
	
	// Gets the header that will be used in the Member file
	public static String getHeader() {
		return "name" + D0 + User.getHeader() + "password" + D0;
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = name + D0 + super.toFileString() + password + D0;
		if (this.getClass().getSimpleName().equals("Member")) {
			return "Member" + D0 + info;
		}
		else { // Impossible for now, since nothing currently extends Member
			return info;
		}
	}

	@Override
	public String toString() {
		return "Member [name=" + name + ", email=" + super.getEmail() + ", password=" + password + "]";
	}
	
}

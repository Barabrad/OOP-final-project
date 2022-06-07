/**
 * This class will create NonMember users
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

import java.util.*;

public class NonMember extends User {
	
	public NonMember(String email, Map<Party, RSVP> invs) {
		super(email, invs);
	}
	
	public NonMember(String email) {
		this(email, new HashMap<Party, RSVP>());
	}

	/**
	 * Makes a Member out of a NonMember
	 * @param: a name and a password
	 * @return: a Member
	 */
	public Member register(String name, String pass) {
		return new Member(super.getEmail(), super.getInvites(), name, pass, new HashSet<Party>());
	}
	
	// Gets the header that will be used in the NonMember file
	public static String getHeader() {
		return User.getHeader();
	}
	
	// Produces a String (that is compatible with the DataReader) to be printed in the files
	@Override
	public String toFileString() {
		String info = super.toFileString();
		if (this.getClass().getSimpleName().equals("NonMember")) {
			return "NonMember" + D0 + info;
		}
		else { // Impossible for now, since nothing currently extends NonMember
			return info;
		}
	}

	@Override
	public String toString() {
		return "NonMember [email=" + super.getEmail() + "]";
	}

}

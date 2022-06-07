/**
 * This class is responsible for reading in the data from the Member, NonMember, and Party files
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package viewController;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

import model.*;

public class DataReader {

	protected static final String M_FILE = "src/resources/members.txt";
	protected static final String NM_FILE = "src/resources/nonMembers.txt";
	protected static final String P_FILE = "src/resources/parties.txt";
	public static final String DELIM0 = ";"; // Main delimiter
	public static final String DELIM1 = ","; // Delimiter to use in list items separated by DELIM0
	public static final String DATE_FORM = "MM/dd/yyyy";
	public static final String TIME_FORM = "HH:mm";

	/**
	 * Converts a String of delimiter-separated values to a List of Integers
	 * @param: the String with the values, the delimiter, and the line number
	 * @return: a List of Integers with the String's contents
	 */
	public static List<Integer> string2dsl_int(String sDSL, String delim, int i) {
		List<Integer> dsl = new ArrayList<>();
		Scanner ds = new Scanner(sDSL);
		ds.useDelimiter(delim);
		if (!(sDSL.endsWith(delim))) {sDSL += delim;}
		while (ds.hasNext()) {
			try {
				dsl.add(ds.nextInt());
			}
			catch (InputMismatchException e) {
				System.err.println("There was an input mismatch when parsing delimiter-separated integers" + 
						" on line #" + i + ".");
				e.printStackTrace();
				ds.next(); // Dump faulty line
			}
		}
		ds.close();
		return dsl;
	}

	/**
	 * Converts a String of delimiter-separated values to a List of Strings
	 * @param: the String with the values, the delimiter, and the line number
	 * @return: a List of Strings with the String's contents
	 */
	public static List<String> string2dsl_str(String sDSL, String delim, int i) {
		List<String> dsl = new ArrayList<>();
		Scanner ds = new Scanner(sDSL);
		ds.useDelimiter(delim);
		if (!(sDSL.endsWith(delim))) {sDSL += delim;}
		while (ds.hasNext()) {
			try {
				dsl.add(ds.next());
			}
			catch (InputMismatchException e) {
				System.err.println("There was an input mismatch when parsing delimiter-separated Strings" + 
						" on line #" + i + ".");
				e.printStackTrace();
				ds.next(); // Dump faulty line
			}
		}
		ds.close();
		return dsl;
	}

	/**
	 * Reads the Member data from the Member file
	 * @param: none
	 * @return: a Map of the Members, with the emails as keys
	 */
	public static Map<String, Member> readMembers() {
		Map<String, Member> members = new HashMap<>();
		try (FileInputStream fis = new FileInputStream(M_FILE);
				Scanner sc = new Scanner(fis)) {
			// Header: "[Member];name;email;password"
			int i = 1; // We are on the first line
			while (sc.hasNextLine()) {
				try {
					String line = sc.nextLine();
					Scanner ls = new Scanner(line);
					ls.useDelimiter(DELIM0);
					String type = ls.next();
					if (type.equalsIgnoreCase("[Member]")) {
						// This is the header. Do nothing.
					}
					else if (type.equalsIgnoreCase("Member")) {
						Member m = parseMember(ls);
						members.put(m.getEmail(), m);
					}
					else {
						System.err.println("No Member detected on line #" + i + ". Moving to next line.");
					}
					ls.close();
				}
				catch (InputMismatchException e) {
					System.err.println("Member data on line #" + i + " corrupted. Moving to next line.");
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("The members file has not been found at " + M_FILE + ".");
			System.err.println("An empty Map will be returned for members.");
		} catch (IOException e) {
			System.err.println("Something went wrong while reading the Member file.");
			e.printStackTrace();
		}
		return members;
	}

	/**
	 * Reads the Member data from a Scanner of a line from the Member file
	 * @param: the Scanner of the line
	 * @return: a Member
	 */
	public static Member parseMember(Scanner ls) throws InputMismatchException {
		String name = ls.next();
		String email = ls.next().toLowerCase();
		String pass = ls.next();
		return new Member(name, email, pass);
	}

	/**
	 * Reads the NonMember data from the Member file
	 * @param: none
	 * @return: a Map of the NonMembers, with the emails as keys
	 */
	public static Map<String, NonMember> readNonMembers() {
		Map<String, NonMember> nonMembs = new HashMap<>();
		try (FileInputStream fis = new FileInputStream(NM_FILE);
				Scanner sc = new Scanner(fis)) {
			// Header: "[NonMember];email"
			int i = 1; // We are on the first line
			while (sc.hasNextLine()) {
				try {
					String line = sc.nextLine();
					Scanner ls = new Scanner(line);
					ls.useDelimiter(DELIM0);
					String type = ls.next();
					if (type.equalsIgnoreCase("[NonMember]")) {
						// This is the header. Do nothing.
					}
					else if (type.equalsIgnoreCase("NonMember")) {
						NonMember nm = parseNonMember(ls);
						nonMembs.put(nm.getEmail(), nm);
					}
					else {
						System.err.println("No NonMember detected on line #" + i +
								". Moving to next line.");
					}
					ls.close();
				}
				catch (InputMismatchException e) {
					System.err.println("Non-member data on line #" + i +
							" corrupted. Moving to next line.");
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("The non-members file has not been found at " + M_FILE + ".");
			System.err.println("An empty Map will be returned for non-members.");
		} catch (IOException e) {
			System.err.println("Something went wrong while reading the NonMember file.");
			e.printStackTrace();
		}
		// Check to see if there are any duplicate emails in the different lists
		checkForDups(nonMembs, readMembers());
		return nonMembs;
	}

	/**
	 * Reads the NonMember data from a Scanner of a line from the NonMember file
	 * @param: the Scanner of the line
	 * @return: a NonMember
	 */
	public static NonMember parseNonMember(Scanner ls) throws InputMismatchException {
		String email = ls.next().toLowerCase();
		return new NonMember(email);
	}
	
	/**
	 * Removes duplicates (User objects that are in both the NonMember and Member maps)
	 * @param: the NonMember and Member maps, respectively
	 * @return: void
	 */
	public static void checkForDups(Map<String, NonMember> nms, Map<String, Member> ms) {
		Set<String> nmEmails = nms.keySet(); // The key Set is linked to the Map
		for (String e : nmEmails) {
			if (ms.containsKey(e)) {
				ms.get(e).addInvites(nms.get(e).getInvites());
				nmEmails.remove(e);
			}
		}
	}
	
	/**
	 * Retrieves information from Party invitation lists that may not be in the User subclass files
	 * @param: the party list, the NonMember map, the Member map
	 * @return: void
	 */
	public static void mergeFromParties(Set<Party> parties, Map<String, NonMember> nms,
			Map<String, Member> ms) {
		for (Party p : parties) {
			Set<String> emails = p.getInvites().keySet();
			for (String e : emails) {
				Map<Party, RSVP> inv = new HashMap<>();
				inv.put(p, p.getInvites().get(e));
				if (!(nms.containsKey(e)) && !(ms.containsKey(e))) {
					nms.put(e, new NonMember(e, inv));
				}
				else if (nms.containsKey(e)) {
					NonMember nm = nms.get(e);
					nm.addInvites(inv);
				}
				else if (ms.containsKey(e)) {
					Member m = ms.get(e);
					m.addInvites(inv);
				}
			}
		}
		checkForDups(nms, ms);
	}

	/**
	 * Reads the Party data from the Party file
	 * @param: none
	 * @return: A set of the parties from the file
	 */
	public static Set<Party> readParties() {
		Set<Party> parties = new HashSet<>();
		try (FileInputStream fis = new FileInputStream(P_FILE);
				Scanner sc = new Scanner(fis)) {
			// Header: "PartyType;..."
			int i = 1; // We are on the first line
			while (sc.hasNextLine()) {
				try {
					String line = sc.nextLine();
					if (!(line.endsWith(DELIM0))) {line += DELIM0;}
					Scanner ls = new Scanner(line);
					ls.useDelimiter(DELIM0);
					Party p = parseParty(ls, i);
					if (p != null) {
						parties.add(p);
					}
					ls.close();
				}
				catch (InputMismatchException e) {
					System.err.println("Party data on line #" + i + " corrupted. Moving to next line.");
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("The parties file has not been found at " + P_FILE + ".");
			System.err.println("An empty Set will be returned for parties.");
		} catch (IOException e) {
			System.err.println("Something went wrong while reading the Party file.");
			e.printStackTrace();
		}
		return parties;
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: the Scanner of the line
	 * @return: a Party
	 */
	public static Party parseParty(Scanner ls, int i) throws InputMismatchException, IOException {
		Party p = null;
		String gTypes = "Gathering CateredGathering Potluck"; // Gathering types
		String cTypes = "Celebration Birthday Graduation Wedding"; // Celebration types
		// Party (abs.) info: hostEmail;location;guests(csv);RSVPs(csv);date;timeStart;timeEnd;formality
		String type = ls.next();
		if (type.startsWith("[") && type.endsWith("]")) {
			// This is a header. Do nothing.
		}
		else if (gTypes.contains(type) || cTypes.contains(type)){
			String email = ls.next().toLowerCase();
			String location = ls.next();
			List<String> guests = string2dsl_str(ls.next(),DELIM1,i);
			List<String> rsvps = string2dsl_str(ls.next(),DELIM1,i);
			String date = ls.next();
			String timeS = ls.next();
			String timeE = ls.next();
			String formality = ls.next();

			Map<String, RSVP> invites = parseInvites(guests, rsvps);

			DateTimeFormatter DF = DateTimeFormatter.ofPattern(DATE_FORM);
			DateTimeFormatter TF = DateTimeFormatter.ofPattern(TIME_FORM);

			try {
				LocalDate d = LocalDate.parse(date, DF);
				LocalTime tS = LocalTime.parse(timeS, TF);
				LocalTime tE = LocalTime.parse(timeE, TF);

				Gathering g = null;
				Celebration c = null;
				if (gTypes.contains(type)) {
					g = parseGathering(email, location, invites, d, tS, tE, formality, ls, i);
					// No need for null checks since parseGathering will always return a Gathering
				}
				else { // (cTypes.contains(type))
					c = parseCelebration(email, location, invites, d, tS, tE, formality, ls, i);
				}

				if (type.equalsIgnoreCase("Gathering")) {
					p = g;
				}
				else if (type.equalsIgnoreCase("CateredGathering")) {
					p = parseCatGathering(g, ls, i);
				}
				else if (type.equalsIgnoreCase("Potluck")) {
					p = parsePotluck(g, ls, i);
				}
				else if (type.equalsIgnoreCase("Celebration")) {
					p = c;
				}
				else if (type.equalsIgnoreCase("Birthday")) {
					p = parseBirthday(c, ls, i);
				}
				else if (type.equalsIgnoreCase("Graduation")) {
					p = parseGraduation(c, ls, i);
				}
				else if (type.equalsIgnoreCase("Wedding")) {
					p = parseWedding(c, ls, i);
				}

			}
			catch (DateTimeParseException e) {
				System.err.println("A date or time was not formatted properly on line #" + i +
						". Moving to next line.");
				e.printStackTrace();
			}

		}
		else {
			System.err.println("No party detected on line #" + i + ". Moving to next line.");
		}
		return p;
	}

	/**
	 * Recreates the invite Map (<String, RSVP>) from Lists of Strings
	 * This method assumes that the nth guest corresponds to the nth RSVP
	 * @param: a List of the guests (Strings), a List of the RSVPs (Strings)
	 * @return: the invite Map
	 */
	public static Map<String, RSVP> parseInvites(List<String> gList, List<String> rList) {
		Map<String, RSVP> invites = new HashMap<>();
		int max = Math.min(gList.size(), rList.size());
		for (int i = 0; i < max; i++) {
			invites.put(gList.get(i), RSVP.getOption(rList.get(i)));
		}
		return invites;
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: host email, location, invitation Map, date, start time, end time, formality,
	 * the Scanner of the line, and the line number
	 * @return: a Gathering
	 */
	public static Gathering parseGathering(String email, String loc, Map<String, RSVP> invs,
			LocalDate d, LocalTime tS, LocalTime tE, String form, Scanner ls, int i) {
		boolean gifts = false;
		try {
			gifts = ls.nextBoolean();
		}
		catch (InputMismatchException e) {
			System.err.println("Error in parsing Gathering data on line " + i +
					". Assuming that gifts aren't needed.");
		}
		return new Gathering(email, loc, invs, d, tS, tE, form, gifts);
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: a Gathering, the Scanner of the line, and the line number
	 * @return: a CateredGathering
	 */
	public static CateredGathering parseCatGathering(Gathering g, Scanner ls, int i) {
		boolean catered = false;
		String foodTheme = "None";
		boolean dessert = false;
		try {
			catered = ls.nextBoolean();
			foodTheme = ls.next();
			dessert = ls.nextBoolean();
		}
		catch (InputMismatchException e) {
			System.err.println("Error in parsing CateredGathering data on line #" + i +
					". Assuming no catering.");
		}
		return new CateredGathering(g, catered, foodTheme, dessert);
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: a Gathering, the Scanner of the line, and the line number
	 * @return: a Potluck
	 */
	public static Potluck parsePotluck(Gathering g, Scanner ls, int i) {
		String foodTheme = "None";
		try {
			foodTheme = ls.next();
		}
		catch (InputMismatchException e) {
			System.err.println("Error in parsing Potluck data on line #" + i +
					". Assuming no food theme.");
		}
		return new Potluck(g, foodTheme);
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: host email, location, invitation Map, date, start time, end time, formality,
	 * the Scanner of the line, and the line number
	 * @return: a Celebration
	 */
	public static Celebration parseCelebration(String email, String loc, Map<String, RSVP> invs,
			LocalDate d, LocalTime tS, LocalTime tE, String form, Scanner ls, int i) {
		String reason = "None";
		boolean catered = false;
		String foodTheme = "None";
		boolean dessert = false;
		Collection<String> celebrants = new ArrayList<>();
		try {
			reason = ls.next();
			catered = ls.nextBoolean();
			foodTheme = ls.next();
			dessert = ls.nextBoolean();
			celebrants = string2dsl_str(ls.next(),DELIM1,i);
		}
		catch (InputMismatchException e) {
			System.err.println("Error in parsing Celebration data on line #" + i +
					". Corrupted data set to null equivalents.");
		}
		return new Celebration(email, loc, invs, d, tS, tE, form, reason, celebrants, catered, foodTheme,
				dessert);
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: a Celebration, the Scanner of the line, and the line number
	 * @return: a Birthday
	 */
	public static Birthday parseBirthday(Celebration c, Scanner ls, int i) {
		String theme = "None";
		Collection<Integer> ages = new ArrayList<>();
		try {
			ages = string2dsl_int(ls.next(),DELIM1,i);
			theme = ls.next();
		}
		catch (InputMismatchException e) {
			System.err.println("Error in parsing Birthday data on line #" + i +
					": Ages will be an zero list, and there will be no theme.");
			for (int j = 0; j < c.getCelebrants().size(); j++) {
				ages.add(0);
			}
		}
		return new Birthday(c, theme, ages);
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: a Celebration, the Scanner of the line, and the line number
	 * @return: a Graduation
	 */
	public static Graduation parseGraduation(Celebration c, Scanner ls, int i) {
		String prev = "SchoolNotFound";
		String next = "None";
		try {
			prev = ls.next();
			next = ls.next();
		}
		catch (InputMismatchException e) {
			System.err.println("Error in parsing Graduation data on line #" + i +
					": School information not found.");
		}
		return new Graduation(c, prev, next);
	}

	/**
	 * Reads the Party data from a Scanner of a line from the Party file
	 * @param: a Celebration, the Scanner of the line, and the line number
	 * @return: a Wedding
	 */
	public static Wedding parseWedding(Celebration c, Scanner ls, int i) {
		boolean hasE = false;
		boolean cAll = false;
		try {
			hasE = ls.nextBoolean();
			cAll = ls.nextBoolean();
		}
		catch (InputMismatchException e) {
			System.err.println("Error in parsing Wedding data on line #" + i +
					". Assuming no entertainment, and no children allowed.");
		}
		return new Wedding(c, hasE, cAll);
	}

}

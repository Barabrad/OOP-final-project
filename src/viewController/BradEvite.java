/**
 * This class is the one that the user will interact with, and will save session data
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package viewController;

import java.io.*;
import java.time.*;
import java.util.*;

import model.*;

public class BradEvite {

	protected static final String M_F = DataReader.M_FILE;
	protected static final String NM_F = DataReader.NM_FILE;
	protected static final String P_F = DataReader.P_FILE;
	public static final String D0 = DataReader.DELIM0;
	public static final String D1 = DataReader.DELIM1;
	public static final String DFORM = DataReader.DATE_FORM;
	public static final String TFORM = DataReader.TIME_FORM;
	
	private Map<String, Member> memberMap;
	private Map<String, NonMember> nonMemberMap;
	private Set<Party> partyList;
	private Helper bff;
	private User user;
	
	public BradEvite() {
		memberMap = DataReader.readMembers();
		nonMemberMap = DataReader.readNonMembers();
		partyList = DataReader.readParties();
		bff = new Helper();
		user = null;
	}
	
	/**
	 * Gets the User associated with an email address
	 * @param: the email address in question
	 * @return: If a User has that email, the User; if no User has that email, null
	 */
	private User getUserFromEmail(String email) {
		if (memberMap.containsKey(email)) {
			Member m = memberMap.get(email);
			Map<Party, RSVP> invites = getInvitesFromEmail(email);
			Set<Party> hostings = getHostingsFromEmail(email);
			m.addInvites(invites);
			m.addHostings(hostings);
			return m;
		}
		else if (nonMemberMap.containsKey(email)) {
			NonMember nm = nonMemberMap.get(email);
			Map<Party, RSVP> invites = getInvitesFromEmail(email);
			nm.addInvites(invites);
			return nm;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Gets the hostings associated with an email address
	 * @param: the email address in question
	 * @return: A set with the parties hosted by the User with that email
	 */
	private Set<Party> getHostingsFromEmail(String email) {
		Set<Party> host = new HashSet<>();
		for (Party p : partyList) {
			if (p.getHostEmail().equals(email)) {
				host.add(p);
			}
		}
		return host;
	}
	
	/**
	 * Gets the invitations sent to an email address
	 * @param: the email address in question
	 * @return: A map with the parties that the email has received invitations for
	 */
	private Map<Party, RSVP> getInvitesFromEmail(String email) {
		Map<Party, RSVP> invs = new HashMap<>();
		for (Party p : partyList) {
			Map<String, RSVP> invites = p.getInvites();
			if (invites.containsKey(email)) {
				invs.put(p, invites.get(email));
			}
		}
		return invs;
	}
	
	/**
	 * Gets input that won't cause trouble for the DataReader later on
	 * @param: the prompt for the user
	 * @return: an answer from the user
	 */
	private String getSafeInput(String prompt) {
		String ans = bff.inputLine(prompt);
		while (ans.contains(D0) || ans.contains(D1)) {
			System.out.println("Your response cannot contain \"" + D0 + "\" or \"" + D1 + "\"");
			ans = bff.inputLine(prompt);
		}
		return ans;
	}
	
	/**
	 * Displays the menu for invitations, and interacts with the user
	 */
	private void invMenu() {
		if (user.getInvites().isEmpty()) {
			System.out.println("You currently have no invites.");
		}
		else {
			boolean notBlank = true;
			System.out.println("What would you like to do?");
			System.out.println("0) Display all invites");
			System.out.println("1) Display invites you RSVP'd \"Yes\" for");
			System.out.println("2) Display invites you RSVP'd \"No\" for");
			System.out.println("3) Display invites you RSVP'd \"Maybe\" for");
			int choice = bff.inputInt("Enter your choice: ", 0, 3);
			if (choice == 0) {user.dispAllInvs();}
			else if (choice == 1) {notBlank = user.dispInvsByRSVP(RSVP.YES);}
			else if (choice == 2) {notBlank = user.dispInvsByRSVP(RSVP.NO);}
			else {notBlank = user.dispInvsByRSVP(RSVP.MAYBE);}
			
			if (notBlank) {
				boolean update = bff.inputYesNo("Would you like to update an RSVP? ");
				if (update) {
					int inv = bff.inputInt("Enter the index of the invite you want to RSVP to: ", 0,
							user.getInvites().size());
					Party p = user.getPartyFromIndex(inv);
					System.out.println("Your current RSVP to Invitation #" + inv + " is " +
							user.getInvites().get(p));
					String uRSVP = bff.inputWord("What would you like to update the RSVP to? ",
							RSVP.YES.toString(), RSVP.NO.toString(), RSVP.MAYBE.toString());
					user.rsvp(p, RSVP.getOption(uRSVP));
				}
			}
			else {
				System.out.println("There are no invitations in that category.");
			}
		}
	}
	
	/**
	 * Displays the menu for non-members, and interacts with the user
	 * @return: a boolean that shows if the user did not choose to quit
	 */
	private boolean nmMenu() {
		System.out.println("0) Go to invites");
		System.out.println("1) Make an account");
		System.out.println("2) Save and quit");
		System.out.println("3) Quit without saving");
		int choice = bff.inputInt("Enter your choice: ", 0, 3);
		System.out.println("");
		boolean keepLooping = (choice < 2);
		
		if (choice == 0) {invMenu();}
		else if (choice == 1) {register();}
		else if (choice == 2) {quit(true);}
		else {quit(false);}
		
		return keepLooping;
	}
	
	/**
	 * Displays the menu for members, and interacts with the user
	 * @return: a boolean that shows if the user did not choose to quit
	 */
	private boolean mMenu() {
		System.out.println("0) Go to invites");
		System.out.println("1) Go to hostings");
		System.out.println("2) Create party");
		System.out.println("3) Change password");
		System.out.println("4) Save and quit");
		System.out.println("5) Quit without saving");
		int choice = bff.inputInt("Enter your choice: ", 0, 5);
		System.out.println("");
		boolean keepLooping = (choice < 4);
		
		if (choice == 0) {invMenu();}
		else if (choice == 1) {((Member)user).dispHostings();}
		else if (choice == 2) {createParty();}
		else if (choice == 3) {changePass();}
		else if (choice == 4) {quit(true);}
		else {quit(false);}
		
		return keepLooping;
	}
	
	/**
	 * Displays the menu, and interacts with the user
	 * @return: a boolean that shows if the user did not choose to quit
	 */
	private boolean dispMenu() {
		System.out.println("\nWhat would you like to do?");
		if (user instanceof Member) {return mMenu();}
		else {return nmMenu();}
	}
	
	/**
	 * Logs the user in, and sets the instance User to the user's User
	 */
	private void login(String email) {
		user = getUserFromEmail(email.toLowerCase());
		if (user == null) {
			System.out.println("You do not have an account, nor any invitations.");
			boolean createAcc = bff.inputYesNo("Would you like to create an account? ");
			if (createAcc) { // User wants to make an account
				user = new NonMember(email, getInvitesFromEmail(email));
				register();
			}
			// u will be null if the user doesn't want to create an account
		}
		else if (user instanceof Member) {
			String pass = getSafeInput("Enter your password: ");
			if (!(((Member)user).checkPassword(pass))) {
				System.out.println("Password incorrect.");
				user = null;
			}
		}
	}
	
	/**
	 * Allows the user to change the User's password
	 */
	private void changePass() {
		if (user instanceof Member) {
			String passN = getSafeInput("What would you like your new password to be? ");
			String passO = getSafeInput("To authenticate, enter your current password: ");
			((Member)user).changePassword(passO, passN);
		}
		else {
			System.out.println("Only members can have and change passwords.");
		}
	}
	
	/**
	 * Changes the User from a non-member to a member
	 */
	private void register() {
		if (user instanceof NonMember) {
			String name = getSafeInput("Enter your name: ");
			String pass = getSafeInput("Enter your password: ");
			Member m = ((NonMember)user).register(name, pass);
			memberMap.put(m.getEmail(), m);
			if (nonMemberMap.containsKey(m.getEmail())) {
				nonMemberMap.remove(m.getEmail(), (NonMember)user);
			}
			user = m;
		}
		else {
			System.out.println("Only non-members can register.");
		}
	}
	
	/**
	 * Gets the information necessary to make any Party subclass
	 * @return: a list containing the seven parameters that all Party subclasses have in common
	 */
	private List<Object> getPartyInfo() {
		List<Object> pInfo = new ArrayList<>(7);
		if (user instanceof Member) {
			//array holds {hostEmail,location,invites,date,timeStart,timeEnd,formality}
			String email = user.getEmail();
			String location = getSafeInput("Enter the party's location: ");
			Map<String, RSVP> invites = new HashMap<>();
			String guest = getSafeInput("Enter an email to invite (\"stop\" to stop): ").toLowerCase();
			while (!(guest.equals("stop"))) {
				if (guest.equalsIgnoreCase(email)) {
					System.out.println("Do you really have no friends?");
				}
				else if (!(invites.containsKey(guest))) {
					invites.put(guest, RSVP.MAYBE);
				}
				else {
					System.out.println("You already invited " + guest);
				}
				guest = getSafeInput("Enter an email to invite (\"stop\" to stop): ").toLowerCase();
			}
			System.out.println("Enter the date of the party:");
			LocalDate date = bff.getDate();
			System.out.println("Enter the start time of the party:");
			LocalTime tS = bff.getTime();
			System.out.println("Enter the end time of the party:");
			LocalTime tE = bff.getTime();
			System.out.println(DressCode.getMenu());
			int choice = bff.inputInt("Enter your choice: ", 0, DressCode.getNumOptions()-1);
			String formality = DressCode.getOption(choice).toString();
			if (DressCode.getOption(choice) == DressCode.OTHER) {
				formality = getSafeInput("Enter the dress code: ");
			}
			pInfo.add(email);
			pInfo.add(location);
			pInfo.add(invites);
			pInfo.add(date);
			pInfo.add(tS);
			pInfo.add(tE);
			pInfo.add(formality);
		}
		else {
			System.out.println("Only members can create parties.");
		}
		return pInfo;
	}
	
	/**
	 * Creates a Gathering after getting information from the user
	 * @return: a Gathering
	 */
	@SuppressWarnings("unchecked")
	private Gathering createGathering() {
		Gathering g = null;
		System.out.println("Choose your gathering subtype:");
		System.out.println("0) Catered Gathering");
		System.out.println("1) Potluck");
		System.out.println("2) Other");
		System.out.println("3) Cancel");
		int choice = bff.inputInt("Enter your choice: ", 0, 3);
		if (choice != 3) {
			List<Object> pI = getPartyInfo();
			boolean gifts = bff.inputYesNo("Are gifts needed? ");
			try {
				String email = (String)(pI.get(0));
				String location = (String)(pI.get(1));
				Map<String,RSVP> invites = (Map<String,RSVP>)(pI.get(2));
				LocalDate date = (LocalDate)(pI.get(3));
				LocalTime tS = (LocalTime)(pI.get(4));
				LocalTime tE = (LocalTime)(pI.get(5));
				String formality = (String)(pI.get(6));
				g = new Gathering(email, location, invites, date, tS, tE, formality, gifts);
				if (choice == 0) {
					boolean catered = true;
					System.out.println(FoodTheme.getMenu());
					int choice2 = bff.inputInt("Enter your choice: ", 0, FoodTheme.getNumOptions()-1);
					String food = FoodTheme.getOption(choice2).toString();
					if (FoodTheme.getOption(choice2) == FoodTheme.OTHER) {
						food = getSafeInput("Enter the food theme: ");
					}
					if (food.equalsIgnoreCase("None")) {
						catered = false;
					}
					boolean hasD = bff.inputYesNo("Is there dessert? ");
					g = new CateredGathering(g, catered, food, hasD);
					System.out.println("\nCatered Gathering created!");
				}
				else if (choice == 1) {
					System.out.println(FoodTheme.getMenu());
					int choice2 = bff.inputInt("Enter your choice: ", 0, FoodTheme.getNumOptions()-1);
					String food = FoodTheme.getOption(choice2).toString();
					if (FoodTheme.getOption(choice2) == FoodTheme.OTHER) {
						food = getSafeInput("Enter the food theme: ");
					}
					g = new Potluck(g, food);
					System.out.println("\nPotluck created!");
				}
				else {
					System.out.println("\nGathering created!");
				}
			}
			catch (ClassCastException e) {
				System.out.println("Something went wrong when making a gathering");
				e.printStackTrace();
			}
		}
		return g;
	}
	
	/**
	 * Creates a Celebration after getting information from the user
	 * @return: a Celebration
	 */
	@SuppressWarnings("unchecked")
	private Celebration createCelebration() {
		Celebration c = null;
		String[] reasons = {"Birthday","Graduation","Wedding","Other","Cancel"};
		System.out.println("Choose your celebration subtype:");
		for (int i = 0; i < reasons.length; i++) {
			System.out.println(i + ") " + reasons[i]);
		}
		int choice = bff.inputInt("Enter your choice: ", 0, reasons.length - 1);
		String reason = reasons[choice];
		if (reason.equals("Other")) {
			reason = getSafeInput("What is the occasion for this celebration? ");
		}
		if (choice != 4) {
			List<Object> pI = getPartyInfo();
			Collection<String> celebs = new ArrayList<>();
			String guest = getSafeInput("Who is being celebrated (\"stop\" to stop)? ");
			while (!(guest.equals("stop"))) {
				celebs.add(guest);
				guest = getSafeInput("Who is being celebrated (\"stop\" to stop)? ");
			}
			boolean catered = true;
			System.out.println(FoodTheme.getMenu());
			int choice2 = bff.inputInt("Enter your choice: ", 0, FoodTheme.getNumOptions()-1);
			String food = FoodTheme.getOption(choice2).toString();
			if (FoodTheme.getOption(choice2) == FoodTheme.OTHER) {
				food = getSafeInput("Enter the food theme: ");
			}
			if (food.equalsIgnoreCase("None")) {
				catered = false;
			}
			boolean hasD = bff.inputYesNo("Is there dessert? ");
			try {
				String email = (String)(pI.get(0));
				String location = (String)(pI.get(1));
				Map<String,RSVP> invites = (Map<String,RSVP>)(pI.get(2));
				LocalDate date = (LocalDate)(pI.get(3));
				LocalTime tS = (LocalTime)(pI.get(4));
				LocalTime tE = (LocalTime)(pI.get(5));
				String formality = (String)(pI.get(6));
				c = new Celebration(email, location, invites, date, tS, tE, formality, reason, celebs,
						catered, food, hasD);
				if (choice == 0) {
					String theme = getSafeInput("Enter the birthday theme: ");
					Collection<Integer> ages = new ArrayList<>(celebs.size());
					for (String celeb : celebs) {
						String prompt = "How old is " + celeb + " turning? ";
						int a = bff.inputInt(prompt, 0, 1000); // It needs to be a positive number
						ages.add(a);
					}
					c = new Birthday(c, theme, ages);
					System.out.println("\nBirthday created!");
				}
				else if (choice == 1) {
					String prev = getSafeInput("Which school did the celebrant finish? ");
					boolean cont = bff.inputYesNo("Will the celebrant start at another school?");
					String next = "None";
					if (cont) {
						next = getSafeInput("Which school will the celebrant start " +
								"(\"None\" if not applicable)? ");
					}
					c = new Graduation(c, prev, next);
					System.out.println("\nGraduation created!");
				}
				else if (choice == 2) {
					boolean ent = bff.inputYesNo("Will there be entertainment? ");
					boolean child = bff.inputYesNo("Will children be allowed? ");
					c = new Wedding(c, ent, child);
					System.out.println("\nWedding created!");
				}
				else {
					System.out.println("\nCelebration created!");
				}
			}
			catch (ClassCastException e) {
				System.out.println("Something went wrong when making a celebration");
				e.printStackTrace();
			}
		}
		return c;
	}
	
	/**
	 * Creates a Party after getting information from the user, and then adds it to the system's records
	 */
	private void createParty() {
		if (user instanceof Member) {
			Party p = null;
			System.out.println("Choose your party type:");
			System.out.println("0) Gathering");
			System.out.println("1) Celebration");
			System.out.println("2) Cancel");
			int choice = bff.inputInt("Enter your choice: ", 0, 2);
			if (choice != 2) {
				if (choice == 0) {p = createGathering();}
				else {p = createCelebration();}
			}
			if (p != null) {partyList.add(p); ((Member)user).addHosting(p);}
		}
		else {
			System.out.println("Only members can create parties.");
		}
	}
	
	/**
	 * A wrapper method that creates a file if it does not already exist
	 */
	private void checkForFile(File f) {
		try {
			f.createNewFile(); // creates a file only if it does not exist
		}
		catch (SecurityException e1) {
			System.err.println("Permission denied for altering " + f.getPath());
			e1.printStackTrace();
		}
		catch (IOException e1) {
			System.err.println("An IO Problem happened while trying to find " + f.getPath());
			e1.printStackTrace();
		}
	}
	
	/**
	 * Saves all Member data to the file specified in DataReader
	 */
	private void saveMFile() {
		File mFile = new File(M_F);
		checkForFile(mFile);
		try (FileOutputStream fos = new FileOutputStream(mFile);
				PrintWriter out = new PrintWriter(fos)){
			// Write each Member to the file
			out.println("[Member]" + D0 + Member.getHeader());
			for (Member m: memberMap.values()) {
				out.println(m.toFileString());
			}
			System.out.println("Saved members to " + M_F);
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found, problem saving data to " + M_F);
		}
		catch (IOException e) {
			System.err.println("Some other IO Problem happened while saving data to " + M_F);
			e.printStackTrace();
		} // Files are closed automatically during try-with-resource
	}
	
	/**
	 * Saves all NonMember data to the file specified in DataReader
	 */
	private void saveNMFile() {
		File nmFile = new File(NM_F);
		checkForFile(nmFile);
		try (FileOutputStream fos = new FileOutputStream(nmFile);
				PrintWriter out = new PrintWriter(fos)){
			// Write each NonMember to the file
			out.println("[NonMember]" + D0 + NonMember.getHeader());
			for (NonMember nm: nonMemberMap.values()) {
				out.println(nm.toFileString());
			}
			System.out.println("Saved non-members to " + NM_F);
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found, problem saving data to " + NM_F);
		}
		catch (IOException e) {
			System.err.println("Some other IO Problem happened while saving data to " + NM_F);
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves all Party data to the file specified in DataReader
	 */
	private void savePFile() {
		File pFile = new File(P_F);
		checkForFile(pFile);
		try (FileOutputStream fos = new FileOutputStream(pFile);
				PrintWriter out = new PrintWriter(fos)){
			// want to write each Party to the file
			out.println("[Gathering]" + D0 + Gathering.getHeader());
			out.println("[CateredGathering]" + D0 + CateredGathering.getHeader());
			out.println("[Potluck]" + D0 + Potluck.getHeader());
			out.println("[Celebration]" + D0 + Celebration.getHeader());
			out.println("[Birthday]" + D0 + Birthday.getHeader());
			out.println("[Graduation]" + D0 + Graduation.getHeader());
			out.println("[Wedding]" + D0 + Wedding.getHeader());
			for (Party p: partyList) {
				out.println(p.toFileString());
			}
			System.out.println("Saved parties to " + P_F);
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found, problem saving data to " + P_F);
		}
		catch (IOException e) {
			System.err.println("Some other IO Problem happened while saving data to " + P_F);
			e.printStackTrace();
		}
	}
	
	/**
	 * Responsible for exiting out of the main loop
	 * @param: a boolean that determines whether or not data will be saved upon quitting
	 * @return: a boolean (false) that will allow the user to exit the main loop
	 */
	private boolean quit(boolean save) {
		System.out.println("Goodbye!");
		if (save) {
			DataReader.mergeFromParties(partyList, nonMemberMap, memberMap);
			saveMFile();
			saveNMFile();
			savePFile();
			System.out.println("\n(Files saved successfully)");
		}
		return false; // This will be put into the loop variable
	}
	
	/**
	 * Runs the program, and contains the main loop
	 */
	private void run() {
		System.out.println("Welcome to Brad's Party Planner!\n");
		String email = getSafeInput("Enter your email: ");
		login(email);
		boolean loop = (user != null); // if loop == false, the unregistered user didn't to make an account
		if (!loop) {
			quit(false);
		}
		while (loop) {
			loop = dispMenu();
		}
	}
	
	/**
	 * The main method
	 */
	public static void main(String[] args) {
		BradEvite be = new BradEvite();
		be.run();
	}
	
}

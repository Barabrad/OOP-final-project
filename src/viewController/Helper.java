/**
 * This class is meant to serve ITP 265 students as a help for getting input and error checking on input, but
 * may also be used as a general purpose class to store methods which may be needed across lots of projects.
 *
 * @author Kendra Walther and Spring 2022 students
 * @version Spring 2022
 */

package viewController;

import java.time.*;
import java.util.*;

public class Helper{
	private Scanner sc; //declare

	public Helper() {
		sc = new Scanner(System.in); // initialize
	}
	
	/**
	 * Prompt the user and read one word of text as a String
	 * @param prompt: the question to ask the user
	 * @return: a one word String - if the user enters multiple words, all other input until the return will be discarded.
	 */
	public String inputWord(String prompt) {
		System.out.print(prompt);
		String word = sc.next();
		sc.nextLine(); // remove any "garbage" (like extra whitespace or the return key) after the one word that is read in
		return word;
	}
	
	/**
	 * Prompt the user and read one line of text as a String
	 * @param prompt: the question to ask the user
	 * @return: a line of user input (including spaces, until they hit enter)
	 */
	public String inputLine(String prompt) {
		System.out.print(prompt);
		return sc.nextLine();
	}

	/**
	 * Prompt the user and read one word of text as a String, returns a String that matches
	 * one of the allowed words passed in as parameters (case sensitive)
	 * @param prompt: the question to ask the user
	 * @param matches: words the input is allowed to be
	 * @return: a one-word String that matches one of the allowed words (case-sensitive)
	 */
	public String inputWord(String prompt, String... matches) {
		String word = inputWord(prompt);
		while (!match(word, matches)) {
			String msg = word + " was not one of the expected words (case-sensitive)." +
					"\nAllowed options are: ";
			for (int i = 0; i < matches.length; i++) {
				msg += matches[i];
				if (i < matches.length - 1) {msg += ", ";}
			}
			System.out.println(msg);
			word = inputWord(prompt);
		}
		return word;
	}

	public boolean match(String word, String[] matches) {
		boolean found = false;
		for (String s: matches) {
			if (s.equals(word)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Prompt the user and read an int, clearing whitespace or the enter after the number
	 * @param prompt: the question to ask the user 
	 * @return: an int 
	 */
	public int inputInt(String prompt) {
		System.out.print(prompt);
		while (!sc.hasNextInt()) {
			String garbage = sc.nextLine(); // grab the "bad data"
			System.out.println(garbage + " was not an int.");
			System.out.print(prompt);
		} //here, we know an int is waiting on System.in
		int num = sc.nextInt(); // grab the number
		sc.nextLine();
		return num;
	}

	/**
	 * Prompt the user and read an int between (inclusive) of minValue and maxValue, clearing whitespace or the enter after the number
	 * @param prompt: the question to ask the user 
	 * @return: an int between minValue and maxValue
	 */
	public int inputInt(String prompt, int minValue, int maxValue) {
		//get a number
		int number = inputInt(prompt); 
		// check the number is in range
		while (!(number >= minValue && number <= maxValue)) {
			System.out.println(number + " is not in the allowed range, [" 
					+ minValue + " - " + maxValue + "]");
			number = inputInt(prompt);
		}
		return number;
	}

	/**
	 * Prompt the user enter yes or no (will match y/yes and n/no any case) and 
	 * return true for yes and false for no.
	 * @param prompt: the question to ask the user 
	 * @return: a boolean value 
	 */
	public boolean inputYesNo(String prompt) {
		String word = inputWord(prompt);
		while (!( isYes(word) || isNo(word))) {
			// || word.equalsIgnoreCase("n") || word.equalsIgnoreCase("no"))){
			System.out.println(word + " was not a y/n answer. Please enter y or n.");
			word = inputWord(prompt);
		} //exit while loop means word is y/yes OR n/no
		return isYes(word);
	}
	public boolean isYes(String word) {
		return word.equalsIgnoreCase("y") || word.equalsIgnoreCase("yes");
	}

	public boolean isNo(String word) {
		return word.equalsIgnoreCase("n") || word.equalsIgnoreCase("no");
	}

	/**
	 * Prompt the user for information to make a date
	 * @param: none
	 * @return: a LocalDate 
	 */
	public LocalDate getDate() {
		int year = this.inputInt("Enter year: ", 1900, LocalDate.now().getYear() + 2);
		int month = this.inputInt("Enter month (as num): ", 1, 12);
		int day = this.inputInt("Enter day (as num): ", 1, Month.of(month).maxLength());
		return LocalDate.of(year, month, day); // LocalDate is immutable
	}

	/**
	 * Prompt the user for information to make a time
	 * @param: none
	 * @return: a LocalTime 
	 */
	public LocalTime getTime() {
		int hour = this.inputInt("Enter hour of day (0-23): ", 0, 23);
		int minute = this.inputInt("Enter minute (0-59): ", 0, 59);
		return LocalTime.of(hour, minute); // LocalTime is immutable
	}

}

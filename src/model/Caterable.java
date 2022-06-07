/**
 * This interface will be used for events that could be potentially caterable
 *
 * @author Brad Barakat
 * ITP 265, Spring 2022
 * Coffee Section
 * Email: bbarakat@usc.edu
 *
 */

package model;

public interface Caterable {
	
	// Returns if the event is actually catered
	public boolean isCatered();
	
	// Returns the food theme
	public String getFoodTheme();
	
	// Returns if the event has dessert
	public boolean hasDessert();
}

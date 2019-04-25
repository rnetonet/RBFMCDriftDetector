/**
 * 
 */
package br.usp.icmc.biocomp.elements;

import java.util.LinkedList;
import java.util.List;

/**
 * Realization (or occurrence) is a point in the time series.
 * 
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */
public class Realization {
	
	/** Events represents a dimension, which it gives some information about the realization  */
	private List<Object> event;

	public Realization(List<Object> event) {
		super();
		this.event = event;
	}
	
	public Realization() {
		super();
		this.event = new LinkedList<Object>();
	}

	public List<Object> getEvent() {
		return event;
	}

	public void setEvent(List<Object> event) {
		this.event = event;
	}
	
	public void copyEvents(Realization element) {
		this.event.clear();
		for(Object obj : element.getEvent())
			this.event.add(obj);
	}
	
	public void addEvent(Object event){
		this.event.add(event);
	}
	
	public int dimension(){
		return this.event.size();
	}
	
	public String toString(){
		String str = "Dim: " + this.dimension();
		str += "\n\tEvent [";
		for(Object obj : this.event){
			str += obj + ",";
		}
		str = str.substring(0, str.length()-1);
		str += "]";
		
		return str;
	}
	
	public String eventsToString(){
		String str = "Event [";
		for(Object obj : this.event){
			str += obj + ",";
		}
		str = str.substring(0, str.length()-1);
		str += "]";
		
		return str;
	}
	
	public boolean equals(Object realization){
		Realization compared = (Realization) realization;
		return this.event.equals(compared.getEvent());
	}
	
	public int hashCode(){
		return this.event.hashCode();
	}

}

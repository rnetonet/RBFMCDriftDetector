/**
 * 
 */
package br.usp.icmc.biocomp.distances;

import org.apache.commons.logging.LogFactory;

import br.usp.icmc.biocomp.elements.Realization;

/**
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */
public class CommonDistances {
	
	public static double euclidian(Realization element, Realization center){
		LogFactory.getLog(CommonDistances.class).debug("Calculating the distance between: \n" + element + " \n " + center);
		double sum = 0.0D;
		int index;
		
		if(element.getEvent().size() != center.getEvent().size()){
			LogFactory.getLog(CommonDistances.class).error("The length of the object is not equal. [" + 
					element.getEvent().size() + "," + center.getEvent().size() + "]");
			return -1;
		}
		
		for(index = 0; index < element.getEvent().size(); index++){
			sum += Math.pow(((Double)element.getEvent().get(index) - (Double)center.getEvent().get(index)), 2.0);
		}
		
		return Math.sqrt(sum);
	}

}

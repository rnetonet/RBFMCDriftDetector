/**
 * 
 */
package br.usp.icmc.biocomp.operations;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import br.usp.icmc.biocomp.distances.CommonDistances;
import br.usp.icmc.biocomp.elements.Realization;

/**
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */
public class RBF implements MainNNet{
	
	private List<Realization> centers;
	private double alpha;
	private double sigma;
	private double threshold;
	private List<Integer> timeInstant;
	
	//constructors
	
	public RBF(List<Realization> centers, double alpha, double sigma,
			double threshold) {
		super();
		this.centers = centers;
		this.alpha = alpha;
		this.sigma = sigma;
		this.threshold = threshold;
		this.timeInstant = new ArrayList<Integer>();
	}
	
	public RBF(double alpha, double sigma,
			double threshold) {
		super();
		this.centers = new ArrayList<Realization>();
		this.alpha = alpha;
		this.sigma = sigma;
		this.threshold = threshold;
		this.timeInstant = new ArrayList<Integer>();
	}
	
	//getters
	
	public double getAlpha() {
		return alpha;
	}

	public double getSigma() {
		return sigma;
	}

	public double getThreshold() {
		return threshold;
	}
	
	public List<Integer> getTimeInstant(){
		return this.timeInstant;
	}

	//setters
	
	public void setCenters(List<Realization> centers) {
		this.centers = centers;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public void addTimeInstant(int a) {
		this.timeInstant.add(a);
	}

	//general

	@Override
	public Realization activation(Realization event) {
		LogFactory.getLog(this.getClass()).debug("Calculating the activation to event: " + event);
		double activation = 0.0D;
		double activationMax = this.threshold;
		double distance = 0.0D;
		Realization activatedCenter = null;
		
		for(Realization center : centers){
			distance = CommonDistances.euclidian(event, center);
			activation = Math.exp(-(Math.pow(distance, 2.0)) / (2.0 * Math.pow(this.sigma, 2.0)));//sigmoid function
			//activation = distance;
			//if(activation < activationMax){
			if(activation > activationMax){
				activatedCenter = center;
				activationMax = activation;
			}
		}
		
		return activatedCenter;
	}

	@Override
	public Realization createCenter(Realization event) {
		LogFactory.getLog(this.getClass()).debug("Creating a new center with event: " + event);
		Realization newCenter = new Realization();
		
		newCenter.copyEvents(event);
		
		return newCenter;
	}

	@Override
	public Realization move(Realization event, Realization center) {
		LogFactory.getLog(this.getClass()).debug("Moving the center " + center);
		int index;
		double newPostition;
		
		for(index = 0; index < event.getEvent().size(); index++){
			newPostition = ((Double)center.getEvent().get(index)) * (1.0 - this.alpha);
			newPostition += ((Double)event.getEvent().get(index)) * this.alpha;
			center.getEvent().set(index, newPostition);
		}
		
		return center;
	}

	@Override
	public void printCenters() {
		int index;
		//LogFactory.getLog(this.getClass()).info("Printing centers");
		System.out.println("Printing centers");
		for(index = 0; index < this.centers.size(); index++) {
			//System.out.println("ID: " + index + "\n" + this.centers.get(index));
			System.out.println(this.timeInstant.get(index) + ";" + this.centers.get(index).getEvent().get(0) + ";" + this.sigma);
		}
			//LogFactory.getLog(this.getClass()).info("ID: " + index + "\n" + this.centers.get(index));
		
	}

	@Override
	public List<Realization> getCenters() {
		LogFactory.getLog(this.getClass()).debug("Getting centers");
		return this.centers;
	}

	@Override
	public void addCenters(Realization center) {
		LogFactory.getLog(this.getClass()).debug("Adding centers");
		this.centers.add(center);
		
	}

}

/**
 * 
 */
package br.usp.icmc.biocomp.main;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import br.usp.icmc.biocomp.elements.Realization;
import br.usp.icmc.biocomp.fileio.FileIO;
import br.usp.icmc.biocomp.operations.Markov;
import br.usp.icmc.biocomp.operations.RBF;

/**
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */
public class Main {
	
	public static List<Realization> events;
	public static String fileName;
	public static RBF rbfNet;
	
	public static void report(double alpha, double sigma,
			double threshold){
		
		int centers = rbfNet.getCenters().size();
		int dimension = (rbfNet.getCenters().size() == 0)? 0 : rbfNet.getCenters().get(0).dimension();
		
		System.out.println("\n\n-------------------------------------------------------------");
		System.out.println("---------------Printing final report (NNet)------------------");
		System.out.println("-------------------------------------------------------------");
		
		System.out.println("Total of events: " + events.size());
		System.out.println("Parameters: \n" + 
				"\t- Alpha: " +  alpha +
				"\t- Sigma: " + sigma +
				"\t- Threshold: " + threshold);
		
		System.out.println("----------------Printing centers-----------------------------");
		System.out.println("Total of centers: " + centers);
		System.out.println("Dimensions: " + dimension);
		rbfNet.printCenters();
		System.out.println("-------------------------------------------------------------");
		
	}

	public static void main(String[] args) {
		double alpha = 0.0, sigma = 0.0, threshold = 0.0;
		Realization activation, actualCenter = null;
		String graphicOption = null;
		Markov markov = new Markov();
		double markovEntropy = 0.0;
		LinkedList<Double> entropy;
		
		if(args.length < 4){
			LogFactory.getLog(Main.class).error("Error: Usage Main filename alpha sigma threshold [graph_option]");
			return;
		}
		
		fileName = args[0];
		alpha = Double.parseDouble(args[1]);
		sigma = Double.parseDouble(args[2]);
		threshold = Double.parseDouble(args[3]);
		if(args.length == 5)
			graphicOption = args[4];
		
		rbfNet = new RBF(alpha, sigma, threshold);
		
		events = FileIO.readData(fileName);
		
		entropy = new LinkedList<Double>();
		
		int line = 0;
		//rbf execution
		for(Realization event : events){
			
			activation = rbfNet.activation(event);
			line++;
			
			if(activation == null) {
				rbfNet.addCenters(rbfNet.createCenter(event));
				rbfNet.addTimeInstant(line);
			}
			//else
				//rbfNet.move(event, activation); //TODO: move and markov!

			if(actualCenter == null){
				actualCenter = event;
			} else {
				if(activation == null){
					markov.addTransition(actualCenter, event, alpha);
					actualCenter = event;
				}else if(!actualCenter.equals(activation)){
					markov.addTransition(actualCenter, activation, alpha);
					actualCenter = activation;
				}else if(actualCenter.equals(activation)){
					markov.addTransition(actualCenter, activation, alpha);
				}
				
				markovEntropy = markov.getEntropy();
				//TODO check entropy
				//if(!entropy.isEmpty())
					//markovEntropy -= ((markovEntropy == 0.0)? 0.0 : (entropy.getLast()/markovEntropy));
				
				entropy.add(markovEntropy);
				
			}
			
		}

		System.out.println("AMI: " + markov.getAMI(entropy));
		
		//markov.printSystem();
		//markov.printingProbabilities(fileName + ".dot");
		markov.printingDot(fileName, graphicOption);
		
		report(alpha, sigma, threshold);
		
	}

}

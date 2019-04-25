/**
 * 
 */
package br.usp.icmc.biocomp.main;

import java.util.List;

import br.usp.icmc.biocom.deal.DealArgs;
import br.usp.icmc.biocomp.elements.Realization;
import br.usp.icmc.biocomp.fileio.FileIO;
import br.usp.icmc.biocomp.operations.RBF;

/**
 * @author ricardo
 *
 */
public class RBFMain {

	private static DealArgs params;
	public static String fileName;
	public static RBF rbfNet;
	public static List<Realization> events;
	
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

	private static void printHelp(){
		System.out.print("\n\nUsage: ");
		System.out.print("java main.code options\n");
		System.out.println("Options:");
		System.out.println("-h			Show this help.");
		System.out.println("-i	filename	Time series file.");
		System.out.println("-a 	double		Set the ALPHA parameter (used to move the centers).");
		System.out.println("-s 	double		Set the SIGMA parameter.");
		System.out.println("-t	double		Set the THRESHOLD parameter.");
		System.out.println("-o	filename	File used to save the centers.");
		System.out.println("-u			Update centers.");
		System.out.println("-p			Print a final report.");

		System.out.println("\n Examples:");
		System.out.println("java -cp .:nnet.jar br.usp.icmc.biocomp.main.Main /tmp/ruido1.dat 0.00 0.035 0.01");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String []restrictions = {"-h", "-i", "-a", "-s", "-t", "-o", "-u", "-p"};
		double alpha = 0.0, sigma = 0.0, threshold = 0.0;
		Realization activation = null, actualCenter = null;
		
		params = new DealArgs();
		params.setRestrictions(restrictions);
		params.dealMain(args);
		
		//checking parameters
		// basic
		if(params.containsKey("-h") || params.get("-i")==null 
				|| params.get("-s")==null || params.get("-a")==null 
				|| params.get("-t")==null){
			printHelp();
			return;
		}
		
		alpha = Double.parseDouble(params.get("-a"));
		sigma = Double.parseDouble(params.get("-s"));
		threshold = Double.parseDouble(params.get("-t"));
		fileName = params.get("-i");
		
		rbfNet = new RBF(alpha, sigma, threshold);
		
		events = FileIO.readData(fileName);
		
		//rbf execution
		for(Realization event : events){
			
			activation = rbfNet.activation(event);
			
			if(activation == null)
				rbfNet.addCenters(rbfNet.createCenter(event));
			else if(params.containsKey("-u"))
				rbfNet.move(event, activation);

			if(actualCenter == null){
				actualCenter = event;
			} else {
				if(activation == null)
					actualCenter = event;
				else if(!actualCenter.equals(activation))
					actualCenter = activation;
			}
			
		}

		if(params.containsKey("-p"))
			report(alpha, sigma, threshold);
		
		if(params.get("-o") != null)
			FileIO.printCenters(params.get("-o"), rbfNet.getCenters());
		
		
	}

}

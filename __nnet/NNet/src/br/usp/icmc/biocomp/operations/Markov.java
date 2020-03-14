/**
 *
 */
package br.usp.icmc.biocomp.operations;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import org.kohsuke.graphviz.Edge;
import org.kohsuke.graphviz.Graph;
import org.kohsuke.graphviz.Node;

import br.usp.icmc.biocomp.elements.Realization;
import br.usp.icmc.biocomp.fileio.FileIO;

/**
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */
public class Markov {
	/** All transitions between the states of markov chain. [out->in (total of occurrences)] */
	private HashMap<Transitions, Double> system;

	public Markov() {
		super();
		this.system = new HashMap<Transitions, Double>();
	}

	public void addTransition(Realization out, Realization in, double psi){
		double transitionsCostTmp = 0.0;
		int isNull = 0;
		Transitions tmp = null;

		Transitions transition = new Transitions();
		transition.setIn(in);
		transition.setOut(out);

		if(!system.containsKey(transition))
			system.put(transition, psi);

		for(Transitions trans : system.keySet()){
			if(trans.getOut().equals(out) && trans.getIn().equals(in)){
				transitionsCostTmp = system.get(trans);
				system.put(trans, transitionsCostTmp + psi);
				isNull++;
				tmp = trans;
			}else if(trans.getOut().equals(out) && !trans.getIn().equals(in)){
				transitionsCostTmp = system.get(trans);
				system.put(trans, ((1 - psi) * transitionsCostTmp));
				isNull++;
			}

		}

		if(isNull == 0){
			system.put(transition, 1.0);
		}else if(isNull == 1){
			system.put(tmp, 1.0);
		}

	}

	public void printSystem(){
		System.out.println("Printing markov system");
		for(Transitions transition : system.keySet()){
			System.out.println(transition.getIn().eventsToString() + " => " + transition.getOut().eventsToString() +
					": " + system.get(transition));
		}
	}

	public void printingProbabilities(String fileName){
		System.out.println("Printing probabilities " + system.size());

		DecimalFormat df = new DecimalFormat("#,###0.0000");

		String str = "digraph G {\n";

		for(Transitions transition : system.keySet()){
			//System.out.println(transition.getOut() + " -> " + transition.getIn());
			str += "\t\"" + transition.getOut().eventsToString() + "\" -> \"" + transition.getIn().eventsToString() +
					"\" [label=\"" + df.format(system.get(transition)) + "\"]\n";
		}

		str += "}";

		FileIO.createFile(fileName, str, false);

	}

	public void printingDot(String fileName, String opt){
		System.out.println("Printing probabilities " + system.size());

		DecimalFormat df = new DecimalFormat("#,###0.0000");

		Graph graph = new Graph();

		for(Transitions transition : system.keySet()){
			Edge edge = new Edge(new Node().id("\"" + transition.getOut().eventsToString() + "\""),
					new Node().id("\"" + transition.getIn().eventsToString() + "\""));
			edge.attr("label", "\"" + df.format(system.get(transition)) + "\"");
			graph.edge(edge);
		}

		FileIO.generateDot(graph, fileName);

		if(opt != null)
			FileIO.generateGraph(graph, fileName, opt);

	}

	public double getEntropy(){
		double entropy = 0.0;
		double tmp;

		for(Transitions transition: system.keySet()){
			tmp = system.get(transition);//transition probability
			entropy += tmp * GeneralMath.log(tmp, 2);//transition * log_2(transition)
		}

		return ((entropy > 0.0)? (-1.0 * entropy) : entropy);
	}

	public int getAMI(List<Double> entropy){
		int ami = 0;
		double max = -1 * Double.MIN_VALUE;

		for(int i = 0; i < entropy.size(); i++){
			if(entropy.get(i) > max){
				max = entropy.get(i);
				ami = i;
			}
		}
		return ((entropy.size()-1) - ami);
	}

	/*public int getAMI(List<Double> entropy){
		int ami = 0;
		List<Double> differences = new LinkedList<Double>();
		double tmp = Double.MIN_VALUE;

		for(int ent = 1; ent < entropy.size()-1; ent++)
			differences.add(entropy.get(ent-1) - entropy.get(ent));

		for(int i = differences.size()-1; i > 0; i--){
			if(tmp > differences.get(i))
				break;
			ami++;
		}

		return ami;
	}*/

}

/**
 * Transitions (relationship) between states of Markov Chain
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */

class Transitions {
	private Realization in;
	private Realization out;

	public Transitions() {
		super();
		this.in = new Realization();
		this.out = new Realization();
	}

	public Realization getIn() {
		return in;
	}

	public Realization getOut() {
		return out;
	}

	public void setIn(Realization in) {
		this.in.copyEvents(in);
	}

	public void setOut(Realization out) {
		this.out.copyEvents(out);
	}

	public boolean equals(Object obj){
		Transitions compered = (Transitions) obj;
		return (this.in.equals(compered.getIn()) && this.out.equals(compered.getOut()));
	}

	public int hashCode(){
		return in.hashCode() + out.hashCode();
	}
}
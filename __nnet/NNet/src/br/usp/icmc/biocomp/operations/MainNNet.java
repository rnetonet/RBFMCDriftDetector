/**
 * 
 */
package br.usp.icmc.biocomp.operations;

import java.util.List;

import br.usp.icmc.biocomp.elements.Realization;

/**
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */
public interface MainNNet {
	
	public abstract Realization activation(Realization event);
	public abstract Realization move(Realization event, Realization center);
	public abstract Realization createCenter(Realization event);
	public abstract void printCenters();
	public abstract List<Realization> getCenters();
	public abstract void addCenters(Realization center);

}

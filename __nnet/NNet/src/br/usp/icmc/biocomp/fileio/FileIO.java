package br.usp.icmc.biocomp.fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.LogFactory;
import org.kohsuke.graphviz.Graph;

import br.usp.icmc.biocomp.elements.Realization;

/**
 * @author Ricardo A. Rios (rios.fsa@gmail.com)
 *
 */
public class FileIO {
	
	public static LinkedList<Realization> readData(String fileName){
		LinkedList<Realization> events = new LinkedList<Realization>();
		File file = new File(fileName);
		
		LogFactory.getLog(FileIO.class).debug("Creating data from file: " + fileName);
		
		if(!file.exists()){
			LogFactory.getLog(FileIO.class).error("File [" + fileName + "] not found ");
			return events;
		}
		
		try {
			
			FileReader reader = new FileReader(file);
			BufferedReader buffer = new BufferedReader(reader);
			String line;

			while((line = buffer.readLine()) != null){
				Realization realization = new Realization();
				StringTokenizer strTok = new StringTokenizer(line);
				
				while(strTok.hasMoreElements())
					realization.addEvent(Double.parseDouble(strTok.nextToken()));
				
				events.add(realization);
			}

			buffer.close();
			reader.close();
		} catch (FileNotFoundException e) {
			LogFactory.getLog(FileIO.class).error("File not found ", e);
		} catch (NumberFormatException e) {
			LogFactory.getLog(FileIO.class).error("Error number format ", e);
		} catch (IOException e) {
			LogFactory.getLog(FileIO.class).error("IO exception ", e);
		}

		return events;
	} 
	
	public static void createFile(String file, List<?> data, boolean append){
		File elements = new File(file);
		
		LogFactory.getLog(FileIO.class).debug("Creating file: " + file);
		
		if(elements.exists() && !append){
			LogFactory.getLog(FileIO.class).error("The data was not saved, because the file " +  file + 
					" exists and the option used to open it was " + append);
			return;
		}

		try {
			FileWriter writeElements = new FileWriter(elements, append);
			BufferedWriter bw = new BufferedWriter(writeElements);			
			
			for(int i = 0; i < data.size(); i++)
				bw.write(data.get(i) + "\n");

			bw.close();
			writeElements.close();

		} catch (FileNotFoundException e) {
			LogFactory.getLog(FileIO.class).error("File not found ", e);
		} catch (IOException e) {
			LogFactory.getLog(FileIO.class).error("Cannot create file ", e);
		}
	}
	
	public static void printCenters(String file, List<Realization> data){
		File elements = new File(file);
		
		LogFactory.getLog(FileIO.class).debug("Creating file: " + file);

		try {
			FileWriter writeElements = new FileWriter(elements);
			BufferedWriter bw = new BufferedWriter(writeElements);			
			
			for(int i = 0; i < data.size(); i++){
				for(Object obj : data.get(i).getEvent())
					bw.write(obj + " ");
				bw.write("\n");
			}

			bw.close();
			writeElements.close();

		} catch (FileNotFoundException e) {
			LogFactory.getLog(FileIO.class).error("File not found ", e);
		} catch (IOException e) {
			LogFactory.getLog(FileIO.class).error("Cannot create file ", e);
		}
	}
	
	public static void createFile(String file, String content, boolean append){
		File elements = new File(file);
		
		LogFactory.getLog(FileIO.class).debug("Creating file: " + file);
		
		if(elements.exists() && !append){
			LogFactory.getLog(FileIO.class).error("The data was not saved, because the file " +  file + 
					" exists and the option used to open it was " + append);
			return;
		}

		try {
			FileWriter writeElements = new FileWriter(elements, append);
			BufferedWriter bw = new BufferedWriter(writeElements);			
			
			bw.write(content);

			bw.close();
			writeElements.close();

		} catch (FileNotFoundException e) {
			LogFactory.getLog(FileIO.class).error("File not found ", e);
		} catch (IOException e) {
			LogFactory.getLog(FileIO.class).error("Cannot create file ", e);
		}
	}
	
	public static void generateGraph(Graph graph, String name, String opt){
		try {
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add("dot");
			arrayList.add("-T" + opt);
			graph.generateTo(arrayList, new File(name+ "." + opt));
			LogFactory.getLog(FileIO.class).info("Graph created in the file: " + name+ "." + opt);
		} catch (FileNotFoundException e) {
			LogFactory.getLog(FileIO.class).error("File not found", e);
		} catch (InterruptedException e) {
			LogFactory.getLog(FileIO.class).error("Interrupted Exception", e);
		} catch (IOException e) {
			LogFactory.getLog(FileIO.class).error("IOException", e);
		}
	}
	
	public static void generateDot(Graph graph, String name){
		try {
			FileOutputStream fos = new FileOutputStream(new File(name + ".dot"));
			graph.writeTo(fos);
			LogFactory.getLog(FileIO.class).info("Dot file created in the file: " + name+ ".dot");
		} catch (FileNotFoundException e) {
			LogFactory.getLog(FileIO.class).error("File not found", e);
		} 
	}

}

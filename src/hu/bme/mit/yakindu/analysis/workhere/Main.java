package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading modelD
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		State stateFrom = null;
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if(stateFrom != null) {
					System.out.println(stateFrom.getName() + " -> " + state.getName());
				}
				stateFrom = state;
			}
		}
		
		trap(s);
		noname(s);
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
	public static void trap(Statechart s) {
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if (state.getOutgoingTransitions().isEmpty() == true) {
					System.out.println(state.getName() + " is trap state...");
				}
			}
		}
	}
	
	public static void noname(Statechart sc) {
		TreeIterator<EObject> iterator = sc.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if (state.getName().isEmpty() == true) {
					System.out.println("There is a no name state. It should be " + state.getIncomingTransitions().get(0).toString() + "_in_" + state.getOutgoingTransitions().get(0).toString()  + "_out");
				}
			}
		}
	}
}

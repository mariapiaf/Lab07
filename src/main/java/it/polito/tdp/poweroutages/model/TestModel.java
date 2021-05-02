package it.polito.tdp.poweroutages.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		//System.out.println(model.getNercList());
		int anni = 4;
		int ore = 200;
		System.out.println(model.calcolaSottoinsiemeNerc(model.getNercList().get(3), anni, ore));
	}

}

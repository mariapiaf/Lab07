package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	
	private List<PowerOutages> partenza;
	private List<PowerOutages> soluzioneMigliore;
	int totCustomersOttimo;
	int annoMinore;
	int annoMaggiore;
	
	public Model() {
		podao = new PowerOutageDAO();
		partenza = new ArrayList<PowerOutages>();
		totCustomersOttimo = 0;
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}

	public List<PowerOutages> calcolaSottoinsiemeNerc(Nerc nerc, int maxAnni, int maxOre){
		List<PowerOutages> parziale = new ArrayList<PowerOutages>();
		soluzioneMigliore = null;
		partenza.addAll(this.podao.getPowerOutagesList(nerc));
		annoMinore = partenza.get(0).getDateEventFinished().getYear();
		annoMaggiore = partenza.get(partenza.size()-1).getDateEventFinished().getYear();
		cercaSoluzione(parziale, 0, maxAnni, maxOre);
		return soluzioneMigliore;
	}
	
	public void cercaSoluzione(List<PowerOutages> parziale, int livello, int maxAnni, int maxOre) {
		
		int customers = this.sommaCustomers(parziale);
		
		if(soluzioneMigliore == null || customers > totCustomersOttimo) {
			totCustomersOttimo = customers;
			soluzioneMigliore = new ArrayList<>(parziale);
		}
		
		if(livello == partenza.size())
			return;
		
		parziale.add(partenza.get(livello));
		if(this.isValid(parziale, maxAnni, maxOre)) {
			cercaSoluzione(parziale, livello+1, maxAnni, maxOre);
		}
			
		parziale.remove(partenza.get(livello));
		cercaSoluzione(parziale, livello+1, maxAnni, maxOre);
		
		
	}
	
	public boolean isValid(List<PowerOutages> parziale, int maxAnni, int maxOre) {
		
		for(PowerOutages po: parziale) {
			
			float durataSec = Duration.between(po.getDateEventBegan(), po.getDateEventFinished()).getSeconds();
			int durataOre = (int) (durataSec/3600);
			if(durataOre>maxOre) {
				return false;
			}
			if((po.getDateEventFinished().getYear()-annoMinore)>maxAnni) {
				return false;
			}
			
		}
		
		return true;
		
	}
	
	public int sommaCustomers(List<PowerOutages> parziale) {
		int somma = 0;
		for(PowerOutages po: parziale) {
			somma += po.getCustomersAffected();
		}
		return somma;
	}
}

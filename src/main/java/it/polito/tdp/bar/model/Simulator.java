package it.polito.tdp.bar.model;
import java.time.Duration;
import java.util.*;

import it.polito.tdp.bar.model.Event.EventType;
public class Simulator {
	
	//MODELLO : LISTA DI TAVOLI (BANCONE CAPACITA' ILLIMITATA)
	private List<Tavolo> tavoli;
	
	//PARAMETRI DELLA SIMULAZIONE 
	private int NUM_EVENTI = 2000; //TENGO TRACCIA NUM EVENTI
	private int T_ARRIVO_MAX = 10; //TRA L'ARRIVO DI 1 UN GRUPPO E L'ALTRO CI POSSONO ESSERE MAX 10 MINUTI 
	private int NUM_PERSONE_MAX;
	private int DURATA_MIN = 60;
	private int DURATA_MAX = 120;
	private double TOLLERANZA_MAX = 0.9;
	private double OCCUPAZIONE_MIN = 0.5;
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	//PARAMETRI IN USCITA -> STATISTICHE
	private Statistiche statistiche;
	
	public void init() {
		this.queue = new PriorityQueue<Event>();
		this.statistiche = new Statistiche();
		
		//CREO EVENTI E TAVOLI
		creaTavoli();
		creaEventi();
	}

	private void creaEventi() {
		Duration arrivo = Duration.ofMinutes(0); //tempo di arrivo dell'evento
		for(int i=0; i<this.NUM_EVENTI;i++) {
			int nPersone = (int) (Math.random()*this.NUM_PERSONE_MAX + 1); //perche' math.random*10 mi da un numero da 0 a 9!
			Duration durata = Duration.ofMinutes(this.DURATA_MIN + (int)(Math.random()*(this.DURATA_MAX-this.DURATA_MIN + 1)));
			double tolleranza = Math.random()*this.TOLLERANZA_MAX;
			
			Event e = new Event(arrivo, EventType.ARRIVO_GRUPPO_CLIENTI, nPersone, durata, tolleranza, null);
			this.queue.add(e);
			arrivo = arrivo.plusMinutes((long) (Math.random()*this.T_ARRIVO_MAX + 1));
		}
		
	}

	private void creaTavolo(int quantità, int dimensione) {
		for(int i=0; i<quantità; i++) {
			this.tavoli.add(new Tavolo(dimensione, false));
		}
	}
	private void creaTavoli() {
		creaTavolo(2,10);
		creaTavolo(4,8);
		creaTavolo(4,6);
		creaTavolo(5,4);
		
		//ORDINO I TAVOLI PER DIMENSIONE
		Collections.sort(this.tavoli, new Comparator<Tavolo>(){

			@Override
			public int compare(Tavolo o1, Tavolo o2) {
				return o1.getPosti()-o2.getPosti();
			}
		});
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
		
		case ARRIVO_GRUPPO_CLIENTI:
			//ARRIVANO DEI CLIENTI QUINDI ASSEGNO IL TAVOLO 
			this.statistiche.incrementaTotClienti(e.getnPersone());
			
			//VERIFICO CHE CI SIA UN TAVOLO -> IL PIU' PICCOLO POSSIBILE E DEVONO OCCUPARE ALMENO IL 50% DEI POSTI
			Tavolo tavolo = null;
			
			for(Tavolo t : this.tavoli) {
				if(!t.isOccupato() && t.getPosti() >= e.getnPersone() && t.getPosti()*this.OCCUPAZIONE_MIN <= e.getnPersone()) {
					tavolo = t;
					break;
				}
			}
			if(tavolo != null) {
				System.out.format("Trovato tavolo da %d per %d persone", tavolo.getPosti(), e.getnPersone());
				statistiche.incrementaClientiSoddisfatti(e.getnPersone());
				tavolo.setOccupato(true);
				e.setTavolo(tavolo); //INUTILE PERCHE' POI LO INSERISCO DOPO
				//DOPO UN PO I CLIENTI SI ALZERANNO
				queue.add(new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO, e.getnPersone(), e.getDurata(), e.getTolleranza(), tavolo));
			}else {
				double bancone = Math.random();
				if(bancone<=e.getTolleranza()) {
					//GLI UTENTI SI FERMANO AL BANCONE
					System.out.format("%d persone si fermano al bancone", e.getnPersone());
					statistiche.incrementaClientiSoddisfatti(e.getnPersone());
				}else {
					System.out.format("%d persone vanno a casa", e.getnPersone());
					statistiche.incrementaClientiInsoddisfatti(e.getnPersone());
				}
			}
			
			break;
		case TAVOLO_LIBERATO:
			//I CLIENTI SE NE VANNO -> RISETTO IL TAVOLO A LIBERO
			e.getTavolo().setOccupato(false);
			break;
		
		}
		
	}
	

}

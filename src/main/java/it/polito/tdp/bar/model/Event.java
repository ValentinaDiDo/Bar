package it.polito.tdp.bar.model;

import java.time.Duration;

public class Event implements Comparable<Event>{

	//DUE TIPI DI EVENTO -> L'ARRIVO DEI CLIENTI, E L'USCITA DEI CLIENTI (LIBERANO TAVOLO X ALTRI CLIENTI)
	public enum EventType {
		ARRIVO_GRUPPO_CLIENTI,
		TAVOLO_LIBERATO
	}
	
	private EventType type; //INDICO IL TIPO DI EVENTO CHE STO DEFINENDO
	private Duration time; //CONSIDERO TEMPO 1,2,3 ECC... NON USO LE DATE, MA LA DURATA DELL'EVENTO
	private int nPersone;
	private Duration durata;
	private double tolleranza;
	private Tavolo tavolo;
	public Event(Duration time,EventType type,  int nPersone, Duration durata, double tolleranza, Tavolo tavolo) {
		super();
		this.type = type;
		this.time = time;
		this.nPersone = nPersone;
		this.durata = durata;
		this.tolleranza = tolleranza;
		this.tavolo = tavolo;
	}
	public void setTavolo(Tavolo tavolo) {
		this.tavolo = tavolo;
	}
	public EventType getType() {
		return type;
	}
	public Duration getTime() {
		return time;
	}
	public int getnPersone() {
		return nPersone;
	}
	public Duration getDurata() {
		return durata;
	}
	public double getTolleranza() {
		return tolleranza;
	}
	public Tavolo getTavolo() {
		return tavolo;
	}
	@Override
	//UTILIZZO UN COMPARABLE PERCHE' AVENDO UNA CODA PRIORITARIA, IN QUALCHE MODO DOVRO' PUR ORDINARE / DARE PRIORITA' AGLI EVENTI...
	public int compareTo(Event o) {
		return this.time.compareTo(o.getTime());
	}
	
	
}

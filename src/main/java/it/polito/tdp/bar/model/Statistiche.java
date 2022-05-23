package it.polito.tdp.bar.model;

public class Statistiche {

	private int totClienti;
	private int clientiSoddisfatti;
	private int clientiInsoddisfatti;
	public Statistiche() {
		super();
		this.totClienti = 0;
		this.clientiSoddisfatti = 0;
		this.clientiInsoddisfatti = 0;
		//INCREMENTO I VALORI MANO A MANO
	}
	
	public int getTotClienti() {
		return totClienti;
	}
	public int getClientiSoddisfatti() {
		return clientiSoddisfatti;
	}
	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}
	
	public void incrementaTotClienti(int n) {
		this.totClienti += n;
	}
	public void incrementaClientiSoddisfatti(int c) {
		this.clientiSoddisfatti += c;
	}
	public void incrementaClientiInsoddisfatti(int c) {
		this.clientiInsoddisfatti += c;
	}
	
	
}

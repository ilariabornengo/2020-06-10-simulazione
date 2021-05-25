package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {

	//input
	int giorni;
	
	//output
	int numAttori;
	int numPause;
	
	//modello del mondo
	private Map<Integer,Actor> queue;
	private List<Actor> attoriLiberi;
	private Graph<Actor,DefaultWeightedEdge> grafo;
	
	public Simulator(int n,Graph<Actor,DefaultWeightedEdge> graph)
	{
		this.giorni=n;
		this.grafo=graph;
		
	}
	public void init()
	{
		this.numAttori=0;
		this.numPause=0;
		this.attoriLiberi=new ArrayList<>(this.grafo.vertexSet());
		this.queue=new HashMap<Integer,Actor>();
	}
	public void run()
	{
		
		for(int i=1;i<this.giorni;i++)
		{ 
			Random rand=new Random();
			//se siamo nel primo caso oppure ci siamo fermati per una pausa
			if(i==1 || !queue.containsKey(i-1))
			{
				Actor a=this.attoriLiberi.get(rand.nextInt(attoriLiberi.size()));
				queue.put(i, a);
				attoriLiberi.remove(a);
				System.out.println("GIORNO "+i+" SCELTO CASUALMENTE "+a+"\n");
				continue;
			}
			//se sono maggiori uguali a 3 giorni e i due elementi dello stesso genere entro e con il 90% di possibilità prendo una pausa
			if(i>=3 && queue.containsKey(i-1) && queue.containsKey(i-2) && queue.get(i-1).getGender().equals(queue.get(i-2).getGender()))
			{
				if(rand.nextFloat()<=0.9)
				{
					this.numPause++;
					System.out.println("GIORNO "+i+" PAUSA+\n");
					continue;
				}
			}
			//se non mi sono preso una pausa
			//con il 60% di probabilità scelgo casualmente
			//con il restante 40% mi faccio consigliare dall'ultimo intervistato
			if(rand.nextFloat()<=0.6)
			{
				Actor a=this.attoriLiberi.get(rand.nextInt(attoriLiberi.size()));
				this.queue.put(i, a);
				this.attoriLiberi.remove(a);
				System.out.println("GIORNO "+i+" SCELTO CASUALMENTE "+a+"\n");
				continue;
			}
			else
			{
				Actor ultimoIntervistato=this.queue.get(i-1);
				Actor raccomandato=this.getRaccomandato(ultimoIntervistato);
				//se il raccomandato non c'è o non è tra le possibili scelte (anche perchè già scelto magari)
				//vado casualmente
				//sennò scelgo il raccomandato
				if(raccomandato==null || !this.attoriLiberi.contains(raccomandato))
				{
					Actor a=this.attoriLiberi.get(rand.nextInt(attoriLiberi.size()));
					this.queue.put(i, a);
					this.attoriLiberi.remove(a);
					System.out.println("GIORNO "+i+" SCELTO CASUALMENTE "+a+"\n");
					continue;
				}
				else
				{
					this.queue.put(i, raccomandato);
					this.attoriLiberi.remove(raccomandato);
					System.out.println("GIORNO "+i+" SCELTO PER RACCOMANDAZIONE "+raccomandato+"\n");
					continue;
				}
			}
			
		}
	}
	private Actor getRaccomandato(Actor ultimoIntervistato) {
		Actor racco=null;
		int peso=0;
		for (Actor a:Graphs.neighborListOf(this.grafo, ultimoIntervistato))
		{
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(ultimoIntervistato, a))>peso)
			{
				peso=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimoIntervistato, a));
				racco=a;
			}
		}
		return racco;
	}
	public int getPause()
	{
		return this.numPause;
	}
	public Collection<Actor> getAttori()
	{
		return this.queue.values();
	}
}

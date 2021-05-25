package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;
	private Map<Integer,Actor> idMap;
	private Graph<Actor,DefaultWeightedEdge>grafo;
	 Simulator sim;
	
	public Model()
	{
		this.dao=new ImdbDAO();
	}
	
	public List<String> getGeneri()
	{
		return this.dao.listAllGeneres();
		
	}
	
	public void creaGrafo(String genere)
	{
		this.idMap=new HashMap<Integer,Actor>();
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//imposto i vertici
		this.dao.getVertices(genere, idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//aggiungo gli archi 
		//creo la class adiacenza
		for(Adiacenza a:this.dao.getAdiacenze(idMap, genere))
		{
			if(this.grafo.containsVertex(a.getA1())&& this.grafo.containsVertex(a.getA2()))
			{
				Graphs.addEdge(this.grafo,a.getA1(),a.getA2(),a.getPeso());
			}
		}
		System.out.println("GRAFO CREATO");
		System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
		
		
	}
	public List <Actor> getConnectedActors(Actor a)
	{
		ConnectivityInspector<Actor,DefaultWeightedEdge> ci=new ConnectivityInspector<Actor,DefaultWeightedEdge>(this.grafo);
		List<Actor> lista=new ArrayList<Actor>(ci.connectedSetOf(a));
		lista.remove(a);
		Collections.sort(lista,new ComparatorCognome());
		return lista;
	}
	
	public void simulate(int n)
	{
		sim=new Simulator(n,grafo);
		sim.init();
		sim.run();
	}
	public Collection<Actor> getAttoriSim()
	{
		if(sim==null)
		{
			return null;
		}
		return this.sim.getAttori();
	}
	public Integer getPause()
	{
		if(sim==null)
		{
			return null;
		}
		return this.sim.getPause();
	}
	public Map<Integer, Actor> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<Integer, Actor> idMap) {
		this.idMap = idMap;
	}

	public Graph<Actor, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Actor, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public int getNumeroArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public int getNumeroVertici()
	{
		return this.grafo.vertexSet().size();
	}
	

	public ImdbDAO getDao() {
		return dao;
	}

	public void setDao(ImdbDAO dao) {
		this.dao = dao;
	}
	
}

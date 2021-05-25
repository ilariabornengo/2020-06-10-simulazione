package it.polito.tdp.imdb.model;

import java.util.Comparator;

public class ComparatorCognome implements Comparator<Actor> {

	@Override
	public int compare(Actor o1, Actor o2) {
		// TODO Auto-generated method stub
		return o1.getLastName().compareTo(o2.getLastName());
	}

}

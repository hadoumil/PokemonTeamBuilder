package com.cwgames.pokemonteambuilder2.main;

import java.util.Comparator;

public class PokemonComparator implements Comparator<Pokemon> {
	
	@Override
	public int compare(Pokemon p1, Pokemon p2) {
		return p1.species.compareTo(p2.species);
	}
	
}

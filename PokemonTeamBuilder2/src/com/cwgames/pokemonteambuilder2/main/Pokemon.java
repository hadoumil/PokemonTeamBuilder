package com.cwgames.pokemonteambuilder2.main;

public class Pokemon {
	
    public String key; // key used in pokedex.js
    
	public int num;
	public String species;
	public String baseSpecies;
	public String formLetter;
	public String[] types;
	public BaseStats baseStats;
	public Abilities abilities;
	
	public Pokemon(String key, int num, String species, String baseSpecies, String formLetter, String[] types, BaseStats baseStats, Abilities abilities) {
	    this.key = key;
		this.num = num;
	    this.species = species;
	    this.baseSpecies = baseSpecies;
		this.formLetter = formLetter;
		this.types = types;
		this.baseStats = baseStats;
		this.abilities = abilities;
	}
	
	public int calcBaseStatTotal() {
		return baseStats.hp + baseStats.atk + baseStats.def + baseStats.spa + baseStats.spd + baseStats.spd;
	}
	
	@Override
	public String toString(){
		return species.toString();
	}
}

package com.cwgames.pokemonteambuilder2.main;

import java.util.Arrays;

public class Tier {
    
	public static final String BANK = "Bank";
	public static final String BANKLC = "Bank-LC";
	public static final String BANKNFE = "Bank-NFE";
	public static final String BANKUBER = "Bank-Uber";
	public static final String BL = "BL";
	public static final String BL2 = "BL2";
	public static final String BL3 = "BL3";
	public static final String BL4 = "BL4";
	public static final String CAP = "CAP";
	public static final String ILLEGAL = "Illegal";
	public static final String LC = "LC";
	public static final String LCUBER = "LC Uber";
	public static final String NEW = "New";
	public static final String NFE = "NFE";
	public static final String NU = "NU";
	public static final String OU = "OU";
	public static final String OU_BT = "(OU)"; // OU by technicality
	public static final String PU = "PU";
	public static final String RU = "RU";
	public static final String UBER = "Uber";
	public static final String UNRELEASED = "Unreleased";
	public static final String UU = "UU";
	
	public String name;
	public Pokemon[] pokemon;
	
	public Tier(String name) {
		this.name = name;
		
		this.pokemon = new Pokemon[0];
	}
	
	public Tier(String name, Pokemon[] pokemon) {
		this.name = name;
		this.pokemon = pokemon;
	}
	
	public void addPokemon(Pokemon p) {
		// create copy of original array
		Pokemon[] tempPokemon = Arrays.copyOf(pokemon, pokemon.length + 1);
		
		// add last Pokemon
		tempPokemon[tempPokemon.length - 1] = p;
		
		// edit original array
		pokemon = Arrays.copyOf(tempPokemon, tempPokemon.length);
	}
	
}

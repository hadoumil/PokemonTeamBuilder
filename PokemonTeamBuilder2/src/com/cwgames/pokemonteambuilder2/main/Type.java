package com.cwgames.pokemonteambuilder2.main;

public final class Type {
	
	public static final int numberOfTypes = 18;
	
	public static final String BUG = "Bug";
	public static final String DARK = "Dark";
	public static final String DRAGON = "Dragon";
	public static final String ELECTRIC = "Electric";
	public static final String FAIRY = "Fairy";
	public static final String FIGHTING = "Fighting";
	public static final String FIRE = "Fire";
	public static final String FLYING = "Flying";
	public static final String GHOST = "Ghost";
	public static final String GRASS = "Grass";
	public static final String GROUND = "Ground";
	public static final String ICE = "Ice";
	public static final String NORMAL = "Normal";
	public static final String POISON = "Poison";
	public static final String PSYCHIC = "Psychic";
	public static final String ROCK = "Rock";
	public static final String STEEL = "Steel";
	public static final String WATER = "Water";
	
	public static final String[] POSSIBLE_TYPES = new String[]{BUG, DARK, DRAGON, ELECTRIC, FAIRY, FIGHTING, FIRE, FLYING, GHOST, GRASS, GROUND, ICE, NORMAL, POISON, PSYCHIC, ROCK, STEEL, WATER};
	
	public static boolean isPossibleType(String type) {
		for (String possibleType : POSSIBLE_TYPES)
			if (type.equals(possibleType))
				return true;
		
		return false;
	}
	
}

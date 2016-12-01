package com.cwgames.pokemonteambuilder2.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamBuilder {
    
    // primitive variables
    private static final String typeChartPath = "assets/data/currentgen/typechart";
    private static final String pokedexPath = "assets/data/currentgen/pokedex";
    private static final String formatsPath = "assets/data/currentgen/formats";
    private static final String sampleTeamsPath = "assets/data/sampleteams2";
    
    private static boolean stealthRocks;
    private static boolean spikes;
    private static boolean toxicSpikes;
//  private static boolean conditionalHazardControl; 
    private static boolean defog;
    private static boolean rapidSpin; // if defog and rapidSpin are true, only one will be required
    
    private static long iterations;
    private static double buildStartTime;
    private static double buildTotalTime;
    
    // main arrays
    private static String[] types;
    private static int[][] typeChart;
    private static Pokemon[] pokedex;
    private static Tier[] tiers;
    private static Pokemon[] buildSet;
    private static ArrayList<Pokemon[]> teams;
    
    // preset arrays
    private static String[] presetTeam;
    
    private static String[] presetBuildSet; 	// ignores additionalPokemon, excludedPokemon, and presetTiers
    private static String[] additionalPokemon;
    private static String[] excludedPokemon;
    private static String[] presetTiers;
    
    private static String[] preferredStealthRockers;
    private static String[] preferredSpikers;
    private static String[] preferredToxicSpikers;
    private static String[] preferredDefoggers;
    private static String[] preferredRapidSpinners;
    
    public static void main(String[] args) {
		// set presets
		setPresets();
		
		// init data
		initTypes();
		initPokedex();
		initTiers();
		initBuildSet();
		
		// TODO determine counters
		// TODO determine checks
		
		// optional: analyze sample teams type matchups
//		analyzeSampleTeams();
		
		// build
		buildTeams();
    }
    
    private static void setPresets() {
    	// presets
		stealthRocks = true;
		spikes = false;
		toxicSpikes = false;
//		conditionalHazardControl = true;
		defog = false;
		rapidSpin = false;
		
		presetTeam = new String[]{"Pikachu"};
		
		presetBuildSet = new String[]{"Pheromosa", "Mandibuzz", "Gyarados-Mega","Landorus-Therian", "Tapu Koko", "Landorus", "Tapu Fini", "Genesect", "Latios", "Tapu Lele", "Venusaur-Mega", "Rotom-Wash", "Tapu Bulu", "Toxapex", "Ferrothorn", "Celesteela", "Heatran", "Marowak-Alola", "Metagross-Mega", "Garchomp", "Magearna", "Hoopa-Unbound", "Buzzwole", "Greninja", "Dugtrio", "Alakazam", "Charizard-Mega-X", "Tangrowth", "Magnezone", "Scizor-Mega", "Mimikyu", "Porygon-Z", "Tornadus-Therian", "Keldeo", "Clefable", "Excadrill", "Hippowdon", "Nihilego", "Xurkitree", "Bisharp", "Kartana", "Skarmory", "Weavile", "Amoonguss", "Chansey", "Muk-Alola", "Kyurem-Black", "Manaphy", "Zygarde-10%", "Jirachi"}; // "Mantine"
		additionalPokemon = new String[]{};
		excludedPokemon = new String[]{"Aggron-Mega", "Altaria-Mega", "Ampharos-Nega", "Audino-Mega", "Banette-Mega", "Beedrill-Mega", "Blaziken-Mega", "Camerupt-Mega", "Diancie-Mega", "Gallade-Mega", "Gardevoir-Mega", "Heracross-Mega", "Houndoom-Mega", "Latias-Mega", "Latios-Mega", "Lopunny-Mega", "Manectric-Mega", "Mawile-Mega", "Medicham-Mega", "Mewtwo-Mega-X", "Mewtwo-Mega-Y", "Pidgeot-Mega", "Steelix-Mega", "Sceptile-Mega", "Swampert-Mega", "Tyranitar-Mega"};
		presetTiers = new String[]{};
		
		preferredStealthRockers = new String[]{"Excadrill", "Marowak-Alola", "Garchomp", "Landorus-Therian", "Landorus", "Heatran", "Clefable", "Ferrothorn", "Hippowdon"};
		preferredSpikers = new String[]{"Skarmory", "Ferrothorn", "Greninja", "Klefki", "Scolipede"};
		preferredToxicSpikers = new String[]{"Tentacruel", "Scolipede", "Forretress", "Nidoqueen", "Roserade"};
		preferredDefoggers = new String[]{"Latias", "Latios", "Latias-Mega", "Latios-Mega", "Decidueye", "Zapdos", "Scizor-Mega", "Skarmory", "Mew", "Tapu Fini", "Mantine"};
		preferredRapidSpinners = new String[]{"Excadrill", "Starmie", "Tentacruel", "Forretress"};
		
		// error checking
		if (presetTeam.length > 6)
			System.out.println("Error. Too many preset team members. Only using the first 6 specified.");
		
		// print information
		if (presetTeam.length != 0) {
			System.out.print("Preset team: " );
			for (String s : presetTeam) System.out.print(s + "   ");
			System.out.println();
		}
		if (presetBuildSet.length != 0) {
			System.out.print("Preset build set: " );
			for (String s : presetBuildSet) System.out.print(s + "   ");
			System.out.println();
		}
		else {
			if (additionalPokemon.length != 0) {
				System.out.print("Additional Pokemon: " );
				for (String s : additionalPokemon) System.out.print(s + "   ");
				System.out.println();
			}
			if (excludedPokemon.length != 0) {
				System.out.print("Excluded Pokemon: " );
				for (String s : excludedPokemon) System.out.print(s + "   ");
				System.out.println();
			}
			if (presetTiers.length != 0) {
				System.out.print("Preset tiers: " );
				for (String s : presetTiers) System.out.print(s + "   ");
				System.out.println();
			}
		}
		System.out.print("Conditions: ");
		if (stealthRocks) System.out.print("Stealth Rocks   ");
		if (spikes) System.out.print("Spikes   ");
		if (toxicSpikes) System.out.print("Toxic Spikes   ");
		if (defog && rapidSpin) System.out.print("Defog or Rapid Spin   ");
		else {
			if (defog) System.out.print("Defog   ");
			if (rapidSpin) System.out.print("Rapid Spin");
		}
		System.out.println();
		if (stealthRocks && (preferredStealthRockers.length != 0)) {
			System.out.print("Preferred Stealth Rockers: " );
			for (String s : preferredStealthRockers) System.out.print(s + "   ");
			System.out.println();
		}
		if (spikes && (preferredSpikers.length != 0)) {
			System.out.print("Preferred Spikers: " );
			for (String s : preferredSpikers) System.out.print(s + "   ");
			System.out.println();
		}
		if (toxicSpikes && (preferredToxicSpikers.length != 0)) {
			System.out.print("Preferred Toxic Spikers: " );
			for (String s : preferredToxicSpikers) System.out.print(s + "   ");
			System.out.println();
		}
		if (defog && (preferredDefoggers.length != 0)) {
			System.out.print("Preferred Defoggers: " );
			for (String s : preferredDefoggers) System.out.print(s + "   ");
			System.out.println();
		}
		if (rapidSpin && (preferredRapidSpinners.length != 0)) {
			System.out.print("Preferred Rapid Spinners: " );
			for (String s : preferredRapidSpinners) System.out.print(s + "   ");
			System.out.println();
		}
    }
    
    private static void initTypes() {
    	System.out.print("Initializing types... ");
    	double startTime = System.currentTimeMillis();
    	
    	// ArrayList for allocation and getIndexOf()
    	ArrayList<String> typesList = new ArrayList<String>();
    	
    	// convert typechart.js to String
		String typeChartString = "";
		try {
		    typeChartString = new String(Files.readAllBytes(Paths.get(typeChartPath)));
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		JSONObject typeChartJS = new JSONObject(typeChartString);
		Iterator<String> keysDef = typeChartJS.keys(); // keys for all defending types
		
		// typesList
		while (keysDef.hasNext()) {
			String keyDef = keysDef.next();
			if (Type.isPossibleType(keyDef)) {
				try {
					typesList.add(keyDef);
				}
				catch (JSONException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		Collections.sort(typesList); // alphabetically
		
		// typeChartList
		typeChart = new int[typesList.size()][];
		keysDef = typeChartJS.keys(); // reinitiate iterator
				
		while (keysDef.hasNext()) {
			String keyDef = keysDef.next();
			JSONObject type1 = typeChartJS.getJSONObject(keyDef);
			JSONObject damageTakenJS = type1.getJSONObject("damageTaken");
			
			int[] damagesTaken = new int[typesList.size()];
			
			try {
				Iterator<String> keysAtk = damageTakenJS.keys(); // keys for all attacking types
				while (keysAtk.hasNext()){
					String keyAtk = keysAtk.next();
					
					if (Type.isPossibleType(keyAtk)) {
						try {
							damagesTaken[typesList.indexOf(keyAtk)] = damageTakenJS.getInt(keyAtk);
						}
						catch (JSONException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			catch (JSONException ex) {
				ex.printStackTrace();
			}
			
			typeChart[typesList.indexOf(keyDef)] = damagesTaken;
		}
		
		// convert typesList to array
		types = typesList.toArray(new String[typesList.size()]);
			
		System.out.print("complete. Time: " + (System.currentTimeMillis() - startTime) / 1000 + "s\n");
    }
    
    private static void initPokedex()  {
    	System.out.print("Initializing PokeDex... ");
    	double startTime = System.currentTimeMillis();
    	
    	// ArrayList for allocation
    	ArrayList<Pokemon> pokedexList = new ArrayList<Pokemon>();
    	
		// convert pokedex.js to String
		String pokedexString = "";
		try {
		    pokedexString = new String(Files.readAllBytes(Paths.get(pokedexPath)));
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		// parse pokedexString to pokedexList
		JSONObject pokedexJS = new JSONObject(pokedexString);
		Iterator<String> keys = pokedexJS.keys(); // keys for all pokemon
		
		int num = 0;
		String species = null;
		String baseSpecies = null;
		String formLetter = null;
		String[] types = null;
		BaseStats baseStats = null;
		Abilities abilities = null;
		
		while(keys.hasNext()) {
		    String key = keys.next();
		    JSONObject pokemonJS = pokedexJS.getJSONObject(key);
		    
		    // num
		    try {
		    	num = pokemonJS.getInt("num");
		    } catch(JSONException ex) {
				num = 0;
				System.out.println("Error. No pokedex number for key " + key);
		    }
		    
		    // species
		    try {
		    	species = pokemonJS.getString("species");
		    } catch(JSONException ex) {
				species = null;
				System.out.println("Error. No species for key " + key);
		    }
		    
		    // base species
		    try {
		    	baseSpecies = pokemonJS.getString("baseSpecies");
		    } catch(JSONException ex) {
		    	baseSpecies = null;
		    }
		    
		    // formLetter
		    try {
		    	formLetter = pokemonJS.getString("formLetter");
		    } catch(JSONException ex) {
		    	formLetter = null;
		    }
		    
		    // types
		    try {
		    	types = new String[pokemonJS.getJSONArray("types").length()];
		    	for (int i = 0; i < pokemonJS.getJSONArray("types").length(); i++)
		    		types[i] = pokemonJS.getJSONArray("types").getString(i);
		    } catch(JSONException ex) {
		    	types = null;
		    	System.out.println("Error. No types for key " + key);
		    }
		    
		    // base stats
		    try {
				int hp = pokemonJS.getJSONObject("baseStats").getInt("hp");
				int atk = pokemonJS.getJSONObject("baseStats").getInt("atk");
				int def = pokemonJS.getJSONObject("baseStats").getInt("def");
				int spa = pokemonJS.getJSONObject("baseStats").getInt("spa");
				int spd = pokemonJS.getJSONObject("baseStats").getInt("spd");
				int spe = pokemonJS.getJSONObject("baseStats").getInt("spe");
				baseStats = new BaseStats(hp, atk, def, spa, spd, spe);
		    } catch(JSONException ex) {
				baseStats = null;
				System.out.println("Error. No base stats for key " + key);
		    }
		    
		    // abilities
		    String zero;
		    String one;
		    String h;
		    try {
		    	zero = pokemonJS.getJSONObject("abilities").getString("0");
		    } catch(JSONException ex) {
				zero = null;
				System.out.println("Error. No 0 ability for key " + key);
		    }
		    try {
		    	one = pokemonJS.getJSONObject("abilities").getString("1");
		    } catch(JSONException ex) {
		    	one = null;
		    }
		    try {
		    	h = pokemonJS.getJSONObject("abilities").getString("H");
		    } catch(JSONException ex) {
		    	h = null;
		    }
		    abilities = new Abilities(zero, one, h);
		    
		    pokedexList.add(new Pokemon(key, num, species, baseSpecies, formLetter, types, baseStats, abilities));
//		    System.out.println("Added Pokemon " + species + " to Pokedex");
		}
		
		// convert ArrayList to array
		pokedex = pokedexList.toArray(new Pokemon[pokedexList.size()]);
		
		System.out.print("complete. Time: " + (System.currentTimeMillis() - startTime) / 1000 + "s\n");
	}
    
    private static void initTiers() {
    	System.out.print("Initializing tiers... ");
    	double startTime = System.currentTimeMillis();
    	
    	// ArrayList for allocation
    	ArrayList<Tier> tiersList = new ArrayList<Tier>();
    	
		// convert formats.js to String
		String formatsString = "";
		try {
		    formatsString = new String(Files.readAllBytes(Paths.get(formatsPath)));
		}
		catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		// parse formatsString to tiersList
		JSONObject formatsJSONObject = new JSONObject(formatsString);
		Iterator<String> keys = formatsJSONObject.keys(); // keys for all pokemon
		
		
		while(keys.hasNext()) {
			String tierName = null;
			Pokemon p = null;
			
		    String key = keys.next();
		    JSONObject pokemonJS = formatsJSONObject.getJSONObject(key);
		    
		    p = pokemonFromKey(key);
		    
		    // find tier
		    try {
		    	tierName = pokemonJS.getString("tier");
		    }
		    catch (JSONException ex) {
//				System.out.println("No tier for key " + key);
		    }
		    
		    // create tiers list
		    if (tierName != null) {
		    	// create new tier if it doesn't exist yet
		    	boolean isNew = true;
			    for (Tier t : tiersList) {
			    	if (t.name.equals(tierName)) {
			    		isNew = false;
			    	}
			    }
			    if (isNew) tiersList.add(new Tier(tierName));
			    
			    // add pokemon to corresponding tier after tier has been added
			    for (Tier t : tiersList) {
			    	if (t.name.equals(tierName)) {
				    	t.addPokemon(p);
//				    	System.out.println("Added Pokemon " + p.species + " to tier " + tierName);
			    	}
			    }
		    }
		}
		
		// sort tiers alphabetically
		for (Tier t : tiersList) {
			Arrays.sort(t.pokemon, new PokemonComparator());
		}
		
		// convert ArrayList to array
		tiers = tiersList.toArray(new Tier[tiersList.size()]);
		
		System.out.print("complete. Time: " + (System.currentTimeMillis() - startTime) / 1000 + "s\n");
    }
    
    private static void initBuildSet() {
    	// ArrayList for allocation
    	ArrayList<Pokemon> buildList = new ArrayList<Pokemon>();
    	
    	// check if preset build set exists
    	if (presetBuildSet.length != 0) {
    		buildList = pokemonListFromStrings(presetBuildSet);
    	}
    	else {
	    	// add preset tiers
	    	for (String s : presetTiers) {
	    		for (Tier t : tiers) {
	    			if (t.name.equals(s)) {
	    				buildList.addAll(new ArrayList<Pokemon>(Arrays.asList(t.pokemon)));
	    			}
	    		}
	    	}
	    	
	    	// add additional pokemon
	    	for (String s : additionalPokemon) {
	    		if (!buildList.contains(pokemonFromString(s)))
	    			buildList.add(pokemonFromString(s));
	    	}
	    	
	    	// remove excluded pokemon
	    	ArrayList<Pokemon> tempPokemonList = new ArrayList<Pokemon>(buildList); // pokemonList itself can't be modified when looping
	    	for (Pokemon p : tempPokemonList) {
	    		for (String s : excludedPokemon) {
	    			if (p.species.equals(s)) {
	    				if(buildList.remove(p)) {
//	    					System.out.println("Removed " + p.species + " from build set");
	    				}
	    			}
	    		}
    		}
    	}
    	
    	// remove pokemon from preset team
    	ArrayList<Pokemon> tempPokemonList = new ArrayList<Pokemon>(buildList); // pokemonList itself can't be modified when looping
    	for (Pokemon p : tempPokemonList) {
	    	for (String s : presetTeam) {
				if (p.species.equals(s)) {
					if(buildList.remove(p)) {
//						System.out.println("Removed " + p.species + " from build set");
					}
				}
			}
    	}
    	
    	// convert ArrayList to array
    	buildSet = buildList.toArray(new Pokemon[buildList.size()]);
    	
    	// check if build set has at least 6 pokemon
    	if (buildSet.length < 6) {
    		System.out.println("Error. Build set does not have enough Pokemon. Exiting...");
    		System.exit(0);
    	}
    	
    	// sort build set alphabetically
    	Arrays.sort(buildSet, new PokemonComparator());
    }
    
    @SuppressWarnings("unused")
	private static void analyzeSampleTeams() {
    	ArrayList<Pokemon[]> sampleTeams = new ArrayList<Pokemon[]>();
    	
    	// convert typechart.txt to String
		String sampleTeamsStringFull = "";
		try {
			sampleTeamsStringFull = new String(Files.readAllBytes(Paths.get(sampleTeamsPath)));
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		// separate lines
		String[] sampleTeamsStringLines = sampleTeamsStringFull.split("\n"); // delimeter "\n"
		
		// separate pokemon and add teams to teams list
		String[][] sampleTeamsString = new String[sampleTeamsStringLines.length][];
		for (int i = 0; i < sampleTeamsStringLines.length; i++) {
			sampleTeamsString[i] = sampleTeamsStringLines[i].split(","); // delimeter ","
			if (sampleTeamsString[i].length == 6) {
				sampleTeams.add(pokemonListFromStrings(sampleTeamsString[i]).toArray(new Pokemon[6]));
			}
			else System.out.println("Sample team " + (i+1) + " has incorrect size");
		}
		
		// calculate type matchups, resistances, and weaknesses for each type
		int[] totalResistances = new int[types.length];
		int[] totalWeaknesses = new int[types.length];
		
		int[] resistanceAnomalies = new int[types.length];
		int[] weaknessAnomalies = new int[types.length];
		
		int resistanceAnomalyCutoff = 2;
		int weaknessAnomalyCutoff = 2;
		
		for (Pokemon[] sampleTeam : sampleTeams) {
			int[][] typeMatchupsChart = calcMatchupChart(sampleTeam);
			int[] resistances = calcResistances(typeMatchupsChart);
			int[] weaknesses = calcWeaknesses(typeMatchupsChart);
			
			for (int i = 0; i < types.length; i++) {
				totalResistances[i] += resistances[i];
				totalWeaknesses[i] += weaknesses[i];
			}
			
			// print team
			System.out.print("Team: ");
			for (Pokemon p : sampleTeam)
				System.out.print(p.species + "   ");
			System.out.println();
			
			// print type matchup chart
			for (int i = 0; i < types.length; i++) {
				System.out.print(String.format("%10s: ", types[i]));
				for (int j = 0; j < typeMatchupsChart[i].length; j++)
					System.out.print(typeMatchupsChart[i][j] + "   ");
				System.out.print("Resistances: " + resistances[i] + "   Weaknesses: " + weaknesses[i]);
				System.out.println();
			}
			
			// find and print resistance anomalies (< a)
			for (int i = 0; i < resistances.length; i++) {
				if (resistances[i] < resistanceAnomalyCutoff) {
					System.out.print(resistances[i] + " " + types[i] + " Resist(s)   ");
					resistanceAnomalies[i]++;
				}
			}
			System.out.println();
			
			// find and print weakness anomalies (> b)
			for (int i = 0; i < weaknesses.length; i++) {
				if (weaknesses[i] > weaknessAnomalyCutoff) {
					System.out.print(weaknesses[i] + " " + types[i] + " Weakness(es)   ");
					weaknessAnomalies[i]++;
				}
			}
			System.out.println();
			
			System.out.println();
		}
		
		// total analysis
		System.out.println("Sample size: " + sampleTeams.size());
		for (int i = 0; i < types.length; i++) {
			System.out.println(String.format("%10s: %2d Resistances   %2d Weaknesses   %2dx < %1d %8s Resists   %2dx > %1d Weaknesses", types[i], totalResistances[i], totalWeaknesses[i], resistanceAnomalies[i], resistanceAnomalyCutoff, types[i], weaknessAnomalies[i], weaknessAnomalyCutoff, types[i]));
		}
		
		System.out.println();
    }
    
    private static void buildTeams() {
    	teams = new ArrayList<Pokemon[]>();
    	Pokemon[] team = new Pokemon[6];
    	
    	int presets = presetTeam.length;
		
		boolean preset1 = false;
		boolean preset2 = false;
		boolean preset3 = false;
		boolean preset4 = false;
		boolean preset5 = false;
		boolean preset6 = false;
		
		// set preset members
		switch (presets) {
		case 6:
			team[5] = pokemonFromString(presetTeam[5]);
			preset6 = true;
		case 5:
			team[4] = pokemonFromString(presetTeam[4]);
			preset5 = true;
		case 4:
			team[3] = pokemonFromString(presetTeam[3]);
			preset4 = true;
		case 3:
			team[2] = pokemonFromString(presetTeam[2]);
			preset3 = true;
		case 2:
			team[1] = pokemonFromString(presetTeam[1]);
			preset2 = true;
		case 1:
			team[0] = pokemonFromString(presetTeam[0]);
			preset1 = true;
		}
		
		int istart;
		int jstart;
		int kstart;
		int lstart;
		int mstart;
		int nstart;
		
		int iend;
		int jend;
		int kend;
		int lend;
		int mend;
		int nend;
		
		if (preset1) iend = 1; else iend = buildSet.length;
		if (preset2) jend = 1; else jend = buildSet.length;
		if (preset3) kend = 1; else kend = buildSet.length;
		if (preset4) lend = 1; else lend = buildSet.length;
		if (preset5) mend = 1; else mend = buildSet.length;
		if (preset6) nend = 1; else nend = buildSet.length;
		
		System.out.println("Started building process...");
		
		iterations = 0;
		buildTotalTime = 0;
		buildStartTime = System.currentTimeMillis();
		
		istart = 0;
//		OuterLoop:
		for (int i = istart; i < iend; i++) { // 1st member
			if (preset1) jstart = 0; else jstart = i + 1;
			for (int j = jstart; j < jend; j++) { // 2nd member
				if (preset2) kstart = 0; else kstart = j + 1;
				for (int k = kstart; k < kend; k++) { // 3rd member
					if (preset3) lstart = 0; else lstart = k + 1;
					for (int l = lstart; l < lend; l++) { // 4th member
						if (preset4) mstart = 0; else mstart = l + 1;
						for (int m = mstart; m < mend; m++) { // 5th member
							if (preset5) nstart = 0; else nstart = m + 1;
							InnerLoop:
							for (int n = nstart; n < nend; n++) { // 6th member
								iterations++;
								
								if (!preset1) team[0] = buildSet[i];
								if (!preset2) team[1] = buildSet[j];
								if (!preset3) team[2] = buildSet[k];
								if (!preset4) team[3] = buildSet[l];
								if (!preset5) team[4] = buildSet[m];
								if (!preset6) team[5] = buildSet[n];
								
								// progress update
								if (iterations % 100000 == 0) {
									System.out.print(String.format("%10d iterations", iterations) + "\tLatest Team: ");
									for (Pokemon p : team)
										System.out.print(String.format("%17s", p.species) + "   ");
									System.out.println();
								}
								
								// conditions: no more than 1 mega, mega and baseSpecies can't be on the same team
								int megaCounter = 0;
								for (Pokemon p : team) {
									if ((p.formLetter != null) && p.formLetter.equals("M")) {
										megaCounter++;
										
										// check if non-mega form is on the team
										for (Pokemon p2 : team) {
											if (p.baseSpecies.equals(p2.species)) continue InnerLoop;
										}
									}
								}
								if (megaCounter > 1) continue InnerLoop;
								
								// TODO add condition: no alternate forms
								
								// condition: Stealth Rocks
								if (stealthRocks && Collections.disjoint(Arrays.asList(team), pokemonListFromStrings(preferredStealthRockers))) continue InnerLoop;
								
								// condition: Spikes
								if (spikes && Collections.disjoint(Arrays.asList(team), pokemonListFromStrings(preferredSpikers))) continue InnerLoop;
								
								// condition: Toxic Spikes
								if (toxicSpikes && Collections.disjoint(Arrays.asList(team), pokemonListFromStrings(preferredToxicSpikers))) continue InnerLoop;
								
								// condition: Defog or Rapid Spin
								if (defog && rapidSpin) { // if both are true, only one of the two is required
									if ((Collections.disjoint(Arrays.asList(team), pokemonListFromStrings(preferredDefoggers)) && (Collections.disjoint(Arrays.asList(team), pokemonListFromStrings(preferredRapidSpinners)))))
										continue InnerLoop;
								}
								else {
									if (defog && Collections.disjoint(Arrays.asList(team), pokemonListFromStrings(preferredDefoggers))) continue InnerLoop;
									if (rapidSpin && Collections.disjoint(Arrays.asList(team), pokemonListFromStrings(preferredRapidSpinners))) continue InnerLoop;
								}
								
								// calculate type matchup chart, resistances, and weaknesses
								int[][] typeMatchupsChart = calcMatchupChart(team);
								int[] resistances = calcResistances(typeMatchupsChart);
								int[] weaknesses = calcWeaknesses(typeMatchupsChart);
								
								// condition: the team must have a minimum number of resistances for every type
								for (int x = 0; x < resistances.length; x++) {
									switch (types[x]) {
									case Type.GHOST:
										continue;
									case Type.DARK:
										if (resistances[x] < 1) continue InnerLoop;
										break;
									default:
										if (resistances[x] < 2) continue InnerLoop;
									}
								}
								
								// condition: the team must have at least 1 ground immunity
								if (typeMatchupsChart[typeIndexOf(Type.GROUND)][0] == 0)
									continue InnerLoop;
								
								// condition: the team must have a maximum number of weaknesses for every type
								for (int x = 0; x < weaknesses.length; x++) {
									if (weaknesses[x] > 2) continue InnerLoop;
								}
								
								// all conditions are met
								buildTotalTime += (System.currentTimeMillis() - buildStartTime);
								
								// add clone of team to teams array, so that no reference exists
								teams.add(team.clone());
								
								// print team and type matchup chart
								System.out.print("Team " + teams.size() + ": ");
								for (Pokemon p : team)
									System.out.print(p.species + "   ");
								System.out.println();
								
								for (int x = 0; x < types.length; x++) {
									System.out.print(String.format("%10s: ", types[x]));
									for (int y = 0; y < typeMatchupsChart[x].length; y++)
										System.out.print(typeMatchupsChart[x][y] + "   ");
									System.out.println();
								}
								
								System.out.println("Iterations: " + iterations);
								System.out.println("Time: " + buildTotalTime / 1000 + "s");
								System.out.println();
								
								buildStartTime = System.currentTimeMillis();
							}
						}
					}
				}
			}
		}
		
		buildTotalTime += (System.currentTimeMillis() - buildStartTime);
		System.out.println("Calculations complete.");
		
		// print teams
		for (int i = 0; i < teams.size(); i++) {
			System.out.print(String.format("Team %3s:", (i+1)));
			for (int j = 0; j < teams.get(i).length; j++)
				System.out.print(String.format("%17s", teams.get(i)[j].species) + "   ");
			System.out.println();
		}
		
		// print information
		System.out.print("Conditions: ");
		if (stealthRocks) System.out.print("Stealth Rocks   ");
		if (spikes) System.out.print("Spikes   ");
		if (toxicSpikes) System.out.print("Toxic Spikes   ");
		if (defog) System.out.print("Defog   ");
		if (rapidSpin) System.out.print("Rapid Spin");
		System.out.println();
		
		System.out.println("Build selection size: " + buildSet.length);
		System.out.println("Iterations: " + iterations);
		System.out.println("Total time: " + buildTotalTime / 1000 + "s");
    }
    
    private static int[][] calcMatchupChart(Pokemon[] t) {
    	int[][] typeMatchupsChart = new int[types.length][6]; // {0x, 0.25x, 0.5x, 1x, 2x, 4x}
    	
		for (int x = 0; x < t.length; x++) {
			double[] typeMatchups = calcTypeMatchups(t[x]);
			// add resistances cumulatively
			for (int y = 0; y < typeMatchups.length; y++) {
				if (typeMatchups[y] == 0) typeMatchupsChart[y][0]++;		// immune
				if (typeMatchups[y] == 0.25) typeMatchupsChart[y][1]++;		// 0.25x resistance
				if (typeMatchups[y] == 0.5) typeMatchupsChart[y][2]++;		// 0.5x resistance
				if (typeMatchups[y] == 1) typeMatchupsChart[y][3]++;		// neutral
				if (typeMatchups[y] == 2) typeMatchupsChart[y][4]++;		// 2x weakness
				if (typeMatchups[y] == 4) typeMatchupsChart[y][5]++;		// 4x weakness
			}
		}
		
		return typeMatchupsChart;
    }
    
    private static int[] calcResistances(int[][] typeMatchupsChart) {
    	int[] resistances = new int[types.length];
    	
		for (int x = 0; x < types.length; x++) {
			resistances[x] = typeMatchupsChart[x][0] + typeMatchupsChart[x][1] + typeMatchupsChart[x][2];
		}
		
		return resistances;
    }
    
    private static int[] calcWeaknesses(int[][] typeMatchupsChart) { 	
		int[] weaknesses = new int[types.length];
		
		for (int x = 0; x < types.length; x++) {
			weaknesses[x] = typeMatchupsChart[x][4] + typeMatchupsChart[x][5];
		}
		
		return weaknesses;
    }
    
    private static double[] calcTypeMatchups(Pokemon p) {
    	double[] typeMatchups = new double[types.length]; 
    	
    	// default to all 1's
    	for (int i = 0; i < typeMatchups.length; i++) {
    		typeMatchups[i] = 1;
    	}
    	
    	// successively multiply matchup value of each type
		for (int i = 0; i < types.length; i++) { // attacking type
			for (String s : p.types) { // defending type
				switch (typeChart[typeIndexOf(s)][i]) {
				case 0: // normal
					// value stays the same
					break;
				case 1: // super effective
					typeMatchups[i] *= 2;
					break;
				case 2: // not very effective
					typeMatchups[i] *= 0.5;
					break;
				case 3: // immune
					typeMatchups[i] *= 0;
					break;
				default:
					System.out.println("Error. Unknown damageTaken value: " + typeChart[typeIndexOf(s)][i]);
				}
			}
		}
		
		// handle special ability immunities
		switch (p.abilities.zero) {
		case "Levitate":
			typeMatchups[typeIndexOf("Ground")] = 0;
			break;
		case "Flash Fire":
			typeMatchups[typeIndexOf("Fire")] = 0;
			break;
		case "Water Absorb":
		case "Storm Drain":
			typeMatchups[typeIndexOf("Water")] = 0;
			break;
		case "Sap Sipper":
			typeMatchups[typeIndexOf("Grass")] = 0;
			break;
		case "Lightning Rod":
		case "Volt Absorb":
		case "Motor Drive":
			typeMatchups[typeIndexOf("Electric")] = 0;
			break;
		}
		
		if (p.abilities.one != null) {
			switch (p.abilities.one) {
			case "Levitate":
				typeMatchups[typeIndexOf("Ground")] = 0;
				break;
			case "Flash Fire":
				typeMatchups[typeIndexOf("Fire")] = 0;
				break;
			case "Water Absorb":
			case "Storm Drain":
				typeMatchups[typeIndexOf("Water")] = 0;
				break;
			case "Sap Sipper":
				typeMatchups[typeIndexOf("Grass")] = 0;
				break;
			case "Lightning Rod":
			case "Volt Absorb":
			case "Motor Drive":
				typeMatchups[typeIndexOf("Electric")] = 0;
				break;
			}
		}
		
		if (p.abilities.one != null) {
			switch (p.abilities.one) {
			case "Levitate":
				typeMatchups[typeIndexOf("Ground")] = 0;
				break;
			case "Flash Fire":
				typeMatchups[typeIndexOf("Fire")] = 0;
				break;
			case "Water Absorb":
			case "Storm Drain":
				typeMatchups[typeIndexOf("Water")] = 0;
				break;
			case "Sap Sipper":
				typeMatchups[typeIndexOf("Grass")] = 0;
				break;
			case "Lightning Rod":
			case "Volt Absorb":
			case "Motor Drive":
				typeMatchups[typeIndexOf("Electric")] = 0;
				break;
			}
		}
		
    	return typeMatchups; // every matchup has a value of 0, 0.25, 0.5, 1, or 2
    }
    
    private static int typeIndexOf(String s) {
    	for (int i = 0; i < types.length; i++) {
    		if (s.equals(types[i]))
    			return i;
    	}
    	
    	System.out.println("Could not get index of type " + s);
    	return -1;
    }
    
    private static Pokemon pokemonFromKey(String s) {
    	for (Pokemon p : pokedex) {
    		if (p.key.equalsIgnoreCase(s))
    			return p;
    	}
    	
    	System.out.println("Error. Can't get Pokemon with key " + s + " from pokedex");
    	return null;
    }
    
    private static Pokemon pokemonFromString(String s) {
    	for (Pokemon p : pokedex)
    		if (p.species.equalsIgnoreCase(s))
    			return p;
    	
    	System.out.println("Error. Can't get Pokemon with species " + s + " from pokedex");
    	return null;
    }
    
    private static ArrayList<Pokemon> pokemonListFromStrings(String[] strings) {
    	ArrayList<Pokemon> pokemonList = new ArrayList<Pokemon>();
    	
    	for (String s : strings)
    		pokemonList.add(pokemonFromString(s));
    	
    	return pokemonList;
    }
    
}
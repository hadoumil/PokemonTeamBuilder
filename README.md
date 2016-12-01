# PokemonTeamBuilder
The goal of this project is to develop an algorithm that can build competitive Pokemon teams. There is no UI yet so you will have to manually configure the setPresets() method in TeamBuilder.java if you want to use the program.
First, you a list of utility move requirements to choose from:
<ul>
  <li>Stealth Rocks
  <li>Spikes
  <li>Toxic Spikes
  <li>Defog
  <li>Rapid Spin
</ul>
If both defog and rapidSpin are true, only one of the two will be required. You should set your preferred users for these moves in the respective arrays:
<ul>
  <li>preferredStealthRockers
  <li>preferredSpikers
  <li>preferredToxicSpikers
  <li>preferredDefoggers
  <li>preferredRapidSpinners
</ul>
Next, you can choose the core members of your team by configuring the presetTeam[] array (or just leave it blank). The buildSet[] determines the Pokemon considered for filling the rest of your team. Since gen7 tiers are not released yet, ignore the presetTiers[], additionalPokemon[], and excludedPokemon[] arrays. I added the most common gen7 Pokemon to presetBuildSet[] for now.<br/>
The main algorithm is contained in the buildTeams() method. It cycles through all possible combinations of Pokemon to fill the remained of your team and decides whether a team is valid based on the following conditions:
<ol>
  <li>The team has no more than 1 mega evolution, and a mega and its baseSpecies are not on the same team
	<li>The utility move requirements are fulfilled
</ol>
It also decides on whether a team is "competitive" based on these criteria:
<ol>
	<li>The team has a minimum number of 0 ghost resists, 1 dark resist, and 2 resists for every other type
	<li>The team has at least 1 ground immunity
	<li>The team has a maximum of 2 weaknesses against every type
</ol>
These criteria can be customized and are currently based on an analysis of a set of sample teams performed by the optional analyzeSampleTeams() method. In the future, the algorithm will be much more complexed and probably based on metagame usage statistics and checks and counters calculations. Check out the todos file to see some implementation ideas.

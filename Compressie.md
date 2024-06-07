# Compressie

# Inleiding
Compressie is het verkleinen van de bestandsgrootte van een bestand. Dit kan
gebeuren door het bestand te comprimeren. Er zijn verschillende manieren om
bestanden te comprimeren. In dit artikel worden de verschillende manieren
besproken.

# Lossless compressie
Bij lossless compressie wordt het bestand verkleind zonder dat er informatie
verloren gaat. Dit betekent dat het bestand na decompressie exact hetzelfde is
als het originele bestand. Lossless compressie wordt vaak gebruikt voor
tekstbestanden, zoals broncode en documenten.

# Lossy compressie
Bij lossy compressie wordt het bestand verkleind door informatie weg te laten.
Dit betekent dat het bestand na decompressie niet exact hetzelfde is als het
originele bestand. Lossy compressie wordt vaak gebruikt voor audio- en
videobestanden, waarbij een kleine afwijking in de kwaliteit niet merkbaar is.

# Compressiealgoritmen
Er zijn verschillende compressiealgoritmen die gebruikt kunnen worden om
bestanden te comprimeren. Enkele bekende compressiealgoritmen zijn:

- Run-length encoding (RLE)
- Burrows-Wheeler-transformatie (BWT)
- Huffman-codering

## Run-length encoding (RLE)
Run-length encoding (RLE) is een eenvoudig compressiealgoritme dat herhalende
tekens in een bestand vervangt door het teken en het aantal herhalingen. Dit
kan vooral effectief zijn bij bestanden met lange reeksen van hetzelfde teken.
Vaak wordt RLE gebruikt als onderdeel van andere compressiealgoritmen.

## Burrows-Wheeler-transformatie (BWT)
De Burrows-Wheeler-transformatie (BWT) is een algoritme dat de volgorde van de
tekens in een bestand verandert, zodat herhalende tekens dicht bij elkaar
komen te staan. Hierdoor wordt het bestand beter comprimeerbaar. De BWT wordt
vaak gebruikt in combinatie met andere compressiealgoritmen, zoals de
Move-to-Front-transformatie en de Huffman-codering.

## Huffman-codering
Huffman-codering is een compressiealgoritme dat gebruikmaakt van een
variabele-lengtecode om tekens te vervangen door kortere codes voor tekens die
vaak voorkomen en langere codes voor tekens die minder vaak voorkomen. Hierdoor
kunnen bestanden efficiënter gecomprimeerd worden. Huffman-codering wordt vaak
gebruikt in combinatie met andere compressiealgoritmen, zoals de BWT.

# Binnen het ASD project
Binnen het ASD project gaan we nieuwe spelers die binnen komen in het spel de 
volledige gamestate opsturen. Dit bestand gaat redelijk veel data bevatten. Ik 
wil eerst kijken hoeveel data dit zou zijn als het in een json bestand zou 
worden opgestuurd.

## Welke data moet er worden opgestuurd?
Veel van de data die nodig is voor het spel kan gegenereerd worden door de
client zelf, met een seed. De data die opgestuurd moet worden is alle dynamische
data, zoals de posities van de spelers en monsters, de hoeveelheid items op de
grond en in de inventories. De data die opgestuurd moet worden is als volgt:
### Volledige gamestate:

    Pos = [pos: x, y, z]
    Item = [id]
    Inventory = [Item]
    GroundItems = [Item][Pos]

    Player = [Player id][Pos][healthpoints][level][CurrectXp][Inventory]

    Monster = [monsterid][Pos][healthpoints]

    Floor = [Players][Monsters][GroundItems]
    Gamestate = [Floors]

In het bestand `data.json` staat de volledige gamestate van één vloer.
In json formaat, de grootte dit bestand is 173228 bytes.
    
    json: 173228 bytes -> 169,16 kb -> 0,16 mb

Dit is een redelijk groot bestand om op te sturen. Ik wil kijken of ik dit
bestand kan comprimeren.

### Optimalisatie van datastructuur
Om het bestand te verkleinen had ik in eerste instantie gekeken naar het 
compressen van de json file. Echter, ik kwam erachter dat de json file al
redelijk klein was. Ik heb gekeken naar de datastructuur van de json file en
gezien dat er veel data redundant is. Hierdoor heb ik gekozen om de datastructuur
te veranderen. Ik heb de datastructuur veranderd naar een binair formaat.

    Pos = [pos: 12 bits each for x, y]
    Item = [id: 4 bits]
    Inventory = [Amount of items: 4 bits] [[Item]...repeat for amount of items]
    GroundItems = [Amount of items: 12 bits] [[Item][Pos]...repeat for amount of items]

    Player = [001:playerid 8 bits][Pos][healthpoints: 8 bits][level: 8 bits][CurrectXp: 8 bits][Inventory]
    Players = [Amount of players: 8 bits] [[Player]...repeat for amount of players]

    Monster = [monsterid: 4 bits][Pos][healthpoints: 8 bits]
    Monsters = [Amount of monsters: 12 bits] [[Monster]...repeat for amount of monsters]

    Floor = [[z: 12 bits][Players][Monsters][GroundItems]]
    Gamestate = [Amount of floors: 12 bits][[Floor]...repeat for each floor]

In het bestand `data.bin` staat de volledige gamestate van één vloer.
In binair formaat, de grootte dit bestand is 25293 bytes.

    optimized data structure: 25293 bytes -> 24,70 kb -> 0,024 mb

Dit is een optimalisatie van 86% ten opzichte van de json file.

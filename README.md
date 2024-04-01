# Cinema

Simple cinema application that lets the user filter movies, get recommendations and select seats.

## Installation and setup

1) Clone this repo
In your terminal run:
```
git clone [https://github.com/Rsand03/cinema-application.git]
```
2) Open the project in your IDE (preferably IntelliJ):
3) Run CinemaApplication.java
4) Navigate to http://localhost:8080/ in your browser

## Usage
Kirjutan selle osa eesti keeles, kuna ka proovitöö juhend oli eesti keeles.

Avalehel kuvatakse kasutajale esialgu kõik kinoseansid. Igal seansil on filmi pealkiri, žanr, vanusepiirang, algusaeg ning keel.
### Filmide sorteerimine
Veebilehe ülemises osas on dropdown menüüd. Kasutaja saab valida, mille järgi filme sorteeritakse. Kui valitud on "-", siis antud kategooria alusel filme ei filtreerita. Filtreerimise teostamiseks tuleb paremal pool vajutada nuppu "Apply filter". Seejärel ilmuvad sobivad filmid ekraanile või kuvatakse veateade, et ühtegi filmi ei leitud. Algse menüü juurde naasmiseks tuleb vajutada nuppu "All movies".

### Filmide soovitamine
Soovitatud filmide seansside saamiseks tuleb vajutada nuppu "Recommended movies". Kui kasutajal puudub vaatamiste ajalugu (ehk ühtegi piletit pole veel ostetud), siis kuvatakse veateade. Filme soovitatakse eelnevalt vaadatud filmide žanrite põhjal. Juba vaadatud filmide seansse ei soovitata, isegi kui seansi keel või algusaeg on erinevad.

### Pileti(te) ostmine
Pileti ostmiseks tuleb märkida soovitud seanss (seansil klikkides) ning valida ostetavate piletite arv. Võimalik on osta 1 kuni 5 piletit, vaikimisi on piletite arv 1. Ostu sooritamiseks tuleb vajutada rohelist nuppu "BUY". Kui pole valitud ühtegi filmi, kuvatakse veateade.

### Istekohad
Peale "BUY" nupu vajutamist suunatakse kasutaja saali istekohtade plaani juurde. Määratud kohad on märgitud rohelisega. Kui pileteid on mitu, siis soovitatakse esmajärjekorras istekohti, mis asuvad kõik üksteise kõrval ning ühes reas. Lisaks sellele võimalikult saali keskel. Kui valitud on näiteks 5 piletit ning viit üksteise kõrval asetsevat vaba istet ei leidu, siis valitakse ükskõik millised võimalikult saali keskel asetsevad istmed. Viimasel juhul kuvatakse ka vastav teade. "Back" nupp viib tagasi filmide valimise lehele (kasutaja vaatamiste ajalugu salvestub).

## Projekti kokkuvõte
* Realiseerisin kõik nõutud funktsionaalsused, välja arvatud terve nädala kinokava kuvamise. Selle lahendamiseks oleks vaja kasutada LocalDate objekti koos kuupäevaga, või siis front-end'is filmid suvaliselt erinevate nädalapäevade vahel ära jagada.
* Peaaegu kogu kood on minu enda kirjutatud. Kopeerisin ainult mõned HTML ja CSS *template*id, ning needki pärinevad mu varasematest projektides.
* Abi saamiseks kasutasin Spring booti Youtube tutoriale, googeldamist, Stackoverflowi ning CSSi jaoks ka chatGPTd.
* Kõige keerulisem osa projekti juures oli CSSi abil kinosaali plaani kuvamine, millele aitas lõpuks lahenduse leida Stackoverflow. Istekohtade front-end oli isegi keerulisem kui back-endi soovitamise algoritm.
* Kood toetab valdavalt uute filmide, seansiaegade, žanrite jne lisamist, kuid ainus "staatiline" takistus on front-endi filtreerimise valikute dropdown menüüd. Neid ei jõudnud kahjuks api requestide abil dünaamiliseks muuta. Dünaamiliste menüüdega oleks võinud juba mõne filmide andmebaasi integreerimise peale mõelda.
* Filmid ja seansid on omavahel eraldatud. Filmidel on pealkiri, žanr ja vanusepiirang, seansil on film (koos selle parameetritega), algusaeg ning keel. Üldise loogika osas on ainus "viga" see, et filmidele määratakse žanr ja vanusepiirang juhuslikult. Võib esineda huvitavaid kombinatsioone.
* Kahjuks jõudsin testid lisada ainult Seat klassile, kuigi põhjalikud testid on iga projekti lahutamatu osa.
* Kasulikud konstandid, mille muutmist võib katsetada: SESSION_COUNT, SEAT_BEING_OCCUPIED_PROBABILITY.

## Contributing

RSand03

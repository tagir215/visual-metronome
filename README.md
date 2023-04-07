# VisualMetronome
<img width="350" alt="visualmetronomekuvat" src="https://user-images.githubusercontent.com/117892331/225331975-8f40658c-4e62-435a-9668-9dda001c0f55.png">

*Java, Android Studio, OpenGL*

**Visual Metronome on musiikin harjoitteluun tarkoitettu apuohjelma. Joillakin muusikoilla rytmitaju saattaa heikentyä soittaessa monimutkaisempia kuvioita, jolloin aivot priorisoi rytmin pitämisen sijaan muita soittamiseen tarvittavia keskittymis alueita. Tässä ongelmana on myös se, että itse soittaja ei välttämättä edes kuule omia rytmi ongelmiaan. Visual Metronome pyrkii ratkaisemaan nämä ongelmat antamalla reaali aikaista selkeää feedbackiä, jolloin soittaja näkee virheet heti niiden tapahduttua.**

- Sovellus pyrkii tunnistamaan rytmejä musiikista
- Kokeilin erilaisia tapoja rytmien tunnistamiseen, kuten Fast Fourier transformia ja autokorrelaatiota, mutta päädyin lopulta vain laskemaan ääni aaltojen nollakohtien määrät nopeuden vuoksi, sekä ottamaan huomioon myös amplitudin muutokset ylöspäin. Jos nollakohtien määrä muuttuu edelliseen nuottiin verrattuna, tai jos amplitudi nousee ylöspäin yllättäen. niin se on todennäköisesti uusi nuotti
- Ääniallot ja rytmi kuviot on mallinnettu käyttäen OpenGL:ää
- Rytmi kuviot muodostuvat y suunnassa suurempina nollakohtien perusteella sekä taas keltaisena tai läpinäkyvän punaisena äänen amplitudin mukaan
- Sovelluksessa on mahdollisuus myös äänittää soittoa sekä toistaa soitto äänten hiljennettyä automaattisesti valitun viiveen jälkeen. Tällöin soittajan ei tarvitse jatkuvasti painella 'record' ja 'play' nappeja
- Äänityksen ollessa päällä, sovellus tallentaa nauhoitusta AudioBufferiin, josta se karsii n. sekunnin vanhat osiot pois, jotta äänitystä toistaessa nauhoitus alkaisi sopivasta kohdasta
- Ylesiä asetuksia esim. playback odotusajalle ja temmolle, sekä tahtilajin muutoksille
- Ohjelma toimii hyvin kitaran kanssa, mutta toistaiseksi vielä puutteellisesti esimerkiksi pianon kanssa, joten joitakin parannuksia pitää vielä tehdä

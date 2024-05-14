"use strict";
import {byId, toon, verberg, setText} from "./util.js";

const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`;
//console.log(werknemer)
setText("bestellingId", naam);
setText("naarWerknemer",naam);
const response = await fetch(`werknemers/${werknemer.id}/bestellingen`);
if (response.ok) {
    const bestellingen = await response.json();
    //console.log(bestellingen)
    const bestellingenBody = byId("bestellingenBody");
    for (const bestelling of bestellingen) {
        const tr = bestellingenBody.insertRow();
        tr.insertCell().innerText = bestelling.id;
        tr.insertCell().innerText = bestelling.omschrijving;
        tr.insertCell().innerText = bestelling.bedrag;
        tr.insertCell().innerText =
            new Date(bestelling.moment).toLocaleString("nl-BE");
    }
}else{
    toon("storing");
}
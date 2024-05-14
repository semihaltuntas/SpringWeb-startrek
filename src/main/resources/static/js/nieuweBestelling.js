"use strict";
import {byId, setText, toon, verberg} from "./util.js";
const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`;
//console.log(werknemer)
setText("bestellingId", naam);
setText("naarWerknemer",naam);

byId("bestel").onclick = async function () {
    verbergFouten();
    const omschrijvingInput = byId("omschrijving");
    if (!omschrijvingInput.checkValidity()) {
        toon("omschrijvingFout");
        omschrijvingInput.focus();
        return;
    }
    const bedragInput = byId("bedrag");
    if (!bedragInput.checkValidity()) {
        toon("bedragFout");
        bedragInput.focus();
        return;
    }
// JavaScript object maken
    const bestelling = {
        omschrijving: omschrijvingInput.value,
        bedrag: Number(bedragInput.value)
    };
    console.log(bestelling)
    bestel(bestelling);
};

function verbergFouten() {
    verberg("omschrijvingFout");
    verberg("bedragFout");
    verberg("storing");
}

async function bestel(bestelling) {
    verberg("nietGevonden");
    verberg("conflict");
    verberg("storing");
    const response = await fetch(`werknemers/${werknemer.id}/bestellingen`,
        {
            method: "POST",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(bestelling)
        });
    if (response.ok) {
        werknemer.budget -= bestelling.bedrag;
        sessionStorage.setItem("werknemer", JSON.stringify(werknemer));
        window.location = "vorigebestellingen.html";
    } else {
        switch (response.status) {
            case 404:
                toon("nietGevonden");
                break;
            case 409:
                const responseBody = await response.json();
                // console.log(responseBody)
                // console.log(responseBody.message)
                setText("conflict", responseBody.message);
                toon("conflict");
                break;
            default:
                toon("storing");
        }
    }
}
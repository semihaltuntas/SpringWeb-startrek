"use strict";
import {byId, toon} from "./util.js";
const response = await fetch("werknemers");
if (response.ok) {
    const werknemers = await response.json();
    //console.log(werknemers)
    const ul = byId("werknemers");
    for (const werknemer of werknemers) {
        const li = document.createElement("li");
        const hyperlink = document.createElement("a");
        hyperlink.href = "werknemer.html";
        hyperlink.innerText = `${werknemer.voornaam} ${werknemer.familienaam}`;
        hyperlink.onclick = function () {
            sessionStorage.setItem("werknemer", JSON.stringify(werknemer));
        }
        li.appendChild(hyperlink);
        ul.appendChild(li);
    }
} else {
    toon("storing");
}
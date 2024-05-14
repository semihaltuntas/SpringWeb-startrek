package be.vdab.startrek.bestellingen;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record NieuweBestelling( @NotBlank String omschrijving, @NotNull @Positive BigDecimal bedrag) {
}

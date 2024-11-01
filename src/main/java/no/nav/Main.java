package no.nav;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.saksbehandler.Saksbehandler;
import no.nav.årslønn.Årslønn;

public class Main {
    public static void main(String[] args) {
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator();
        Saksbehandler behandleSak = new Saksbehandler(dagpengerKalkulator);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 500000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2022, 450000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2021, 400000));
        System.out.println("\n\n---🤖 Kalkulerer dagsats... 🤖---");
        behandleSak.BehandlLønn();
        System.out.println("---🤖 Dagsats ferdig kalkulert 🤖---\n\n");
    }
}
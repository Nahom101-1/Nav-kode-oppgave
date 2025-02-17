package no.nav.dagpenger;

import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.årslønn.Årslønn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Kalkulator for å beregne hvor mye dagpenger en person har rett på i Norge basert på dagens grunnbeløp (1G).
 * For at en person skal ha rett på dagpenger, må en av de to følgene kravene være møtt:
 *      De siste 3 årene må gjennomsnitslønnen være høyere enn 3G.
 *      Tjent mer det siste året enn 1.5G.
 * Hvis en person har rett på dagpenger, må følgende ting vurderes for å kalkulere dagsatsen:
 *      Hva er størst av gjennomsnittlig årslønn de 3 siste årene og siste årslønn.
 *      Hvis siste årslønn er størst, er årslønnen høyere enn 6G.
 * Antall årlige arbeidsdager i Norge er satt til å være 260, så ved beregning av dagsats må 260 dager
 * brukes og ikke 365.
 *
 * @author Emil Elton Nilsen
 * @version 1.0
 */
public class DagpengerKalkulator {
    /**
     * enum: Alle regne  metoder
     */
    public enum BeregningsMetode{
        SISTE_ÅRSLØNN,
        GJENNOMSNITTET_AV_TRE_ÅR,
        MAKS_ÅRLIG_DAGPENGERGRUNNLAG
    }

    public final GrunnbeløpVerktøy grunnbeløpVerktøy;
    public final int ARBEIDSDAGER_PER_ÅR = 260;
    public final List<Årslønn> årslønner;

    private double dagsats_verdi; 
    private boolean harRettPåDagslønn;
    private BeregningsMetode beregningsMetode;

    //getters
    /**
    * Henter dagsats verdi.
    */
    public double getDagsatsVerdi() {
       return dagsats_verdi;
    }

    /**
    * Henter om personen har rett på dagslønn.
    */
    public boolean getHarRettPåDagslønn() {
        return harRettPåDagslønn;
    }

    /**
    * Henter den valgte beregningsmetoden.
    */
    public BeregningsMetode getBeregningsMetode() {
       return beregningsMetode;
    }

    public DagpengerKalkulator() {
        this.grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        this.årslønner = new ArrayList<>();
        this.dagsats_verdi = 0;
    }

    /**
     * Hvis en person har rett på dagpenger, vil den kalkulere dagsatsen en person har rett på.
     * Hvis ikke en person har rett på dagpenger, vil metoden returnere 0kr som dagsats, som en antagelse på at det
     * er det samme som å ikke ha rett på dagpenger.
     * @return dagsatsen en person har rett på.
     */
    public double kalkulerDagsats() {
        double dagsats;
        //Har ikke rett på Dagpenger
        if(!harRettigheterTilDagpenger()){
            return this.dagsats_verdi;
        };

        this.harRettPåDagslønn = true;
        // Beregn dagsats basert på valgt metode 
        switch (velgBeregningsMetode()) {
            case SISTE_ÅRSLØNN:
                dagsats = Math.ceil(hentÅrslønnVedIndeks(0).hentÅrslønn() / this.ARBEIDSDAGER_PER_ÅR);
                break;
            case GJENNOMSNITTET_AV_TRE_ÅR:
               dagsats = Math.ceil((summerNyligeÅrslønner(3) / 3) / this.ARBEIDSDAGER_PER_ÅR);
                break;
            case MAKS_ÅRLIG_DAGPENGERGRUNNLAG:
                dagsats = Math.ceil(grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag() / this.ARBEIDSDAGER_PER_ÅR);
                break;
            default: return 0;
        }
        this.dagsats_verdi = dagsats;
        return this.dagsats_verdi;
    }

    /**
     * Sjekker om en person har rettighet til dagpenger eller ikke.
     * @return om personen har rett på dagpenger basert på G(grunnlønn).
     */
    public boolean harRettigheterTilDagpenger() {
        // Sjekker om gjennomsnittlig lønn de siste 3 årene er >= 3G
        boolean harRettigheterBasertPåGjennomsnittslønn = 
        summerNyligeÅrslønner(3) >= grunnbeløpVerktøy.hentTotaltGrunnbeløpForGittAntallÅr(3);
    
        // Sjekker om lønn fra siste år er >= 1,5G
        boolean harRettigheterBasertPåSisteÅrslønn = 
        hentÅrslønnVedIndeks(0).hentÅrslønn() >= grunnbeløpVerktøy.hentMinimumÅrslønnForRettPåDagpenger();

        // Returnerer sann hvis en av betingelsene er oppfylt
        this.harRettPåDagslønn = harRettigheterBasertPåGjennomsnittslønn || harRettigheterBasertPåSisteÅrslønn;
        return harRettigheterBasertPåGjennomsnittslønn || harRettigheterBasertPåSisteÅrslønn;
    };


    /**
     * Velger hva som skal være beregnings metode for dagsats ut ifra en person sine årslønner.
     * @return beregnings metode for dagsats.
     */
    public BeregningsMetode velgBeregningsMetode() {
        // Hent lønn for det siste året og gjennomsnittet av de siste tre årene
        double sisteÅrslønn = hentÅrslønnVedIndeks(0).hentÅrslønn();
        double gjennomsnittsLønnTreÅr = (summerNyligeÅrslønner(3) / 3);
        double maksDagpengegrunnlag = grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag();

         // Bestem beregningsmetode basert på lønnsnivå og maksimumsgrenser
        if(sisteÅrslønn > gjennomsnittsLønnTreÅr){
            if(sisteÅrslønn > maksDagpengegrunnlag){
                this.beregningsMetode = BeregningsMetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG;
                return BeregningsMetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG;
            }
            this.beregningsMetode = BeregningsMetode.SISTE_ÅRSLØNN;
            return BeregningsMetode.SISTE_ÅRSLØNN;
        }else{
             this.beregningsMetode = BeregningsMetode.GJENNOMSNITTET_AV_TRE_ÅR;
            return BeregningsMetode.GJENNOMSNITTET_AV_TRE_ÅR;
        }
    };

   /**
   * Legger til Årslønn i array og sorterer lønnene.
   * @param årslønn Årslønn objektet som skal legges til.
   */
    public void leggTilÅrslønn(Årslønn årslønn) {
        this.årslønner.add(årslønn);
        this.sorterÅrslønnerBasertPåNyesteÅrslønn();
    }

    /**
     * Henter årslønnen i registeret basert på dens posisjon i registeret ved gitt indeks.
     * @param indeks posisjonen til årslønnen.
     * @return årslønnen ved gitt indeks.
     */
    public Årslønn hentÅrslønnVedIndeks(int indeks) {
        return this.årslønner.get(indeks);
    }

    /**
     * Summemer sammen antall årslønner basert på gitt parameter.
     * @param antallÅrÅSummere antall år med årslønner vi vil summere.
     * @return summen av årslønner.
     */
    public double summerNyligeÅrslønner(int antallÅrÅSummere) {
        double sumAvNyligeÅrslønner = 0;

        if (antallÅrÅSummere <= this.årslønner.size()) {
            List<Årslønn> subÅrslønnListe = new ArrayList<>(this.årslønner.subList(0, antallÅrÅSummere));

            for (Årslønn årslønn : subÅrslønnListe) {
                sumAvNyligeÅrslønner += årslønn.hentÅrslønn();
            }
        }

        return sumAvNyligeÅrslønner;
    }

    /**
     * Sorterer registeret slik at den nyligste årslønnen er det først elementet i registeret.
     * Først blir årslønnene i registeret sortert ut at den eldstre årslønnen skal først i registeret,
     * deretter blir registeret reversert.
     */
    public void sorterÅrslønnerBasertPåNyesteÅrslønn() {
        this.årslønner.sort(Comparator.comparingInt(Årslønn::hentÅretForLønn));
        Collections.reverse(this.årslønner);
    }
}

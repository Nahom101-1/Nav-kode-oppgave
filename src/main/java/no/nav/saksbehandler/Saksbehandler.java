package no.nav.saksbehandler;

// import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;

import no.nav.dagpenger.DagpengerKalkulator;

public class Saksbehandler extends DagpengerKalkulator {

    private Map<String, String> responsMap = new HashMap<>();
    private DagpengerKalkulator dagpengerKalkulator_;
    private double totalDagsPenger = 0;

    /**
    * Konstruktøren initialiserer og lagrer en instans av DagpengerKalkulator.
    * Denne metoden kaller initResponsMap for å sette opp responsmeldinger.
    *
    * @param dagpengerKalkulator Instansen av DagpengerKalkulator som skal lagres.
    * @see initResponsMap
    */
    public Saksbehandler(DagpengerKalkulator dagpengerKalkulator) {
       this.dagpengerKalkulator_= dagpengerKalkulator;
       this.initResponsMap();
    }

    /**
    *  Initialiserer responsmeldinger
    */
    private void initResponsMap() {
        //Respon ved for lav lønn
        responsMap.put("IKKE_NOK_LØNN", "😞 Beklager, men du har dessverre ikke rett på dagpenger på grunn av for lav inntekt. " +
        "For å kvalifisere, må du ha en gjennomsnittlig lønn de siste tre årene på minst 3G eller en lønn fra det siste året på minst 1.5G. 💔");
        //Respons ved rett på dag penger
        responsMap.put("HAR_RETT_PÅ_DAGPENGER", 
         "🎉 Gratulerer! Du oppfyller kravene for dagpenger! 💰 " +
         "Dette betyr at du kan motta støtte under arbeidsledighet. " +
         "Din gjennomsnittlig lønn de siste tre årene er minst 3G eller " +
         "er din lønn fra det siste året på minst 1.5G.\n\n");
        //Respons med beregning metode:
        responsMap.put("SISTE_ÅRSLØNN", "📅 Vi kalkulerer dagpengene dine basert på lønnen din fra det siste året.");
        responsMap.put("GJENNOMSNITTET_AV_TRE_ÅR", "📊 Vi kalkulerer dagpengene dine basert på gjennomsnittlig lønn de tre siste årene.");
        responsMap.put("MAKS_ÅRLIG_DAGPENGERGRUNNLAG", "📈 Vi kalkulerer dagpengene dine basert på maks årlig dagpengegrunnlag.");
        //Respons med total dagpenger
        responsMap.put("VIS_TOTAL_DAG_PENGER","💵 Dine totale dagpenger over perioden er: ");
    }; 

    /**
    * Behandler lønnsinformasjon for å avgjøre om brukeren har rett på dagpenger.
    * Hvis brukeren ikke har rett, vises en passende melding.
    * Hvis brukeren har rett, vises en bekreftelse og det beregnes hvor mye dagpenger de har rett på.
    * @see RegnUtDagPenger
    */
    public void behandleSøknad() {
        //Behandle om det ikker er nok lønn
        if (!dagpengerKalkulator_.getHarRettPåDagslønn()) {
            System.out.println(responsMap.get("IKKE_NOK_LØNN"));
            return;
        } 
        //Behandle søknad er nok lønn
        System.out.println(responsMap.get("HAR_RETT_PÅ_DAGPENGER"));
        System.out.println("Regner ut hvor mye dagpenger du har rett på................⏳⌛️");
        regnUtDagPenger();
    };



   /**
   * Behandler beregningsmetoden for dagpenger basert på resultatene fra 
   * den valgte metoden i dagpengerKalkulator_.velgBeregningsMetode().
   * @see RegnUtTotalDagpengr
   */
    public void regnUtDagPenger() {
        //Printer ut riktig Behandlings metode vha sin dagpengerKalkulator_.velgBeregningsMetode() resultater
        switch (dagpengerKalkulator_.getBeregningsMetode()) {
            case SISTE_ÅRSLØNN:
                System.out.println(responsMap.get("SISTE_ÅRSLØNN"));
                break;
            case GJENNOMSNITTET_AV_TRE_ÅR:
                System.out.println(responsMap.get("GJENNOMSNITTET_AV_TRE_ÅR"));
                break;
            case MAKS_ÅRLIG_DAGPENGERGRUNNLAG:
                System.out.println(responsMap.get("MAKS_ÅRLIG_DAGPENGERGRUNNLAG"));
                break;
            default:
                System.out.println("❌ Ugyldig beregningsmetode");
                return;
        }
        RegnUtTotalDagpengr();
    };

   /**
    * Behandler totalen av dagpengene basert på den valgte metoden.
    *  @see dagpengerKalkulator_.kalkulerDagsats for å få dagsatsen for den totale beregningen.
    */
   public void RegnUtTotalDagpengr() {
        //henter resultat for sum av dagpenger 
        this.totalDagsPenger = dagpengerKalkulator_.getDagsatsVerdi();
        System.out.println(responsMap.get("VIS_TOTAL_DAG_PENGER"));  System.out.println(this.totalDagsPenger);
   };

}

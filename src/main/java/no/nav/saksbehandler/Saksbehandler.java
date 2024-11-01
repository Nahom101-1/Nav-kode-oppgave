package no.nav.saksbehandler;

// import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;

import no.nav.dagpenger.DagpengerKalkulator;

public class Saksbehandler extends DagpengerKalkulator {

    private Map<String, String> responsMap = new HashMap<>();
    private DagpengerKalkulator dagpengerKalkulator_;
    // private double totalDagsPenger = 0;

    // public enum muligUtfall {
    //     AVSLAG_LAV_LØNN,
    //     INNVILGET,
    //     INNVILGET_MEDMAKS_SATS
    // };
    // private boolean HAR_RETT_PÅ_DAGPENGER;
    public Saksbehandler(DagpengerKalkulator dagpengerKalkulator) {
        this.dagpengerKalkulator_ = dagpengerKalkulator;
        initResponsMap();
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
        responsMap.put("BEREGNING_SISTE_ÅRSLØNN", "📅 Vi kalkulerer dagpengene dine basert på lønnen din fra det siste året.");
        responsMap.put("BEREGNING_GJENNOMSNITTET_AV_TRE_ÅR", "📊 Vi kalkulerer dagpengene dine basert på gjennomsnittlig lønn de tre siste årene.");
        responsMap.put("BEREGNING_MAKS_ÅRLIG_DAGPENGERGRUNNLAG", "📈 Vi kalkulerer dagpengene dine basert på maks årlig dagpengegrunnlag.");
        //Respons med total dagpenger
        // responsMap.put("VIS_TOTAL_DAG_PENGER", String.format("💵 Dine totale dagpenger over perioden er: ", this.totalDagsPenger));
    }; 

    
    // public void BehandlLønn(){}
}

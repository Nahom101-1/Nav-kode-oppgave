package no.nav.årslønn;


import java.time.LocalDate;

/**
 * Representeren en person sin lønn et kalenderår.
 * Holder på informasjon som hvilket år lønnen tilhører, og selve lønnen det kalenderåret.
 *
 * @author Emil Elton Nilsen
 * @version 1.0
 */
public class Årslønn {

    private final int åretForLønn;
    private final double årslønn;

   /**
   * Oppretter et Årslønn-objekt med et spesifisert år og årslønn.
   * @param åretForLønn året lønnen tilhører.
   * @param årslønn årslønnen.
   */
    public Årslønn(int åretForLønn, double årslønn) {
        //se om år og lønn er gydlig før vi initalisere  
        if (åretForLønn < 1899 || åretForLønn > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Året må være gyldig.");
        }
        if (årslønn < 0) {
          throw new IllegalArgumentException("Årslønn kan ikke være negativ.");
        }
       this.åretForLønn = åretForLønn;
       this.årslønn = årslønn;
    }

    /**
     * Henter året som lønnen tilhører.
     * @return året for lønnen.
     */
    public int hentÅretForLønn() {
        return this.åretForLønn;
    }

    /**
     * Henter årslønnen.
     * @return årslønnen.
     */
    public double hentÅrslønn() {
        return this.årslønn;
    }

}

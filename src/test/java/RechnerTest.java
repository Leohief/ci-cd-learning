

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RechnerTest {
    
    @Test
    void testAddieren(){

        Rechner rechner = new Rechner();
        int ergebnis = rechner.addieren(3,5);
        assertEquals(8, ergebnis);
    }

    @Test
    void testMultiplizieren() {
        Rechner rechner = new Rechner();

        int ergebnis = rechner.multiplizieren(4, 5);

        assertEquals(20, ergebnis);
    }

}

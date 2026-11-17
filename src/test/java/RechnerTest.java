

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RechnerTest {
    
    @Test
    void testAddieren(){

        Rechner rechner = new Rechner();
        int ergebnis = rechner.addieren(3,5);
        assertEquals(8, ergebnis);
    }

}

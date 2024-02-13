package student;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class WaltzGeneratorTest {

    WaltzGenerator waltzGenerator = new WaltzGenerator(3);

    @Test
    public void rollDiceTest() {
        int dice1 = waltzGenerator.rollDice(1);
        assertEquals(true, dice1 <= 6 && dice1 >= 1);
        int dice2 = waltzGenerator.rollDice(2);
        assertEquals(true, dice2 <= 12 && dice2 >= 2);
        int dice3 = waltzGenerator.rollDice(3);
        assertEquals(true, dice3 <= 18 && dice3 >= 3);
    }

    @Test
    public void rollDiceOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> waltzGenerator.rollDice(0));
    }

    @Test
    public void buildTableTest() {

        ArrayList<String> listA = new ArrayList<>();
        listA.add("2");
        listA.add("13");
        listA.add("12");
        listA.add("17");
        listA.add("19");
        listA.add("14");
        listA.add("20");
        listA.add("40");
        listA.add("60");
        listA.add("70");
        listA.add("15");
        listA.add("10");

        assertEquals(0, waltzGenerator.buildTable(listA)[0].length);
        assertEquals(13, waltzGenerator.buildTable(listA).length);
        listA.set(0,"1");
        assertEquals(7, waltzGenerator.buildTable(listA).length);

    }


}
    //
//    Write tests for `buildTable(List<String>)`. Tip: Your test lines don't
//    need to have 16 strings just because most of the lines in `minuet.csv`
//    and `trio.csv` do.




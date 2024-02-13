package student;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.sound.sampled.LineUnavailableException;
import java.io.*;
import java.util.List;
import java.util.Random;

/**
 * An implementation of Mozart's musical dice game to generate waltzes.
 */
public class WaltzGenerator {
    // Constants
    public static final String MINUET_CSV_PATH = "./res/minuet.csv";
    public static final String TRIO_CSV_PATH = "./res/trio.csv";
    public static final String WALTZ_S_WAV = "waltz-%s.wav";
    public static final int MEASURES_NUM = 16;
    public static final int TOTAL_SIDES = 6;
    public static final int TOTAL_COLUMN = 15;


    // Instance variables
    private long seed; // initialized in constructor
    private Random random; // initialized in constructor
    private double[] waltz; // initialized lazily in getWaltz()

    /**
     * Constructs a new waltz generator.
     */
    public WaltzGenerator() {
        // This version of the constructor does not get a seed as a
        // parameter. Create a seed by getting the current time in
        // milliseconds, and call the other constructor with that as
        // an argument.
        this.seed = (System.currentTimeMillis());
        random = new Random();
    }

    /**
     * Constructs a new waltz generator using the specified seed for dice rolls.
     *
     * @param seed the seed
     */
    public WaltzGenerator(long seed) {
        // Initialize the appropriate instance variables.
        this.seed = seed;
        random = new Random(seed);
    }

    private double[] getWaltz() throws IOException {
        if (waltz == null) {
            String[] minuetMeasures = makeMinuet(buildTable("./res/minuet.csv"));
            String[] trioMeasures = makeTrio(buildTable("./res/trio.csv"));
            waltz = SupportCode.buildWaltz(minuetMeasures, trioMeasures);
        }
        return waltz;
    }

    /**
     * Plays the waltz created by this generator. If called repeatedly on
     * the same instance (or another instance with the same {@link #seed}),
     * this always plays the same waltz.
     *
     * @throws LineUnavailableException if {@link javax.sound.sampled.AudioSystem}
     *                                  is unavailable to satisfy requests
     * @throws IOException              if the files containing dice data or music cannot be
     *                                  read
     */
    public void playWaltz() throws LineUnavailableException, IOException {
        StdAudio.play(getWaltz());
    }

    /**
     * Saves the waltz created by this generator into a file whose name
     * includes the {@link #seed}.
     *
     * @return the name of the file
     * @throws IOException if the files containing dice data or music cannot be
     *                     read
     */
    public String saveWaltz() throws IOException {
        String filename = String.format(WALTZ_S_WAV, seed);
        StdAudio.save(filename, getWaltz());
        return filename;

    }

    // Returns the total of rolling the specified number of 6-sided dice.
    // This should throw IllegalArgumentException if numDice < 1.
    @VisibleForTesting
    int rollDice(int numDice) {
        // You will implement this after writing and running the tests.
        if (numDice < 1) {
            throw new IllegalArgumentException(numDice + "is not in bounds.");
        }
        int total = 0;
        for (int i = 0; i < numDice; i++) {
            total += (int) (Math.random() * numDice) + 1;
        }
        return total;
    }


    @VisibleForTesting
    String[] convertCsvToStringArray(String data) {
        return data.split(", ");
    }

    // You will need to implement this method, which takes a path as an argument
    // and returns a two-dimensional array of ints (like the ones shown on
    // Canvas).
    @VisibleForTesting
    String[][] buildTable(List<String> lines) {
        // Get the number of dice from the first line.
        String firstLine = lines.get(0);

        // Allocate a 2D array of String with the necessary number of rows
        // for the table. For example, if there are 2 dice, you would allocate
        // an array with 13 rows (because 12 is the largest total of two dice).
        // The first two rows would never be used, since 0 and 1 could never
        // be the total of two dice. You are encouraged to create well-named
        // variables, such as numRows.
        int numDie = Integer.parseInt(firstLine);
        int numRows = (numDie * TOTAL_SIDES + 1);
        String[][] newTable = new String[numRows][];

        // Iterate over the remaining lines, splitting each into an array of
        // String, which is returned and assigned to the appropriate row.

        // You may assume that the input is one or more valid numbers
        // delimited by ", ". You don't have to check if lines are null,
        // empty, malformed, etc.
        for (int i = 0; i < numDie; i++) {
            newTable[i] = new String[0];
        }
        for (int i = numDie; i < numRows; i++) {
            newTable[i] = lines.get(1 + i - numDie).split(", ");
        }
        return newTable;
    }

    private String[][] buildTable(String path) throws IOException {
        // Read the lines from the file as the specified path.
        File file = new File(path);
        List<String> lines = Files.readLines(file, Charsets.UTF_8);

        // The remainder of this method should be pulled into a helper method
        // with the same name that takes a List<String> as an argument.

        // Return the populated 2D-array.
        return buildTable(lines);
    }

    private String[] minuetAndTrio(int rollDice, String prefix, String[][] measuresBoth) {
        String[] minuetAndTrioSample = new String[MEASURES_NUM];

        for (int i = 0; i <= TOTAL_COLUMN; i++) {
            minuetAndTrioSample[i] =  prefix + measuresBoth[rollDice][i];
        }
        return minuetAndTrioSample;
    }

    // Create and populate an array of samples to use for the minuet.
    private String[] makeMinuet(String[][] minuetMeasures) {
        return minuetAndTrio(2, "M", minuetMeasures);
    }

    // Create and populate an array of samples to use for the trio.
    private String[] makeTrio(String[][] trioMeasures) {
        return minuetAndTrio(1, "T", trioMeasures);
    }

    public static void main(String[] args) throws LineUnavailableException, IOException {
        WaltzGenerator generator = new WaltzGenerator();
        generator.playWaltz();
        // Uncomment the following two lines to save your waltzes.
        String filename = generator.saveWaltz();
        System.out.printf("Saved waltz to %s.%n", filename);
    }
}

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class main {

    // Universal Variables

    // The alphabet put into an ArrayList so I can use it in for loops
    static ArrayList<String> letters = new ArrayList<>(Arrays.asList("A","B","C","D","E",
    "F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"));

    //-----------------------------------------------Primary Method------------------------------------------------

    /**
     * This is the class that starts the code
     * @param argc
     * @throws NoSuchAlgorithmException
     * @throws InterruptedException
     */
    public static void main(String[] argc) throws NoSuchAlgorithmException, InterruptedException{
        primaryFunction();
    }

    /**
     * The Class that is primary used to crack the hashes
     * This is required for repeat()
     * @throws NoSuchAlgorithmException
     * @throws InterruptedException
     */
    public static void primaryFunction() throws NoSuchAlgorithmException, InterruptedException{
        // variables
        String [] hashes;
        String shaValue;

        // functions
        Scanner keyboard = new Scanner(System.in);

        // getting hashs
        System.out.print("Please Input SHA-256 Value: ");
        shaValue = keyboard.next();

        // Seperating Hashes
        hashes = shaValue.trim().split(",");

        // Making the Thread Pools
        ExecutorService threads = Executors.newFixedThreadPool(hashes.length); //makes threads
        CountDownLatch lock = new CountDownLatch(hashes.length); // makes locks

        // --- Problem Solving Code ---

        System.out.println("The Liscense Plate Numbers are: ");
        for(String part : hashes){ // loops for each hash
            threads.execute(() -> { //creates threads for each hash to reduce time
                try {
                    String solution = algorithm(part); // sends thread to cracking algo
                    System.out.println("- " + solution +" : " + part); // prints solution
                } catch (NoSuchAlgorithmException ex) {
                    System.out.println("An Error has Occured");
                }
                lock.countDown(); // decreases countdown on the lock
            });
        }

        lock.await(); // makes program wait for all the threads to be finished

        // --- Problem Solving Code ---

        repeat(); // allows user to repeat code without restarting
    }

    //------------------------------------------Hash Cracking Methods----------------------------------------------

    /**
     * This converts the binary that we got from getSha into a string that we are able to use
     * @param hash
     * @return
     */
    public static String hexString(byte[] hash){

        // converts byte[] into a BigInteger so it is in one variable
        BigInteger binary = new BigInteger(1, hash);

        // Transforms binary to a string
        StringBuilder hexString = new StringBuilder(binary.toString(16));
        
        // Makes sure that there are enough characters
        while(hexString.length() < 64){
            hexString.insert(0,'0');
        }

        // fixes format and returns hash
        return "0x" + hexString.toString().toUpperCase();
    }

    /**
     * This converts input string into encoded binary "String"
     * needs to be converted back to an actual string
     * @param inputtedString
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSha(String inputtedString) throws NoSuchAlgorithmException{

        // the encoder that will trans for the binary to hashed binary
        MessageDigest encoder = MessageDigest.getInstance("SHA-256");

        // hashes and returns the binary
        return encoder.digest(inputtedString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * This is used to find the unhashed version of the liscense plate
     * @param result
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String algorithm(String result) throws NoSuchAlgorithmException{

        // variables
        String returnValue = null;

        // Cracking Loops
        for(int x = 0; x < 26; x++){ // Goes through slot 1
            for(int y = 0; y < 26; y++){ // Goes throught slot 2
                for(int z = 0; z < 26; z++){ // Goes throught slot 3
                    for(int num1 = 0; num1 < 10; num1++){ // Goes throught slot 4
                        for(int num2 = 0; num2 < 10; num2++){ // Goes throught slot 5
                            for(int num3 = 0; num3 < 10; num3++){ // Goes throught slot 6
                                for (int num4 = 0; num4 < 10; num4++){ // Goes throught slot 7

                                    // converts the present values into a liscense plate number
                                    String tempString = letters.get(x) + letters.get(y) + letters.get(z) 
                                    + "-" + Integer.toString(num1) + Integer.toString(num2) + 
                                    Integer.toString(num3) + Integer.toString(num4);

                                    // hashes the liscense plate
                                    String tempHash = hexString(getSha(tempString));

                                    // Compares the results of the hashed liscense plate
                                    if(result.compareTo(tempHash)==0){ //checks if they are the same
                                        returnValue = tempString; // if true will set the returnValue to match
                                        break; // breaks the loop
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Returns found un-hashed value
        return returnValue;
    }

    //------------------------------------------------MISC----------------------------------------------------------

    /**
     * This allows the user to repeat the code without restarting the entire program
     * @throws NoSuchAlgorithmException
     * @throws InterruptedException
     */
    public static void repeat() throws NoSuchAlgorithmException, InterruptedException{
       
        // functions
        Scanner keyboard = new Scanner(System.in);

        // ask user
        System.out.print("\nDo you wish to use again? Y/N: ");
        String response = keyboard.next();

        // process response
        if(response.compareTo("Y")==0 || response.compareTo("y")==0){
            primaryFunction(); // restarts loop
        }
        else if(response.compareTo("N")==0 || response.compareTo("n")==0){
            System.out.println("Thank you for your service");
            System.exit(0); // ends program
        }
        else{
            System.out.println("Improper Response; Please respond with a Y or a N");
        }
    }
}

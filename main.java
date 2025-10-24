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

/**
 * Program to find hashes of liscense plates
 * @author Ed T
 */
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
        shaValue = keyboard.nextLine(); // I felt the pain of putting next() instead of nextLine()

        // Seperating Hashes
        hashes = shaValue.replaceAll("\\s+","").split(",");

        // Making the Thread Pools
        ExecutorService threads = Executors.newFixedThreadPool(hashes.length); //makes threads
        CountDownLatch lock = new CountDownLatch(hashes.length); // makes locks

        // --- Problem Solving Code ---

        System.out.println("\nWorking on the " + hashes.length + " hashes, please wait! \n");
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
        keyboard.close(); // closes keyboard
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
        boolean breaker = false;

        // Cracking Loops
        for(int x = 0; x < 26; x++){ // Goes through slot 1
            if(breaker){
                break;
            }
            for(int y = 0; y < 26; y++){ // Goes throught slot 2
                if(breaker){
                    break;
                }
                for(int z = 0; z < 26; z++){ // Goes throught slot 3
                    if(breaker){
                        break;
                    }
                    for(int num1 = 0; num1 < 10; num1++){ // Goes throught slot 4
                        if(breaker){
                            break;
                        }
                        for(int num2 = 0; num2 < 10; num2++){ // Goes throught slot 5
                            if(breaker){
                                break;
                            }
                            for(int num3 = 0; num3 < 10; num3++){ // Goes throught slot 6
                                if(breaker){
                                    break;
                                }
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
                                        breaker = true; // makes it so the rest of the loops break
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
        if(response.toUpperCase().compareTo("Y")==0 || response.toUpperCase().compareTo("YES")==0){
            primaryFunction(); // restarts loop
        }
        else if(response.toUpperCase().compareTo("N")==0 || response.toUpperCase().compareTo("NO")==0){
            System.out.println("Thank you for your service");
            System.exit(0); // ends program
        }
        else{
            System.out.println("Improper Response; Please respond with a Y or a N");
        }

        keyboard.close();
    }
}

/*
 * Test Values For The Code:
 * sha256(LUE-2129) = 0xB4ECF4E82FCD763E2125CF8B9BAD4837239FF8E5EB2AB04D42293CEEBE636B0E
 * sha256(FBL-9680) = 0xCFB0518AA3001B7BAE333093C1510334C1BE3F849F37795EAC217E07F31DB28C
 * sha256(GNG-6571) = 0x62FE28DB9EB337A7133233134DD2CF34C5810691A4B04E64C0E9C732343BB813
 */

 /*
  * Home Assignment Values:
  * 
  * 1) 0x339E7E05350D90AC9F9334EBAD788CCB0B8A79713BD47F525BAA7A4536563886
  * 2) 0xB9625EB12235EB18B077A594705C35A67D3999469C5DC43A7837CFDAEFC728D1
  * 3) 0x1EA8E7248B5879A1DC4738FA68ECC4A91500D59D3896C87C3F09AC7061B58E2F
  * 4) 0x98677E16E83584AB00C4667AD1EEFC2F325025B248E463FCC368AE0944C0101C
  * 5) 0x09F3D69FBCB52E4355675816A3C47293832AEE351589C29CE768F433A76D1048
  * 6) 0xF2A30637A648CECF1FECCB7F6470242D2160036F1E1403CF037A2A7D907DE6C5
  *
  * Anwers:
  * 
  * 1) ONR-3844 : 0x339E7E05350D90AC9F9334EBAD788CCB0B8A79713BD47F525BAA7A4536563886
  * 2) ZQY-5445 : 0xB9625EB12235EB18B077A594705C35A67D3999469C5DC43A7837CFDAEFC728D1
  * 3) XVG-9493 : 0x1EA8E7248B5879A1DC4738FA68ECC4A91500D59D3896C87C3F09AC7061B58E2F
  * 4) PUT-5640 : 0x98677E16E83584AB00C4667AD1EEFC2F325025B248E463FCC368AE0944C0101C
  * 5) IFG-8661 : 0x09F3D69FBCB52E4355675816A3C47293832AEE351589C29CE768F433A76D1048
  * 6) AFL-7095 : 0xF2A30637A648CECF1FECCB7F6470242D2160036F1E1403CF037A2A7D907DE6C5
  */
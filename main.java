import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class main {

    // Universal Variables
    static ArrayList<String> letters = new ArrayList<>(Arrays.asList("A","B","C","D","E",
    "F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"));

    
    /**"
     * This is the class that starts the code
     */
    public static void main(String[] argc) throws NoSuchAlgorithmException{
        primaryFunction();
    }

    /**
     * This is the class that calls the other methods that will be used to solve the problem
     * The Methods called are the following:
     * 
     */
    public static void primaryFunction() throws NoSuchAlgorithmException{
        // variables
        String liscenseNumber = null;

        // functions
        Scanner keyboard = new Scanner(System.in);
        ExecutorService threads = Executors.newCachedThreadPool(null);

        // getting hash
        System.out.print("Please Input SHA-256 Value: ");
        String shaValue = keyboard.next();

        // --- Problem Solving Code ---

        for(int x = 0; x < 26; x++){
            for(int y = 0; y < 26; y++){
                for(int z = 0; z < 26; z++){
                    for(int num1 = 0; num1 < 10; num1++){
                        for(int num2 = 0; num2 < 10; num2++){
                            for(int num3 = 0; num3 < 10; num3++){
                                for (int num4 = 0; num4 < 10; num4++){
                                    String tempString = letters.get(x) + letters.get(y) + letters.get(z) 
                                    + "-" + Integer.toString(num1) + Integer.toString(num2) + 
                                    Integer.toString(num3) + Integer.toString(num4);
                                    String tempHash = hexString(getSha(tempString));
                                    if(shaValue.compareTo(tempHash)==0){
                                        liscenseNumber = tempString;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } //});
        }

            System.out.println("The Liscense Plate is " + liscenseNumber);

        // --- Problem Solving Code ---

        repeat();
    }

    /**
     * This converts the binary that we got from getSha into a string that we are able to use
     * @param hash
     * @return
     */
    public static String hexString(byte[] hash){
        BigInteger binary = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(binary.toString(16));
        
        while(hexString.length() < 64){
            hexString.insert(0,'0');
        }

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
        MessageDigest encoder = MessageDigest.getInstance("SHA-256");

        return encoder.digest(inputtedString.getBytes(StandardCharsets.UTF_8));
    }

    public static void repeat() throws NoSuchAlgorithmException{
       
        // functions
        Scanner keyboard = new Scanner(System.in);

        // ask user
        System.out.print("\nDo you wish to use again? Y/N: ");
        String response = keyboard.next();

        // process response
        if(response.compareTo("Y")==0 || response.compareTo("y")==0){
            /* main(); */ System.out.println("fix this");
        }
        else if(response.compareTo("N")==0 || response.compareTo("n")==0){
            System.out.println("Thank you for your service");
        }
        else{
            System.out.println("Improper Response; Please respond with a Y or a N");
        }
    }

    public static String algorithm(String result) throws NoSuchAlgorithmException{
        String returnValue = null;
        for(int x = 0; x < 26; x++){
            for(int y = 0; y < 26; y++){
                for(int z = 0; z < 26; z++){
                    for(int num1 = 0; num1 < 10; num1++){
                        for(int num2 = 0; num2 < 10; num2++){
                            for(int num3 = 0; num3 < 10; num3++){
                                for (int num4 = 0; num4 < 10; num4++){
                                    String tempString = letters.get(x) + letters.get(y) + letters.get(z) 
                                    + "-" + Integer.toString(num1) + Integer.toString(num2) + 
                                    Integer.toString(num3) + Integer.toString(num4);
                                    String tempHash = hexString(getSha(tempString));
                                    if(result.compareTo(tempHash)==0){
                                        returnValue = tempString;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnValue;
    }

}

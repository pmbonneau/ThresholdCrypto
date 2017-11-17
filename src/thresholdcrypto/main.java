/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thresholdcrypto;
import commandLineArgsParser.*;
import java.util.Random;

/**
 *
 * @author root
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // Command line arguments parser usage is based from:
        // https://stackoverflow.com/questions/367706/how-to-parse-command-line-arguments-in-java
        // Using Apache Commons CLI
        Options options = new Options();

        Option optGenerate = new Option("g", "generate", false, "hide the secret using Shamir's algorithm");
        optGenerate.setRequired(false);
        options.addOption(optGenerate);
        
        Option optCoefficient = new Option("k", "coefficient", true, "number of coefficients");
        optCoefficient.setRequired(false);
        options.addOption(optCoefficient);
        
        Option optNumberOfPoints = new Option("n", "numberpts", true, "number of points");
        optNumberOfPoints.setRequired(false);
        options.addOption(optNumberOfPoints);
        
        Option optSecret = new Option("s", "secret", true, "secret to hide");
        optSecret.setRequired(false);
        options.addOption(optSecret);
        
        Option optModulo = new Option("q", "modulo", true, "modulo to apply");
        optModulo.setRequired(false);
        options.addOption(optModulo);
        
        Option optRetreive = new Option("r", "retreive", true, "retreive secret");
        optRetreive.setRequired(false);
        options.addOption(optRetreive);
        
        Option optPoints = new Option("p", "points", true, "input points");
        optPoints.setRequired(false);
        options.addOption(optPoints);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try 
        {
            cmd = parser.parse(options, args);
        } 
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }
        
        boolean GenerateSet = false;
        boolean RetreiveSet = false;
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-g"))
            {
                GenerateSet = true;
            }
            if (args[i].equals("-r"))
            {
                RetreiveSet = true;
            }
        }

        String Generate = cmd.getOptionValue("generate");   
        int NumberOfCoefficients  = Integer.parseInt(cmd.getOptionValue("coefficient"));
        String NumberOfPoints  = cmd.getOptionValue("numberpts");
        String Secret = cmd.getOptionValue("secret");
        String Modulo = cmd.getOptionValue("modulo");
        String Retreive = cmd.getOptionValue("retreive");
        String[] Points = cmd.getOptionValues("points");
        
        int PolynomialArray[] = new int[NumberOfCoefficients];
        
        for (int i = 0; i < NumberOfCoefficients - 1; i++)
        {
            // Building the polynomial array from reverse to fit polynome degrees and array positions.
            // Constante which is degree 0 is at PolynomialArray[0] and so.
            PolynomialArray[PolynomialArray.length - 1 - i] = randomInteger(1,99);
        }
        
        PolynomialArray[0] = Integer.parseInt(Secret);
        String hack = "";
    }
    
    public static int randomInteger(int minimum, int maximum)
    {
        Random RandomGenerator = new Random();
        return RandomGenerator.nextInt((maximum - minimum) + 1) + minimum;
    }
    
}

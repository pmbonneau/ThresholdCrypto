/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thresholdcrypto;
import commandLineArgsParser.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Random;

/**
 *
 * @author Pierre-Marc Bonneau
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
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
        
        Option optRetreive = new Option("r", "retreive", false, "retreive secret");
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
                // Check if -g arg is set
                GenerateSet = true;
            }
            if (args[i].equals("-r"))
            {
                // Check if -r arg is set
                RetreiveSet = true;
            }
        }

        String Generate = cmd.getOptionValue("generate");   
        int NumberOfCoefficients = 0;
        if (GenerateSet == true)
        {
            NumberOfCoefficients = Integer.parseInt(cmd.getOptionValue("coefficient"));
        }
        String NumberOfPoints  = cmd.getOptionValue("numberpts");
        String Secret = cmd.getOptionValue("secret");
        String Modulo = cmd.getOptionValue("modulo");
        String Retreive = cmd.getOptionValue("retreive");
        String[] Points = cmd.getOptionValues("points");
        
        if (GenerateSet == true)
        {
            int PolynomialArray[] = new int[NumberOfCoefficients];
        
            for (int i = 0; i < NumberOfCoefficients - 1; i++)
            {
                // Building the polynomial array from reverse to fit polynome degrees and array positions.
                // Constante which is degree 0 is at PolynomialArray[0] and so.
                PolynomialArray[PolynomialArray.length - 1 - i] = randomInteger(1,99);
            }

            // Secret (constante) is stored at index 0
            PolynomialArray[0] = Integer.parseInt(Secret);
            
            // Create random points from polynome
            point[] pointsArray = getPoints(PolynomialArray, Integer.parseInt(NumberOfPoints));
            writePointsArrayToFile(pointsArray);
        }
        
        if (RetreiveSet == true)
        {
            point[] PointsArray = new point[Points.length];
            
            // Create point objects from points passed as arguments
            for (int i = 0; i < Points.length; i++)
            {
                point NewPoint = new point(Points[i]);
                PointsArray[i] = NewPoint;
            }
            
            // Applying Lagrange polynomial interpolation to retreive secret from points
            int RetreivedSecret = calculateLagrangePolynomialInterpolation(PointsArray, Integer.parseInt(Modulo));
            System.out.println(Integer.toString(RetreivedSecret));
        }
    }
    
    public static int calculateLagrangePolynomialInterpolation(point[] PointsArray, int Modulo)
    {
        int L0 = calculateL0(PointsArray, Modulo);
        int L1 = calculateL1(PointsArray, Modulo);
        int L2 = calculateL2(PointsArray, Modulo);
        
        return (L0 + L1 + L2) % Modulo;
    }
    
    public static int calculateL0(point[] PointsArray, int Modulo)
    {
        int Y0 = PointsArray[0].getY();
        int L0 = (0 - PointsArray[1].getX() * (0 - PointsArray[2].getX()));
        int Fact = (PointsArray[0].getX() - PointsArray[1].getX())*(PointsArray[0].getX() - PointsArray[2].getX());
        if (Fact < 0)
        {
            Fact = Modulo + Fact;
        }
        
        int Inverse = modInverse(Fact,Modulo);
        
        int Result = (Y0 * L0 * Inverse) % 127;
        
        return Result;
    }
    
    public static int calculateL1(point[] PointsArray, int Modulo)
    {
        int Y1 = PointsArray[1].getY();
        int L1 = (1 - PointsArray[0].getX() * (1 - PointsArray[2].getX()));
        int Fact = (PointsArray[1].getX() - PointsArray[0].getX())*(PointsArray[1].getX() - PointsArray[2].getX());
        if (Fact < 0)
        {
            Fact = Modulo + Fact;
        }
        
        int Inverse = modInverse(Fact,Modulo);
        
        int Result = (Y1 * L1 * Inverse) % 127;
        
        return Result;
    }
    
    public static int calculateL2(point[] PointsArray, int Modulo)
    {
        int Y2 = PointsArray[2].getY();
        int L2 = (2 - PointsArray[0].getX() * (2 - PointsArray[1].getX()));
        int Fact = (PointsArray[2].getX() - PointsArray[0].getX())*(PointsArray[2].getX() - PointsArray[1].getX());
        if (Fact < 0)
        {
            Fact = Modulo + Fact;
        }
                
        int Inverse = modInverse(Fact,Modulo);
        
        int Result = (Y2 * L2 * Inverse) % 127;
        
        return Result;
    }
    
    // Generate a random integer from a range
    public static int randomInteger(int minimum, int maximum)
    {
        Random RandomGenerator = new Random();
        return RandomGenerator.nextInt((maximum - minimum) + 1) + minimum;
    }
    
    // This method creates NumberOfPoints point objects from a given polynome
    public static point[] getPoints(int[] PolynomialArray, int NumberOfPoints)
    {
        point[] Points = new point[NumberOfPoints];
        int Fx = 0;
        
        // Does something like (1,f(1)), (2,f(2)), (3,f(3)), (4,f(4)) and (5,f(5)) for NumberOfPoints = 5
        for (int i = 1; i <= NumberOfPoints; i++)
        {
            for (int j = 1; j <= PolynomialArray.length - 1; j++)
            {
                Fx = Fx + PolynomialArray[j] ^ j;
            }
            Fx = Fx + PolynomialArray[0];
            
            // Result modulus 31
            point NewPoint = new point(i,Fx % 31);
            
            // Place the new point object in the array
            Points[i - 1] = NewPoint;
        }
        return Points;
    }
    
    public static void writePointsArrayToFile(point[] PointsArray) throws IOException
    {
        BufferedWriter PointsFile;
        PointsFile = new BufferedWriter(new FileWriter("points.txt", true));
        for (int i = 0; i < PointsArray.length; i++)
        {
            PointsFile.write(PointsArray[i].toString());
        }
        PointsFile.close();
    }
    
    // Calculate modulus inverse
    public static int modInverse(int a, int n)
    {
        a = a%n;
        for (int x=1; x<n; x++)
        if ((a*x) % n == 1)
        {
            return x;
        }
        return 0;
    }
}

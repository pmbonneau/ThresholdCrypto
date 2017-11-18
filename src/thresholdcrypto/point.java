/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thresholdcrypto;

/**
 *
 * @author Pierre-Marc Bonneau
 */
public class point 
{
    int FirstElem;
    int SecondElem;
    public point(int x, int y)
    {
        FirstElem = x;
        SecondElem = y;
    }
    
    public point(String sPoint)
    {
        String[] TempArray = sPoint.split(",");
        FirstElem = Integer.parseInt(TempArray[0].replace("(", ""));
        SecondElem =  Integer.parseInt(TempArray[1].replace(")", ""));
    }
    
    public int getX()
    {
        return FirstElem;
    }
    
    public int getY()
    {
        return SecondElem;
    }
    
    public void setX(int x)
    {
        FirstElem = x;
    }
    
    public void setY(int y)
    {
        SecondElem = y;
    }
    
    @Override
    public String toString() 
    {
        return "(" + Integer.toString(FirstElem) + "," + Integer.toString(SecondElem) + ")";
    }
}

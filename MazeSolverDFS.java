import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/*
    Different design compared to Generator. Tried different ways to do the same thing. End result is still the same.
    A lot of the design in the solver was more just finding ways to do certain things and seeing if it would work
    For example, mapping a cell number to a location within an array directly, compared to searching for it
    Had some problems with cell openness, just seems clunky to say the least. Generator was much simpler and easier to read whereas this
    was just pure experimentation
 */
public class MazeSolverDFS
{
    public static void main(String[] args)
    {
        String fileName = args[0];
        try
        {
            FileReader myFileReader = new FileReader(fileName);//will later be input
            BufferedReader myBufferedReader = new BufferedReader(myFileReader);//buffered reader, used to simple .read() function which is per char

            long startTime = System.nanoTime();//log to track time

            FileScanner fs = new FileScanner(myBufferedReader);//new FileScanner class
            System.out.println(fs.printOutput());//print output of FileScanner

            long endTime = System.nanoTime();//log end time after the above function has ended
            long timeElapsed = endTime - startTime;//get difference between the two times

            //output in milliseconds the difference or rather total time to compute a solution to the maze given to the solver
            System.out.println("Execution time in milliseconds: " + timeElapsed / (double) 1000000);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Cannot find file named: " + fileName);//simple catch if a file is not found
            System.exit(1);//exit status
        }
        catch (IOException e)
        {
            e.printStackTrace();//print reason why an IOException occurred
        }
    }
}

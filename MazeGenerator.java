import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MazeGenerator
{
    public static void main(String[] args)
    {
        //dimensions as input from terminal
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        long startTime = System.nanoTime();//start time to track

        Maze myMaze = new Maze(n, m);//creation of new maze with dimensions as input
        String mazeState = n+","+m+":"+myMaze.getStartNode()+":"+myMaze.getFinishNode()+":"+myMaze.getCellOpennessList();//output formatting


        try//try block for output
        {
            File myFile = new File("new_maze_1.txt");//first file name
            if (myFile.createNewFile())
            {
                output(n, m, mazeState, myFile);//function for writing and output
            }
            else//can't create file under name above
            {
                int counter = 2;//number to be added at end of name
                while(!myFile.createNewFile())
                {
                    myFile = new File("new_maze_"+counter+".txt");//add to name
                    counter++;//increment for later use
                }
                output(n, m, mazeState, myFile);//function for output with new file name
            }
        }
        catch (IOException e)//catch when error occurs
        {
            System.out.println("Error!");//print
            e.printStackTrace();//error trace
        }
        long endTime = System.nanoTime();//log end time

        long timeElapsed = endTime - startTime;//get time between two points

        System.out.println("Execution time in milliseconds: " + timeElapsed / (double) 1000000);//time in milliseconds
        System.out.println("Execution time in seconds: " + timeElapsed / (double) 1000000000);//time in seconds

    }

    private static void output(int n, int m, String mazeState, File myFile) throws IOException
    {
        System.out.println("File: " + "\"" + myFile.getName() + "\"" + " has been successfully created!");//print success when occurs
        FileWriter myWriter = new FileWriter(myFile.getName());//write to file
        myWriter.write(mazeState);//current state of maze
        if(n*m <= 50)//if lower than bound, print contents of file. Stops number vomit in terminal but gives some data for small mazes. I liked it so...
        {
            System.out.println("File Contents: " + mazeState);//print maze state that will be written
        }
        myWriter.close();//close writer object
    }
}

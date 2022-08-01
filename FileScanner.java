import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

public class FileScanner
{
    //constructor
    public FileScanner(BufferedReader myBufferedReader) throws IOException
    {
        readFile(myBufferedReader);//function to initialise scheduler with needed data
        output = new StringBuilder("(");//start stringBuilder with first parenthesis
        mazeSolver();//start maze solver, not sure if it should be another class or not. Felt to small to do so
    }

    public void readFile(BufferedReader myReader) throws IOException//read file broken up into separate chunks
    {
        StringBuilder data = new StringBuilder();//new sb for inputted data
        int c;//int c to save next char
        while((c = myReader.read()) != -1 && c != 58)//while next char being read != -1 (EOF) and c != 58 (Colon)
        {
            data.append((char) c);//append
        }
        //data should only contain a string of the form (int + "," + int)
        setMatrixDimensions(data.toString());//give data to set dimensions of new maze
        data.setLength(0);//clear data

        while((c = myReader.read()) != -1 && c != 58)//same loop as above, used for start node number
        {
            data.append((char) c);
        }
        setStartNode(data.toString());//set start node with data as input
        data.setLength(0);//clear

        while((c = myReader.read()) != -1 && c != 58)//same loop
        {
            data.append((char) c);
        }
        setFinishNode(data.toString());//set end node with data as input
        data.setLength(0);//clear

        fillCellOpenness(myReader.readLine());//assume rest of data is cell openness list
    }


    public void mazeSolver()//main function to solve maze
    {
        int stepCount = 0;//track steps for output
        Stack<Cell> cellStack = new Stack<>();//new stack
        int startNodeRow = getSpecificRow(startNode);//start node row, with special function
        int startNodeColumn = getSpecificColumn(startNode);//start node column, with special function

        int currentNode = startNode;//save current node to start node
        output.append(currentNode);//append current node to SB output
        cellStack.add(maze[startNodeRow][startNodeColumn]);//add start node to cell stack

        while(!cellStack.isEmpty())//While not empty,
        {
            if(currentNode == finishNode)//if current node == the finish node -> break, found solution!!!
            {
                break;
            }
            if(hasMove(currentNode))//if currentNode has a move
            {
                currentNode = nextNode(currentNode);//get next node
                cellStack.peek().setHasVisited();//set new node to visited

                cellStack.add(maze[getSpecificRow(currentNode)][getSpecificColumn(currentNode)]);//add new node to cell stack
                stepCount++;//increment
                output.append(",").append(currentNode);//append comma for output

            }
            else//prepare for removal of dead end node
            {
                cellStack.peek().setHasVisited();//set has visited in case has not
                cellStack.pop();//remove
                if(cellStack.size() > 0)//if cell stack has more cells
                {
                    currentNode = cellStack.peek().getCellNumber();//current node is equal the cell's number at top of stack
                }
            }
        }
        output.append(")\n");//append new line char for number of steps to be printed
        output.append("Number of Steps: ").append(stepCount);//print number of steps

    }


    //get the next node by giving the current node
    //targets node of the smallest size
    //Node north < Node left < currentNode < Node right < Node down
    public int nextNode(int currentNode)
    {
        //if row is not 0
        //can move up
        if(getSpecificRow(currentNode) > 0)//move up check
        {
            //get cell above the current cell
            Cell c = maze[getSpecificRow(currentNode - columnAmount)][getSpecificColumn(currentNode- columnAmount)];
            if(!c.hasVisited() && c.canMoveUpToThisNode())//if this cell has not been visited and can move up to this node
            {
                return currentNode-columnAmount;//return this node as an int
            }
        }

        //if column is not 0
        //can move left
        if(getSpecificColumn(currentNode) > 0)//move left check
        {
            //get cell to the left of current cell
            Cell c = maze[getSpecificRow(currentNode - 1)][getSpecificColumn(currentNode- 1)];
            if(!c.hasVisited() && c.canMoveLeftToThisNode())//if it has not been visited and can move left to this node
            {
                return currentNode-1;//then return this new node as an int
            }
        }

        //obtain currentCell as an actual cell to look at data
        //was playing around with different ways
        Cell current = maze[getSpecificRow(currentNode)][getSpecificColumn(currentNode)];

        if(getSpecificColumn(currentNode) < columnAmount-1)//move right check
        {
            //get cell to the right of this current node
            Cell c = maze[getSpecificRow(currentNode + 1)][getSpecificColumn(currentNode + 1)];
            //if right cell has not been visited and can move either because openness is 1 or 3
            if(!c.hasVisited() && (current.getCellOpenness() == 1 || current.getCellOpenness() == 3))
            {
                return currentNode+1;//then return this node as the next move
            }
        }

        //else assume next move is straight down as this function would not have run without a valid move
        //therefore assume downward move
        return currentNode + columnAmount;

    }


    public boolean hasMove(int currentNode)//in specific order to give the lowest grid numbered cell priority, up < left < currentCell < right < down
    {
        if(getSpecificRow(currentNode) > 0)//can move up check
        {
            //get cell above current cell
            Cell c = maze[getSpecificRow(currentNode - columnAmount)][getSpecificColumn(currentNode- columnAmount)];
            if(!c.hasVisited() && c.canMoveUpToThisNode())//if this cell has not been visited and can move up to this node
            {
                return true;//valid move found!
            }
        }
        if(getSpecificColumn(currentNode) > 0)//move left check
        {
            //get cell to the left of current cell
            Cell c = maze[getSpecificRow(currentNode - 1)][getSpecificColumn(currentNode- 1)];
            if(!c.hasVisited() && c.canMoveLeftToThisNode())//if it has not been visited and can move left to this node
            {
                return true;//valid move found!
            }
        }

        Cell current = maze[getSpecificRow(currentNode)][getSpecificColumn(currentNode)];

        if(getSpecificColumn(currentNode) < columnAmount-1)//move right check
        {
            //get cell to the right of this current node
            Cell c = maze[getSpecificRow(currentNode + 1)][getSpecificColumn(currentNode + 1)];
            if(!c.hasVisited() && (current.getCellOpenness() == 1 || current.getCellOpenness() == 3))
            //if right cell has not been visited and can move either because openness is 1 or 3
            {
                return true;//valid move found!
            }
        }

        if(getSpecificRow(currentNode) < rowAmount-1)//move down check
        {
            //get cell below current node
            Cell c = maze[getSpecificRow(currentNode + columnAmount)][getSpecificColumn(currentNode + columnAmount)];
            if(!c.hasVisited() && (current.getCellOpenness() == 2 || current.getCellOpenness() == 3))
            {//if bottom cell has not been visited and can move either because openness is 2 or 3
                return true;//valid move found!
            }
        }

        return false;//no move found
    }


    //special functions that when given a cell number of any sort, find the exact row and column
    public int getSpecificRow(int cellNumber)
    {
        return (cellNumber-1)/columnAmount;//using integers floor ability to reduce to array values. (0 to n-1)
        //returns index
    }

    public int getSpecificColumn(int cellNumber)
    {
        if(cellNumber % columnAmount == 0)//number mod column amount equal to 0? Then index is column - 1
        {
            return columnAmount-1;
        }
        else
        {
            return cellNumber % columnAmount -1;//else other case
        }
    }

    public void setMatrixDimensions(String s)//set matrix dimensions
    {
        int commaIndex = 0;//comma index
        while(s.charAt(commaIndex) != ',')//loop until a comma is found
        {
            commaIndex++;//increment by 1
        }

        //once found
        //two substrings from 0 to comma index
        //and comma index+1
        rowAmount = Integer.parseInt(s.substring(0, commaIndex));
        columnAmount = Integer.parseInt(s.substring(commaIndex+1));
        //these values are dimensions
        maze = new Cell[rowAmount][columnAmount];//creation of n*m maze
    }

    public void setStartNode(String s)//setting of start node
    {
        startNode = Integer.parseInt(s);
    }

    public void setFinishNode(String s)//setting of finish node
    {
        finishNode = Integer.parseInt(s);
    }


    //fill cell openness function
    public void fillCellOpenness(String s)
    {
        int count = 0;
        int index = 0;
        for(int i = 0; i < rowAmount; i++)
        {
            for (int j = 0; j < columnAmount; j++)
            {
                maze[i][j] = new Cell(++count,Integer.parseInt(String.valueOf(s.charAt(index++))));
            }
        }
    }


    //print string output to caller
    public String printOutput()
    {
        return output.toString();
    }



    //private member data
    private final StringBuilder output;
    private int rowAmount;
    private int columnAmount;
    private int startNode;
    private int finishNode;
    private Cell[][] maze;


}

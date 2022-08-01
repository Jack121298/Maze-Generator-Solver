import java.util.Random;
import java.util.Stack;

public class Maze
{
    //constructor
    public Maze(int n, int m)//dimensions as input
    {
        cell_n_size = n;//row
        cell_m_size = m;//column
        cellMatrix = new Cell[cell_n_size][cell_m_size];//new Cell matrix with inputted dimensions
        endNodes = new Cell[2];//finish nodes of size 2, ease of use. Location at index 0 is start, index 1 is end
        cellStack = new Stack<>();//new cell stack for DFS algorithm
        init_CellMatrix();//join up adjacent cells for each cell. Ease of use and made my life a lot easier
        start_DFS_Generation();//generation function for actual maze after everything else is setup
    }

    public int getStartNode()//return start node of the array
    {
        return endNodes[0].getNumber();
    }

    public int getFinishNode()//return finish node of the array
    {
        return endNodes[1].getNumber();
    }

    public String getCellOpennessList()//returns a String of all cells' openness
    {
        StringBuilder runningList = new StringBuilder();//stringifier to append to
        for(int i = 0; i < cell_n_size; i++)//loop through all cells
        {
            for(int j = 0; j < cell_m_size; j++)
            {
                runningList.append(cellMatrix[i][j].getCellOpenness());//append number from 0 to 3 to the stringbuilder
            }
        }
        return runningList.toString();//return sb
    }



    //main maze generation function
    public void start_DFS_Generation()
    {
        int count = 1;//track vertex count, in hindsight
        Random r = new Random();//new random object to make new random numbers
        //found this as a way get a random number from 0 to bound, pretty sure I like other methods better though
        int currentRow = r.ints(1, 0, cell_n_size - 1).findFirst().getAsInt();
        int currentColumn = r.ints(1, 0, cell_m_size-1).findFirst().getAsInt();
        cellMatrix[currentRow][currentColumn].setHasVisited();//set has visited for first node
        cellStack.add(cellMatrix[currentRow][currentColumn]);//add node to stack
        endNodes[0] = cellStack.peek();//set start node within array for later retrieval, makes it easier


        while(!cellStack.isEmpty())//main loop, while stack has some paths to explore is essentially what it means
        {
            if(cellStack.peek().hasMove())//if a particular cell has a way to traverse to next, then attempt to do so
            {
                Cell oldCell = cellStack.peek();//save old cell for later
                count++;//increment counter to track nodes
                cellStack.add(cellStack.peek().getNextMove());//add new node by obtaining the next move from the node at the top of the stack. Then add it to stack
                cellStack.peek().setHasVisited();//set new node to visited to prevent traversing again
                if(oldCell.getNextMoveType() == 0)//set north, so change north cell to 2 -> down only
                {
                    cellStack.peek().setCellOpenness(2);
                }
                else if(oldCell.getNextMoveType() == 3)//set west, so change west cell to 1 -> right only
                {
                    cellStack.peek().setCellOpenness(1);
                }
                else if(oldCell.getNextMoveType() == 1)//set south, so change current cell to 2 -> down only
                {
                    oldCell.setCellOpenness(2);
                }
                else if(oldCell.getNextMoveType() == 2)//set east, so change current cell to 1 -> right only
                {
                    oldCell.setCellOpenness(1);
                }

                if(count == cell_m_size*cell_n_size)//check if count is equal to bound
                {
                    endNodes[1] = cellStack.peek();//if true, means current node is the final node
                }
            }
            else//else, remove the top of the stack and continue to do so until algorithm finds a node that has a path to explore. If all nodes are removed, then all paths have been mapped!
            {
                cellStack.pop();//let GC handle removed data
            }
        }
    }



    public void init_CellMatrix()//general setup
    {
        int count = 0;//track current cell number from left to right, up to down
        for(int i = 0; i < cell_n_size; i++)//i for row
        {
            for (int j = 0; j < cell_m_size; j++)//j for column
            {
                cellMatrix[i][j] = new Cell(++count);//new Cell at position i,j in array with incremented number
            }
        }
        //mapping all adjacent cells to an array of references to N S E W nodes, each index corresponds to that particular type of move
        //makes it simple I guess
        for(int i = 0; i < cell_n_size; i++)//same loop as abode
        {
            for (int j = 0; j < cell_m_size; j++)
            {
                setAdjacentNodes(i, j);//give indexes to function
            }
        }
    }


    //function used to map adjacent nodes by figuring out particular location in array
    //has 3 main types: middle piece, edge, corner
    //each type has 1 less needed adjacent node then the previous, MP = 4, EP = 3, CP = 2
    //EP and CP have specific requirements to determine how to set their adjacent nodes
    //A top left corner node has south and east node but a bottom right corner has north and west nodes
    //same can be said about edges but they only differ by 1 node
    public void setAdjacentNodes(int row, int column)//index of current node given as data
    {
        if((row > 0 && column > 0) && (row < cell_n_size-1 && column < cell_m_size-1))//middle piece, not edge or corner
        {
            //set all adjacent nodes in all 4 directions
            cellMatrix[row][column].setNorth(cellMatrix[row-1][column]);
            cellMatrix[row][column].setSouth(cellMatrix[row+1][column]);
            cellMatrix[row][column].setEast(cellMatrix[row][column+1]);
            cellMatrix[row][column].setWest(cellMatrix[row][column-1]);
        }
        else if((row == 0 && (column == 0 || column == cell_m_size-1)) || (row == cell_n_size-1 && (column == 0 || column == cell_m_size-1)))//corners
        {
            if(row == 0 && column == 0)//check if top left
            {
                cellMatrix[row][column].setSouth(cellMatrix[row+1][column]);
                cellMatrix[row][column].setEast(cellMatrix[row][column+1]);
            }
            else if(row == cell_n_size-1 && column == 0)//bottom left
            {
                cellMatrix[row][column].setNorth(cellMatrix[row-1][column]);
                cellMatrix[row][column].setEast(cellMatrix[row][column+1]);
            }
            else if(row == 0 && column == cell_m_size-1)//top right
            {
                cellMatrix[row][column].setSouth(cellMatrix[row+1][column]);
                cellMatrix[row][column].setWest(cellMatrix[row][column-1]);
            }
            else if(row == cell_n_size-1 && column == cell_m_size-1)//bottom right
            {
                cellMatrix[row][column].setNorth(cellMatrix[row-1][column]);
                cellMatrix[row][column].setWest(cellMatrix[row][column-1]);
            }
        }
        else//edges
        {
            if(row == 0)//top edges, no north adjacent node
            {
                cellMatrix[row][column].setSouth(cellMatrix[row+1][column]);
                cellMatrix[row][column].setEast(cellMatrix[row][column+1]);
                cellMatrix[row][column].setWest(cellMatrix[row][column-1]);
            }
            else if(row == cell_n_size-1)//bottom edges, no south adjacent node
            {
                cellMatrix[row][column].setNorth(cellMatrix[row-1][column]);
                cellMatrix[row][column].setEast(cellMatrix[row][column+1]);
                cellMatrix[row][column].setWest(cellMatrix[row][column-1]);
            }
            else if(column == 0)//left edge, no west adjacent node
            {
                cellMatrix[row][column].setNorth(cellMatrix[row-1][column]);
                cellMatrix[row][column].setSouth(cellMatrix[row+1][column]);
                cellMatrix[row][column].setEast(cellMatrix[row][column+1]);
            }
            else if(column == cell_m_size-1)//right edge, no east adjacent node
            {
                cellMatrix[row][column].setNorth(cellMatrix[row-1][column]);
                cellMatrix[row][column].setSouth(cellMatrix[row+1][column]);
                cellMatrix[row][column].setWest(cellMatrix[row][column-1]);
            }
        }
    }

    //private member data
    private final int cell_n_size;
    private final int cell_m_size;
    private final Stack<Cell> cellStack;
    private final Cell[][] cellMatrix;
    private final Cell[] endNodes;

}

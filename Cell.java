import java.util.ArrayList;
import java.util.Random;

public class Cell
{
    //constructor
    public Cell(int number)//information regarding cell type and references to neighbor etc
    {
        hasVisited = false;//boolean if cell has been visited
        this.number = number;//number to track what particular cell this is
        cellOpenness = 0;//cell openness, default is 0. Ranges from 0 to 3
        adjacentCells = new Cell[4];//adjacent cell list, in order of N S E W
    }


    public void setCellOpenness(int nextMove)//set cell openness
    {
        if(cellOpenness == 0 && nextMove != cellOpenness)//if openness == 0 and next move is not 0
        {
            cellOpenness = nextMove;//set
        }
        else if(cellOpenness == 1 || cellOpenness == 2 && nextMove != cellOpenness && nextMove != 0)//if current openness is 1 or 2 and next move != current cell openness and not 0
        {
            cellOpenness = 3;//must be 3, so set 3
        }
    }

    public int getNumber()//return cell number
    {
        return number;
    }

    public int getCellOpenness()//return current cell openness
    {
        return cellOpenness;
    }

    public void setHasVisited()//set has visited
    {
        hasVisited = true;
    }


    public boolean hasMove()//if has a move by some criteria below, return true;
    {
        for(int i = 0; i < 4; i++)//loop through adjacent nodes
        {
            if(adjacentCells[i] != null && !adjacentCells[i].hasVisited())//if not null and has not visited
            {
                return true;//has at least 1 move to just return true for efficiency
            }
        }
        return false;// no move found, return false for stack removal for this cell as it is dead -> no move available
    }



    public Cell getNextMove()//obtain next move
    {
        //list of valid adjacent nodes for random selection. Doing so in array gives problems as some locations can be null
        ArrayList<Cell> listOfValidAdjacentNodes = new ArrayList<>();
        for(int i = 0; i < 4; i++)//loop through adjacent node array
        {
            if(adjacentCells[i] != null && !adjacentCells[i].hasVisited())//if not null and has not visited in this adjacent node
            {
                listOfValidAdjacentNodes.add(adjacentCells[i]);//add to list
            }
        }
        Random myRand = new Random();//new random object for next random number
        //get next random number from 0 to size of list-1
        //gives index
        //like this style better compared to other implementation
        int randomNum = myRand.nextInt(listOfValidAdjacentNodes.size()-1 + 1);
        for(int i = 0; i < 4; i++)//loop through adjacent list
        {
            if(listOfValidAdjacentNodes.get(randomNum) == adjacentCells[i])//match the selected random cell to one in the array
            {
                nextMoveType = i;//save that index i to determine next move's direction
            }
        }
        return listOfValidAdjacentNodes.get(randomNum);
    }

    public int getNextMoveType()//get move type as an int
    {
        return nextMoveType;
    }


    public boolean hasVisited()//return result of saved boolean variable to determine if this cell has been visited
    {
        return hasVisited;
    }


    //setters for specific location, 0 = N, 1 = S, 2 = E, 3 = W
    public void setNorth(Cell c)
    {
        adjacentCells[0] = c;
    }

    public void setSouth(Cell c)
    {
        adjacentCells[1] = c;
    }

    public void setEast(Cell c)
    {
        adjacentCells[2] = c;
    }

    public void setWest(Cell c)
    {
        adjacentCells[3] = c;
    }

    //private member data
    private final Cell[] adjacentCells;
    private final int number;
    private int cellOpenness;
    private int nextMoveType;
    private boolean hasVisited;
}

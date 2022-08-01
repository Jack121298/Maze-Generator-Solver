public class Cell
{

    //constructor
    public Cell(int cellNumber, int cellOpenness)
    {
        this.cellNumber = cellNumber;//cell number
        this.cellOpenness = cellOpenness;//cell openness
        hasVisited = false;
    }



    //return cell number
    public int getCellNumber()
    {
        return cellNumber;
    }

    //set has visited
    public void setHasVisited()
    {
        hasVisited = true;
    }

    //check if has visited
    public boolean hasVisited()
    {
        return hasVisited;
    }


    //check if cell below can move up to this current node
    public boolean canMoveUpToThisNode()
    {
       return cellOpenness == 2 || cellOpenness == 3;
    }


    //check if cell to the right can move left to this current node
    public boolean canMoveLeftToThisNode()
    {
       return cellOpenness == 1 || cellOpenness == 3;
    }

    //get cell openness
    public int getCellOpenness()
    {
        return cellOpenness;
    }


    //private member data
    private boolean hasVisited;
    private final int cellNumber;
    private final int cellOpenness;
}

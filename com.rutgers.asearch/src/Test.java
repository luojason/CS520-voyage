import Agents.BasicInferenceAgent;
import Agents.InferenceAgent;
import Algorithms.AStarSearch;
import Algorithms.SearchAlgo;
import Entity.*;
import Utility.Heuristics;
import Agents.NaiveAgent;
import Utility.Point;
import Utility.Sentiment;

// just tests the search algos to make sure they work
// usage: java Test xSize ySize blockedProbability%
public class Test {
    public static void printResults(GridWorldInfo result, Grid world) {
        System.out.println("num cells expanded: " + result.numberOfCellsProcessed);
        System.out.println("trajectory length: " + result.trajectoryLength);
        System.out.println("number of bumps: " + result.numBumps);
        System.out.println("number of (re)-plans: " + result.numPlans);
        System.out.println("number of cells determined: " + result.numCellsDetermined);
        System.out.println("runtime: " + result.runtime);

        for(int j = 0; j < world.getYSize(); ++j) {
            for(int i = 0; i < world.getXSize(); ++i) {
                GridCell cell = world.getCell(i, j);
                String symbol = "o"; // default symbol
                if(cell.isVisited()) symbol = "\u001B[36m-\u001B[0m";
                else if(cell.getBlockSentiment() == Sentiment.Free) symbol = "\u001B[32mo\u001B[0m";
                else if(cell.getBlockSentiment() == Sentiment.Blocked) symbol = "\u001B[33mx\u001B[0m";
                else if(cell.isBlocked()) symbol = "\u001B[31mx\u001B[0m";
                System.out.print(symbol);
            }
            System.out.print('\n');
        }
    }

    public static void printMineSweeper(Grid world) {
        for (int j = 0; j < world.getYSize(); j++) {
            for (int i = 0; i < world.getXSize(); i++) {
                GridCell cell = world.getCell(i, j);
                System.out.print(cell.isBlocked() ?  "\u001B[31m" : "\u001B[0m"); // set color
                System.out.print(cell.getNumSensedBlocked());
            }
            System.out.print('\n');
        }
        System.out.print("\u001B[0m");
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int prob = Integer.parseInt(args[2]);
        Grid world = new Grid(x, y, prob);
        Grid world2 = new Grid(world);
        Point start = new Point(0, 0);
        Point goal = new Point(x-1, y-1);

        SearchAlgo algo = new AStarSearch(Heuristics::manhattanDistance);
        InferenceAgent agent = new BasicInferenceAgent();
        InferenceAgent agentBlindfold = new NaiveAgent();
        printMineSweeper(world);

         Entity.Robot robot = new Entity.Robot(start, goal, agentBlindfold, world, algo);
         Entity.GridWorldInfo result = robot.run();
         printResults(result, world);



        Robot robot2 = new Robot(start, goal, agent, world2, algo);
        GridWorldInfo result2 = robot2.run();
        printResults(result2, world2);
    }
}

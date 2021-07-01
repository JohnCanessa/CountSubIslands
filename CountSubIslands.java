import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 * 
 */
public class CountSubIslands {


    // **** global variables (for ease of use) ****
    static int      n;
    static int      m;
    static int[][]  g1;
    static int[][]  g2;

    static boolean  valid = false;


    /**
     * Display grid.
     * Utility function.
     */
    static void displayGrid(int[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            System.out.println(Arrays.toString(grid[r]));
        }
    }


    /**
     * Check if cell (r,c) is in bounds (inside the grid).
     * Utility function.
     */
    static boolean inBounds(int r, int c) {
        return (r >= 0 && r < n && c >= 0 && c < m);
    }


    /**
     * Color the island starting at the specifed cell in grid1
     * with the specified color.
     */
    static void colorIsland(int r, int c, int color) {

        // **** sanity check(s) ****
        if (inBounds(r, c) == false || g1[r][c] != 1) return;

        // **** cell belongs to island with specified color ****
        g1[r][c] = color;

        // **** recursively color all adjacent cells with specified color ****
        colorIsland(r + 1, c, color);       // recurse down
        colorIsland(r - 1, c, color);       // recurse up
        colorIsland(r, c + 1, color);       // recurse right
        colorIsland(r, c - 1, color);       // recurse left
    }


    /**
     * Remove (change to water) from grid2 the specified island 
     * starting at the specified cell.
     */
    static void clearIsland(int r, int c, int color) {

        // **** sanity check(s) ****
        if (inBounds(r, c) == false || g2[r][c] == 0) return;

        // **** check if cell not in island in grid1 ****
        if (g1[r][c] != color)
            valid = false;

        // **** flag this cell as being processed (mark it as water) ****
        g2[r][c] = 0;

        // **** check adjacent cells recursively ****
        clearIsland(r + 1, c, color);       // recurse down
        clearIsland(r - 1, c, color);       // recurse up
        clearIsland(r, c + 1, color);       // recurse right
        clearIsland(r, c - 1, color);       // recurse left
    }


    /**
     * You are given two m x n binary matrices grid1 and grid2 
     * containing only 0's (representing water) and 1's (representing land). 
     * An island is a group of 1's connected 4-directionally (horizontal or vertical). 
     * Any cells outside of the grid are considered water cells.
     * 
     * An island in grid2 is considered a sub-island 
     * if there is an island in grid1 that contains all the cells that make up this island in grid2.
     * 
     * Return the number of islands in grid2 that are considered sub-islands.
     * 
     * Runtime: 33 ms, faster than 51.53% of Java online submissions.
     * Memory Usage: 75.7 MB, less than 81.59% of Java online submissions.
     */
    static int countSubIslands0(int[][] grid1, int[][] grid2) {
        
        // **** initialization ****
        int count = 0;
        int color = 2;              // island color

        // **** for ease of use ****
        n = grid1.length;           // # of rows
        m = grid1[0].length;        // # of columns
        g1 = grid1;
        g2 = grid2;

        // **** color island(s) in grid1 ****
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (grid1[r][c] == 1)
                    colorIsland(r, c, color++);
            }
        }

        // **** traverse the grids ****
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {

                // **** ****
                if (g1[r][c] != 0 && g2[r][c] == 1)  {

                    // **** valid sub-island ****
                    valid = true;

                    // **** clear island from grid2 ****
                    clearIsland(r, c, g1[r][c]);

                    // **** increment sub-island count (if needed) ****
                    if (valid) count++;
                }
            }
        }

        // **** return sub-island count ****
        return count;
    }


    /**
     * DFS 
     */
    static int dfs(int r, int c) {

        // **** base case(s) ****
        if (r < 0 || c < 0 || r >= n || c >= m || g2[r][c] == 2) return 1;
        if (g2[r][c] == 0) return 1;
        
        // **** set this cell to 2 (was 1) ****
        g2[r][c] = 2;
        
        // **** recursive calls in four directions (if g1 cell is part of an island) ****
        return  g1[r][c]            // land on g1
                & dfs(r + 1, c)     // recursive call down
                & dfs(r - 1, c)     // recursive call up
                & dfs(r, c + 1)     // recursive call right
                & dfs(r, c - 1);    // recursive call left
    }


    /**
     * You are given two m x n binary matrices grid1 and grid2 
     * containing only 0's (representing water) and 1's (representing land). 
     * An island is a group of 1's connected 4-directionally (horizontal or vertical). 
     * Any cells outside of the grid are considered water cells.
     * 
     * An island in grid2 is considered a sub-island 
     * if there is an island in grid1 that contains all the cells that make up this island in grid2.
     * 
     * Return the number of islands in grid2 that are considered sub-islands.
     * 
     * Runtime: 17 ms, faster than 98.51% of Java online submissions.
     * Memory Usage: 76.6 MB, less than 80.59% of Java online submissions.
     */
    static int countSubIslands(int[][] grid1, int[][] grid2) {

        // **** for ease of use ****
        n = grid1.length;           // # of rows
        m = grid1[0].length;        // # of columns
        g1 = grid1;
        g2 = grid2;

        // **** initialization ****
        int count = 0;

        // **** DFS on each cell in grid2 (if on land) ****
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {

                // **** unvisited land in g2 ****
                if (g2[r][c] == 1) {

                    // **** increment count (if island in g1) ****
                    count += dfs(r, c);

                    // ???? ????
                    System.out.println("<<< (" + r + "," + c + ") count: " + count);
                }
            }
        }

        // ???? ????
        System.out.println("<<< g1:"); displayGrid(g1);
        System.out.println("<<< g2:"); displayGrid(g2);

        // **** return count of sub-islands ****
        return count;
    }


    /**
     * Test scaffold
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // **** initialization ****
        int m = 0;
        int n = 0;

        int[][] grid1 = null;
        int[][] grid2 = null;

        // **** open buffered reader ****
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // **** read m & n ****
        String[] mn = br.readLine().trim().split(",");
        m = Integer.parseInt(mn[1]);
        n = Integer.parseInt(mn[0]);

        // **** read grid 1 ****
        grid1 = new int[m][n];
        for (int i = 0; i < m; i++) {
            int[] row = Arrays.stream(br.readLine().trim().split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
            grid1[i] = row;
        }

        // **** read grid 2 ****
        grid2 = new int[m][n];
        for (int i = 0; i < grid1.length; i++) {
            int[] row = Arrays.stream(br.readLine().trim().split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
            grid2[i] = row;
        }

        // **** close buffered reader ****
        br.close();

        // ???? ????
        System.out.println("main <<< m: " + m + " n: " + n);
        System.out.println("main <<< grid1:"); displayGrid(grid1);
        System.out.println("main <<< grid2:"); displayGrid(grid2);

        // **** call function of interest and display result ****
        // System.out.println("main <<< sub-islands: " + countSubIslands0(grid1, grid2));

        // **** call function of interest and display result ****
        System.out.println("main <<< sub-islands: " + countSubIslands(grid1, grid2));
    }
}
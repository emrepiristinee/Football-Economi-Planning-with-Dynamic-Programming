import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class Emre_Piristine_2020510118 {

	public static void main(String[] args) throws FileNotFoundException {

		int[] yearArray = new int[fileCounter("yearly_player_demand.txt")];
		int[] salaryArray = new int[fileCounter("players_salary.txt")];
		fileOperation("yearly_player_demand.txt", yearArray);
		fileOperation("players_salary.txt", salaryArray);
		int player = 5, coach = 5;

		long startTime = System.nanoTime();
		System.out.println("\nDP Results: " + DP(yearArray.length - 1, player, coach, yearArray, salaryArray));
		long estimatedTime = System.nanoTime();
		System.out.println("nanoTime: " + (estimatedTime - startTime));
	}

	public static int DP(int n, int p, int c, int[] year, int[] salary) {
		int totalCost = 0;
		int maxPlayer = maxPlayer(year);
		int[][] dp = new int[n + 1][maxPlayer];
		int[][] path = new int[n + 1][maxPlayer];

		for (int i = 1; i <= year[0]; i++) { //first year calculate
			if (i <= p)
				dp[1][i] = 0;
			else if (year[0] > p)
				dp[1][i] = dp[1][i - 1] + c;
			else if (year[0] < p) {
				dp[1][i] = salary[i - year[0] - 1];
			}
		}
		path[1][year[0]] = dp[1][year[0]];

		int i = 2, j = 1, k = 0;
		for (i = 2; i < n; i++) { //calculate without first year
			int demandPlayer = year[i - 1];
			for (j = 1; j <= demandPlayer; j++) {
				if (j <= p)
					dp[i][j] = 0;
			}

			if (demandPlayer < p)
				k = j;
			else
				k = 6;

			for (; k < maxPlayer; k++) {
				if (k > demandPlayer && p >= demandPlayer) {
					if (year[i - 1] < p && k <= p)
						dp[i][k] = salary[k - demandPlayer - 1];
				} else if (k <= demandPlayer) {
					if (dp[i - 1][k - 1] != 0 && year[i - 2] < p)
						dp[i][k] = Math.min(dp[i][k - 1] + (1 * c), dp[i][k - 1] + dp[i - 1][k - 1]);
					else
						dp[i][k] = dp[i][k - 1] + (1 * c);
				}
			}
			path[i][demandPlayer] = dp[i][demandPlayer];
		}

		// cost calculate with path
		for (int y = 0; y < n; y++) {
			for (int players = 0; players < maxPlayer; players++) {
//				System.out.print(dp[y][players] + "\t"); //print knapsack table
				totalCost = totalCost + path[y][players];
				if (players == year[y]) {
					System.out.println(y + "," + players); // print path
				}
			}
//			System.out.println(); //for knapsack table
		}
		return totalCost;
	}

	public static void fileOperation(String fileName, int[] array) throws FileNotFoundException {
		String[] splitWords = null;
		File Obj1 = new File(fileName);
		Scanner Reader = new Scanner(Obj1);
		int counter = 0;
		splitWords = Reader.nextLine().split("\t"); // to pass the first line
		while (Reader.hasNextLine()) {
			splitWords = Reader.nextLine().split("\t");
			array[counter] = Integer.parseInt(splitWords[1]);
			counter++;
		}
		Reader.close();
	}

	public static int fileCounter(String fileName) throws FileNotFoundException {
		File Obj1 = new File(fileName);
		Scanner Reader = new Scanner(Obj1);
		int counter = 0;
		Reader.nextLine().split("\t"); // to pass the first line
		while (Reader.hasNextLine()) {
			Reader.nextLine().split("\t");
			counter++;
		}
		Reader.close();
		return counter + 1;
	}

	public static int maxPlayer(int[] year) { //most wanted player counter
		int maxPlayer = 0;
		for (int i = 0; i < year.length; i++) {
			if (year[i] > maxPlayer) {
				maxPlayer = year[i];
			}
		}
		return maxPlayer + 1;
	}
}

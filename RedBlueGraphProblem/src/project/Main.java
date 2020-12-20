package project;

import project.strategies.Heuristic1;
import project.strategies.Heuristic2;

public class Main {

    public static void main(String[] args) {
        System.out.println("----Heuristic 1 - Running ----");
        double[][] res = Stats.run(new Heuristic1());
        printRes(res);
        System.out.println("-------------End---------------");
        System.out.println("----Heuristic 2 - Running ----");
        res = Stats.run(new Heuristic2());
        printRes(res);
        System.out.println("-------------End---------------");
    }

    static void printRes(double[][] res) {
        System.out.print("p | q");
        for (int j = 0; j < 11; j++) {
            System.out.print("  " + j/10.);
        }
        for (int i = 0; i < 11; i++) {
            System.out.println();
            System.out.print(i/10.);
            for (int j = 0; j < 11; j++) {
                System.out.print("  " + res[i][j]);
            }
        }
        System.out.println();
    }
}

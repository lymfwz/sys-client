package com.fwzlym.grpcclient;

import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2023-10-17-19:50
 */
public class BD {
}
//import java.util.*;
class Main{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] dishes = new int[n][2];
        for (int i = 0; i < n; i++) {
            dishes[i][0] = in.nextInt();
            dishes[i][1] = in.nextInt();
        }
        int[][] dp = new int[n+1][m + 1];
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j >= dishes[i-1][1]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - dishes[i-1][1]] + dishes[i-1][1] - dishes[i-1][0]);
                }
            }
        }
        System.out.println(dp[n][m]);
    }
}
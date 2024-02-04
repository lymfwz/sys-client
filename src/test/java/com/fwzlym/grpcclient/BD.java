package com.fwzlym.grpcclient;

import java.math.BigInteger;
import java.util.*;

/**
 * @AUTHOR: qiukui
 * @CREATE: 2023-10-17-19:50
 */
public class BD {
}

class Main1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int a = in.nextInt();
        int b = in.nextInt();

        String[] matrix = new String[n];
        for (int i = 0; i < n; i++) {
            matrix[i] = in.next();
        }
        List<int[]> portals = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i].charAt(j) == '*') {
                    portals.add(new int[]{i, j});
                }
            }
        }

        PriorityQueue<int[]> pQue = new PriorityQueue<>(new Comparator<int[]>() {//每次只取最小的代价，更新它周围的代价
            @Override
            public int compare(int[] v1, int[] v2) {
                return v1[2] - v2[2];
            }
        });

        int[][] dist = new int[n][n];
        int[][] isVisit = new int[n][n];

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(isVisit[i], 0);
        }

        dist[0][0] = 0;
        isVisit[0][0] = 1;
        pQue.add(new int[]{0, 0, 0});

        while (!pQue.isEmpty()) {
            int[] vecCur = pQue.poll();
            int curX = vecCur[0];
            int curY = vecCur[1];

            if (curX == portals.get(0)[0] && curY == portals.get(0)[1]) {//传送门
                int x = portals.get(1)[0];
                int y = portals.get(1)[1];//彼岸
                if (isVisit[x][y] == 0) {
                    if (dist[x][y] > dist[curX][curY] + b) {
                        dist[x][y] = dist[curX][curY] + b;
                        isVisit[x][y] = 1;
                        pQue.add(new int[]{x, y, dist[x][y]});
                    }
                }
            } else if (curX == portals.get(1)[0] && curY == portals.get(1)[1]) {
                int x = portals.get(0)[0];
                int y = portals.get(0)[1];
                if (isVisit[x][y] == 0) {
                    if (dist[x][y] > dist[curX][curY] + b) {
                        dist[x][y] = dist[curX][curY] + b;
                        isVisit[x][y] = 1;
                        pQue.add(new int[]{x, y, dist[x][y]});
                    }
                }
            }

            for (int[] d : dirs) {//四个方向
                int x = curX + d[0];
                int y = curY + d[1];

                if (x < 0 || y < 0 || x >= n || y >= n || isVisit[x][y] != 0) {//越界
                    continue;
                }

                if (matrix[x].charAt(y) == '#') {//跨越障碍
                    if (dist[x][y] > dist[curX][curY] + a) {
                        dist[x][y] = dist[curX][curY] + a;
                        isVisit[x][y] = 1;
                        pQue.add(new int[]{x, y, dist[x][y]});
                    }
                } else {//不跨越障碍
                    if (dist[x][y] > dist[curX][curY]) {
                        dist[x][y] = dist[curX][curY];
                        isVisit[x][y] = 1;
                        pQue.add(new int[]{x, y, dist[x][y]});
                    }
                }
            }
        }

        System.out.println(dist[n - 1][n - 1]);
    }
}

class Main{
    public static void main(String[] args) {
        String s = "carrier";
        StringBuffer s1 = new StringBuffer(s);
        s1.append("中国移动");
        s1.insert(s.length(), '/');
        s1.replace(0, s.length(), "live China Mobile");
        s1.delete(0, 5);
        System.out.println(s1);
    }
}
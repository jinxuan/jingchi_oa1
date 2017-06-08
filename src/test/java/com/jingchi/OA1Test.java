package com.jingchi;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jinxuanwu on 6/5/17.
 */
public class OA1Test {
    @Test
    public void testMinTraverseDistance() {
        OA1 oa1 = new OA1();
        try {
            File resourcesDirectory = new File("src/test/resources");
            File[] files = resourcesDirectory.listFiles();
            for (File file : files) {
                long startTime = System.nanoTime();
                if (file.isFile()) {
                    List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
                    char[][] townMap = new char[lines.size() - 1][lines.get(0).length()];
                    for (int i = 0; i < lines.size() - 1; i++) {
                        for (int j = 0; j < lines.get(0).length(); j++) {
                            townMap[i][j] = lines.get(i).charAt(j);
                        }
                    }
                    int expected = Integer.parseInt(lines.get(lines.size() - 1));
                    assertEquals("Failed running test case " + file.getName(), expected, oa1.minTraverseDistance(townMap, true));
                    oa1.minTraverseDistance(townMap, true);
                }
                long endTime = System.nanoTime();
                long ececutionTime = (endTime - startTime) / 1000000;
                System.out.println("TestCase" + file.getName() + " succeed in " + ececutionTime + "mili seconds");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
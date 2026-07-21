package com.banaanae.javasccore.titan;

import com.banaanae.javasccore.logic.data.LogicDataTableResource;
import com.banaanae.javasccore.logic.data.LogicResources;
import com.banaanae.javasccore.titan.csv.CSVNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ResourceManager {

    public static void init() {
        long start = System.nanoTime();
        System.out.println("[ResourceManager] Loading CSV");
        loadResources();
        double seconds = (System.nanoTime() - start) / 1_000_000_000.0;
        System.out.printf("[ResourceManager] Loading took %.3f seconds%n", seconds);
    }

    public static void loadResources() {
        List<LogicDataTableResource> resources = LogicResources.createDataTableResourcesArray();

        for (int i = 0; i < resources.size(); i++) {
            LogicDataTableResource resource = resources.get(i);

            String fileName = resource.getFileName();
            String content = loadResourceContent(fileName);

            if (content != null) {
                String[] rows = content.split("\n\0?");
                if (rows.length > 0 && rows[rows.length - 1].isEmpty()) {
                    rows = Arrays.copyOf(rows, rows.length - 1);
                }
                CSVNode node = new CSVNode(rows, fileName);
                LogicResources.load(resources, i, node);
            } else {
                Debugger.error("ResourceManager.loadResources: file " + fileName + " not exist.");
            }
        }
    }

    public static String loadResourceContent(String file) {
        try {
            String baseDir = System.getProperty("user.dir").replaceAll("\\.titan$", "");

            Path path = Paths.get(".", "assets", file).toAbsolutePath().normalize();

            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}
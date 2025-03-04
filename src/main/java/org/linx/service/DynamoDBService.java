package org.linx.service;

import java.util.*;

public class DynamoDBService {
    private static final Map<String, List<String>> tagIndex = new HashMap<>();

    public void saveFileTags(String fileName, String projectName, String[] tags, String s3Url) {
        for (String tag : tags) {
            tagIndex.computeIfAbsent(tag.toLowerCase(), k -> new ArrayList<>()).add(fileName + " (Project: " + projectName + ")");
        }
        System.out.println("Mock: Saved tags " + Arrays.toString(tags) + " for file '" + fileName + "'");
    }

    public List<String> searchFilesByTags(String[] tags) {
        Set<String> results = new HashSet<>();
        for (String tag : tags) {
            results.addAll(tagIndex.getOrDefault(tag.toLowerCase(), new ArrayList<>()));
        }
        return new ArrayList<>(results);
    }

    public void deleteFileTags(String fileName, String projectName) {
        tagIndex.forEach((tag, files) -> files.remove(fileName + " (Project: " + projectName + ")"));
        System.out.println("Mock: Deleted tags for file '" + fileName + "' in project '" + projectName + "'");
    }
}
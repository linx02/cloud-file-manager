package org.linx.service;

import java.util.*;

public class DatabaseService {
    private static final Map<String, List<String>> projectFiles = new HashMap<>();
    private static final Set<String> projects = new HashSet<>();

    public boolean projectExists(String projectName) {
        return projects.contains(projectName);
    }

    public void createProject(String projectName, String description) {
        projects.add(projectName);
        projectFiles.put(projectName, new ArrayList<>());
        System.out.println("Mock: Created project '" + projectName + "' with description: " + description);
    }

    public void saveFileMetadata(String fileName, String projectName, String s3Url) {
        projectFiles.get(projectName).add(fileName);
        System.out.println("Mock: Saved file '" + fileName + "' in project '" + projectName + "' (URL: " + s3Url + ")");
    }

    public List<String> listFiles(String projectName) {
        return projectFiles.getOrDefault(projectName, new ArrayList<>());
    }

    public void deleteFileMetadata(String fileName, String projectName) {
        projectFiles.getOrDefault(projectName, new ArrayList<>()).remove(fileName);
        System.out.println("Mock: Deleted file '" + fileName + "' from project '" + projectName + "'");
    }

    public List<String> getProjects() {
        return new ArrayList<>(projects);
    }
}
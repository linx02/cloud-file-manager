package org.linx.service;

import org.linx.exception.ProjectNotFoundException;

import java.util.List;

public class ProjectService {
    public List<String> getProjects() {
        return List.of("Project 1", "Project 2", "Project 3");
    }

    public List<String> getFilesFromProject(String project) throws ProjectNotFoundException {
        List<String> projects = getProjects();
        if (!projects.contains(project)) {
            throw new ProjectNotFoundException("Project not found");
        }

        return List.of("File 1", "File 2", "File 3");
    }

    public void createProject(String project) {
        System.out.println("Creating project " + project);
    }

    public void deleteProject(String project) {
        System.out.println("Deleting project " + project);
    }
}

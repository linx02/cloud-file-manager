package org.linx.service;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DatabaseService {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public DatabaseService() {
        loadDatabaseConfig();
        initializeDatabaseIfEmpty();
    }

    private void loadDatabaseConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
            dbUrl = props.getProperty("DB_URL");
            dbUser = props.getProperty("DB_USER");
            dbPassword = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            System.err.println(".env file issue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void initializeDatabaseIfEmpty() {
        if (isDatabaseEmpty()) {
            System.out.println("Initializing database...");
            executeSQLFile("/db/schema.sql");
//            executeSQLFile("/db/data.sql");
            System.out.println("Database initialized.");
        }
    }

    private boolean isDatabaseEmpty() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {

            return !rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void executeSQLFile(String filePath) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             InputStream inputStream = getClass().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    sqlBuilder.append(line).append("\n");
                    if (line.trim().endsWith(";")) {
                        stmt.execute(sqlBuilder.toString().trim());
                        sqlBuilder.setLength(0);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error executing SQL file: " + filePath);
            e.printStackTrace();
        }
    }

    public boolean projectExists(String projectName) {
        String sql = "SELECT COUNT(*) FROM projects WHERE project_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectName);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createProject(String projectName) {
        String sql = "INSERT INTO projects (project_name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listFiles(String projectName) {
        List<String> files = new ArrayList<>();
        String sql = "SELECT file_name FROM files WHERE project_id = (SELECT id FROM projects WHERE project_name = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                files.add(rs.getString("file_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    public List<String> getProjects() {
        List<String> projects = new ArrayList<>();
        String sql = "SELECT project_name FROM projects";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projects.add(rs.getString("project_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public void addFile(String projectName, String fileName, String s3Url) {
        String sql = "INSERT INTO files (project_id, file_name, s3_url) VALUES ((SELECT id FROM projects WHERE project_name = ?), ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectName);
            stmt.setString(2, fileName);
            stmt.setString(3, s3Url);

            stmt.executeUpdate();
            System.out.println("File added successfully: " + fileName + " (Project: " + projectName + ")");
        } catch (SQLException e) {
            System.err.println("Error adding file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
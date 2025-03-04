INSERT INTO projects (project_name) VALUES
('project1'),
('project2'),
('project3');

INSERT INTO files (project_id, file_name, s3_url) VALUES
(1, 'file1', 's3://mock-bucket/project1/file1'),
(1, 'file2', 's3://mock-bucket/project1/file2'),
(2, 'file3', 's3://mock-bucket/project2/file3'),
(2, 'file4', 's3://mock-bucket/project2/file4'),
(3, 'file5', 's3://mock-bucket/project3/file5'),
(3, 'file6', 's3://mock-bucket/project3/file6');
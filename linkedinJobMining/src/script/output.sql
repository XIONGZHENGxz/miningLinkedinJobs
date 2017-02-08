USE "LinkedInJobs"
SELECT Id,Title,Company,Type,Experience,Function,Location,views 
INTO OUTFILE '/tmp/linkedinJobsRaw.csv' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'
FROM jobs


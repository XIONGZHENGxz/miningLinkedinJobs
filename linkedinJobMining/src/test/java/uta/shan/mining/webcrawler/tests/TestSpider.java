package uta.shan.mining.webcrawler.tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import uta.shan.mining.webcrawler.Result;
import uta.shan.mining.webcrawler.Spider;
import java.util.List;
public class TestSpider {
	private Spider spider;
	private String path;
	private String url;
	@Before 
	public void initSpider(){
		url="https://www.linkedin.com/jobs/search?keywords=&location=&trk=jobshomev2_2boxsearch&orig=JSHP&locationId=";
		path=System.getProperty("property_path");
		String linkPattern="viewJobTextUrl\":\"(.*?)\",";
		/**
		String jobPattern="title\" content=\"(.*?)\">.*+viewCount\":(.*?),.*+formattedLocation\":\"(.*?)\",\"formattedIndustries.*+universalName\":\"(.*?)\".*+formattedJobFunctions\":\"(.*?)\",\"formattedEmploymentStatus\":\"(.*?)\".*+formattedExperience\":\"(.*?)\"";
		*/
		String jobPattern = "title\" content=\"(.*?)\">(?s).*viewCount\":(.*?),";
		String nextPagePattern="next\":\"(.*?)\",";
		this.spider=new Spider(path,"LinkedInJobs","jobs",url,linkPattern,jobPattern,nextPagePattern);
	}

	@Test 
	public void testInit(){
		assertNotNull(spider);
		assertNotNull(spider.db);
		assertTrue(spider.db.name.equals("LinkedInJobs"));
	}

	@Test
	public void testCrawl(){
		spider.crawl();
	}
/**	
	@After
	public void clean(){
		spider.db.drop();
	}
*/
}

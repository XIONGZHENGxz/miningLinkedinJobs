package uta.shan.mining.webcrawler.tests;

import org.junit.Test;
import uta.shan.mining.webcrawler.Result;
import uta.shan.mining.webcrawler.Spider;
import java.util.List;
public class TestSpider {
	@Test 
	public void test(){
		Spider spider=new Spider();
		String url="https://www.linkedin.com/jobs/search?keywords=&location=&trk=jobshomev2_2boxsearch&orig=JSHP&locationId=";
		String pattern="job-title-link.*+href=\"(.*?)\">";
		List<Result> res=spider.search(url,pattern,"computer");
		for(Result result:res){
			System.out.println(result.getVal());
		} 
	}
}

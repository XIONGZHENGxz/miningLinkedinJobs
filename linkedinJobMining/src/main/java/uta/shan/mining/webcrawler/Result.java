package uta.shan.mining.webcrawler;
/**
 * 
 * @author xz
 * crawl result class
 */
import java.time.LocalDateTime;

public class Result {
	String url;
	String title;
	String company;
	String type;//full-time or internship
	String experience;
	String function;
	String location;
	String now;
	int numOfViews;
	public Result(String url,String t,String com,String type,String exp,String func,String loc,int num){
		this.url=url;
		title=t;
		company=com;
		this.type=type;
		experience=exp;
		function=func;
		location=loc;
		now=LocalDateTime.now().toString();
		numOfViews=num;
	}
}

package uta.shan.mining.webcrawler;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import uta.shan.mining.createDataBase.DataBase;
import uta.shan.mining.webcrawler.Result;
public class Spider {
	private final static int MAX_PAGES=1000;
	private Set<String> pageVisited;//page crawled
	private List<String> pageToVisit;//page to crawl
	public DataBase db;
	private SpiderLeg leg;
	private List<String> urls;
	private int count=1;
	private String linkPattern;
	private String jobInfoPattern;
	private String nextPagePattern;
	private String startUrl;
	private static String fields1="(Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, URL VARCHAR(1000) NOT NULL,Title VARCHAR(400) NOT NULL,Company VARCHAR(100) NOT NULL,Type VARCHAR(100),Experience VARCHAR(200),Function VARCHAR(400),Location VARCHAR(100) NOT NULL,Recordtime VARCHAR(100),views INT NOT NULL)";
	public Spider(String path,String dbname,String tableName,String url,String pattern,String jobPattern,String nextPage){
		db=new DataBase(path,dbname,tableName,fields1);
		pageVisited=new HashSet<>();
		pageToVisit=new ArrayList<>();
		leg=new SpiderLeg();
		urls=new ArrayList<>();
		startUrl=url;
		linkPattern=pattern;
		jobInfoPattern=jobPattern;
		nextPagePattern=nextPage;
	}

	//crawl linkedin jobs,starting point
	public void crawl(){
		this.findCurrUrls();
		while(count<MAX_PAGES*10){
			this.insertRecords();
			this.findNextPage();
			this.findCurrUrls();
		}
	}

	//find current page job urls
	public void findCurrUrls(){
		this.urls=this.leg.searchCurrJobs(this.startUrl,this.linkPattern);
	}
	
	//store all urls in this page into urlTalbe
	public void insertRecords(){
		String query="INSERT INTO "+this.db.table+" VALUES ";
		for(String url:this.urls){
			Result result=this.findJobInfo(url,this.jobInfoPattern);
			if(result==null) continue;
			this.db.insert(query+"("+String.valueOf(count++)+","+result.url+","+result.title+","+result.company+","+result.type+","+result.experience+","+result.function+","+result.location+","+result.now+","+result.numOfViews+")");
		}
	}
	//find a particular job info 
	public Result findJobInfo(String url,String jobInfoPattern){
		return this.leg.findJobInfo(url,jobInfoPattern);
	}
	
	//find next button url
	public void findNextPage(){
		this.startUrl=this.leg.searchNextPage(this.startUrl,this.nextPagePattern);
	}
	/**	
	// starting point of spider 
	public List<Result> search(String url,String linkPattern,String pattern_to_search){
		List<Result> res=new ArrayList<>();
		while(this.pageVisited.size()<Spider.MAX_PAGES){
			String curr_url="";
			if(this.pageToVisit.size()==0){
				curr_url=url;
			}else{
				curr_url=this.nextUrl();
			}
			pageVisited.add(curr_url);
			SpiderLeg leg=new SpiderLeg();
			List<Result> tmp=leg.crawl(curr_url,linkPattern,pattern_to_search);
			if(tmp!=null)
			res.addAll(tmp);
		this.pageToVisit.addAll(leg.getUrl());
		}
		return res;
	}

*/
	//get next url to visit
	public String nextUrl(){
		String next=this.pageToVisit.remove(0);
		while(this.pageVisited.contains(next)){
			next=this.pageToVisit.remove(0);
		}
		this.pageVisited.add(next);
		return next;
	}
}

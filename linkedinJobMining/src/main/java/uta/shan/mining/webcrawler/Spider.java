package uta.shan.mining.webcrawler;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
public class Spider {
	private final static int MAX_PAGES=20;
	private Set<String> pageVisited;//page crawled
	private List<String> pageToVisit;//page to crawl
	public Spider(){
		pageVisited=new HashSet<>();
		pageToVisit=new ArrayList<>();
	}
	
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
			if(tmp==null) continue;
			res.addAll(tmp);
			this.pageToVisit.addAll(leg.getUrl());
		}
		return res;
	}

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

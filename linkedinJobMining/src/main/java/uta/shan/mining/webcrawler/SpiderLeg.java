package uta.shan.mining.webcrawler;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
	private List<String> urls=new ArrayList<>();
	private Document htmlDocument;
	private static final String USER_AGENT="Mozilla/5.0 (Windows NT 6.1; WOW64) "
			+ "AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	//get all urls in this page
	public List<String> getUrl(){
		return urls;
	}
	
	//crawl this page
	public List<Result> crawl(String url,String linkPattern,String pattern_to_search){
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			this.htmlDocument=connection.get();
			System.out.println("received a page from: "+url);
		} catch(Exception e){
			return  null;
		}
		if(this.htmlDocument==null){
			System.out.println("page is null");
			return null;
		}
		this.urls=this.searchForLinks(linkPattern);
		return this.seasrchForPattern(pattern_to_search);
	}

	public List<String> searchForLinks(String linkPattern){
		Pattern p=Pattern.compile(linkPattern);
		Matcher matcher = p.matcher(this.htmlDocument.body().text());
		List<String> links=new ArrayList<>();
		while(matcher.find()){
			links.add(matcher.group(0));
			System.out.println("links..."+matcher.group(0));
		}
		return links;
	}

	public List<Result> seasrchForPattern(String pattern){
		if(this.htmlDocument==null){
			System.err.println("Error! crawl first befor search.");
			return null;
		}
		Pattern p=Pattern.compile(pattern);
		List<Result> res=new ArrayList<>();
		String bodyText=this.htmlDocument.body().text();
		Matcher matcher = p.matcher(bodyText);
		while(matcher.find()){
			Result result=new Result(matcher.group());
			System.out.println("found page url: "+result.getVal());
			res.add(result);
		}
		return res;
	}
}

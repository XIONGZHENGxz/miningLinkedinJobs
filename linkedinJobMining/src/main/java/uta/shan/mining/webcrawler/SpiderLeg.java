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

import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SpiderLeg {
	private List<String> urls=new ArrayList<>();
	private Document htmlDocument;
	private static final String USER_AGENT="Mozilla/5.0 (Windows NT 6.1; WOW64) "
			+ "AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	//get all urls in this page
	public List<String> getUrl(){
		return urls;
	}

	//retrieve source from remote url
	public String retrieve(String url){
		try{
			Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
			this.htmlDocument=connection.get();
			System.out.println("retrieve page from:  "+url);
		} catch(Exception e){
			e.printStackTrace();
		}
		if(this.htmlDocument==null){
			System.out.println("page is null");
			return null;
		}
		return this.htmlDocument.html();
	}
		
	//crawl this page for all job urls
	public List<String> searchCurrJobs(String url,String linkPattern){
		String source=this.retrieve(url);
		List<String> links=new ArrayList<>();
		Pattern p=Pattern.compile(linkPattern);
		Matcher m= p.matcher(source);
		while(m.find()){
			links.add(m.group(1));
		}
		return links;
	}
	/**
	public static List<String> seasrchForPattern(String pattern){
		List<Result> res=new ArrayList<>();
		if(this.htmlDocument==null){
			System.err.println("Error! crawl first befor search.");
			return res;
		}
		Pattern p=Pattern.compile(pattern);
		Matcher matcher = p.matcher(bodyText);
		while(matcher.find()){
			Result result=new Result(matcher.group());
			System.out.println("found page url: "+result.getVal());
			res.add(result);
		}
		return res;
	}
*/
	//search for a particular job info
	public Result findJobInfo(String url,String regex){
		Result result=null;
		String source=this.retrieve(url);
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(source);
		if(m.find()){
			System.out.println(m.group(1)+"..."+m.group(2));
			result=new Result(url,m.group(1),m.group(4),m.group(6),m.group(7),m.group(5),m.group(3),Integer.parseInt(m.group(2)));
		}
		return result;
	}

	//find next page url
	public String searchNextPage(String url,String nextPattern){
		String source=this.retrieve(url);
		Pattern p=Pattern.compile(nextPattern);
		Matcher m=p.matcher(source);
		if(m.find()) return m.group(1);
		else return "";
	}
}

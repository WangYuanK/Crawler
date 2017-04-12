package com.jimi.crwaler;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractService {
	
	public static List<LinkTypeData> extract(Rule rule){
		validateRule(rule);
		List<LinkTypeData> datas = new ArrayList<LinkTypeData>();
		LinkTypeData data = null;
		try{
			String url = rule.getUrl();
			String[] params = rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int requestType = rule.getRequestMethod();
			
			Connection conn = Jsoup.connect(url).ignoreContentType(true);
			if(params != null){
				for(int i=0;i<params.length;i++){
					conn.data(params[i],values[i]);
				}
			}
			Document doc = null;
			switch(requestType){
				case Rule.GET:
					doc = conn.timeout(10000).get();
					break;
				case Rule.POST:
					doc = conn.timeout(10000).post();
					break;
			}
			
			Elements results = null;
			switch(type){
				case Rule.CLASS:
					results = doc.getElementsByClass(resultTagName);
					break;
				case Rule.ID:
					Element result = doc.getElementById(resultTagName);
					results.add(result);
				case Rule.SELECTION:
					results = doc.select(resultTagName);
					break;
				default:
					if(resultTagName==null){
						results = doc.getElementsByTag("body");
					}
			}
			for(Element result:results){
				Elements links = result.getElementsByTag("a");
				for (Element link : links)  
                {  
                    String linkHref = link.attr("href");  
                    String linkText = link.text();  
                    data = new LinkTypeData();  
                    data.setLinkHref(linkHref);  
                    data.setLinkText(linkText);  
  
                    datas.add(data);  
                }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return datas;
	}
	
	public static void validateRule(Rule rule){
		String url = rule.getUrl();
		if(url.equals("")){
			throw new RuleException("url����Ϊ��");
		}
		if(!url.startsWith("http://")&&!url.startsWith("https://")){
			throw new RuleException("url��ʽ����ȷ");
		}
		if(rule.getParams()!=null && rule.getValues()!=null){
			if(rule.getParams().length!=rule.getValues().length){
				throw new RuleException("�����ļ�ֵ�Ը�����ƥ��");
			}
		}
	}
}

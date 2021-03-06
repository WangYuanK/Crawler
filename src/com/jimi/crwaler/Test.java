package com.jimi.crwaler;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		Rule rule = new Rule(  
                "http://news.baidu.com/ns",  
        new String[] {"word"}, new String[] {"支付宝"},  
                "zs", Rule.SELECTION, Rule.GET);
        List<LinkTypeData> extracts = ExtractService.extract(rule);
        printf(extracts);
	}
	public static void printf(List<LinkTypeData> datas){
		for (LinkTypeData data : datas){
			System.out.println(data.getLinkText());  
			System.out.println(data.getLinkHref());  
			System.out.println("***********************************");  
		}  
	}  
}

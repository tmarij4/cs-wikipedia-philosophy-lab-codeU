package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        
        boolean success = false, running = true;
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        List<String> store = new ArrayList<String>();
        
        while (!success && running){
            
            Elements paragraphs = wf.fetchWikipedia(url);
            Element firstPara = paragraphs.get(0);
            String help = visit(url, firstPara);
            if (help.equals(null)){
                running = false;
            }else{
                store.add(help);
                if (help.equals("https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy")){
                    success = true;
                }
                url = help;
            }
        }
    }
    
    public static String visit(String current, Element para){
        Iterable<Node> iter = new WikiNodeIterable(para);
        for (Node node: iter) {
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (valid(ele, current)){
                    String url = node.attr("href");
                    return url;
                }
            }
        }
        return null;
    }
    
    public static boolean valid(Element ele, String current){
        
        //Italics Check
        for (Element start = ele; start != null; start = start.parent()){
            if (start.tagName().equals("i") || start.tagName().equals("em")){
                return false;
            }
        }
        
        //Starts with strange letters
        if (!ele.attr("href").startsWith("/wiki/Help:")){
            return false;
        }
        
        //Is the current page
        if (ele.attr("href").equals(current)){
            return false;
        }
        return true;
    }
}

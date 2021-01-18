package kr.blue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Project02_A {
    public static void main(String[] args) {
        String url = "https://sports.news.naver.com/wfootball/index.nhn";
        Document doc = null;
        try{
            doc = Jsoup.connect(url).get();
        }catch (IOException e){
            e.printStackTrace();
        }
        //추천 뉴스로 나오는 태그를 찾아서 가져오도록 한다.
        Elements elements = doc.select("div.good_news");

        //1. 헤더 부분의 제목을 가져온다
        String title = elements.select("h2").text();

        System.out.println("===========================================");
        System.out.println(title);
        System.out.println("===========================================");

        for(Element el : elements.select("li")){ //하위 뉴스 기사들을 for문 돌면서 출력
            System.out.println(el.text());
        }
        System.out.println("===========================================");
    }


}

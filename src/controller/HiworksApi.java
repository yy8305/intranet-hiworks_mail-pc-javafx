package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.mysql.jdbc.Connection;

public class HiworksApi {
	
	public static String cookie = "";
	public static String login_html = "";
	public static String main_html = "";
	public static Response main_respone;
	public static int mail_cnt = 0;
	public static int new_mail_cnt = 0;
	
	public HiworksApi(String id1, String password1) throws IOException {
		HiworksLogin(id1, password1);
	}
	
	public void HiworksLogin(String id2, String password2) throws IOException {
		Response first_rs = (Response) Jsoup.connect("http://hiworks.<office ID>.co.kr/")
	            .header("Host", "office.hiworks.com")
	            .header("Connection", "keep-alive")
	            .header("Cache-Control", "max-age=0")
	            .header("Upgrade-Insecure-Requests", "1")
	            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
	            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
	            .header("Accept-Encoding", "gzip, deflate, br")
	            .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
	            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
	            .method(Method.GET).execute();
				
		System.out.println(first_rs.cookies().toString());
        String url = "https://office.hiworks.com/<office domain>/home/ssl_login";
        Response rs = (Response) Jsoup.connect(url) 
                .data("ssl_login", "Y") 
                .data("link_url", "outlogin") 
                .data("ip_security", "1") 
                .data("office_id", id2) 
                .data("office_passwd", password2) 
                .header("Host", "office.hiworks.com")
                .header("Connection", "keep-alive")
                .header("Content-Length", "78")
                .header("Cache-Control", "max-age=0")
                .header("Origin", "http://hiworks.<office domain>")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Referer", "http://hiworks.<office domain>/")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cookie", first_rs.cookies().toString())
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .method(Method.POST).execute();
	        
        Document doc = rs.parse();
        main_respone = rs;
        this.login_html = doc.html().toString();

        System.out.println(rs.cookies().toString());
        StringCookie();

        Document mainPage = Jsoup.connect("https://office.hiworks.com/<office domain>/common/office_main").cookies(rs.cookies()).get();
        this.main_html = mainPage.html().toString();
	}
	
	public static void HiworksLogout() throws IOException {
		System.out.println(cookie);
		Response rs = (Response) Jsoup.connect("https://mail.office.hiworks.com/<office domain>/home/Logout")
                .data("unixtime", "1528645439000&_=") 
                .header("Host", "mail.office.hiworks.com")
                .header("Connection", "keep-alive")
                .header("Content-Length", "25")
                .header("Cache-Control", "max-age=0")
                .header("Origin", "http://hiworks.<office domain>")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("X-Prototype-Version", "1.6.0.3")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Referer", "https://mail.office.hiworks.com/<office domain>/mail/webmail/m_list/b0")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cookie", cookie)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .method(Method.POST).execute();
	}
	
	public int WebmailCount() throws IOException {
		Document doc = Jsoup.connect("https://mail.office.hiworks.com/<office domain>/mail/webmailcount")
                .header("Host", "mail.office.hiworks.com")
                .header("Connection", "keep-alive")
                .header("Cache-Control", "max-age=0")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cookie", this.cookie)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .method(Method.POST).ignoreContentType(true).get();
		
		System.out.println("====json body====");
		System.out.println(doc.text());
		
		if(doc.html().toString().indexOf("alert(\"보안을 위하여 다시 로그인해 주시기 바랍니다.\")") == -1) {
			List<String> result = new ArrayList<>();

			JSONParser parser = new JSONParser();
			try {
				Object object = parser.parse(doc.text());
				JSONArray array = (JSONArray) object;
				JSONObject jsonObject = (JSONObject) array.get(0);
				/*System.out.println("mbox_no:" + jsonObject.get("mbox_no"));*/
				this.new_mail_cnt = Integer.parseInt(jsonObject.get("new_mail_cnt").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			JOptionPane.showMessageDialog(null, "보안을 위하여 다시 로그인해 주시기 바랍니다.");
        	return 1;
		}
		return 0;
	}
	
	public void StringCookie() {
		this.cookie = main_respone.cookies().toString().replaceAll(" ", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\,", ";");
		System.out.println(this.cookie);
	}
	
	/*public static void main(String[] args) throws IOException {
		controller.HiworksApi hiworks = new controller.HiworksApi("<id>","<password>");
		hiworks.WebmailCount();
		hiworks.HiworksLogout();
	}*/
}

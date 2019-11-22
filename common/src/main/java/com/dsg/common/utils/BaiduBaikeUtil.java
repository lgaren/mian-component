package com.dsg.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created on 2018/9/6  Thu AM 11:36
 * producttag
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: BaiduBaikeUtil V 0.0, Sep 06, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class BaiduBaikeUtil {

    public static String urlPex = "https://baike.baidu.com/item/";

    public static String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    public static String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
    public static String regEx_html = "<[^>]+>";
    public static String format = " ";
    public static String tarin = "乘车路线</h2>((?!<h2 class=\"title-text\"><span class=\"title-prefix\">).)*";
    public static String mutl = "\n*\t*[ ]*";

    public static Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
    public static java.util.regex.Matcher m_script;
    public static Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
    public static java.util.regex.Matcher m_style;
    public static Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
    public static java.util.regex.Matcher m_html;

    public static String edit= "<em class=\"cmn-icon wiki-lemma-icons wiki-lemma-icons_edit-lemma\"></em>编辑</a>";
    public static String error = "检查输入的文字是否有误";
    public static String mutil = "这是一个多义词，请在下列义项上选择浏览";

    public static String getHtmlText(String name )  {
        if (StringUtils.isBlank(name )) return "";
        return gettext(getHtml(urlPex + name));
    }

    public static String  getHtml(String url)  {
        System.out.println(url);
        try {
            Document doc = Jsoup.parse(new URL(url), 5000);
            Elements doc2 ;
            doc2 = doc.body()
                    .getElementsByClass("body-wrapper")
                    .select("div[class=content-wrapper]")
                    .select("div[class=main-content]")
                    .first()
                    .children();
            String str = gettext(doc2.toString()).replaceAll(mutl,"");
            if (str.contains(error) ) {
                return "";
            }
            if (str.contains(mutil)) {
                String itemurl = url + "/" +doc2.select("ul[class=custom_dot  para-list list-paddingleft-1]").select("li[class=list-dot list-dot-paddingleft]")
                        .first().children().first().children().first().attr("data-lemmaid");
                System.out.println(itemurl);
                return getHtml( itemurl);
            }

            String fitler  =  "basic-info cmn-clearfix top-tool album-list rs-container-foot lemmaWgt-lemmaTitle lemmaWgt-lemmaTitle- basic-info cmn-clearfix lemmaWgt-lemmaCatalog promotion-declaration configModuleBanner rs-container-foot clear anchor-list";
            doc2.removeIf( e ->
                    fitler.contains(e.className()) || e.id().equals("open-tag")
            );
            Elements doc4 = doc2.select("div[class=lemma-summary]").select("div[class=para]");
            doc2.add(0, doc4.first());
            return doc2.toString();
        }catch (Exception e ){
            return "";
        }
    }

    private static String gettext(String doc) {
        String doc1 = doc.replaceAll("\n","").replaceAll(tarin,"").replace(edit,"");
        m_script = p_script.matcher(doc1);
        doc1 = m_script.replaceAll("");
        m_style = p_style.matcher(doc1);
        doc1 = m_style.replaceAll("");
        m_html = p_html.matcher(doc1);
        doc1 = m_html.replaceAll("");
        return doc1.replaceAll(format,"");
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.home"));
    }

}

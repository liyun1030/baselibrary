package base.view.emoji;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * created by 李云 on 2019/5/5
 * 本类的作用:
 */
public class XmlParser {
    public static List<EmoBean> parseXml(Context ctx) {
        try {
            InputStream is = ctx.getAssets().open("emotion_list.xml");
            DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
            //获取：DocumentBuilder对象
            DocumentBuilder dBuilder = dBuilderFactory.newDocumentBuilder();
            //将数据源转换成：document对象
            Document document = dBuilder.parse(is);
            //获取根元素
            Element element = (Element) document.getDocumentElement();
            //获取子对象的数值读取lan标签的内容
            NodeList nodeList = element.getElementsByTagName("dict");
            List<EmoBean> beanList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                //获取对应的对象
                Element dict = (Element) nodeList.item(i);
                EmoBean emoBean = new EmoBean();
                emoBean.setCh(dict.getElementsByTagName("ch").item(0).getTextContent());
                emoBean.setSuffix(dict.getElementsByTagName("suffix").item(0).getTextContent());
                emoBean.setFileName(dict.getElementsByTagName("fileName").item(0).getTextContent());
                beanList.add(emoBean);
            }
            return beanList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}

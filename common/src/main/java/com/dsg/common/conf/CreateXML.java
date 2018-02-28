package com.dsg.common.conf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2017/11/272017 十一月 星期一下午 15:07
 * seagull
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  CreateXML, V 0.0  2017/11/27 下午 15:07 DSG Exp$$
 */
public class CreateXML {
    public static void main(String[] args) {
        File file = new File("dsg-site.xml");
        try {
//            file.createNewFile();
            FileWriter out = new FileWriter(file,true);
            Document doc = asXmlDocument();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(out);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(source, result);
            out.flush();out.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

    static class Property {
        public String name;
        public String value;
        public String description;

        public Property(String name, String value, String description) {
            this.name = name;
            this.value = value;
            this.description = description;
        }
    }

    private static List readProper() {
//        Configuration conf =  DSGConfig.getConf();
        DSGConfig.ConfVars[] properties = DSGConfig.ConfVars.values();
        System.out.print(properties.length);
        List<Property> prop = new ArrayList<>();
        Arrays.stream(properties).forEach(e -> {
            prop.add(new Property(e.varname, e.defaultStrVal, e.description));
//            prop.add(new Property(e.varname, String.valueOf(e.defaultBoolVal), e.description));
//            prop.add(new Property(e.varname,  String.valueOf(e.defaultFloatVal), e.description));
//            prop.add(new Property(e.varname,  String.valueOf(e.defaultIntVal), e.description));
//            prop.add(new Property(e.varname,  String.valueOf(e.defaultLonglVal), e.description));
        });
        return prop;
    }

    /**
     * Return the XML DOM corresponding to this Configuration.
     */
    private static Document asXmlDocument() throws IOException {
        Document doc;
        try {
            doc =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException pe) {
            throw new IOException(pe);
        }
        Element conf = doc.createElement("configuration");
        doc.appendChild(conf);
        conf.appendChild(doc.createTextNode("\n"));

        List<Property> properties = readProper();

        properties.stream().forEach(e -> {
            Element propNode = doc.createElement("property");
            conf.appendChild(propNode);
            propNode.appendChild(doc.createTextNode("\n"));

            Element nameNode = doc.createElement("name");
            nameNode.appendChild(doc.createTextNode(e.name));
            propNode.appendChild(nameNode);
            propNode.appendChild(doc.createTextNode("\n"));


            Element valueNode = doc.createElement("value");
            valueNode.appendChild(doc.createTextNode(e.value));
            propNode.appendChild(valueNode);
            propNode.appendChild(doc.createTextNode("\n"));

            Element sourceNode = doc.createElement("description");
            sourceNode.appendChild(doc.createTextNode(e.description));
            propNode.appendChild(sourceNode);
            propNode.appendChild(doc.createTextNode("\n"));

            conf.appendChild(doc.createTextNode("\n"));
        });
        return doc;
    }

}

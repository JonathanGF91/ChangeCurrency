/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package changedivisas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Jonathan
 */
public class GetDatas {
    private static final String urlBase = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    static String xml = "";
    
    private static String getDatasECB(){
        try {
            StringBuilder builder = new StringBuilder();
            URL url = new URL(urlBase);
            
            //Abrimos la conexion
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            
           
            //Buffer para leer
            BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String datos;
            
            while((datos = br.readLine()) != null){
                builder.append(datos);
            }
            br.close();
            
            xml = builder.toString();
            
            File f = new File("file.xml");
            
            if(f.createNewFile()){
                FileWriter fichero = null;
                PrintWriter pw = null;
                
                fichero = new FileWriter("file.xml");
                pw = new PrintWriter(fichero);
                pw.println(xml);
                
                fichero.close();
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GetDatas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetDatas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xml;
    }
    
    public static Map<String, Double> readXML(){
        xml = getDatasECB();
        Map<String, Double> map = new HashMap<>();
        String currency = "";
        Double rate = 0.0;
        if(!xml.isEmpty()){
            try {                
                DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = xmlFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(new File("file.xml"));
                
                document.getDocumentElement().normalize();
                
                NodeList nList = document.getElementsByTagName("Cube");
                
                for(int i = 2; i < nList.getLength(); i++){
                    Node node = nList.item(i);
                    
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element) node;
                        
                        currency = element.getAttribute("currency");
                        rate = Double.parseDouble(element.getAttribute("rate"));                        
                        rate = 1 / rate;
                        
                        map.put(currency, rate);
                    }
                }
                
                map.put("EUR", 1.0);
                
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(GetDatas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(GetDatas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GetDatas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return map;  
    }
}

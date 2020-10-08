package changedivisas;
import java.util.Map;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Jonathan
 */
public class Coin {
    
	public Coin() {};
	
	private Map<String, Double> currency = GetDatas.readXML();
	
    public double getCurrency(String m) {
    	return currency.get(m);	
    }
    
    public String [] getKey() {
    	String[] strings = currency.keySet().toArray(new String[currency.size()]);
        return strings;
    }
    
    public int size(){
        return currency.size();
    }
    
    private double equalCurrency (double x){
        return x;
    }
    
    private double euroToOther (String name, double x){
        return x * currency.get(name);
    }
    
    private double otherToEuro (String name, double x){
        double dif = 1 / currency.get(name);
        return dif * x;
    }
    
    private double otherToOther(String other1, String other2, double x) {  	
    	double x1 = euroToOther(other1, 1);
    	return (x1 / currency.get(other2)) * x;
    }
    
    public String selectMethod(String other1, String other2, String x){
        double result;
        double quantity = Double.parseDouble(x);
        
        if(other1.equals("EUR")){
            if(other2.equals("EUR")){
                result = equalCurrency(quantity);
            }else{
                result = euroToOther(other2, quantity);
            }
        }else{
            if (other2.equals("EUR")){
                result = otherToEuro(other1, quantity);
            }else if(other2.equals(other1)){
                result = equalCurrency(quantity);
            }else{
                result = otherToOther(other1, other2, quantity);
            }
        }
        return Double.toString(result);
    }
}

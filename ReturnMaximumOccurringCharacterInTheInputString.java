
import java.io.Console;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ReturnMaximumOccurringCharacterInTheInputString {
    
    public static void main(String args[]) {
        Console con = System.console();
        String str = con.readLine();
        // aaabcca
        //aabbc xrebbbssss
        printMaxOccuering(str);
        
    }
    
    public static void printMaxOccuering(String str){
        HashMap<Character,Integer> map = new HashMap<Character, Integer>();
        
        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            if(map.containsKey(ch)){
                map.put(ch,map.get(ch)+1);
            }else{
                map.put(ch, 1);
            }
        }
        
        int max = 0;
        Character key =null;
        Set<Map.Entry<Character,Integer>> set = map.entrySet();
        Iterator<Map.Entry<Character,Integer>> iter = set.iterator();
        while(iter.hasNext()){
            Map.Entry<Character,Integer> single = iter.next();
            if(single.getValue() > max){
                max = single.getValue();
                key = single.getKey();
            }
            
        }

        System.out.println("key : "+key +" value : "+max);
    }
    

}

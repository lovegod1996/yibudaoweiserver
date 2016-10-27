package serverSocket;
import java.util.StringTokenizer;
import java.util.Vector;


public class Strings {
	public  String[] Str(String str){
	  System.err.println(str);
	 String strs[];  
	 Vector<String> vec=new Vector<String>();  
	 StringTokenizer st = new StringTokenizer(str,";");  
	 String temp=null;  
	 while (st.hasMoreTokens()) 
	 {     
		 temp=st.nextToken();     
		 try
		 {         
			 vec.add(temp);     
		 }
		 catch(Exception e)
		 {      
				 e.printStackTrace();      
				 System.exit(-1);     
		}  
	 }  
	 strs=new String[vec.size()];  
	 for(int i=0;i<strs.length;i++)
	 {   
		 strs[i]=vec.get(i);  
	}
	 return strs;
	 
	}

}

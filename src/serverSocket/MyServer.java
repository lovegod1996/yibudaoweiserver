package serverSocket;

import java.net.*;
import java.io.*;
import java.util.*;


public class MyServer
{
	// ���屣������Socket��ArrayList
	public static ArrayList<Socket> socketList= new ArrayList<Socket>();
    public static void main(String[] args)throws IOException
    {
    	
		ServerSocket ss = new ServerSocket(8144);
		System.out.println("������������");
		Socket s=null;
		while(true)
		{
			// ���д������������һֱ�ȴ����˵�����
		s = ss.accept();
			socketList.add(s);
			// ÿ���ͻ������Ӻ�����һ��ServerThread�߳�Ϊ�ÿͻ��˷���
			new Thread(new ServerThread(s)).start();
	
		}
    }
}

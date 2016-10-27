package serverSocket;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

import javax.naming.spi.ResolveResult;

import dbUnit.Database;

// 负责处理每个线程通信的线程类
public class ServerThread implements Runnable {
	// 定义当前线程所处理的Socket
	Socket s = null;
	// 该线程所处理的Socket所对应的输入流
	BufferedReader br = null;
	BufferedReader br1 = null;
	Strings ss = new Strings();
	Database db = new Database();
	String str1 = "";
	String Pname = "";

	public ServerThread(Socket s) throws IOException {
		this.s = s;
		// 初始化该Socket对应的输入流
		br = new BufferedReader(
				new InputStreamReader(s.getInputStream(), "gbk")); // ②
	}

	public void run() {
		try {
			String content = null;

			Connection conn = db.connect();
			// 采用循环不断从Socket中读取客户端发送过来的数据
			while ((content = readFromClient()) != null) {
				System.out.println("---"
						+ Arrays.toString(content.getBytes("gbk")));
				System.out.println("Client 说:" + content);

				// 遍历socketList中的每个Socket，
				// 将读到的内容向每个Socket发送一次
				String[] str = ss.Str(content);

				// Connection conn=db.connect();

				for (Iterator<Socket> it = MyServer.socketList.iterator(); it
						.hasNext();) {
					System.out.println(MyServer.socketList.size());
					Socket s = it.next();
					int size = MyServer.socketList.size();

					Socket ss = MyServer.socketList.get(size - 1);
					System.out.println(s + "前    后" + ss);
					if (ss == s) {
						try {

							/*
							 * if(str[0].equals("login")) { String
							 * sql="select Pasw from perinfor where Pnum="
							 * +str[1]; ResultSet rs=db.select(conn, sql);
							 * while(rs.next()) { str1=rs.getString(1); }
							 * if(str[2].equals(str1)) { PrintWriter out = new
							 * PrintWriter(new BufferedWriter( new
							 * OutputStreamWriter(s.getOutputStream())),true);
							 * out.println("true");
							 * 
							 * }
							 * 
							 * else { PrintWriter out = new PrintWriter(new
							 * BufferedWriter( new
							 * OutputStreamWriter(s.getOutputStream())),true);
							 * out.println("false"); }
							 * 
							 * }
							 */
							/*
							 * show
							 */
							if (str[0].equals("show")) {
								String sql = "select Name from perinfor where Pnum="
										+ str[1];
								ResultSet rs = db.select(conn, sql);
								String name = "";
								while (rs.next()) {
									name = rs.getString(1);
								}
								PrintWriter out = new PrintWriter(
										new BufferedWriter(
												new OutputStreamWriter(
														s.getOutputStream())),
										true);
								out.println("name;" + name);

							}

							/*
							 * wifi
							 */

							if (str[0].equals("wifi")) {
								if (str[1].equals("192.168.191.3")) {
									PrintWriter out = new PrintWriter(
											new BufferedWriter(
													new OutputStreamWriter(s
															.getOutputStream())),
											true);
									out.println("wifitrue");

								}

								else {
									PrintWriter out = new PrintWriter(
											new BufferedWriter(
													new OutputStreamWriter(s
															.getOutputStream())),
											true);
									out.println("wififalse");
								}
							}
							
							
							
							

							if (str[0].equals("login")) {
								String sql = "select Pasw from perinfor where Pnum="
										+ str[1];
								ResultSet rs = db.select(conn, sql);
								while (rs.next()) {
									str1 = rs.getString(1);
								}
								if (str[2].equals(str1)) {
									PrintWriter out = new PrintWriter(new BufferedWriter(
											new OutputStreamWriter(s.getOutputStream())),
											true);
									out.println("true");

								}

								else {
									PrintWriter out = new PrintWriter(new BufferedWriter(
											new OutputStreamWriter(s.getOutputStream())),
											true);
									out.println("false");
								}

							}

							if (str[0].equals("register")) {
								String sql = "insert into perinfor(Pnum,Pasw,Name,Cnum) values('"
										+ str[1]
										+ "','"
										+ str[2]
										+ "','"
										+ str[3]
										+ "','"
										+ str[4] + "');";
								int result = db.insert(conn, sql);

								if (result > 0) {
									PrintWriter out = new PrintWriter(new BufferedWriter(
											new OutputStreamWriter(s.getOutputStream())),
											true);
									out.println("registertrue");

								}

								else {
									PrintWriter out = new PrintWriter(new BufferedWriter(
											new OutputStreamWriter(s.getOutputStream())),
											true);
									out.println("registerfalse");
								}
							}
							
							
							
							
							/*
							 * if(str[0].equals("kong")) { String
							 * sql="select COUNT(Pno) from status where Pstatus=0"
							 * ; ResultSet rs=db.select(conn, sql);
							 * while(rs.next()){ str1=rs.getString(1); }
							 * PrintWriter out = new PrintWriter(new
							 * BufferedWriter( new
							 * OutputStreamWriter(s.getOutputStream())),true);
							 * out.println(str1); }
							 */

						} catch (SocketException e) {
							e.printStackTrace();
							// 删除该Socket。
							it.remove();
							System.out.println(MyServer.socketList);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				
				// 获取停车场的信息，并且判断是否入场
				if (str[0].equals("kong")) {
					String sqlforfindepinfo = "select Pname,parking_left,PayCri from pinfor where wifiname = '"
							+ str[1] + "'";
					ResultSet rs = db.select(conn, sqlforfindepinfo);
					String pinfo = "";
					while (rs.next()) {
						pinfo = rs.getString("Pname") + "+"
								+ rs.getString("parking_left") + "+"
								+ rs.getString("PayCri");
					}
					// 查询停车记录
					String sqlforfindRecord = "select Intime from fee where Unum = '"
							+ str[2] + "'  and Outtime is null";

					ResultSet res = null;
					String getIn = null;
					String GetInState = "false";
					try {
                        System.out.println(sqlforfindRecord);
						res = db.select(conn, sqlforfindRecord);

						while (res.next()) {
							getIn = res.getString("Intime");
							System.err.println(getIn);
						}

					} catch (Exception e) {
						System.err.println("目前暂无" + str[2] + "的入场记录");
					}

					if (getIn != null) {
						GetInState = getIn;
					}
					System.err.println(pinfo + "    " + GetInState);
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(s.getOutputStream())), true);
					out.println("kong;" + pinfo + "+" + GetInState);
				}
				/*
				 * 入场插入操作并且修改当前停车场剩余车辆信息
				 */
				if (str[0].equals("getin")) {
					String insrtRecord = "insert into fee(Unum,Pname,Intime) values('"
							+ str[1] + "','" + str[2] + "','" + str[3] + "');";
					int result = db.insert(conn, insrtRecord);
					
					String UpDownPleft="update pinfor set parking_left=parking_left-1  where Pname = '"+str[2]+"'";
					int rr=db.update(conn, UpDownPleft);
					
					if (result > 0) {
						PrintWriter out = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(s.getOutputStream())),
								true);
						out.println("getin");
					}

				}
				
				/*
				 * 出场更新操作并且修改剩余车辆
				 */
				if(str[0].equals("getout")){
					String updateRecord="update fee set Outtime ='"+str[3]+"' , Pay= '"+str[4]+"' where Unum= '"+str[1]+"' and Outtime is null";
					int upre=db.update(conn, updateRecord);
					
					String updatePcount="update pinfor set parking_left=parking_left+1 where Pname= '"+str[2]+"'";
					int uppc=db.update(conn, updatePcount);
					
					if(uppc+upre>0){
						PrintWriter out = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(s.getOutputStream())),
								true);
						out.println("getout;"+str[4]);
					}
				}
				
				if(str[0].equals("getImg")){
					String imgPath=null;
					String sqlforfindimgpath="select imgpath from pinfor where wifiname = '"
							+ str[1] + "'";
					ResultSet rs=db.select(conn, sqlforfindimgpath);
					while(rs.next()){
						imgPath=rs.getString("imgpath");
					}
					System.out.println(imgPath);
					FileInputStream fin=new FileInputStream(imgPath);  //創建要發送的數據的文件輸入流
					DataOutputStream dou=new DataOutputStream(s.getOutputStream());
					dou.writeInt(fin.available());  //想输出流中写入文件数据长度
					System.err.println("传输数据大小   "+fin.available());
					//将实际的图片数据读取到byte【】 data中
					byte[] data=new byte[fin.available()];
					fin.read(data);
					//将图片数据写入到输出流中
					dou.write(data);
					System.err.println("dou "+dou.size());
					/*PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(
									s.getOutputStream())), true);
					out.print(data);*/
					//dou.flush();
				}
				
				
				if (str[0].equals("cha")) {
					String s1 = null, s2 = null;
					String sql = "select Count(Intime)，Count(Outtime) from fee where Pnum="
							+ str[1];
					ResultSet rs = db.select(conn, sql);
					while (rs.next()) {
						s1 = rs.getString(1);
						s2 = rs.getString(1);
					}
					if (s1.equals(s2)) {
						String sql1 = "select Intime，Outtime from fee where Pnum="
								+ str[1];
						ResultSet rs1 = db.select(conn, sql);
						while (rs1.next()) {
							s1 = rs1.getString(1);
							s2 = rs1.getString(2);
						}
						// String ss=计算所缴费用
						// 然后费用在数据库中扣除
					} else {
						// 获得当前时间，写入数据库
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
						String sql1 = "insert into fee(Pnum,Intime) values('"
								+ str[1] + "','" + curDate + "');";
						int result = db.insert(conn, sql);
						if (result > 0) {
							PrintWriter out = new PrintWriter(
									new BufferedWriter(new OutputStreamWriter(
											s.getOutputStream())), true);
							out.println("mei");

						}

					}
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(s.getOutputStream())), true);
					out.println(str1);
				}

				/*
				 * 查询所有停车场
				 */
				if (str[0].equals("parkinglist")) {

					String sqlForFindPark = "select Pname from pinfor";
					ResultSet res = db.select(conn, sqlForFindPark);
					String pl = "";
					while (res.next()) {
						pl += (res.getString("Pname") + "+");
					}
					res.close();
					System.err.println("停车场名     " + pl);
					PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(
									s.getOutputStream(), "gbk")), true);
					out.println("pl;" + pl);

				}
				/*
				 * 查询停车场详细信息
				 */
				if (str[0].equals("parkinfo")) {
					String findParkByName = "select Pname, adress,PayCri,phone, parking_left from pinfor where Pname = '"
							+ str[1] + "'";
					ResultSet res = db.select(conn, findParkByName);
					String pp = null;
					while (res.next()) {
						pp = res.getString("Pname") + "+"
								+ res.getString("adress") + "+"
								+ res.getString("PayCri") + "+"
								+ res.getString("phone") + "+"
								+ res.getString("parking_left");
					}
					res.close();
					PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(
									s.getOutputStream(), "gbk")), true);
					out.println("parkinfo;" + pp);

				}
				/*
				 * 缴费记录payrecords
				 */
				if (str[0].equals("payrecords")) {
					String pay_records = "select Pname, Intime,Outtime, Pay from fee where Unum = '"
							+ str[1] + "'  order by fid desc limit 6 ";
					ResultSet res = db.select(conn, pay_records);
					String pa = null;
					while (res.next()) {
						pa = pa+";"+res.getString("Pname") + "+"
								+ res.getString("Intime") + "+"
								+ res.getString("Outtime") + "+"
								+ res.getString("Pay");
					}
					res.close();
					PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(
									s.getOutputStream(), "gbk")), true);
					out.println("payrecord;" + pa);

				}
				
				
				
				
				

			}
			
			
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// 定义读取客户端数据的方法
	private String readFromClient() {
		try {
			return br.readLine();
		}
		// 如果捕捉到异常，表明该Socket对应的客户端已经关闭
		catch (IOException e) {
			e.printStackTrace();
			// 删除该Socket。
			MyServer.socketList.remove(s); // ①
		}
		return null;
	}
}

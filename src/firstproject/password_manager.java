package firstproject;
//import java.io.UnsupportedEncodingException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
//import java.util.Base64;
import java.util.*; 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.sql.*;  
class AES{
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}

public class password_manager
{ 	
	public static String newtb(String acc_name) {
	 final String CREATE_TABLE_SQL="CREATE TABLE "+acc_name+" ("
			+ "ID INT NOT NULL AUTO_INCREMENT,"
			+ "USERNAME VARCHAR(45) NOT NULL UNIQUE,"
            + "PASSWORD VARCHAR(45) NOT NULL,"
            + "PRIMARY KEY (ID))";
	return CREATE_TABLE_SQL;
	}
    public static void main(String[] args)throws IOException, SQLException
    { 	Scanner sc=new Scanner(System.in);
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	System.out.println("Welcome to Password Manager.........v1.0\n");
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");  
    		conn=DriverManager.getConnection(  
    		"jdbc:mysql://remotemysql.com:3306/KfAYCrQnpn","KfAYCrQnpn","rK89lSYX9h");  
    	int inp,f=0;
    	
    	PreparedStatement pstmt;
    	String app_name,user_name,pass;
    	final String secretKey="gjhfsgfjhbfgfjsbfsgj";
    	while(f==0) {
    	
    	System.out.println("1.Add account\n2.Remove account\n3.Dispaly accounts\n4.Exit");
    	inp=sc.nextInt();
    	sc.nextLine();
    	//System.out.println("\n");
    	switch(inp) {
    	case 1:
	    	System.out.println("Enter name of site or app");
	    	app_name= sc.nextLine();
	    	System.out.println("Enter Username");
	    	user_name= sc.nextLine();
	    	System.out.println("Enter Password");
	    	pass= sc.nextLine();
	    	int flag=0;
			String originalString=pass;
			String encryptedString=AES.encrypt(originalString, secretKey);
			stmt=conn.createStatement(); 
			rs=stmt.executeQuery("show tables");  
			while(rs.next())  {
			if((rs.getObject(1).toString()).equals(app_name))
				flag=1;
			else
				continue;
			}
			if(flag==0)
				stmt.executeUpdate(newtb(app_name));
			try {
			pstmt = conn.prepareStatement("insert into "+app_name+"(USERNAME,PASSWORD) values(?,?)");
			//pstmt.setInt (1, 1);
			pstmt.setString (1, user_name);
		    pstmt.setString (2, encryptedString);
			pstmt.executeUpdate();
			}catch(Exception e) {System.out.println("\nUsername already exits\n");continue; }
			System.out.println("\n");
			break;
    	case 3:
    		
    		stmt=conn.createStatement(); 
    		System.out.println("Enter name of site or app");
        	app_name= sc.nextLine();
    		rs=stmt.executeQuery("select * from "+app_name);  
    		System.out.println("\nID  UserName  Password\n");
    		while(rs.next())  {
    			System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+AES.decrypt(rs.getString(3),secretKey));}
    		System.out.println("\n");
    		break;
    	case 4:
    		System.out.println("Exiting the program...........");
    		System.exit(0);
    		break;
    	case 2:
    		int flag1=0;
    		System.out.println("Enter name of site or app");
        	app_name= sc.nextLine();
        	
        	stmt=conn.createStatement();
        	rs=stmt.executeQuery("show tables");  
			while(rs.next())  {
			if((rs.getObject(1).toString()).equals(app_name))
				flag1=1;
			else
				continue;
			}
			if(flag1==0)
				System.out.println(app_name+" does not exist");
			else {
					flag1=0;
					stmt=conn.createStatement();
					rs=stmt.executeQuery("select * from "+app_name);
					System.out.println("Enter username of account to remove");
		        	user_name= sc.nextLine();
		        	while(rs.next())  {
		    			if((rs.getObject(1).toString()).equals(user_name))
		    				flag1=1;
		    			else
		    				continue;
		    			}
		    			if(flag1==0)
		    				System.out.println("\nUsername "+user_name+" does not exist");
		    		pstmt = conn.prepareStatement("delete from "+app_name+" where USERNAME='"+user_name+"'");
					pstmt.executeUpdate();
					System.out.println("\n");
				
				}
			break;
    	}
    	
    	}//end of while loop
    	
		
    	 }catch(Exception e){ 
    	System.out.println(e);
    }  
    	sc.close();
    	conn.close();
    			stmt.close();
    			rs.close();
    }
	
 } 
package com.bus.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class busticket 
{
	Scanner in=new Scanner(System.in);
	Connection con=null;
	busticket obj=null;
	Connection getConnection()
	{
		try
		{
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/busticket?characterEncoding=utf8","root","root");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return con;
	} 
	void addBus() throws SQLException
	{
		System.out.println("Enter Bus Name : ");
		String busname=in.nextLine();
		System.out.println("Enter From location : ");
		String from=in.nextLine();
		System.out.println("Enter To location: ");
		String to=in.nextLine();
		System.out.println("Enter Total Seats : ");
		int totalSeats=in.nextInt();
		System.out.println("Enter Price : ");
		int price=in.nextInt();
		Statement stmt =(Statement) con.createStatement();
		String sql="select bus_name from busdetails where bus_name='"+busname+"'" ;
		ResultSet rs=(ResultSet) stmt.executeQuery(sql);
		if(rs.next()==false) {

		sql="insert into busdetails (bus_name,from_location,to_location,totalseat,price) values('"+busname+"','"+from+"','"+to+"','"+totalSeats+"','"+price+"')";
		stmt.executeUpdate(sql);
		System.out.println("Bus Added  Succesfully");
		}
		else
		{
			System.out.println("Bus Already Added");
		}
	}
	void addAgent() throws SQLException
	{
		in.nextLine();
		System.out.println("Enter Agent Name : ");
		String agentName=in.nextLine();
		System.out.println("Enter Mobileno : ");
		String mobileNo=in.nextLine();
		System.out.println("Enter Password : ");
		String password=in.nextLine();
		Statement stmt =(Statement) con.createStatement();
		String sql="select agentname from agentdetails where agentname='"+agentName+"'" ;
		ResultSet rs=(ResultSet) stmt.executeQuery(sql);
		if(rs.next()==true) {
			System.out.println("Agent already exist");
			return;
		}
		sql="insert into agentdetails (agentname,mobileno,password) values('"+agentName+"','"+mobileNo+"','"+password+"')";
		stmt.close();
		stmt =(Statement) con.createStatement();
		stmt.executeUpdate(sql);
		sql="select max(agentcode) from agentdetails";
		rs =(ResultSet) stmt.executeQuery(sql);
		rs.next();
		System.out.println("Agent Added  Succesfully Agent id: "+rs.getInt(1));
	}
	
	void adminLogin() throws SQLException
	{
		busticket obj=new busticket();
		obj.getConnection();
		System.out.println("Enter the Admin Name : ");
		String adminName=in.nextLine();
		System.out.println("Enter Password : ");
		String password=in.nextLine();
		Statement stmt =(Statement) con.createStatement();
		String sql="select * from adminusers where adminname='"+adminName+"' and password='"+password+"'";
		ResultSet  rs = (ResultSet) stmt.executeQuery(sql);
		if(rs.next()==false)
		{
			System.out.println("Wrong username or Password");
		}
		else
		{
			System.out.println("Login Succesfull");
			boolean check=true;
			while(check)
			{
				System.out.println("Select your choice :"+'\n'+"1.Add Bus"+'\n'+"2.Add Agent"+'\n'+"3.Logout");
				int n=in.nextInt();
				System.out.println();
				in.nextLine();
				switch(n)
				{
					case 1:
						obj.addBus();
						break;
					case 2:
						obj.addAgent();
						break;
					case 3:
						check=false;
						break;
						
				}
			}
		}
	}
	void listBusDetails() throws SQLException
	{
		Statement stmt =(Statement) con.createStatement();
		String sql="select * from busdetails" ;
		ResultSet  rs = (ResultSet) stmt.executeQuery(sql);
		System.out.println("Bus Details ");
		while(rs.next())
		{
			System.out.print(rs.getInt(1)+" ");
			System.out.print(rs.getString(2)+" ");
			System.out.print(rs.getString(3)+" ");
			System.out.print(rs.getString(4)+" ");
			System.out.print(rs.getInt(5)+" ");
			System.out.println(rs.getInt(6)+" ");
		}
	}
	void bookTicket(int agentCode) throws SQLException
	{
		System.out.println("Enter Bus ID : ");
		int busid=in.nextInt();
		String sql="select bus_id from busdetails where bus_id='"+busid+"'" ;
		Statement stmt=(Statement) con.createStatement();
		ResultSet rs=(ResultSet) stmt.executeQuery(sql);
		if(rs.next()==false) {
			System.out.println("Bus not available");
			return;
		}
		sql="select bus_name,from_location,to_location,totalseat,price from busdetails where bus_id='"+busid+"'";
		rs = (ResultSet) stmt.executeQuery(sql);
		rs.next();
		System.out.println("busname : "+rs.getString(1));
		System.out.println("From : "+rs.getString(2));
		System.out.println("To : "+rs.getString(3));
		System.out.println("Available Seats : "+rs.getInt(4));
		int availableSeat =rs.getInt(4);
		int price =rs.getInt(5);
		in.nextLine();
		System.out.println("Enter no of tickets to Book : ");
		int noOfTickets=in.nextInt();
		
		if(noOfTickets<=availableSeat)
		{
			int totalAmount=noOfTickets*price;
			System.out.println("Total fare : "+totalAmount);
			in.nextLine();
			System.out.println("Confirmation (yes/no) : ");
			String confirmation=in.nextLine();
			if(confirmation.equals("yes"))
			{
				String sql2="insert into ticketdetails (bus_id,noOfTicket,totalFare,agentcode) values("+busid+","+noOfTickets+","+totalAmount+","+agentCode+")";
				stmt.executeUpdate(sql2);
				System.out.println("Booking Confirmed");
				int avseats=availableSeat-noOfTickets;
				String sql1="update busdetails set totalseat="+avseats+" where bus_id="+busid+"";
				stmt.executeUpdate(sql1);	
			}
		}
			else
			{
				System.out.println("Tickets not available");
			}
			
		
		
	}
	void showMyBooking(int agentCode) throws SQLException
	{
		Statement stmt =(Statement) con.createStatement();
		String sql="select t.bus_id,t.noOfTicket, t.totalFare, t.ticket_id,b.bus_name,b.from_location,b.to_location,b.price from ticketdetails t left outer join busdetails b on (t.bus_id=b.bus_id) where t.agentcode='"+agentCode+"' order by t.ticket_id desc";
		ResultSet  rs = (ResultSet) stmt.executeQuery(sql);
		while(rs.next())
		{
			System.out.println("Bus ID : "+rs.getInt(1));
			System.out.println("No Of Ticket Booked : "+rs.getInt(2));
			System.out.println("Total Fare : "+rs.getInt(3));
			System.out.println("Bus Name : "+rs.getString(5));
			System.out.println("From : "+rs.getString(6));
			System.out.println("To : "+rs.getString(7));
			System.out.println("-----------------");
		}
		
	}
	void agentLogin() throws SQLException
	{
		busticket obj=new busticket();
		obj.getConnection();
		System.out.println("Enter the Agent Code : ");
		int agentCode=in.nextInt();
		in.nextLine();
		System.out.println("Enter Password : ");
		String password=in.nextLine();
		Statement stmt =(Statement) con.createStatement();
		String sql="select * from agentdetails where agentcode='"+agentCode+"' and password='"+password+"'";
		ResultSet  rs = (ResultSet) stmt.executeQuery(sql);
		if(rs.next()==false)
		{
			System.out.println("Wrong Agent Code or Password");
		}
		else
		{
			System.out.println("Login Succesfull");
			boolean check=true;
			while(check)
			{
				System.out.println("Select your choice :"+'\n'+"1.List the Bus Details"+'\n'+"2.Book Ticket"+'\n'+"3.Show My Bookings "+'\n'+"4.Logout");
				int n=in.nextInt();
				System.out.println();
				in.nextLine();
				switch(n)
				{
					case 1:
						obj.listBusDetails();
						break;
					case 2:
						obj.bookTicket(agentCode);
						break;
					case 3:
						obj.showMyBooking(agentCode);
						break;
					case 4:
						check=false;
						break;
					default:
						
				}
			}
		}
	}
	public static void main (String [] args)throws SQLException,ClassNotFoundException
	{
		Scanner in =new Scanner (System.in);
		busticket obj=new busticket();
		obj.getConnection();
		boolean check=true;
		while(check)
		{
			System.out.println("Select your choice :"+'\n'+"1.Admin Login"+'\n'+"2.Agent Login"+'\n'+"3.Exit");
			int n=in.nextInt();
			System.out.println();
			in.nextLine();
			switch(n)
			{
				case 1:
					obj.adminLogin();
					break;
				case 2:
					obj.agentLogin();
					break;
				case 3:
					check=false;
					break;
					
			}
			in.nextLine();
		}
		
	}

}


import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.stream.Stream;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

public class Starter_Class 
{
	private static ArrayList<String> inserted_values = new ArrayList<>();
	public static void main(String[] args) {
		int no_of_rows=0;
		int no_of_columns=0;
		int no_of_masterdata_partition = 10;
		int size_of_each_masterdata_partition = 10;
		String username, password, db;
		ArrayList<MasterDataTuple> masterdata_tuples = new ArrayList<>(100);
		ArrayList<TransactionTuple> transaction_tuples = new ArrayList<>(10000);
		Scanner obj = new Scanner(System.in);
		System.out.println("Enter the username of MySQL Workbench: ");
		username = obj.nextLine();
		System.out.println("Enter the password of MySQL Workbench: ");
		password = obj.nextLine();
		System.out.println("Enter the schema name in which tables of MASTERDATA and TRANSACTIONS are stored: ");
		db = obj.nextLine();
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");	
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db,username,password);
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("select * from MASTERDATA");
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();	
			
			if (rs.last())
			{
				no_of_rows = rs.getRow();
				rs.beforeFirst();
				no_of_columns = rsmd.getColumnCount();
			}
			
			for(int i=1; i<=no_of_rows; i++)
			{
				if (!rs.next())
					break;
				masterdata_tuples.add(new MasterDataTuple(rs.getString(1), rs.getString(2), 
						rs.getString(3), rs.getString(4), rs.getDouble(5)));
			}

			rs = stmt.executeQuery("select * from TRANSACTIONS");
			rsmd = (ResultSetMetaData) rs.getMetaData();
			
			if (rs.last())
			{
				no_of_rows = rs.getRow();
				rs.beforeFirst();
				no_of_columns = rsmd.getColumnCount();
			}
			for(int i=1; i<=no_of_rows; i++)
			{
				if (!rs.next())
					break;
				transaction_tuples.add(new TransactionTuple(rs.getInt(1), rs.getString(2), 
						rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
						rs.getString(7), rs.getInt(8)));
			}
			
			MeshJoin(masterdata_tuples, transaction_tuples, username, password);
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static boolean are_same(TransactionTuple r1, TransactionTuple r2)
	{
		if (r1.get_transaction_id() == r2.get_transaction_id() && r1.get_customer_id().equals(r2.get_customer_id()) &&
				r1.get_customer_name().equals(r2.get_customer_name()) && r1.get_product_id().equals(r2.get_product_id()) &&
				r1.get_store_id().equals(r2.get_store_id()) && r1.get_store_name().equals(r2.get_store_name()) &&
				r1.get_date().equals(r2.get_date()) && r1.get_quantity() == r2.get_quantity())
			return true;
		else
			return false;
	}
	
	public static void MeshJoin(ArrayList<MasterDataTuple> masterdata, ArrayList<TransactionTuple> transactions, String username, String password)
	{
		int no_of_masterdata_partitions = 10;
		int size_of_each_masterdata_partition = 10;
		int size_of_queue_node = 50;
		int size_of_queue = 10;
		int current_transaction_tuple = 0;
		int total_transaction_tuple = 10000;
		int curr_rec=1;
		boolean deletion = false;
		ArrayList<MasterDataTuple> disk_buffer = new ArrayList<>(size_of_each_masterdata_partition); 
		ArrayList<MasterDataTuple> MasterDataPartition = new ArrayList<>(size_of_each_masterdata_partition);
		ArrayList<TransactionTuple> list_for_qNode = new ArrayList<>(size_of_queue_node);
		Set<String> HashTableKeys;
		List<TransactionTuple> currentRecords;
		
		Queue queue = new Queue(size_of_queue);						//queue of size 10
		MultiMap<String, TransactionTuple> myMap = new MultiValueMap<String, TransactionTuple>();
		
		int current_masterdata_partition = 0;
		int iteration_count = 1;
		while (true)										//the outermost loop of meshjoin
		{
			if (deletion)									//if deletion is on then remove node from queue and hashtable
			{
	
				QNode temp_qNode = queue.dequeue();
				
				HashTableKeys = myMap.keySet();
				TransactionTuple temp_tuple;
				String current_key = "";
				
				for (int i=0; i<temp_qNode.get_tuples().size(); i++)
				{
					current_key = temp_qNode.get_tuples().get(i).get_product_id();	
					for (String key : HashTableKeys)
					{
						if (key.equals(current_key))
						{
							currentRecords = (List<TransactionTuple>) myMap.get(current_key);
							for (int j=0; j<currentRecords.size(); j++)
							{
								if (are_same(temp_qNode.get_tuples().get(i), currentRecords.get(j)))
									myMap.removeMapping(current_key, currentRecords.get(j));							
							}
							break;
						}
					}
				}
			}
			
			for (int i=(current_masterdata_partition * 10); i<((current_masterdata_partition * 10)+size_of_each_masterdata_partition); i++)
			{
				disk_buffer.add(masterdata.get(i));
			}
			
			if (current_transaction_tuple < 10000)
			{
				do 													//creating the queue node to be inserted
				{
					list_for_qNode.add(transactions.get(current_transaction_tuple));
					myMap.put(transactions.get(current_transaction_tuple).get_product_id(), transactions.get(current_transaction_tuple));				
					current_transaction_tuple++;
				} while (current_transaction_tuple % 50 != 0);
				
				queue.enqueue(list_for_qNode); //inserting the queue node in queue
				list_for_qNode.clear();
			}
			else
				current_transaction_tuple++;
			
			HashTableKeys = myMap.keySet();
			for (int i=0; i<disk_buffer.size(); i++)
			{
				if (HashTableKeys.contains(disk_buffer.get(i).get_product_id()))
				{
					currentRecords = (List<TransactionTuple>) myMap.get(disk_buffer.get(i).get_product_id());
					for (int j=0; j<currentRecords.size(); j++)
					{
						MasterDataTuple temp1 = disk_buffer.get(i);
						TransactionTuple temp2 = currentRecords.get(j);
						
						insert_in_dwh(temp1.get_product_id(), temp2.get_customer_id(), temp2.get_customer_name(), temp2.get_store_id(), temp2.get_store_name(),
								temp2.get_date(), temp2.get_quantity(), temp1.get_product_name(), temp1.get_supplier_id(), temp1.get_supplier_name(), temp1.get_price(), username, password);
						System.out.println("Inserted record: " + curr_rec);
						curr_rec++;
					}
				}
			}
		
			current_masterdata_partition++;																//controlling the loop
			if ( current_masterdata_partition >= no_of_masterdata_partitions)
			{
				current_masterdata_partition = 0;
			}
			if (current_transaction_tuple >= 10010)
			{
				break;
			}
			if (current_transaction_tuple >= 500)
				deletion = true;
			iteration_count++;
			disk_buffer.clear();
		}
	}
	
	public static void insert_in_dwh(String pd_id, String cst_id, String cst_name, String st_id, String st_name, String dt, int qnt, String pd_name, String sp_id, 
			String sp_name, double price_, String username, String password)
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");	
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/i180589_project",username,password);
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			PreparedStatement preparedStmt;
			ResultSet rs;
			int year, half, quarter, month, day, date_id, week_of_month, week_of_year, day_of_week;
			String query;
			
			if (inserted_values.contains(pd_id) == false)
			{
				query = " insert into i180589_Project.Product values (?, ?, ?)";
			    preparedStmt = con.prepareStatement(query);
			    preparedStmt.setString(1, pd_id);
			    preparedStmt.setString(2, pd_name);
			    preparedStmt.setDouble(3, price_);
				preparedStmt.execute();
				inserted_values.add(pd_id);
			}
				
			if (inserted_values.contains(cst_id) == false)
			{
				query = "insert into i180589_Project.Customer values ( ?, ?)";
				preparedStmt = con.prepareStatement(query);
			    preparedStmt.setString(1, cst_id);
			    preparedStmt.setString(2, cst_name);    
				preparedStmt.execute();
				inserted_values.add(cst_id);
			}
				
			if (inserted_values.contains(st_id) == false)
			{
				query = "insert into i180589_Project.Store values (?, ?)";
				preparedStmt = con.prepareStatement(query);
			    preparedStmt.setString(1, st_id);
			    preparedStmt.setString(2, st_name);				    
				preparedStmt.execute();
				inserted_values.add(st_id);
			}
				
			if (inserted_values.contains(sp_id) == false)
			{
				query = "insert into i180589_Project.Supplier values (?, ?)";
				preparedStmt = con.prepareStatement(query);
			    preparedStmt.setString(1, sp_id);
			    preparedStmt.setString(2, sp_name);				    
				preparedStmt.execute();
				inserted_values.add(sp_id);
			}
			
			java.sql.Date dat = java.sql.Date.valueOf(dt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dat);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH)+1;
			day = cal.get(Calendar.DAY_OF_MONTH);
			week_of_month = cal.get(Calendar.WEEK_OF_MONTH);
			week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
			day_of_week = cal.get(Calendar.DAY_OF_WEEK);
			
			if (month > 6)
				half = 2;
			else 
				half = 1;
			if (month <= 3)
				quarter = 1;
			else if (month <= 6)
				quarter = 2;
			else if (month <= 9)
				quarter = 3;
			else 
				quarter = 4;
			
			if (inserted_values.contains(dt) == false)
			{
				query = "insert into i180589_Project.Transaction_Date values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStmt = con.prepareStatement(query);
				preparedStmt.setString(1, dt);
				preparedStmt.setInt(2,  year);
				preparedStmt.setInt(3, half);
				preparedStmt.setInt(4,  quarter);
				preparedStmt.setInt(5,  month);
				preparedStmt.setInt(6,  week_of_month);
				preparedStmt.setInt(7,  week_of_year);
				preparedStmt.setInt(8, day_of_week);
				preparedStmt.setInt(9, day);
				preparedStmt.execute();
				inserted_values.add(dt);
			}
				
			query = "insert into i180589_Project.Revenue (Product_id, Customer_id, Store_id, Supplier_id, Date_id, Quantity, Sale) values (?, ?, ?, ?, ?, ?, ?)";
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, pd_id);
			preparedStmt.setString(2, cst_id);
			preparedStmt.setString(3, st_id);
			preparedStmt.setString(4, sp_id);
			preparedStmt.setString(5, dt);
			preparedStmt.setInt(6, qnt);
			preparedStmt.setDouble(7, price_*qnt);				
			preparedStmt.execute();
			
		}
		catch (Exception e)
		{
				System.out.println(e);
		}
	}
}

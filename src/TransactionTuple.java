
public class TransactionTuple {
	int transaction_id;
	String product_id;
	String customer_id;
	String customer_name;
	String store_id;
	String store_name;
	String date;
	int quantity;
	
	TransactionTuple()
	{
		product_id = customer_id = customer_name = store_id = store_name = date = "";
		transaction_id = quantity = 0;
	}

	TransactionTuple(int tr_id, String pd_id, String cs_id, String cs_name, String st_id, String st_name, String dt, int qty)
	{
		transaction_id = tr_id;
		product_id = pd_id;
		customer_id = cs_id; 
		customer_name = cs_name;
		store_id = st_id;
		store_name = st_name;
		date = dt;
		quantity = qty;
	}
	
	int get_transaction_id()
	{
		return transaction_id;
	}
	
	void set_transaction_id(int tr_id)
	{
		transaction_id = tr_id;
	}
	
	String get_product_id()
	{
		return product_id;
	}
	
	void set_product_id(String pd_id)
	{
		product_id = pd_id;
	}
	
	String get_customer_id()
	{
		return customer_id;
	}
	
	void set_customer_id(String cs_id)
	{
		customer_id = cs_id;
	}
	
	String get_customer_name()
	{
		return customer_name;
	}
	
	void set_customer_name(String cs_name)
	{
		customer_name = cs_name;
	}
	
	String get_store_id()
	{
		return store_id;
	}
	
	void set_store_id(String st_id)
	{
		store_id = st_id;
	}
	
	String get_store_name()
	{
		return store_name;
	}
	
	void set_store_name(String st_name)
	{
		store_name = st_name;
	}
	
	String get_date()
	{
		return date;
	}
	
	void set_date(String dt)
	{
		date = dt;
	}
	
	int get_quantity()
	{
		return quantity;
	}
	
	void set_quantity(int qty)
	{
		quantity = qty;
	}
	void display_data()
	{
		System.out.print(transaction_id + "\t");
		System.out.print(product_id + "\t");
		System.out.print(customer_id + "\t");
		System.out.print(customer_name + "\t");
		System.out.print(store_id + "\t");
		System.out.print(store_name + "\t");
		System.out.print(date + "\t");
		System.out.println(quantity);
	}
}

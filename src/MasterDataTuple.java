

public class MasterDataTuple {
	String product_id;
	String product_name;
	String supplier_id;
	String supplier_name;
	double price;
	
	MasterDataTuple()
	{
		product_id = product_name = supplier_id = supplier_name = "";
		price = 0.0;
	}

	MasterDataTuple(String pd_id, String pd_name, String sp_id, String sp_name, double pr)
	{
		product_id = pd_id;
		product_name = pd_name;
		supplier_id = sp_id;
		supplier_name = sp_name;
		price = pr;
	}
	
	String get_product_id()
	{
		return product_id;
	}
	
	void set_product_id(String pd_id)
	{
		product_id = pd_id;
	}
	
	String get_product_name()
	{
		return product_name;
	}
	
	void set_product_name(String pd_name)
	{
		product_name = pd_name;
	}
	
	String get_supplier_id()
	{
		return supplier_id;
	}
	
	void set_supplier_id(String sp_id)
	{
		supplier_id = sp_id;
	}
	
	String get_supplier_name()
	{
		return supplier_name;
	}
	
	void set_supplier_name(String sp_name)
	{
		supplier_name = sp_name;
	}
	
	double get_price()
	{
		return price;
	}
	
	void set_price(double price_)
	{
		price = price_;
	}
	void display_data()
	{
		System.out.print(product_id + "\t");
		System.out.print(product_name + "\t");
		System.out.print(supplier_id + "\t");
		System.out.print(supplier_name + "\t");
		System.out.println(price);
	}
}

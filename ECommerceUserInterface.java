
import java.util.Scanner;

// Simulation of a Simple E-Commerce System (like Amazon)


public class ECommerceUserInterface
{
	public static void main(String[] args)
	{
		// Create the system
		ECommerceSystem amazon = new ECommerceSystem();

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");
		
		// Process keyboard actions
		while (scanner.hasNextLine())
		{
			String action = scanner.nextLine();
			
			try {
				if (action == null || action.equals("")) 
				{
					System.out.print("\n>");
					continue;
				}
				else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
					return;

				else if (action.equalsIgnoreCase("PRODS"))	// List all products for sale
				{
					amazon.printAllProducts(); 
				}
				else if (action.equalsIgnoreCase("BOOKS"))	// List all books for sale
				{
					amazon.printAllBooks(); 
				}
				else if (action.equalsIgnoreCase("CUSTS")) 	// List all registered customers
				{
					amazon.printCustomers();	
				}
				else if (action.equalsIgnoreCase("ORDERS")) // List all current product orders
				{
					amazon.printAllOrders();	
				}
				else if (action.equalsIgnoreCase("SHIPPED"))	// List all orders that have been shipped
				{
					amazon.printAllShippedOrders();	
				}
				else if (action.equalsIgnoreCase("NEWCUST"))	// Create a new registered customer
				{
					String name = "";
					String address = "";
					
					System.out.print("Name: ");
					if (scanner.hasNextLine())
						name = scanner.nextLine();
					
					System.out.print("\nAddress: ");
					if (scanner.hasNextLine())
						address = scanner.nextLine();
					
					boolean success = amazon.createCustomer(name, address);
					if (!success)
					{
						System.out.println(amazon.getErrorMessage());
					}
				}
				else if (action.equalsIgnoreCase("SHIP"))	// ship an order to a customer
				{
						String orderNumber = "";
			
						System.out.print("Order Number: ");
						// Get order number from scanner
						if (scanner.hasNextLine()){
							orderNumber = scanner.nextLine();
						}
						// Ship order to customer (see ECommerceSystem for the correct method to use
						ProductOrder new_order =  amazon.shipOrder(orderNumber);
						if (new_order == null){
							System.out.println(amazon.getErrorMessage());
						}
						else {
							new_order.print();
						}
				
				}
				else if (action.equalsIgnoreCase("CUSTORDERS")) // List all the current orders and shipped orders for this customer id
				{
					String customerId = "";

					System.out.print("Customer Id: ");
					// Get customer Id from scanner
					if (scanner.hasNextLine()){
						customerId = scanner.nextLine();
					}
					// Print all current orders and all shipped orders for this customer
					boolean success = amazon.printOrderHistory(customerId);
					if (!success){
						System.out.println(amazon.getErrorMessage());
					}

				}
				else if (action.equalsIgnoreCase("ORDER")) // order a product for a certain customer
				{
					String productId = "";
					String customerId = "";
					
					System.out.print("Product Id: ");
				// Get product Id from scanner
					if (scanner.hasNextLine()){
						productId = scanner.nextLine();
					}
					
					System.out.print("\nCustomer Id: ");
				// Get customer Id from scanner
					if (scanner.hasNextLine()){
						customerId = scanner.nextLine();
					}
					// Order the product. Check for valid orderNumber string return and for error message set in ECommerceSystem
																																																																																																																																																																																										
					String success = amazon.orderProduct(productId, customerId, "");
					if (success == null){
						System.out.println(amazon.getErrorMessage());
					}
			
					// Print Order Number string returned from method in ECommerceSystem
					System.out.println("Order #" + success);
				}
				else if (action.equalsIgnoreCase("ORDERBOOK")) // order a book for a customer, provide a format (Paperback, Hardcover or EBook)
				{
					String productId = "";
					String customerId = "";
					String options = "";

					System.out.print("Product Id: ");
					// get product id
					if (scanner.hasNextLine()){
						productId = scanner.nextLine();
					}
					System.out.print("\nCustomer Id: ");
					// get customer id
					if (scanner.hasNextLine()){
						customerId = scanner.nextLine();
					}
					System.out.print("\nFormat [Paperback Hardcover EBook]: ");
					// get book forma and store in options string
					if (scanner.hasNextLine()){
						options = scanner.nextLine();
					}
					// Order product. Check for error mesage set in ECommerceSystem
					// Print order number string if order number is not null
					String success = amazon.orderProduct(productId, customerId, options);
					if (success == null){
						System.out.println(amazon.getErrorMessage());
					}
					else{
						System.out.println("Order #" + success);
					}
					
				}
				// else if (action.equalsIgnoreCase("ORDERSHOES")) 
				// {
				// 	String productId = "";
				// 	String customerId = "";
				// 	String options = "";
					
				// 	System.out.print("Product Id: ");
				// 	// get product id
					
				// 	System.out.print("\nCustomer Id: ");
				// 	// get customer id
					
				// 	System.out.print("\nSize: \"6\" \"7\" \"8\" \"9\" \"10\": ");
				// 	// get shoe size and store in options	
					
				// 	System.out.print("\nColor: \"Black\" \"Brown\": ");
				// 	// get shoe color and append to options
					
				// 	//order shoes
					
				// }
				else if (action.equalsIgnoreCase("ADDTOCART")) //adds item to cart
				{
					String productId = "";
					String customerId = "";
					String productOptions = "";

					System.out.println("Product Id: ");
					if (scanner.hasNextLine()){
						productId = scanner.nextLine();
					}
					System.out.println("Customer Id: ");
					if (scanner.hasNextLine()){
						customerId = scanner.nextLine();
					}
					if (productId.equals("702") || productId.equals("706") || productId.equals("707") || productId.equals("708") || productId.equals("702")){
						System.out.print("\nFormat [Paperback Hardcover EBook]: ");
						if (scanner.hasNextLine()){
							productOptions = scanner.nextLine();
						}
					}
					
					amazon.addToCart(productId, customerId, productOptions);
				}

				else if (action.equalsIgnoreCase("REMCARTITEM"))
				{
					String productId = "";
					String customerId = "";

					System.out.println("Product Id: ");
					if (scanner.hasNextLine()){
						productId = scanner.nextLine();
					}
					System.out.println("Customer Id: ");
					if (scanner.hasNextLine()){
						customerId = scanner.nextLine();
					}
					amazon.removeItem(productId, customerId);

				}

				else if (action.equalsIgnoreCase("PRINTCART"))
				{
					String customerId = "";

					System.out.println("Customer Id: ");
					if (scanner.hasNextLine()){
						customerId = scanner.nextLine();
					}
					amazon.printCart(customerId);
				}

				else if (action.equalsIgnoreCase("ORDERITEMS"))
				{
					String customerId = "";

					System.out.print("Customer Id: ");
					if (scanner.hasNextLine()){
						customerId = scanner.nextLine();
					}
					amazon.orderItems(customerId);
				}
				
				//BONUS--RATING COMMANDS PT.1
				else if (action.equalsIgnoreCase("PRINTRATING"))
				{
					String productId = "";

					System.out.println("Product ID: ");
					if (scanner.hasNextLine())
					{
						productId = scanner.nextLine();
					}
					amazon.printRating(productId);
				}

				//BONUS--RATING COMMANDS PT.2
				else if (action.equalsIgnoreCase("ADDRATING"))
				{
					String productId = "";
					String rate = "";

					System.out.println("Product ID: ");
					if (scanner.hasNextLine())
					{
						productId = scanner.nextLine();
					}
					System.out.println("Add a Product Rating from 1-5");
					if (scanner.hasNextLine())
					{
						rate = scanner.nextLine();
					}
					amazon.rateProd(productId, rate);
				}

				//BONUS--RATING COMMANDS PT.3
				// print products with the average rating from the rate input
				else if (action.equalsIgnoreCase("PRINTABOVETHRESH")){ 
					String rate = "";
					String category = "";
					// get rate threshold
					System.out.print("Rate Threshold: ");
					if (scanner.hasNextLine()){
						rate = scanner.nextLine();
					}
					//get category
					System.out.print("Product category: ");
					if (scanner.hasNextLine()){
						category = scanner.nextLine();
					}
					// print all the products above inputted threshold
					amazon.productAboveThreshold(rate,category);
				}

				else if (action.equalsIgnoreCase("ORDERSTATS")) // Cancel an existing order
				{
					amazon.orderStats();
				}

				else if (action.equalsIgnoreCase("CANCEL")) // Cancel an existing order
				{
					String orderNumber = "";

					System.out.print("Order Number: ");
					// get order number from scanner
					if (scanner.hasNextLine()){
						orderNumber = scanner.nextLine();
					}
					// cancel order. Check for error
					boolean success = amazon.cancelOrder(orderNumber);
					if (!success){
						System.out.println(amazon.getErrorMessage());
					}
					
				}

				else if (action.equalsIgnoreCase("PRINTBYPRICE")) // sort products by price
				{
					amazon.printByPrice();
				}
				else if (action.equalsIgnoreCase("PRINTBYNAME")) // sort products by name (alphabetic)
				{
					amazon.printByName();
				}
				else if (action.equalsIgnoreCase("SORTCUSTS")) // sort products by name (alphabetic)
				{
					amazon.sortCustomersByName();
				}

				else if (action.equalsIgnoreCase("BOOKSBYAUTHOR"))
				{
					String author = "";

					System.out.print("Author: ");
					if (scanner.hasNextLine()){
						author = scanner.nextLine();
					}
					boolean books = amazon.sortBooksByAuthor(author);
						if (!books){
							System.out.println(amazon.getErrorMessage());
						}
			}
		}
			catch (UnknownCustomerException e){ 
                System.out.println(e.getMessage());
            } catch (UnknownProductException e){
                System.out.println(e.getMessage());
            } catch (InvalidProductOptionException e){
                System.out.println(e.getMessage());
            } catch (OutOfStockException e){
                System.out.println(e.getMessage());
            } catch (InvalidCustomerNameException e){
                System.out.println(e.getMessage());
            } catch (InvalidCustomerAddressException e){
                System.out.println(e.getMessage());
            } catch (InvalidOrderNumberException e){
                System.out.println(e.getMessage());
            } catch (InvalidRating e){
				System.out.println(e.getMessage());
			}catch (InvalidCategoryException e){
				System.out.println(e.getMessage());

			System.out.print("\n>");
				}
	}
}
}

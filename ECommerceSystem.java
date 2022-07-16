import java.io.*;
import java.util.*;

//Name: Yamilla Prodhan
//Student # 501107717

/*
 * Models a simple ECommerce system. Keeps track of products for sale, registered customers, product orders and
 * orders that have been shipped to a customer
 */
public class ECommerceSystem 
{
    private TreeMap<String, Product>  products = new TreeMap<String, Product>();
    private TreeMap<String, Integer> order_stats = new TreeMap<String, Integer>();
    private ArrayList<Customer> customers = new ArrayList<Customer>();
    
    private ArrayList<ProductOrder> orders   = new ArrayList<ProductOrder>();
    private ArrayList<ProductOrder> shippedOrders   = new ArrayList<ProductOrder>();
    
    // These variables are used to generate order numbers, customer id's, product id's 
    private int orderNumber = 500;
    private int customerId = 900;
    private int productId = 700;
    
    // General variable used to store an error message when something is invalid (e.g. customer id does not exist)  
    String errMsg = null;
    
    // Random number generator
    Random random = new Random();
  
    public ECommerceSystem()
    {
      

      //makes a product object from product.txt with the method newProduct()
      //catches an exception if products.txt doesn't exist
      try{ 
        products = newProduct("products.txt");

        // Create some customers. Notice how generateCustomerId() method is used
        customers.add(new Customer(generateCustomerId(),"Inigo Montoya", "1 SwordMaker Lane, Florin"));
        customers.add(new Customer(generateCustomerId(),"Prince Humperdinck", "The Castle, Florin"));
        customers.add(new Customer(generateCustomerId(),"Andy Dufresne", "Shawshank Prison, Maine"));
        customers.add(new Customer(generateCustomerId(),"Ferris Bueller", "4160 Country Club Drive, Long Beach"));
      }
      catch(FileNotFoundException e)
      {
        System.out.println("File not found");
        System.exit(1);
      }
    }


    private TreeMap<String, Product> newProduct(String file_name) throws FileNotFoundException
    {
      int i = -1; //indexs each line in product.txt to help set the name, id..
      String name = "";
      String id = "";
      double price = 0.0;
      int stock = 0;
      int paperbackStock = 0;
      int hardcoverStock = 0;
      String title = "";
      String author = "";
      String year = "";
      Product.Category category = Product.Category.GENERAL;

      File file = new File(file_name);
      Scanner scanner = new Scanner(file);

      while (scanner.hasNext())
      {
        String x = scanner.nextLine();
        i++;

        if (i == 0) //sets the category
        {
          if(x.equals("BOOKS"))
          {
            category = Product.Category.BOOKS;
          }
          if(x.equals("COMPUTERS"))
          {
            category = Product.Category.COMPUTERS;
          }
          if(x.equals("CLOTHING"))
          {
            category = Product.Category.CLOTHING;
          }
          if(x.equals("FURNITURE"))
          {
            category = Product.Category.FURNITURE;
          }
          if(x.equals("GENERAL"))
          {
            category = Product.Category.GENERAL;
          }
        }
        if (i == 1) //sets the name
        {
          name = x;
        }
        if (i == 2) //sets the price
        {
          price = Double.parseDouble(x);
        }
        if (name.equals("Book") && i == 3) //sets the book stock
        {
          String [] y = x.split(" ");
          paperbackStock = Integer.parseInt(y[0]);
          hardcoverStock = Integer.parseInt(y[1]);
        }
        if (!name.equals("Book") && i == 3) //sets the stock
        {
          stock = Integer.parseInt(x);
        }
        if(name.equals("Book") && i == 4) //sets the information of a book
        {
          ArrayList<String> info = new ArrayList<String>(Arrays.asList(x.split(":")));
          title = info.get(0);
          author = info.get(1);
          year = info.get(2);
          id = generateProductId();
          products.put(id, new Book(name, id, price, paperbackStock, hardcoverStock, title, author, year));
          order_stats.put(name + " " + id, 0);
          i = -1;
        }
        if (!name.equals("Book") && i == 4) //sets the info of a prod
        {
          id = generateProductId();
          products.put(id, new Product(name, id, price, stock, category));
          order_stats.put(name + " " + id, 0);
          i = -1;
        }
      }
      scanner.close(); //closes the scanner
      return products; //returns the product objects
    }

    //method to add item in cart
    public void addToCart(String productId, String customerId, String productOptions) 
    {
      for (Customer cust: customers)
      {
        if (cust.getId().equals(customerId))
        {
          for (Product prod: products.values())
            {
              if (prod.getId().equals(productId))
              {
                cust.getCart().addToCart(prod, productOptions);
              }
              
            }
          
        }
      }
    }

    //method to remove item from cart
    public void removeItem(String productId, String customerId) 
    {
      for (Customer cust: customers)
      {
        if (cust.getId().equals(customerId))
        {
          for (Product prod: products.values())
          {
            if (prod.getId().equals(productId))
            {
              cust.getCart().removeItem(prod);
              break;
            }
          }
        }
        break;
      }
    }

    //method to print out the customers cart
    public void printCart(String customerId) 
    {
      for (Customer cust: customers)
      {
        if (cust.getId().equals(customerId))
        {
          cust.getCart().printCart();
        }
      }
    }

    //method to check if the customer is valid
    public Customer validCustomer(String customerId)
    {
      int i = customers.indexOf(new Customer(customerId));

      if (i == -1)
      {
        throw new UnknownCustomerException("Customer " + customerId + " Not found");
      }
      Customer validCustomer = customers.get(i);
      return validCustomer;
    }

    //method to check if the product is valid
    public Product validProduct(String productId)
    {
      boolean test = products.keySet().contains(productId);

      if (test == false)
      {
        throw new UnknownProductException("Product " + productId + " Not found");
      }
      Product validProduct = products.get(productId);
      return validProduct;

    }

    //orders the items in the cart
    public void orderItems(String customerId)
    {
      Customer validCustomer = validCustomer(customerId);
      ArrayList<CartItem> cl = validCustomer.getCart().getCartItems();

      for (CartItem ci: cl)
      {
        String ID = ci.getCartItems().getId();
        String productOptions = ci.getProductOptions();
        orderProduct(ID, customerId, productOptions);
      }
      validCustomer.getCart().removeAllItems();
    }

    //BONUS-RATING METHOD #1
    public void rateProd(String productID, String rate){
      validRate(rate);
      validProduct(productID);
      for(Product p : products.values()){
        if(p.getId().equals(productID)){
            p.rateProd(rate);
        }
      }
  }

    //BONUS-RATING METHOD #2
    public void printRating(String productID){
      for(Product p : products.values()){
        if(p.getId().equals(productID)){
            p.printRating();
        }
      }
    }

    //BONUS-RATING METHOD #3
    public int validRate(String rate)
    {
      int rate2 = Integer.parseInt(rate);
      int i = 0;

      if (rate2 > 1 || rate2 < 5)
      {
        i = rate2; 
        return i;
      }
      else
      {
        throw new InvalidRating("Invalid Rating: " + i);
      }
    }

    //BONUS-RATING METHOD #4
    public Product.Category validCategory(String category){
      Product.Category c = null;
      if (category.equalsIgnoreCase("GENERAL")){
        c = Product.Category.GENERAL;
      }
      else if (category.equalsIgnoreCase("COMPUTERS")){
        c = Product.Category.COMPUTERS;
      }
      else if (category.equalsIgnoreCase("FURNITURE")){
        c = Product.Category.FURNITURE;
      }
      else if (category.equalsIgnoreCase("CLOTHING")){
        c = Product.Category.CLOTHING;
      }
      else if (category.equalsIgnoreCase("BOOKS")){
        c = Product.Category.BOOKS;
      }
      else {
        throw new InvalidCategoryException("Invalid Category Input: " + category);
      }
  
      if (c == null){
        throw new InvalidCategoryException("Invalid Category Input: " + category);
      }
      return c;
    }

    //BONUS-RATING METHOD #5
    public void productAboveThreshold(String rate, String category)
    {
      validCategory(category);
      validRate(rate);

      for(Product prod : products.values()){ //finds the product
        if(prod.aboveRating(validRate(rate))){ // checks if its above rating
          if (prod.getCategory().equals(validCategory(category))){  // finds its category
            prod.print();
          }
        }  
      }
    }


    public void orderStats()
    {
      ArrayList<String> k = new ArrayList<String>(); //arraylist k to hold the keys as a string
      ArrayList<Integer> v = new ArrayList<Integer>(); //arraylist v to hold the values as a string

      for (String keys: order_stats.keySet())
      {
        k.add(keys);
        v.add(order_stats.get(keys));
      }

      int x =k.size();

      for (int i = 0; i < x; i++)
      {
        String maxKey = k.get(0);
        Integer maxFreq = v.get(0);
        
        for (int j = 0; j < k.size(); j++)
        {
          Integer currFreq = v.get(j);
          String currKey = k.get(j);
          
          if (maxFreq < currFreq)
          {
            maxFreq = currFreq;
            maxKey = currKey;
          }
        }

        String a = maxKey;
        System.out.println(a + " : " + maxFreq);

        v.remove(maxFreq);
        k.remove(maxKey);
      }
    }
    
    private String generateOrderNumber()
    {
    	return "" + orderNumber++;
    }

    private String generateCustomerId()
    {
    	return "" + customerId++;
    }
    
    private String generateProductId()
    {
    	return "" + productId++;
    }
    
    public String getErrorMessage()
    {
    	return errMsg;
    }
    
    public void printAllProducts()
    {
    	for (Product p : products.values())
    		p.print();
    }
    
    // Print all products that are books. See getCategory() method in class Product
    public void printAllBooks()
    {
      for (Map.Entry<String, Product> entry : products.entrySet()){
        if (entry.getValue().getCategory().equals(Product.Category.BOOKS)) 
        {
          entry.getValue().print();
        }
      }
    	
    }
    
    // Print all current orders
    public void printAllOrders()
    {
      for (ProductOrder ord : orders){
        ord.print();
      }
    	
    }
    // Print all shipped orders
    public void printAllShippedOrders()
    {
      for (ProductOrder ship : shippedOrders){
        ship.print();
      }
    	
    }
    
    // Print all customers
    public void printCustomers()
    {
      for (Customer c : customers)
      {
        c.print();
      }
    	
    }
    /*
     * Given a customer id, print all the current orders and shipped orders for them (if any)
     */
    public boolean printOrderHistory(String customerId)
    {
      // Make sure customer exists - check using customerId
    	// If customer does not exist, set errMsg String and return false
    	// see video for an appropriate error message string
    	// ... code here
      Customer ec = null;

      for (Customer cust: customers){
        if (cust.getId().equals(customerId)){ 
          ec = cust;
          break;
        }
      }
      if (ec == null){
        throw new UnknownCustomerException("Customer " + customerId + " Not found");
      }

    	// Print current orders of this customer 
    	System.out.println("Current Orders of Customer " + customerId);
    	// enter code here

      for (ProductOrder po: orders){
        if (po.getCustomer().equals(ec)){
          po.print();
        }
      }
    	
    	// Print shipped orders of this customer 
    	System.out.println("\nShipped Orders of Customer " + customerId);
    	//enter code here

      for (ProductOrder po2: shippedOrders){
        if (po2.getCustomer().equals(ec)){
          po2.print();
        }
      }
    	return true;
    }
    
    public String orderProduct(String productId, String customerId, String productOptions)
    {
    	// First check to see if customer object with customerId exists in array list customers
    	// if it does not, set errMsg and return null (see video for appropriate error message string)
    	// else get the Customer object
      
      Customer vc = null;
      
      for (Customer cust: customers){
        if (cust.getId().equals(customerId)){
          vc = cust;
          break;
        }
          
      }
        if (vc == null){
          throw new UnknownCustomerException("Customer " + customerId + " Not found");
      }
    	
    	// Check to see if product object with productId exists in array list of products
    	// if it does not, set errMsg and return null (see video for appropriate error message string)
    	// else get the Product object

      Product vp = null;

    	for (Map.Entry<String, Product> entry: products.entrySet()){
        if (entry.getValue().getId().equals(productId)){
          vp = entry.getValue();
          break;
        }
      }
  
    	// Check if the options are valid for this product (e.g. Paperback or Hardcover or EBook for Book product)
    	// See class Product and class Book for the method vaidOptions()
    	// If options are not valid, set errMsg string and return null;

      if (vp != null){
        if (!vp.validOptions(productOptions)){
          throw new InvalidProductOptionException("ProductID " + productId + " invalid options" + productOptions);
        }
      
    	// Check if the product has stock available (i.e. not 0)
    	// See class Product and class Book for the method getStockCount()
    	// If no stock available, set errMsg string and return null

      if (vp.getStockCount(productOptions) == 0){
        throw new OutOfStockException("Product " + productId + "out of stock");
      }
      

      // Create a ProductOrder, (make use of generateOrderNumber() method above)
    	// reduce stock count of product by 1 (see class Product and class Book)
    	// Add to orders list and return order number string

      ProductOrder new_order = new ProductOrder(generateOrderNumber(), vp, vc, productOptions);
      vp.reduceStockCount(productOptions);
      orders.add(new_order);
    	for (Product prod: products.values())
      {
        if (prod.getId().equals(productId))
        {
          order_stats.put(prod.getName() + " " + productId, order_stats.get(prod.getName() + " " + productId) + 1);
        }
      }
      return new_order.getOrderNumber();

    }
    else {
      throw new UnknownProductException("Product " + productId + " Not found");
    }
    
  }

    
    /*
     * Create a new Customer object and add it to the list of customers
     */
    
    public boolean createCustomer(String name, String address)
    {
    	// Check name parameter to make sure it is not null or ""
    	// If it is not a valid name, set errMsg (see video) and return false
    	// Repeat this check for address parameter

    	if (name.equals("") || name.equals(null)){
        throw new InvalidCustomerNameException("Invalid customer name");
      }
      if (address.equals("") || address.equals(null)){
        throw new InvalidCustomerAddressException("Invalid customer address");
      }
      
    	// Create a Customer object and add to array list
      Customer person1 = new Customer(generateCustomerId(), name, address);
      customers.add(person1);
    	return true;
    }
    

    public ProductOrder shipOrder(String orderNumber)
    {
      // Check if order number exists first. If it doesn't, set errMsg to a message (see video) 
    	// and return false
    	// Retrieve the order from the orders array list, remove it, then add it to the shippedOrders array list
    	// return a reference to the order

      ProductOrder x = null;
      boolean test = false;

      for(ProductOrder on: orders){
        if(on.getOrderNumber().equals(orderNumber)){
          x = on; 
          orders.remove(on);
          shippedOrders.add(x);
          test = true;
          return x;
        }
        
      }
    	if (test == false)
      {
        throw new InvalidOrderNumberException("Order" + orderNumber + "does not exist."); 
      }
      return null;
    }
    

    /*
     * Cancel a specific order based on order number
     */
    public boolean cancelOrder(String orderNumber)
    {
      // Check if order number exists first. If it doesn't, set errMsg to a message (see video) 
    	// and return false

      for (ProductOrder o: orders){
        if (o.getOrderNumber().equals(orderNumber)){
          orders.remove(o);
          return true;
          
        }
      }
      throw new InvalidOrderNumberException("Invalid order# " + orderNumber);
      
    }

    
     // Sort products by increasing price
     public void printByPrice()
     {
       ArrayList<Product> prodList = new ArrayList<Product>(products.values());
       // lambda expression
       prodList.sort(Comparator.comparing(Product::getPrice));
       for (Product p : prodList){
         p.print();
       }
     }
     
     
     // // Sort products alphabetically by product name
     public void printByName()
     {
       ArrayList<Product> prodList = new ArrayList<Product>(products.values());
       Comparator <Product> prodAlpha = new Comparator<Product>() {
         public int compare(Product p1, Product p2){
           return p1.getName().compareTo(p2.getName());
         }
       };
       Collections.sort(prodList, prodAlpha);
       for (Product prod : prodList){
         prod.print();
       }
     }
 
    // Sort products alphabetically by product name
    public void sortCustomersByName()
    {
      Comparator <Customer> alphaname = new Comparator<Customer>(){
        public int compare(Customer cust1, Customer cust2){   //compares the customer obj 
          return cust1.getName().compareTo(cust2.getName());
        }
      };
      Collections.sort(customers, alphaname);
  	  
    }

    // Sort the book products by increasing year (BONUS)
    public boolean sortBooksByAuthor(String author){


      boolean testing = false;
      ArrayList<Book> book_2 = new ArrayList<Book>(); //make a new arraylist by taking the books from the prod arraylist

      for (Map.Entry<String, Product> entry : products.entrySet()){
        if (entry.getValue().getCategory().equals(Product.Category.BOOKS)){
          Book b = (Book) entry.getValue();
          if (b.getAuthorName().equalsIgnoreCase(author)){
            book_2.add((Book)entry.getValue());
            testing = true;
          }
        }
      }
    if (testing == false){
      throw new UnknownAuthorException("Author not found");
      }

      Comparator <Book> sorted_books = new Comparator<Book>() { //sort the books by author name
        public int compare(Book book1, Book book2){
          return book1.getAuthorName().compareTo(book2.getAuthorName()); 
        }
      };
      Collections.sort(book_2, sorted_books);

      for (int i=0;i<book_2.size();i++){ //prints out the books made by the author
        book_2.get(i).print();
      }
      return true;
      }

}

class UnknownCustomerException extends RuntimeException{
  public UnknownCustomerException(){
  }
  public UnknownCustomerException(String msg){
    super(msg);
  }
}

class UnknownProductException extends RuntimeException{
  public UnknownProductException(){
  }
  public UnknownProductException(String msg){
    super(msg);
  }
}

class InvalidProductOptionException extends RuntimeException{
  public InvalidProductOptionException(){
  }
  public InvalidProductOptionException(String msg){
    super(msg);
  }
}

class OutOfStockException extends RuntimeException{
  public OutOfStockException(){
  }
  public OutOfStockException(String msg){
    super(msg);
  }
}

class InvalidCustomerNameException extends RuntimeException{
  public InvalidCustomerNameException(){
  }
  public InvalidCustomerNameException(String msg){
    super(msg);
  }
}

class InvalidCustomerAddressException extends RuntimeException{
  public InvalidCustomerAddressException(){
  }
  public InvalidCustomerAddressException(String msg){
    super(msg);
  }
}

class InvalidOrderNumberException extends RuntimeException{
  public InvalidOrderNumberException(){
  }
  public InvalidOrderNumberException(String msg){
    super(msg);
  }
}

class UnknownAuthorException extends RuntimeException{
  public UnknownAuthorException(){
  }
    public UnknownAuthorException(String msg){
      super(msg);
    }
  }

class InvalidRating extends RuntimeException{
  public InvalidRating(){
  }
  public InvalidRating(String msg){
    super(msg);
  }
}

class InvalidCategoryException extends RuntimeException{
  public InvalidCategoryException(){
  }
  public InvalidCategoryException(String msg){
    super(msg);
  }
}


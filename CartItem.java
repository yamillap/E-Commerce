
//CartItem class created to reference product objects and add them to the 
//shopping cart created in the class Cart


public class CartItem 
{
    private Product product;
    private String ProductOptions = "";

    //constructs a cart item using the prod obj and it's options
    public CartItem(Product product, String ProductOpt){
        this.ProductOptions = ProductOpt;
        this.product = product;
    }
    
    public Product getProduct(){ //return the product the cart item holds
        return product;
    }

    public String getProductOptions(){ //returns the product options of the product in the cart
        return ProductOptions;
    }

    public Product getCartItems() //returns the cart item (which is a product obj)
    {
        return product;
    }

    public String toString() //prints the product obj as a string
    {
        return this.product.toString();
    }


}

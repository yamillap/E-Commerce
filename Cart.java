import java.util.*;

// Cart Class created so each customer has their own shopping cart

public class Cart 
{
    private LinkedList<CartItem> shopping_cart;
    private ListIterator <CartItem> iter; 

    public Cart(){
        shopping_cart = new LinkedList<CartItem>();
        iter = shopping_cart.listIterator();
    }
    
    public void addToCart(Product product, String productOptions)
    {
        iter.add(new CartItem (product, productOptions));
    }

    public void removeItem(Product product)
    {
        for (Iterator <CartItem> iter2 = shopping_cart.iterator(); iter2.hasNext();)
        {
            CartItem i = iter2.next();
            if (i.getProduct().equals(product))
            {
                shopping_cart.remove();
            }
        }
        
    }

    public CartItem getCart(String customerId){
        return iter.next();
    }

    public void printCart()
    {
        for (CartItem ci: shopping_cart)
        {
            ci.getProduct().print();
        }
    } 

    public ArrayList<CartItem> getCartItems()
    {
        ArrayList<CartItem> cart_items = new ArrayList<CartItem>();

        for (CartItem ci: shopping_cart)
        {
            cart_items.add(ci);
        }
        return cart_items;
    }

    public void removeAllItems()
    {
        shopping_cart.clear();
    }
}

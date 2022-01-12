package com.amigoscode.customer.cart;

import com.amigoscode.customer.bankAccount.BankAccountServiceImpl;
import com.amigoscode.customer.entity.Customer;
import com.amigoscode.customer.order.Order;
import com.amigoscode.customer.order.OrderItemRequest;
import com.amigoscode.customer.order.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController {

    private final OrderServiceImpl orderService;
    private final CartServiceImpl cartService;
    //    private final ProductService productService;
    private final BankAccountServiceImpl bankAccountService;
    private final RestTemplate restTemplate;

    public CartController(OrderServiceImpl orderService, CartServiceImpl cartService, BankAccountServiceImpl bankAccountService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.bankAccountService = bankAccountService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/add")
    public String addProductToCart(@RequestParam int productId,
                                   @RequestParam int quantity, Principal principal) {
        System.out.println("HELP");
        Customer customer = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        System.out.println("Getting price...");
        final ResponseEntity<Double> priceResponse = restTemplate.getForEntity(
                "http://localhost:8082/products/validate?productId={productId}&quantity={quantity}",
                Double.class,
                productId, quantity);
        System.out.println("Got price..." + priceResponse.getBody());
//        double price = cartService.validateProduct(productId, quantity);
        OrderItemRequest item = new OrderItemRequest(productId, quantity, priceResponse.getBody().doubleValue());
        System.out.println("Created item!!");
        cartService.addProductToCart(customer, item);
//        return "redirect:/cart/";
        System.out.println("DONE");
        return "redirect:" + "http://localhost:8080/cart";
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteItemFromCart(@RequestParam int productId, Principal principal, Model model) {
        Customer customer = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Cart cart = cartService.findCartByUser(customer);
        List<OrderItemRequest> items = cartService.getCartContents(customer.getId());
        model.addAttribute("items", items);
        model.addAttribute("cart", cart);
        cartService.deleteItemFromCart(cart, customer.getId(), productId);
        return "cart";
    }

    @GetMapping()
    public String getCartContents(Principal principal, Model model) {
        Customer customer = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Cart cart = cartService.findCartByUser(customer);
        List<OrderItemRequest> items = cartService.getCartContents(customer.getId());
        model.addAttribute("items", items);
        model.addAttribute("cart", cart != null ? cart : new Cart(0));
        model.addAttribute("accounts", bankAccountService.getBankAccountsForUser(customer));
        return "cart";
    }

    @PostMapping("/checkout")
    public ModelAndView checkout(@RequestParam int accountId,
                                 Principal principal) {
        Customer customer = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Optional<Order> order = orderService.createOrder(customer, cartService.getCartItems().get(customer.getId()), accountId);
        ModelAndView modelAndView;
        if (order.isPresent()) {
            modelAndView = new ModelAndView("orders");
            modelAndView.addObject("orders", orderService.getOrdersByUser(customer));
        } else {
            modelAndView = new ModelAndView("cart");
            Cart cart = cartService.findCartByUser(customer);
            List<OrderItemRequest> items = cartService.getCartContents(customer.getId());
            modelAndView.addObject("items", items);
            modelAndView.addObject("cart", cart != null ? cart : new Cart(0));
            modelAndView.addObject("accounts", bankAccountService.getBankAccountsForUser(customer));
        }
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView updateQuantity(@RequestParam int productId,
                                       @RequestParam int quantity,
                                       Principal principal) {

        Customer customer = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        OrderItemRequest itemRequest = cartService.getItemByProductId(productId, customer.getId());
        Cart cart = cartService.findCartByUser(customer);
//        Product product = productService.findProductById(productId);

        final int stock = restTemplate.getForEntity(
                "http://localhost:8082/products/getStock/{productId}",
                int.class,
                productId).getBody();
        ModelAndView model = new ModelAndView("cart");
        if (stock < quantity || quantity <= 0) {
            model.addObject("cart", cart != null ? cart : new Cart(0));
            model.addObject("noStock", "Product with id" + productId + " not in stock");
            model.addObject("accounts", bankAccountService.getBankAccountsForUser(customer));
            return model;
        }
        model.addObject("noStock", "NO");
        int oldQuantity = cartService.updateItemQuantity(customer.getId(), itemRequest, quantity);
        double newValue = cart.getTotalAmount() - oldQuantity * itemRequest.getPrice() + quantity * itemRequest.getPrice();

        cartService.updateCartAmount(cart.getId(), newValue);
        cart = cartService.findCartByUser(customer);

        model.addObject("cart", cart != null ? cart : new Cart(0));
        model.addObject("accounts", bankAccountService.getBankAccountsForUser(customer));
        return model;
    }
}



package com.example.springwebapi.service;

import com.example.springwebapi.entity.*;
import com.example.springwebapi.model.Expirable;
import com.example.springwebapi.model.Shippable;
import com.example.springwebapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EcommerceService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ShippingService shippingService;

    @Transactional
    public void addToCart(Long customerId, Long productId, Integer quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product is available
        if (product.isOutOfStock()) {
            throw new RuntimeException("Product " + product.getName() + " is out of stock");
        }

        // Check if product is expired (if it implements Expirable)
        if (product instanceof Expirable && ((Expirable) product).isExpired()) {
            throw new RuntimeException("Product " + product.getName() + " is expired");
        }

        if (quantity > product.getQuantity()) {
            throw new RuntimeException("Requested quantity " + quantity + " exceeds available quantity " + product.getQuantity());
        }

        // Check if item already exists in cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByCustomerAndProductId(customer, productId);
        
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            if (newQuantity > product.getQuantity()) {
                throw new RuntimeException("Total quantity " + newQuantity + " exceeds available quantity " + product.getQuantity());
            }
            
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }
    @Transactional
    public void removeFromCart(Long customerId, Long productId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check if item exists in cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByCustomerAndProductId(customer, productId);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItemRepository.delete(cartItem);
        } else {
            throw new RuntimeException("Product is not in the cart");
        }
    }

    @Transactional
    public void updateCartItem(Long customerId, Long productId, Integer quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product is available
        if (product.isOutOfStock()) {
            throw new RuntimeException("Product " + product.getName() + " is out of stock");
        }

        // Check if product is expired (if it implements Expirable)
        if (product instanceof Expirable && ((Expirable) product).isExpired()) {
            throw new RuntimeException("Product " + product.getName() + " is expired");
        }

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (quantity > product.getQuantity()) {
            throw new RuntimeException("Requested quantity " + quantity + " exceeds available quantity " + product.getQuantity());
        }

        // Check if item exists in cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByCustomerAndProductId(customer, productId);
        
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("Product is not in the cart");
        }
    }

    @Transactional
    public void checkout(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Validate all items
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            if (product.isOutOfStock()) {
                throw new RuntimeException("Product " + product.getName() + " is out of stock");
            }
            
            // Check if product is expired (if it implements Expirable)
            if (product instanceof Expirable && ((Expirable) product).isExpired()) {
                throw new RuntimeException("Product " + product.getName() + " is expired");
            }
            
            if (cartItem.getQuantity() > product.getQuantity()) {
                throw new RuntimeException("Requested quantity " + cartItem.getQuantity() + " exceeds available quantity " + product.getQuantity());
            }
        }

        // Calculate totals
        double subtotal = cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

        double shippingFees = calculateShippingFees(cartItems);
        double totalAmount = subtotal + shippingFees;

        // Check customer balance
        if (!customer.hasSufficientBalance(totalAmount)) {
            throw new RuntimeException("Insufficient balance. Required: " + totalAmount + ", Available: " + customer.getBalance());
        }

        // Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setSubtotal(subtotal);
        order.setShippingFees(shippingFees);
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.COMPLETED);

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getProduct().getPrice());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        // Update product quantities
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // Deduct customer balance
        customer.deductBalance(totalAmount);
        customerRepository.save(customer);

        // Save order
        orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteByCustomer(customer);

        // Print checkout details
        printCheckoutDetails(cartItems, subtotal, shippingFees, totalAmount, customer.getBalance());

        // Handle shipping
        List<ShippableItem> shippableItems = getShippableItems(cartItems);
        if (!shippableItems.isEmpty()) {
            shippingService.shipItems(shippableItems);
        }
    }

    private double calculateShippingFees(List<CartItem> cartItems) {
        // Simple shipping calculation: 30 for items that require shipping
        boolean hasShippableItems = cartItems.stream()
                .anyMatch(item -> item.getProduct() instanceof Shippable);
        
        return hasShippableItems ? 30.0 : 0.0;
    }

    private List<ShippableItem> getShippableItems(List<CartItem> cartItems) {
        List<ShippableItem> shippableItems = new ArrayList<>();
        
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product instanceof Shippable) {
                Shippable shippable = (Shippable) product;
                for (int i = 0; i < cartItem.getQuantity(); i++) {
                    shippableItems.add(new ShippableItem() {
                        @Override
                        public String getName() {
                            return product.getName();
                        }

                        @Override
                        public double getWeight() {
                            return shippable.getWeight();
                        }
                    });
                }
            }
        }
        
        return shippableItems;
    }

    private void printCheckoutDetails(List<CartItem> cartItems, double subtotal, double shippingFees, double totalAmount, double remainingBalance) {
        System.out.println("** Checkout receipt **");
        
        for (CartItem cartItem : cartItems) {
            System.out.printf("%dx %s %.0f%n", 
                cartItem.getQuantity(), 
                cartItem.getProduct().getName(), 
                cartItem.getSubtotal());
        }
        
        System.out.println("----------------------");
        System.out.printf("Subtotal %.0f%n", subtotal);
        System.out.printf("Shipping %.0f%n", shippingFees);
        System.out.printf("Amount %.0f%n", totalAmount);
        System.out.printf("Customer balance after payment: %.0f%n", remainingBalance);
        System.out.println("END.");
    }

    public List<CartItem> getCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        return cartItemRepository.findByCustomer(customer);
    }

    @Transactional
    public void clearCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        cartItemRepository.deleteByCustomer(customer);
    }
} 
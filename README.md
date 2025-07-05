# Fawry E-commerce System

A complete Spring Boot E-commerce Web API project that implements the Fawry Quantum Internship Challenge requirements with RESTful endpoints, JPA/Hibernate, H2 database, and comprehensive error handling.

## Challenge Requirements Implemented

✅ **Product Management**
- Products with name, price, and quantity
- Expiring products (Cheese, Biscuits) vs non-expiring products (TV, Mobile)
- Shippable products with weight vs non-shippable products (Scratch Cards)

✅ **Shopping Cart Management**
- Add products to cart with quantity validation
- Update cart item quantities
- Remove specific items from cart
- Clear entire cart
- Prevent adding more than available quantity
- Check for expired and out-of-stock products
- Real-time cart validation and error handling

✅ **Checkout System**
- Calculate subtotal, shipping fees, and total amount
- Validate customer balance
- Print detailed checkout receipt to console
- Handle shipping for applicable items

✅ **Error Handling**
- Cart is empty
- Insufficient customer balance
- Products out of stock or expired
- Invalid quantities
- Product not found in cart
- Customer not found
- Validation errors with detailed messages

✅ **Shipping Service**
- Implements the required interface with `getName()` and `getWeight()` methods
- Prints shipment details to console
- Calculates total package weight

## Features

- **Spring Boot 3.2.0** with Java 17
- **Spring Data JPA** for database operations
- **H2 In-Memory Database** for development
- **RESTful API** with full CRUD operations
- **Input Validation** using Bean Validation
- **Global Exception Handling**
- **Lombok** for reducing boilerplate code
- **CORS** enabled for cross-origin requests
- **Sample Data** initialization
- **E-commerce Demo** functionality
- **Comprehensive Cart Management** with full CRUD operations
- **DTO-based API** with validation
- **Transaction Safety** for all operations

## Project Structure

```
src/
├── main/
│   ├── java/com/example/springwebapi/
│   │   ├── SpringWebApiApplication.java
│   │   ├── controller/
│   │   │   ├── ProductController.java
│   │   │   ├── CustomerController.java
│   │   │   ├── EcommerceController.java
│   │   │   └── DemoController.java
│   │   ├── dto/
│   │   │   ├── CartItemRequest.java
│   │   │   └── CartItemResponse.java
│   │   ├── entity/
│   │   │   ├── Product.java
│   │   │   ├── Customer.java
│   │   │   ├── CartItem.java
│   │   │   ├── Order.java
│   │   │   └── OrderItem.java
│   │   ├── repository/
│   │   │   ├── ProductRepository.java
│   │   │   ├── CustomerRepository.java
│   │   │   ├── CartItemRepository.java
│   │   │   └── OrderRepository.java
│   │   ├── service/
│   │   │   ├── EcommerceService.java
│   │   │   ├── EcommerceDemoService.java
│   │   │   ├── ShippingService.java
│   │   │   ├── ShippingServiceImpl.java
│   │   │   └── ShippableItem.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   └── config/
│   │       └── DataInitializer.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/springwebapi/
        └── SpringWebApiApplicationTests.java
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Clone or Download the Project

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Product Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create a new product |
| PUT | `/api/products/{id}` | Update product by ID |
| DELETE | `/api/products/{id}` | Delete product by ID |

### Customer Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers` | Create a new customer |
| PUT | `/api/customers/{id}` | Update customer by ID |
| DELETE | `/api/customers/{id}` | Delete customer by ID |

### E-commerce Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/ecommerce/cart/add` | Add product to cart |
| GET | `/api/ecommerce/cart/{customerId}` | Get customer's cart |
| PUT | `/api/ecommerce/cart/update` | Update cart item quantity |
| DELETE | `/api/ecommerce/cart/remove` | Remove item from cart |
| DELETE | `/api/ecommerce/cart/clear/{customerId}` | Clear entire cart |
| POST | `/api/ecommerce/checkout/{customerId}` | Checkout customer's cart |

### Demo
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/demo/run` | Run the e-commerce demo |

## Sample Requests

### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cheese",
    "price": 100.0,
    "quantity": 10,
    "expiryDate": "2024-08-05",
    "requiresShipping": true,
    "weightGrams": 400.0
  }'
```

### Create Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Customer",
    "email": "alice@example.com",
    "balance": 1000.0
  }'
```

### Add to Cart
```bash
curl -X POST http://localhost:8080/api/ecommerce/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "productId": 1,
    "quantity": 2
  }'
```

### Update Cart Item
```bash
curl -X PUT http://localhost:8080/api/ecommerce/cart/update \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "productId": 1,
    "quantity": 5
  }'
```

### Remove from Cart
```bash
curl -X DELETE "http://localhost:8080/api/ecommerce/cart/remove?customerId=1&productId=1"
```

### Clear Cart
```bash
curl -X DELETE http://localhost:8080/api/ecommerce/cart/clear/1
```

### Checkout
```bash
curl -X POST http://localhost:8080/api/ecommerce/checkout/1
```

### Run Demo
```bash
curl -X POST http://localhost:8080/api/demo/run
```

## Demo Output

When you run the demo, you'll see output similar to this:

```
=== Fawry E-commerce System Demo ===
Customer: Alice Customer (Balance: 1000.0)
Products available:
- Cheese: 100.0 (Quantity: 10)
- Biscuits: 150.0 (Quantity: 15)
- TV: 2000.0 (Quantity: 5)
- Scratch Card: 50.0 (Quantity: 100)

Adding items to cart...
Added 2x Cheese
Added 1x Biscuits
Added 3x TV
Added 1x Scratch Card

Proceeding to checkout...
** Checkout receipt **
2x Cheese 200
1x Biscuits 150
3x TV 6000
1x Scratch Card 50
----------------------
Subtotal 6400
Shipping 30
Amount 6430
Customer balance after payment: -5430.0
END.

** Shipment notice **
2x Cheese 400g
1x Biscuits 700g
3x TV 15000g
Total package weight 16.1kg
=== Demo Complete ===
```

## Database

- **H2 In-Memory Database** is used for development
- **H2 Console** is available at `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

## Sample Data

The application initializes with:

### Customers
- Alice Customer (Balance: 1000.0)
- Bob Customer (Balance: 500.0)

### Products
- **Cheese**: 100.0 (expires, requires shipping, 400g)
- **Biscuits**: 150.0 (expires, requires shipping, 700g)
- **TV**: 2000.0 (no expiry, requires shipping, 15000g)
- **Mobile**: 1500.0 (no expiry, requires shipping, 200g)
- **Scratch Card**: 50.0 (no expiry, no shipping, no weight)

## Validation

The API includes comprehensive validation:
- Product quantities must be non-negative
- Customer balance must be non-negative
- Email addresses must be valid
- Product names are required
- Quantities cannot exceed available stock
- Products cannot be expired or out of stock

## Error Handling

The application includes comprehensive error handling:
- Validation errors return detailed field-specific messages
- Business logic errors (insufficient balance, out of stock, etc.)
- Runtime exceptions return appropriate HTTP status codes
- Generic exceptions are handled gracefully

## Testing

Run the tests using:
```bash
mvn test
```

## Challenge Implementation Details

### Product Types
- **Expiring Products**: Cheese, Biscuits (have expiry dates)
- **Non-expiring Products**: TV, Mobile, Scratch Cards (no expiry dates)
- **Shippable Products**: Cheese, Biscuits, TV, Mobile (have weight and require shipping)
- **Non-shippable Products**: Scratch Cards (no weight, no shipping required)

### Cart Validation
- Cannot add more than available quantity
- Cannot add expired products
- Cannot add out-of-stock products
- Cannot add negative quantities
- Cannot update to invalid quantities
- Cannot remove non-existent items
- Automatic quantity merging for same products

### Checkout Process
1. Validates all cart items
2. Calculates subtotal
3. Calculates shipping fees (30 for shippable items)
4. Validates customer balance
5. Updates product quantities
6. Deducts customer balance
7. Creates order and order items
8. Clears cart
9. Prints receipt to console
10. Handles shipping for applicable items

### Shipping Service
- Implements the required interface with `getName()` and `getWeight()` methods
- Prints shipment details to console
- Calculates total package weight in kg

## API Documentation

For detailed API documentation, see:
- [Cart API Documentation](CART_API.md) - Complete cart management endpoints
- [Shipment API Documentation](SHIPMENT_API.md) - Shipment tracking and management

## Production Deployment

For production deployment:
1. Replace H2 with a production database (MySQL, PostgreSQL, etc.)
2. Update `application.properties` with production database settings
3. Configure proper security measures
4. Set up proper logging
5. Configure environment-specific properties

## Recent Updates

### Cart Management Enhancements (Latest)
- ✅ Added comprehensive cart item management
- ✅ Implemented update cart item functionality
- ✅ Added remove specific items from cart
- ✅ Added clear entire cart functionality
- ✅ Enhanced validation and error handling
- ✅ Added DTO-based API with proper validation
- ✅ Improved transaction safety for all operations

## License

This project is for educational purposes and Fawry Quantum Internship Challenge submission. 

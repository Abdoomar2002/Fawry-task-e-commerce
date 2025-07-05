# Cart API Documentation

## Overview
The Cart API provides comprehensive functionality for managing shopping cart items in the e-commerce system. It includes adding, removing, updating, and clearing cart items.

## Base URL
```
/api/ecommerce/cart
```

## Endpoints

### 1. Add Item to Cart
**POST** `/api/ecommerce/cart/add`

Adds a product to the customer's cart.

**Request Body:**
```json
{
  "customerId": 1,
  "productId": 2,
  "quantity": 3
}
```

**Response:** 200 OK
```json
"Product added to cart successfully"
```

**Error Response:** 400 Bad Request
```json
"Product is out of stock"
```

### 2. Get Cart Items
**GET** `/api/ecommerce/cart/{customerId}`

Retrieves all items in a customer's cart.

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "customerId": 1,
    "productId": 2,
    "productName": "Laptop",
    "productPrice": 999.99,
    "quantity": 2,
    "subtotal": 1999.98
  },
  {
    "id": 2,
    "customerId": 1,
    "productId": 3,
    "productName": "Mouse",
    "productPrice": 25.50,
    "quantity": 1,
    "subtotal": 25.50
  }
]
```

**Error Response:** 400 Bad Request
```json
"Customer not found"
```

### 3. Update Cart Item
**PUT** `/api/ecommerce/cart/update`

Updates the quantity of a specific item in the cart.

**Request Body:**
```json
{
  "customerId": 1,
  "productId": 2,
  "quantity": 5
}
```

**Response:** 200 OK
```json
"Cart item updated successfully"
```

**Error Responses:** 400 Bad Request
```json
"Product is not in the cart"
"Requested quantity 10 exceeds available quantity 5"
"Quantity must be greater than 0"
```

### 4. Remove Item from Cart
**DELETE** `/api/ecommerce/cart/remove?customerId=1&productId=2`

Removes a specific item from the cart.

**Response:** 200 OK
```json
"Item removed from cart successfully"
```

**Error Response:** 400 Bad Request
```json
"Product is not in the cart"
```

### 5. Clear Cart
**DELETE** `/api/ecommerce/cart/clear/{customerId}`

Removes all items from a customer's cart.

**Response:** 200 OK
```json
"Cart cleared successfully"
```

**Error Response:** 400 Bad Request
```json
"Customer not found"
```

### 6. Checkout
**POST** `/api/ecommerce/checkout/{customerId}`

Processes the checkout for a customer's cart.

**Response:** 200 OK
```json
"Checkout completed successfully"
```

**Error Responses:** 400 Bad Request
```json
"Cart is empty"
"Insufficient balance. Required: 1500.00, Available: 1000.00"
"Product Laptop is out of stock"
```

## Validation Rules

### CartItemRequest
- `customerId`: Required, must exist in customers table
- `productId`: Required, must exist in products table
- `quantity`: Required, must be at least 1, cannot exceed available product quantity

### Business Rules
1. **Product Availability**: Cannot add out-of-stock products
2. **Product Expiration**: Cannot add expired products
3. **Quantity Limits**: Cannot exceed available product quantity
4. **Customer Balance**: Must have sufficient balance for checkout
5. **Cart Validation**: All items must be valid before checkout

## Cart Item Properties

### CartItemResponse
- `id`: Unique cart item identifier
- `customerId`: Customer who owns the cart item
- `productId`: Product in the cart
- `productName`: Name of the product
- `productPrice`: Price per unit
- `quantity`: Number of items
- `subtotal`: Total price for this item (price × quantity)

## Error Handling

### Common Error Messages
- **Customer not found**: Invalid customer ID
- **Product not found**: Invalid product ID
- **Product is out of stock**: Product has zero quantity
- **Product is expired**: Product has passed expiration date
- **Quantity exceeds available**: Requested quantity > available quantity
- **Product is not in the cart**: Item doesn't exist in customer's cart
- **Insufficient balance**: Customer doesn't have enough money
- **Cart is empty**: No items to checkout

## Example Usage

### Adding Items to Cart
```bash
curl -X POST http://localhost:8080/api/ecommerce/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "productId": 2,
    "quantity": 3
  }'
```

### Viewing Cart
```bash
curl -X GET http://localhost:8080/api/ecommerce/cart/1
```

### Updating Item Quantity
```bash
curl -X PUT http://localhost:8080/api/ecommerce/cart/update \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "productId": 2,
    "quantity": 5
  }'
```

### Removing Item
```bash
curl -X DELETE "http://localhost:8080/api/ecommerce/cart/remove?customerId=1&productId=2"
```

### Clearing Cart
```bash
curl -X DELETE http://localhost:8080/api/ecommerce/cart/clear/1
```

### Checkout
```bash
curl -X POST http://localhost:8080/api/ecommerce/checkout/1
```

## Integration with Other APIs

The Cart API integrates with:
- **Product API**: Validates product availability and pricing
- **Customer API**: Validates customer existence and balance
- **Order API**: Creates orders during checkout
- **Shipping API**: Handles shipping for shippable products

## Notes

1. **Automatic Quantity Management**: When adding the same product, quantities are automatically combined
2. **Real-time Validation**: All operations validate product availability and customer balance
3. **Transaction Safety**: All operations are wrapped in transactions for data consistency
4. **Automatic Cart Clearing**: Cart is automatically cleared after successful checkout 
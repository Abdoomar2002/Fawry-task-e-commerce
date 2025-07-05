// Global variables
let currentCustomerId = null;
let currentProductId = null;

// API Base URL
const API_BASE = 'http://localhost:8080/api';


// Utility functions
function showNotification(message, type = 'info') {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${type} show`;
    
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

function showLoading(element) {
    element.classList.add('loading');
}

function hideLoading(element) {
    element.classList.remove('loading');
}

// Tab management
function showTab(tabName) {
    // Hide all tab contents
    const tabContents = document.querySelectorAll('.tab-content');
    tabContents.forEach(content => content.classList.remove('active'));
    
    // Remove active class from all tab buttons
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(btn => btn.classList.remove('active'));
    
    // Show selected tab content
    document.getElementById(tabName).classList.add('active');
    
    // Add active class to clicked button
    event.target.classList.add('active');
    
    // Load data for the selected tab
    loadTabData(tabName);
}

function loadTabData(tabName) {
    switch(tabName) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'products':
            loadProducts();
            break;
        case 'customers':
            loadCustomers();
            break;
        case 'orders':
            loadOrders();
            break;
    }
}

// Dashboard functions
async function loadDashboardData() {
    try {
        const [products, customers, orders] = await Promise.all([
            fetch(`${API_BASE}/products`).then(r => r.json()),
            fetch(`${API_BASE}/customers`).then(r => r.json()),
            fetch(`${API_BASE}/orders`).then(r => r.json())
        ]);
        
        document.getElementById('productCount').textContent = products.length;
        document.getElementById('customerCount').textContent = customers.length;
        document.getElementById('orderCount').textContent = orders.length;
        
        // Calculate total cart items
        let totalCartItems = 0;
        for (const customer of customers) {
            try {
                const cart = await fetch(`${API_BASE}/ecommerce/cart/${customer.id}`).then(r => r.json());
                totalCartItems += cart.length;
            } catch (error) {
                // Cart might be empty
            }
        }
        document.getElementById('cartCount').textContent = totalCartItems;
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showNotification('Error loading dashboard data', 'error');
    }
}

// Product management functions
async function loadProducts() {
    try {
        const response = await fetch(`${API_BASE}/products`);
        const products = await response.json();
        
        const tbody = document.getElementById('productsTableBody');
        tbody.innerHTML = '';
        
        products.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>$${product.price}</td>
                <td>${product.quantity}</td>
                <td>${getProductType(product)}</td>
                <td>
                    <button onclick="editProduct(${product.id})" class="btn-secondary">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <button onclick="deleteProduct(${product.id})" class="btn-danger">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading products:', error);
        showNotification('Error loading products', 'error');
    }
}

function getProductType(product) {
    if (product.weightGrams && product.expiryDate) return 'Shippable & Expirable';
    if (product.weightGrams) return 'Shippable';
    if (product.expiryDate) return 'Expirable';
    return 'Simple';
}

function showProductForm() {
    document.getElementById('productForm').style.display = 'block';
    document.getElementById('productFormElement').reset();
    currentProductId = null;
}

function hideProductForm() {
    document.getElementById('productForm').style.display = 'none';
}

function toggleProductFields() {
    const productType = document.getElementById('productType').value;
    const weightGroup = document.getElementById('weightGroup');
    const expiryGroup = document.getElementById('expiryGroup');
    
    weightGroup.style.display = (productType === 'shippable' || productType === 'shippableExpirable') ? 'block' : 'none';
    expiryGroup.style.display = (productType === 'expirable' || productType === 'shippableExpirable') ? 'block' : 'none';
}

// Product form submission
document.getElementById('productFormElement').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const formData = {
        name: document.getElementById('productName').value,
        price: parseFloat(document.getElementById('productPrice').value),
        quantity: parseInt(document.getElementById('productQuantity').value),
        weightGrams: document.getElementById('productWeight').value ? parseFloat(document.getElementById('productWeight').value) : null,
        expiryDate: document.getElementById('productExpiry').value || null
    };
    
    try {
        const url = currentProductId ? `${API_BASE}/products/${currentProductId}` : `${API_BASE}/products`;
        const method = currentProductId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            showNotification(`Product ${currentProductId ? 'updated' : 'created'} successfully`, 'success');
            hideProductForm();
            loadProducts();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error saving product:', error);
        showNotification('Error saving product', 'error');
    }
});

async function editProduct(productId) {
    try {
        const response = await fetch(`${API_BASE}/products/${productId}`);
        const product = await response.json();
        
        document.getElementById('productName').value = product.name;
        document.getElementById('productPrice').value = product.price;
        document.getElementById('productQuantity').value = product.quantity;
        
        if (product.weightGrams) {
            document.getElementById('productWeight').value = product.weightGrams;
        }
        if (product.expiryDate) {
            document.getElementById('productExpiry').value = product.expiryDate;
        }
        
        // Set product type
        let productType = 'simple';
        if (product.weightGrams && product.expiryDate) productType = 'shippableExpirable';
        else if (product.weightGrams) productType = 'shippable';
        else if (product.expiryDate) productType = 'expirable';
        
        document.getElementById('productType').value = productType;
        toggleProductFields();
        
        currentProductId = productId;
        showProductForm();
    } catch (error) {
        console.error('Error loading product:', error);
        showNotification('Error loading product', 'error');
    }
}

async function deleteProduct(productId) {
    if (!confirm('Are you sure you want to delete this product?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/products/${productId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showNotification('Product deleted successfully', 'success');
            loadProducts();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error deleting product:', error);
        showNotification('Error deleting product', 'error');
    }
}

// Customer management functions
async function loadCustomers() {
    try {
        const response = await fetch(`${API_BASE}/customers`);
        const customers = await response.json();
        
        const tbody = document.getElementById('customersTableBody');
        tbody.innerHTML = '';
        
        customers.forEach(customer => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${customer.id}</td>
                <td>${customer.name}</td>
                <td>${customer.email}</td>
                <td>$${customer.balance}</td>
                <td>
                    <button onclick="editCustomer(${customer.id})" class="btn-secondary">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <button onclick="deleteCustomer(${customer.id})" class="btn-danger">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading customers:', error);
        showNotification('Error loading customers', 'error');
    }
}

function showCustomerForm() {
    document.getElementById('customerForm').style.display = 'block';
    document.getElementById('customerFormElement').reset();
    currentCustomerId = null;
}

function hideCustomerForm() {
    document.getElementById('customerForm').style.display = 'none';
}

// Customer form submission
document.getElementById('customerFormElement').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const formData = {
        name: document.getElementById('customerName').value,
        email: document.getElementById('customerEmail').value,
        balance: parseFloat(document.getElementById('customerBalance').value)
    };
    
    try {
        const url = currentCustomerId ? `${API_BASE}/customers/${currentCustomerId}` : `${API_BASE}/customers`;
        const method = currentCustomerId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            showNotification(`Customer ${currentCustomerId ? 'updated' : 'created'} successfully`, 'success');
            hideCustomerForm();
            loadCustomers();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error saving customer:', error);
        showNotification('Error saving customer', 'error');
    }
});

async function editCustomer(customerId) {
    try {
        const response = await fetch(`${API_BASE}/customers/${customerId}`);
        const customer = await response.json();
        
        document.getElementById('customerName').value = customer.name;
        document.getElementById('customerEmail').value = customer.email;
        document.getElementById('customerBalance').value = customer.balance;
        
        currentCustomerId = customerId;
        showCustomerForm();
    } catch (error) {
        console.error('Error loading customer:', error);
        showNotification('Error loading customer', 'error');
    }
}

async function deleteCustomer(customerId) {
    if (!confirm('Are you sure you want to delete this customer?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/customers/${customerId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showNotification('Customer deleted successfully', 'success');
            loadCustomers();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error deleting customer:', error);
        showNotification('Error deleting customer', 'error');
    }
}

// Cart management functions
async function loadCustomerCart() {
    const customerId = document.getElementById('cartCustomerId').value;
    if (!customerId) {
        showNotification('Please enter a customer ID', 'error');
        return;
    }
    
    currentCustomerId = customerId;
    
    try {
        const response = await fetch(`${API_BASE}/ecommerce/cart/${customerId}`);
        const cartItems = await response.json();
        
        const tbody = document.getElementById('cartTableBody');
        tbody.innerHTML = '';
        
        if (cartItems.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5">Cart is empty</td></tr>';
            return;
        }
        
        cartItems.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${item.productName}</td>
                <td>$${item.productPrice}</td>
                <td>${item.quantity}</td>
                <td>$${item.subtotal}</td>
                <td>
                    <button onclick="selectCartItem(${item.productId})" class="btn-secondary">
                        <i class="fas fa-edit"></i> Select
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading cart:', error);
        showNotification('Error loading cart', 'error');
    }
}

function selectCartItem(productId) {
    currentProductId = productId;
    document.getElementById('cartProductId').value = productId;
}

// Cart form submission
document.getElementById('cartFormElement').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    if (!currentCustomerId) {
        showNotification('Please select a customer first', 'error');
        return;
    }
    
    const formData = {
        customerId: parseInt(currentCustomerId),
        productId: parseInt(document.getElementById('cartProductId').value),
        quantity: parseInt(document.getElementById('cartQuantity').value)
    };
    
    try {
        const response = await fetch(`${API_BASE}/ecommerce/cart/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            showNotification('Item added to cart successfully', 'success');
            document.getElementById('cartFormElement').reset();
            loadCustomerCart();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        showNotification('Error adding to cart', 'error');
    }
});

async function updateCartItem() {
    if (!currentCustomerId || !currentProductId) {
        showNotification('Please select a customer and product first', 'error');
        return;
    }
    
    const quantity = parseInt(document.getElementById('cartQuantity').value);
    if (!quantity) {
        showNotification('Please enter a quantity', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/ecommerce/cart/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                customerId: parseInt(currentCustomerId),
                productId: currentProductId,
                quantity: quantity
            })
        });
        
        if (response.ok) {
            showNotification('Cart item updated successfully', 'success');
            loadCustomerCart();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error updating cart item:', error);
        showNotification('Error updating cart item', 'error');
    }
}

async function removeFromCart() {
    if (!currentCustomerId || !currentProductId) {
        showNotification('Please select a customer and product first', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/ecommerce/cart/remove?customerId=${currentCustomerId}&productId=${currentProductId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showNotification('Item removed from cart successfully', 'success');
            loadCustomerCart();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error removing from cart:', error);
        showNotification('Error removing from cart', 'error');
    }
}

async function clearCart() {
    if (!currentCustomerId) {
        showNotification('Please select a customer first', 'error');
        return;
    }
    
    if (!confirm('Are you sure you want to clear the entire cart?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/ecommerce/cart/clear/${currentCustomerId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showNotification('Cart cleared successfully', 'success');
            loadCustomerCart();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error clearing cart:', error);
        showNotification('Error clearing cart', 'error');
    }
}

async function checkout() {
    if (!currentCustomerId) {
        showNotification('Please select a customer first', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/ecommerce/checkout/${currentCustomerId}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            showNotification('Checkout completed successfully', 'success');
            loadCustomerCart();
            loadDashboardData();
        } else {
            const error = await response.text();
            showNotification(`Error: ${error}`, 'error');
        }
    } catch (error) {
        console.error('Error during checkout:', error);
        showNotification('Error during checkout', 'error');
    }
}

// Order management functions
async function loadOrders() {
    try {
        const response = await fetch(`${API_BASE}/orders`);
        const orders = await response.json();
        
        const tbody = document.getElementById('ordersTableBody');
        tbody.innerHTML = '';
        
        if (orders.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7">No orders found</td></tr>';
            return;
        }
        
        orders.forEach(order => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${order.id}</td>
                <td>${order.customer.name}</td>
                <td>$${order.subtotal}</td>
                <td>$${order.shippingFees}</td>
                <td>$${order.totalAmount}</td>
                <td><span class="status-${order.status.toLowerCase()}">${order.status}</span></td>
                <td>${new Date(order.orderDate).toLocaleDateString()}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading orders:', error);
        showNotification('Error loading orders', 'error');
    }
}

// Demo functions
async function runDemo() {
    const output = document.getElementById('demoOutput');
    output.innerHTML = 'Running demo...\n';
    
    try {
        const response = await fetch(`${API_BASE}/demo/run`, {
            method: 'POST'
        });
        
        if (response.ok) {
            output.innerHTML += 'Demo completed successfully!\nCheck the server console for detailed output.';
        } else {
            const error = await response.text();
            output.innerHTML += `Error: ${error}`;
        }
    } catch (error) {
        console.error('Error running demo:', error);
        output.innerHTML += `Error: ${error.message}`;
    }
}

async function runSpecificTest() {
    const output = document.getElementById('demoOutput');
    output.innerHTML = 'Running specific test...\n';
    
    try {
        const response = await fetch(`${API_BASE}/demo/test`, {
            method: 'GET'
        });
        
        if (response.ok) {
            output.innerHTML += 'Specific test completed successfully!\nCheck the server console for detailed output.';
        } else {
            const error = await response.text();
            output.innerHTML += `Error: ${error}`;
        }
    } catch (error) {
        console.error('Error running specific test:', error);
        output.innerHTML += `Error: ${error.message}`;
    }
}

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    // Load dashboard data on page load
    loadDashboardData();
    
    // Set up product type change handler
    document.getElementById('productType').addEventListener('change', toggleProductFields);
}); 
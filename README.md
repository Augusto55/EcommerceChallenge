# E-commerce Challenge API

A comprehensive RESTful API built with Spring Boot for managing an e-commerce platform. This application provides complete functionality for user management, product catalog, shopping cart, order processing, and administrative reporting.

## Technologies

### Core Framework
- **Spring Boot 3.4.5**
- **Java 21**
- **Maven**

### Database & Persistence
- **MySQL 8.0** - Primary database
- **Spring Data JPA** - Data access layer
- **Liquibase** - Database migration and version control

### Security & Authentication
- **Spring Security** - Security framework
- **OAuth2 Resource Server** - JWT-based authentication
- **BCrypt** - Password encryption
- **RSA Keys** - JWT token signing

### Testing
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integration testing

### Additional Technologies
- **Lombok** - Reduces boilerplate code
- **MapStruct 1.6.3** - Object mapping
- **Spring Boot Validation** - Input validation
- **Spring Boot Mail** - Email functionality
- **Spring Boot DevTools** - Development utilities
- **Docker Compose** - Container orchestration

## Features

### User Management
- User registration (Default and Admin users)
- JWT-based authentication and authorization
- Password reset functionality via email
- User profile management
- Role-based access control (Default users and Administrators)

### Product Management
- Product CRUD operations (Admin only)
- Product catalog browsing
- Product activation/deactivation
- Pagination support for product listings

### Shopping Cart
- Add products to cart
- Remove products from cart
- Update product quantities
- View cart contents
- User-specific cart management

### Order Processing
- Place orders from cart items
- Order history tracking
- Order management system

### Administrative Features
- Sales reporting
- Low stock alerts
- Most sold products analysis
- Top buyers identification
- User management

### Email Services
- Password reset emails
- Asynchronous email processing
- SMTP integration with Mailtrap

## 🛠Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **Docker & Docker Compose** (for database)
- **Git**

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd EcommerceChallenge
```

### 2. Database Setup

Start the MySQL database using Docker Compose:

```bash
docker-compose up -d
```

This will create a MySQL container with the following configuration:
- **Host**: localhost
- **Port**: 3308
- **Database**: ecommerce_challenge
- **Username**: user
- **Password**: root

### 3. Application Configuration

Copy the example configuration file and update it with your settings:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Update the following configurations in `application.properties`:

#### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3308/ecommerce_challenge
spring.datasource.username=user
spring.datasource.password=root
```

#### JWT Configuration
Ensure you have the RSA key files:
- `src/main/resources/app.key` (private key)
- `src/main/resources/app.pub` (public key)

#### Email Configuration (Optional)
Update the email settings for password reset functionality:
```properties
spring.mail.host=your-smtp-host
spring.mail.port=your-smtp-port
spring.mail.username=your-username
spring.mail.password=your-password
```

### 4. Build and Run

#### Using Maven Wrapper (Recommended)

```bash
# Build the application
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

#### Using Maven (if installed globally)

```bash
# Build the application
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Database Migration

The application uses Liquibase for database migrations. The database schema will be automatically created and updated when the application starts.

## API Documentation

The API documentation is available through Swagger UI when the application is running.

### Swagger UI
Access the interactive API documentation at: `http://localhost:8080/swagger-ui.html`

The Swagger interface provides:
- Complete endpoint documentation
- Request/response examples
- Interactive API testing
- Authentication support

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/auth/login` | User authentication | Public |
| POST | `/auth/recover-password` | Request password reset | Public |
| POST | `/auth/create-new-password` | Reset password with token | Public |

### User Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/users/default` | Create default user | Public |
| POST | `/users/admin` | Create admin user | Admin |
| GET | `/users/{userId}` | Get user by ID | Owner/Admin |
| GET | `/users` | List all users | Admin |
| PATCH | `/users/{userId}` | Update user | Owner/Admin |
| PATCH | `/users/admin/{userId}` | Admin update user | Admin |
| DELETE | `/users/{userId}` | Delete user | Admin |

### Product Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/products` | List all products | Public |
| GET | `/products/{productId}` | Get product by ID | Public |
| POST | `/products` | Create product | Admin |
| PUT | `/products/{productId}` | Update product | Admin |
| PATCH | `/products/{productId}/active` | Toggle product status | Admin |
| DELETE | `/products/{productId}` | Delete product | Admin |

### Shopping Cart

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/cart` | Get user's cart | Authenticated |
| POST | `/cart` | Add product to cart | Authenticated |
| PATCH | `/cart/{cartProductId}` | Update product quantity | Authenticated |
| DELETE | `/cart/{cartProductId}` | Remove product from cart | Authenticated |

### Orders

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/orders/placeOrder` | Place order from cart | Authenticated |

### Administrative Reports

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/admin/reports` | Sales report | Admin |
| GET | `/admin/reports/lowStock` | Low stock report | Admin |
| GET | `/admin/reports/mostSold` | Most sold products | Admin |
| GET | `/admin/reports/topBuyers` | Top buyers report | Admin |

## Authentication

The API uses JWT (JSON Web Tokens) for authentication. To access protected endpoints:

1. **Register a user** via `POST /users/default`
2. **Login** via `POST /auth/login` to receive a JWT token
3. **Include the token** in the Authorization header: `Bearer <your-jwt-token>`

### User Roles

- **Default**: Regular users who can manage their profile, cart, and place orders
- **Administrator**: Full access to all endpoints including user management, product management, and reports

## Testing

The project includes comprehensive unit tests for the service layer, organized by domain:

### Service Layer Tests
- **User Management Domain**: `UserServiceTest`, `AuthenticationServiceTest`
- **Product Management Domain**: `ProductServiceTest`
- **Shopping Domain**: `CartServiceTest`, `OrderServiceTest`
- **Reporting Domain**: `ReportServiceTest`
- **Communication Domain**: `EmailServiceTest`

### Running Tests

Run all tests:
```bash
./mvnw test
```

Run specific test class:
```bash
./mvnw test -Dtest=UserServiceTest
```

Run tests for a specific domain:
```bash
./mvnw test -Dtest="*ServiceTest"
```

##  Project Structure

```
src/
├── main/
│   ├── java/br/com/compass/ecommercechallenge/
│   │   ├── config/          # Security and application configuration
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── exception/      # Custom exceptions and handlers
│   │   ├── mapper/         # MapStruct mappers
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── util/           # Utility classes
│   └── resources/
│       ├── db/             # Liquibase migrations
│       ├── static/         # Static resources
│       ├── templates/      # Email templates
│       └── application.properties
└── test/                   # Test classes
```

## Docker Support

The project includes Docker Compose configuration for easy database setup. The MySQL container is configured with:

- **Image**: mysql:8.0
- **Container Name**: ecommerce
- **Port Mapping**: 3308:3306
- **Persistent Volume**: mysql-data

## License

This project is licensed under the MIT License - see the LICENSE file for details.

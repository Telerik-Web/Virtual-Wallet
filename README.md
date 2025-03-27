# FlowPay

**FlowPay** is an user-friendly digital wallet that allows users to manage their finances effortlessly. It uses a dummy API for card deposits to simulate adding money to the wallet. With features like money transfers, transaction history, and secure authentication, it ensures a smooth and reliable financial experience.

## Team Members
- **Lachezar Lazarov** - lu4kata
- **Ivan Georgiev** - Ivan200126y
  

## Features
- **Send & Receive Money** – Transfer funds using phone number, email, or username.
- **Transaction History** – View and filter past transactions.
- **Secure Data** – Passwords in the database are encrypted.
- **Card Management** – Manage multiple payment cards.
- **Email Verification** - Users must verify their emails after registration.
- **Currency Converter & Financial News** - The wallet provides currency conversion and financial news.

## Installation 

- **Clone the repository on your computer and install the dependencies in `build.gradle`**
- **Make sure you have JDK 17 and MariaDB installed**
- **Run the create.sql and insert-data.sql script located at `/Virtual Wallet/db`**
- **Access the application at this url - http://localhost:3308**

### Swagger
     API documentation at:
[http://localhost:8080/swagger-ui/index.html](http://localhost:3308/swagger-ui/index.html#/)

## Configuration
The current configuration has changed and will not work for you due to privacy reasons.
<br/>
That's why before running the app, in order to use some of the functionalities you must configure `application.properties` and create an account in Cloudinary.
```
Email verification and API key setup are not mandatory for the app to function,
but if you want the full experience, feel free to set them up.
```

Navigate to:
```css
src/main/resources/application.properties
```

And modify the following properties:
```properties
# Database Configuration
spring.datasource.url=jdbc:mariadb://localhost:3306/virtual_wallet
spring.datasource.username=root
spring.datasource.password={yourpassword}

# Email verification
spring.mail.username={sender_email_username}
spring.mail.password={sender_email_password}

# API Keys
cloudinary.cloud-name={your_cloud_name}
cloudinary.api-key={your_api_key}
cloudinary.api-secret={your_api_secret}
```
### Login Details
    The provided credentials are for testing purposes only!
    It's essential to inform you that the application encripts passwords, 
    ensuring they are not kept in their original form.

- Admin:  **username:** `johndoe`, **password:** `pass1`
- User:  **username:** `alicesmith`, **password:** `pass2`

## Technologies
- Java 17
- Spring Boot
- Spring Security
- Spring MVC
- Spring Data JPA
- Gradle
- Hibernate
- SQL
- MariaDB
- Thymeleaf
- HTML5
- CSS
- Javascript
- JUnit
- Mockito

# Functionalities

### Main Page
<img width="1894" alt="Screenshot 2025-03-26 120215" src="https://github.com/user-attachments/assets/4eb87552-4b9e-40e6-8dfa-7a3514af4ebb" />

### Home 
<img width="1910" alt="Screenshot 2025-03-26 121514" src="https://github.com/user-attachments/assets/e9fc6182-f4b8-42d7-8bc9-93ddc88347f2" />

### Account
<img width="1230" alt="account" src="https://github.com/user-attachments/assets/3ef456b4-b031-46de-9922-c6af66b5f114" />

### About
<img width="1891" alt="Screenshot 2025-03-26 120722" src="https://github.com/user-attachments/assets/39cece2f-58bd-4dd9-9aeb-e2bf74c59b6d" />

### Cards
<img width="1910" alt="Screenshot 2025-03-26 122349" src="https://github.com/user-attachments/assets/333b2bca-aa81-4300-b7eb-397d634e5c0f" />

### Transactions
<img width="1916" alt="Screenshot 2025-03-26 152921" src="https://github.com/user-attachments/assets/01c40177-070e-46c4-ab32-d44331240616" />

### Login
<img width="1918" alt="Screenshot 2025-03-26 153327" src="https://github.com/user-attachments/assets/90f41e42-fbd1-4047-b4a6-6560288f025d" />

### Register
<img width="1906" alt="Screenshot 2025-03-26 153347" src="https://github.com/user-attachments/assets/771f4887-03f3-4491-b25a-fe19de6602fb" />

### Admin Panel
<img width="1903" alt="Screenshot 2025-03-26 121738" src="https://github.com/user-attachments/assets/d3a3a1f1-6ed5-4953-b7ff-d0487028d4ae" />

## Database schema
<img width="731" alt="Screenshot 2025-03-26 122646" src="https://github.com/user-attachments/assets/2625413f-8df4-4788-937d-5e24ed27643f" />










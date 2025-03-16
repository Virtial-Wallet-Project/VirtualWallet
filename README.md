# Spring boot Project "Virtual Wallet"

## Overview

Virtual Wallet is a web application designed to help users efficiently manage their finances. It allows users to send and receive money securely within the system (user-to-user) and deposit funds from their bank accounts into their Virtual Wallet. The platform provides core functionalities essential for secure and seamless transactions, along with optional features to enhance user experience. You can easily register, than log in and add up to 3 credit cards to your account!


---
<br>

## Security and validation rules
1. User
- Username: Unique, 2–20 characters.
- Password: At least 8 characters, including a capital letter, a digit, and a special symbol.
- Email: Unique and valid.
- Phone Number: 10 digits and unique.
2. Credit/Debit Card Management
- Users must register at least one credit or debit card before adding funds to their Virtual Wallet.
- Card Number: 16 unique digits.
- Cardholder Name: 2–30 characters.
- Expiration Date and Check Number (3 digits).


---
<br>

## Public Access (Without Authentication)
Anonymous users can view information about Virtual Wallet and its features.
They can register and log in to gain full access.


---
<br>

## Private Access (Authenticated Users)
- Log in and log out.
- Update and delete your profiles.
- Register / Manage / Delete up to 3 credit cards.
- Deposit money to your account.
- Transfer money to other users via username, email, or phone number.
- View and filter your transaction history (by date, recipient, and transaction type).
- Sort transactions by amount and date, with pagination support.


---
<br>

## Transaction Process:

Transfers go through a confirmation step, displaying details before finalizing.
Users can edit or confirm transactions before processing.
Transactions rely on a dummy REST API for fund withdrawals from bank accounts.


---
<br>

## Admin Panel (Administrative Privileges Required)
- View a list of all users and search by phone number, username, or email.
- Block or unblock users (blocked users cannot make transactions).
- Make user admin or remove as one.
- Monitor all transactions across the platform with filters (by sender, recipient, date, and type).


---
<br>

## Technologies Used

- Java
- Spring Boot / MVC
- HQL
- Hibernate
- HTML / CSS
- JavaScript
- Thymeleaf
- RestAPI
- Swagger (API Documentation)

  
---
<br />

## Pictures representing the different pages of the project

Home Page
![image](https://github.com/user-attachments/assets/f9325e6d-def3-4816-996d-a6c971dd6ee9)
<br><br><br>

About Page
![image](https://github.com/user-attachments/assets/c66a5089-099e-4874-8293-c9bde17d256a)
<br><br><br>

Register page
![image](https://github.com/user-attachments/assets/daeecd9d-2ab7-453d-b9c5-69e8d977a8db)
<br><br><br>

Login Page
![image](https://github.com/user-attachments/assets/6f9065ba-22ce-4889-bc24-10d5951cb0f4)
<br><br><br>

Account Page
![image](https://github.com/user-attachments/assets/c8d6e9af-c44d-42bd-87ee-84db3ffd447a)
![image](https://github.com/user-attachments/assets/5ec02571-87e8-405c-a5b2-45a21f4e2b64)
<br><br><br>

Wallet Page
![image](https://github.com/user-attachments/assets/8f86c8a7-634a-4ccc-913c-b5d4ff8bb5a8)
<br><br><br>

Transfer Page
![image](https://github.com/user-attachments/assets/4526d4ae-e41d-4b15-a6d0-f5641a988751)
<br><br><br>

Confirm Transfer Page
![image](https://github.com/user-attachments/assets/4aa4f230-317c-4202-a92f-a10e5a0256c2)
<br><br><br>

Admin Dashboard
![image](https://github.com/user-attachments/assets/81a086e2-5baa-4262-b353-a5acf0c641f9)
<br><br><br>

Admin User Page
![image](https://github.com/user-attachments/assets/7c1c9245-83cc-446c-ab5b-6f01aa666cfa)
![image](https://github.com/user-attachments/assets/6f7aeac0-8a0d-48db-a5f4-099f2fe5cb74)
<br><br><br>

Admin Transaction Page
![image](https://github.com/user-attachments/assets/f77d02fd-49ef-4bd2-9b47-9c516c29fd11)
<br>


---
<br />

## Database Diagram

![image](https://github.com/user-attachments/assets/31a9a415-ecdb-4dad-81d6-5df1b3b5e216)


---
<br>

## Contributors
{{For further information, please feel free to contact me:}}

| Authors          | Emails                         | GitHub                           |
| -----------------| -------------------------------|--------------------------------  |
| Chavdar Tsvetkov | chavdartsvetkov.2000@gmail.com | https://github.com/Chavo02
| Viktor Angelov   | vikotor05@gmail.com            | https://github.com/Viktor030105  |


---
<br />

# Classroom ATM Simulation (Java Swing + JDBC + MySQL)

A complete, robust desktop ATM (Automated Teller Machine) simulation developed for **BCA / MCA Practical Examinations** and academic lab demonstrations.

> **Disclaimer**: This is an educational classroom simulation utilizing **simulated fake money** and an isolated demo database (`atm_db`). It does not connect to any real banking institution or external financial network.

---

## 📌 Project Architecture & Tech Stack

| Layer | Technology | Details |
|---|---|---|
| **Language** | Java 17+ | Standard LTS release using records, modern switch, and clean OOP design |
| **User Interface** | Java Swing | Responsive `CardLayout`, custom styling, system Look & Feel |
| **Concurrency** | `SwingWorker` | Non-blocking asynchronous database operations off the Event Dispatch Thread (EDT) |
| **Database** | MySQL 8.0+ | Relational schema with primary keys, foreign keys, and decimal monetary types |
| **Data Access** | JDBC | `PreparedStatement` only, transactional commit & rollback |
| **Driver & Build** | Apache Maven | Automatic dependency resolution of `com.mysql:mysql-connector-j` |
| **Currency** | `java.math.BigDecimal` | Arbitrary-precision fixed-point arithmetic formatted as `₹#,##0.00` |

---

## 📁 Project Structure

```text
ATMSimulation/
├── pom.xml                                   # Maven POM with Java 17 & MySQL Connector/J
├── database.sql                              # SQL schema, tables, foreign keys, and seed account
├── README.md                                 # Complete documentation, test checklist & viva Q&As
└── src/
    └── main/
        └── java/
            └── atm/
                ├── DBConnection.java         # JDBC connection manager with credentials placeholder
                ├── ATMService.java           # ACID transactional business logic & validation
                ├── ATMGUI.java               # Swing GUI (Login, Dashboard, JTable History, SwingWorkers)
                └── Main.java                 # Main entry point running on EDT
```

---

## ⚙️ Setup and Installation Guide

### Step 1: Install MySQL Server
If MySQL is not installed on your machine:
- Download and install **MySQL Community Server** (8.0 or 8.4 LTS) from [mysql.com](https://dev.mysql.com/downloads/installer/) or install **XAMPP / WampServer**.
- Ensure the MySQL service is started (Default port: `3306`).

### Step 2: Import the Database Schema
Open **MySQL Command Line Client** or **MySQL Workbench**, then execute:
```sql
SOURCE c:/Users/Premtodkar/Downloads/ATM/ATMSimulation/database.sql;
```
*Or execute from your terminal / PowerShell:*
```bash
mysql -u root -p < database.sql
```
This script will:
1. Create the database `atm_db`.
2. Create the `accounts` and `transactions` tables with foreign key constraints.
3. Seed the default demo account:
   - **Account Number**: `1001001`
   - **Account Holder**: `Demo Student`
   - **Demo PIN**: `1234`
   - **Opening Balance**: `₹5,000.00`

### Step 3: Configure Database Password
Open `src/main/java/atm/DBConnection.java` in any editor:
```java
// Locate line 28 in DBConnection.java:
private static final String DB_PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";
```
Replace `"YOUR_MYSQL_PASSWORD_HERE"` with your local MySQL `root` password (e.g., `"root"` or `"admin123"`). If your root account has no password, set it to `""`.

### Step 4: Build and Compile with Maven
From inside the `ATMSimulation` directory, execute:
```bash
mvn clean compile
```
Maven will automatically download the official MySQL JDBC driver (`mysql-connector-j`) and compile all source files into `target/classes`.

### Step 5: Run the Application
Launch the graphical application with Maven:
```bash
mvn exec:java
```
Or package a standalone JAR:
```bash
mvn clean package
java -cp "target/atm-simulation-1.0.0.jar;target/dependency/*" atm.Main
```

---

## 📋 Comprehensive Testing Checklist

Use this checklist during your lab practical or viva demonstration:

| Test Case | Steps to Execute | Expected Result | Pass / Fail |
|---|---|---|:---:|
| **1. Valid Login** | Enter `1001001` and PIN `1234`, click **Enter ATM / Login** | Login succeeds; transitions to Dashboard showing "Welcome, Demo Student!" and balance `₹5,000.00`. | [ ] |
| **2. Wrong PIN** | Enter `1001001` and PIN `9999` | Error dialog appears: *"Incorrect PIN entered. Please try again."* Login blocked. | [ ] |
| **3. Invalid Account** | Enter `9999999` and PIN `1234` | Error dialog appears: *"Account '9999999' not found. Please check your account number."* | [ ] |
| **4. Invalid PIN Format** | Enter `1001001` and PIN `12` or `ABCD` | Client-side validation blocks: *"PIN must be exactly 4 numeric digits (0-9)."* | [ ] |
| **5. Cash Deposit** | Click **2. Deposit Cash**, input `1500.50` | Dialog shows success; balance updates immediately to `₹6,500.50`. | [ ] |
| **6. Cash Withdrawal** | Click **3. Withdraw Cash**, input `2000.00` | Dialog confirms cash dispensation; balance updates to `₹4,500.50`. | [ ] |
| **7. Overdraw Attempt** | Click **3. Withdraw Cash**, input `50000.00` | Transaction declined: *"Insufficient balance! Available: ₹4,500.50, Requested: ₹50,000.00"*. No funds deducted. | [ ] |
| **8. Invalid Amounts** | Try depositing `-500` or `100.999` or `abc` | Input validation triggers: *"Amount must be greater than zero"* or *"cannot have more than 2 decimal places"*. | [ ] |
| **9. Transaction History** | Click **4. Mini Statement** | Opens modal `JTable` displaying all deposits and withdrawals with timestamp, type, and amount in ₹, newest first. | [ ] |
| **10. Restart Persistence** | Perform a deposit, close application, re-run `mvn exec:java`, and log in | Balance and transactions are persisted in MySQL across restarts. | [ ] |
| **11. Safe Logout** | Click **Logout** in top-right corner | Confirmation prompt appears; upon confirming, returns safely to Login Screen and resets session state. | [ ] |

---

## 🛠️ Common Errors & Fixes

### 1. `java.sql.SQLException: Access denied for user 'root'@'localhost'`
- **Cause**: The password specified in `DBConnection.DB_PASSWORD` does not match your MySQL root password.
- **Fix**: Open `src/main/java/atm/DBConnection.java`, verify the value of `DB_PASSWORD`, save, and recompile. If using MySQL without a password, set `DB_PASSWORD = ""`.

### 2. `Communications link failure: The last packet sent successfully to the server was 0 milliseconds ago`
- **Cause**: The MySQL Server service is stopped or listening on a port other than `3306`.
- **Fix**:
  - Open Windows Services (`services.msc`), locate **MySQL** or **MySQL80**, and click **Start**.
  - If using XAMPP, open the XAMPP Control Panel and click **Start** next to MySQL.

### 3. `ClassNotFoundException: com.mysql.cj.jdbc.Driver`
- **Cause**: The MySQL Connector/J library is missing from the classpath.
- **Fix**: This project uses Maven to automatically download the driver. Run `mvn clean compile` with an active internet connection so Maven can populate your local `~/.m2/repository`.

### 4. `java.sql.SQLSyntaxErrorException: Unknown database 'atm_db'`
- **Cause**: The SQL schema has not been imported into MySQL yet.
- **Fix**: Run the `database.sql` script via MySQL Workbench or command line (`SOURCE database.sql;`).

---

## 🎓 5 Viva Questions & In-Depth Answers (BCA / MCA)

### Q1. What is JDBC and why did you use `PreparedStatement` instead of `Statement`?
**Answer**:
JDBC (Java Database Connectivity) is a standard Java API that provides database-independent connectivity between Java applications and relational database management systems.

In this project, `PreparedStatement` was used exclusively over standard `Statement` for two critical reasons:
1. **Protection Against SQL Injection**: In a standard `Statement`, user inputs are concatenated directly into SQL strings, allowing malicious input (like `' OR '1'='1`) to alter query logic. `PreparedStatement` parameterizes variables using placeholders (`?`), ensuring user inputs are treated strictly as literal data, never executable SQL commands.
2. **Pre-compilation & Performance**: The database parses, compiles, and optimizes the execution plan of the query template once. Subsequent calls with different parameters execute significantly faster without re-compiling the query.

---

### Q2. How does the application ensure ACID transaction properties during cash withdrawal and deposit?
**Answer**:
A financial transaction must follow ACID properties (Atomicity, Consistency, Isolation, Durability):
- In JDBC, auto-commit is enabled by default (`autoCommit = true`), which treats every individual SQL statement as an independent transaction.
- In our `ATMService`, we invoke `connection.setAutoCommit(false)` before executing the balance update and transaction audit log insert.
- **Atomicity**: Both statements (updating `accounts.balance` and inserting into `transactions`) must succeed together. If either fails or an `InsufficientBalanceException` occurs, we call `connection.rollback()` in the catch block to revert all modifications.
- **Isolation**: When withdrawing money, we query `SELECT balance FROM accounts WHERE account_number = ? FOR UPDATE`. This creates a pessimistic row-level lock in MySQL, preventing race conditions (dirty reads or double spending) if simultaneous requests occur.
- Once both statements succeed, `connection.commit()` commits the data permanently (**Durability**).

---

### Q3. Why must we use `BigDecimal` instead of `double` or `float` for currency in financial software?
**Answer**:
`float` and `double` are primitive types that implement the **IEEE 754 floating-point standard**, which represents numbers as binary fractions (base 2). Because numbers like `0.1` or `0.01` cannot be represented with exact precision in binary, repeated calculations accumulate round-off errors (e.g., `1.00 - 0.90` yields `0.09999999999999998` instead of `0.10`).

In financial applications, rounding errors can lead to accounting discrepancies, reconciliation failures, and regulatory non-compliance. `java.math.BigDecimal` represents arbitrary-precision signed decimal numbers (base 10) with exact decimal scale control, ensuring accurate addition, subtraction, multiplication, and validation of rupees and paise.

---

### Q4. What is the Swing Event Dispatch Thread (EDT) and why did we implement `SwingWorker`?
**Answer**:
Java Swing is a **single-threaded GUI toolkit**. All UI rendering, painting, and user input events (such as button clicks or keypresses) are processed sequentially on a single thread called the **Event Dispatch Thread (EDT)**.

If a time-consuming database operation (such as connecting to MySQL or running a transaction) is executed directly on the EDT inside an `ActionListener`, the EDT will be blocked. This causes the GUI to freeze, become unresponsive, and display a "Not Responding" state in the operating system.

To prevent this:
- We extend `SwingWorker<T, V>`, where the network/JDBC call is executed in the background thread inside `doInBackground()`.
- Once finished, the `done()` callback automatically runs back on the EDT, allowing us to safely update Swing UI elements (such as updating labels and opening dialogs) without blocking user interactions.

---

### Q5. Why are PINs stored in plain text in this demo, and how should passwords/PINs be stored in real-world production systems?
**Answer**:
In this academic project, the demo PIN (`1234`) is stored in plain text strictly for classroom transparency, lab testing, and examiner grading simplicity.

In enterprise and real-world banking systems:
- Plaintext passwords or PINs **must never** be stored in a database or logged.
- Passwords must be hashed using a cryptographically secure, slow, salted one-way hashing algorithm such as **Argon2id**, **bcrypt**, or **PBKDF2** (with high iteration counts).
- A unique cryptographic **salt** (random bytes) must be generated per user to defend against precomputed rainbow table attacks.
- ATM hardware uses dedicated **Hardware Security Modules (HSMs)** to encrypt the entered PIN at the physical PIN pad (EPP) using Triple-DES or AES before transmission over secure encrypted networks (ISO 8583 protocol).

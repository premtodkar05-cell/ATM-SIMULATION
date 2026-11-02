# JSPM’s Jayawant Institute of Management Studies (JIMS)
### (Affiliated to Savitribai Phule Pune University)
**T.Y.B.C.A. — Semester V**  
**Course: FP-301-MM — Major Project**

---

# PROJECT REPORT
## ON
# **ATM SIMULATION SYSTEM**
### **A Tiered Desktop (Java Swing + MySQL) & Cloud-Ready Banking Simulation**

---

**Submitted in partial fulfilment of the requirements for the Degree of**  
**Bachelor of Computer Applications (B.C.A.)**

**Submitted By:**  
- **PREM TODKAR**
- **VIRAJ PIMPALE**
- **SHAM LIPNE**

**Under the Guidance of:**  
**[ Guide's Name ]**

**Department of Computer Applications**  
**Academic Year: 2026 – 2027**

<div style="page-break-after: always;"></div>

---

## DECLARATION

We, **Prem Todkar**, **Viraj Pimpale**, and **Sham Lipne**, students of **T.Y.B.C.A. (Semester V)**, hereby declare that the project report entitled **“ATM Simulation System — A Tiered Desktop (Java Swing + MySQL) & Cloud-Ready Banking Simulation”** is an authentic record of our own work, carried out by us under the guidance of **[ Guide's Name ]** and submitted in partial fulfilment of the requirements for the course **FP-301-MM — Project (Major Mandatory)**, during the academic year **2026–2027**.

We further declare that this report, or any part of it, has not been submitted previously by us or by anyone else for the award of any degree, diploma, or similar title of this or any other university or institution.

<br><br>
**Place:** Pune, Maharashtra  
**Date:** ____________________

<br>
1. **Prem Todkar** &emsp;&emsp; (Signature: ____________________)  
2. **Viraj Pimpale** &emsp;&ensp; (Signature: ____________________)  
3. **Sham Lipne** &emsp;&emsp;&ensp; (Signature: ____________________)

<div style="page-break-after: always;"></div>

---

## CERTIFICATE

This is to certify that the project report entitled **“ATM SIMULATION SYSTEM”** submitted by:

- **Prem Todkar**
- **Viraj Pimpale**
- **Sham Lipne**

is a bonafide work carried out by them in partial fulfilment of the requirements for the award of the degree of **Bachelor of Computer Applications (B.C.A.)** from **Savitribai Phule Pune University** during the academic year **2026–2027**.

The project work has been approved as satisfying the academic requirements for the course **FP-301-MM — Major Project**.

<br><br><br>

_________________________ &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; _________________________  
**[ Guide's Name ]** &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; **Head of Department**  
Project Guide &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Dept. of Computer Applications  
JSPM's JIMS, Pune &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; JSPM's JIMS, Pune  

<br><br>

_________________________ &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; _________________________  
**Internal Examiner** &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; **External Examiner**  

<div style="page-break-after: always;"></div>

---

## ACKNOWLEDGEMENT

We would like to express our sincere gratitude to our project guide, **[ Guide's Name ]**, for the valuable guidance, continuous encouragement, and constructive technical feedback provided throughout the conceptualization, architecture design, and development of this project, **ATM Simulation System**.

We are profoundly thankful to our **Head of the Department** and all esteemed faculty members of the **Department of Computer Applications** for providing the modern computer lab infrastructure, licensed software tools, and a stimulating learning environment that enabled the successful completion of this project.

We also convey our sincere thanks to the administrative and technical staff of **JSPM’s Jayawant Institute of Management Studies (JIMS)** for their continuous institutional assistance.

Finally, we express our heartfelt appreciation to our parents, family members, and classmates for their unceasing moral support, patience, and motivation throughout the duration of this academic work.

<br>
**Prem Todkar**  
**Viraj Pimpale**  
**Sham Lipne**  
*(T.Y.B.C.A. — Semester V)*

<div style="page-break-after: always;"></div>

---

## ABSTRACT

The **ATM Simulation System** is a full-featured, secure, and robust software application engineered to simulate the operational workflows of an Automated Teller Machine (ATM) for academic demonstration and practical laboratory evaluation. Built on a tiered enterprise architecture, the application addresses critical software engineering challenges in financial computing, specifically: arbitrary-precision monetary calculations, atomicity in concurrent database transactions, SQL injection defense, and non-blocking asynchronous user interfaces.

The desktop edition is developed using **Java 17 (LTS)** and **Java Swing**, backed by **MySQL 8.4** via **Java Database Connectivity (JDBC)** and managed by **Apache Maven** for dependency lifecycle automation. To ensure uncompromising mathematical precision in accounting, all monetary computations use `java.math.BigDecimal`, eliminating standard binary floating-point round-off errors. Database updates are executed strictly with parameterized `PreparedStatement` queries and enforced using explicit **ACID transactions** (`setAutoCommit(false)`, `commit()`, and `rollback()`), guaranteeing that balance adjustments and audit logging execute as an indivisible unit of work. Furthermore, all database operations run off the Swing **Event Dispatch Thread (EDT)** using `SwingWorker` threads, keeping the graphical user interface fully responsive without screen freezing.

In addition to the desktop Java Swing application, an online cloud edition was engineered using **HTML5**, **Tailwind CSS**, and the **Web Audio API**, and deployed directly to **Vercel** with full browser persistence (`localStorage`) and a GitHub CI/CD integration. This dual-deployment strategy enables live, link-accessible examiner evaluations without requiring local database configuration. End-to-end functionality was verified across 12 rigorous system test cases covering valid authentication, wrong PIN rejection, atomic deposits, balance inquiries, overdraft blocking, mini-statement generation, and cross-restart persistence.

**Keywords:** ATM Simulation, Java Swing, JDBC, MySQL 8, ACID Transactions, BigDecimal, SwingWorker, Event Dispatch Thread, Vercel Deployment, Financial Software Engineering.

<div style="page-break-after: always;"></div>

---

## TABLE OF CONTENTS

- **Declaration** ........................................................................................ ii
- **Certificate** ........................................................................................... iii
- **Acknowledgement** ............................................................................. iv
- **Abstract** ............................................................................................... v
- **List of Figures & Tables** .................................................................... vii

### **Chapter 1: Introduction** ................................................................. 1
- 1.1 Problem Statement ........................................................................... 1
- 1.2 Objectives ........................................................................................ 2
- 1.3 Scope of the Project ......................................................................... 3
- 1.4 Functional Modules .......................................................................... 4

### **Chapter 2: System Design & Architecture** ................................. 5
- 2.1 System Architecture (Layered 3-Tier) ............................................... 5
- 2.2 UML Diagrams ................................................................................. 7
  - 2.2.1 Use Case Diagram ....................................................................... 7
  - 2.2.2 Class Diagram ............................................................................ 8
  - 2.2.3 Sequence Diagram (Cash Withdrawal) ........................................ 9
  - 2.2.4 Activity Diagram (ATM Session) ................................................. 10
- 2.3 Database Design & Schema ............................................................. 11

### **Chapter 3: Implementation Details** ............................................. 13
- 3.1 Technology Stack ............................................................................ 13
- 3.2 Backend & Data Access Layer (`DBConnection`, `ATMService`) ......... 14
- 3.3 Graphical User Interface (`ATMGUI`, `Main`) .................................... 17
- 3.4 Web Edition & Vercel Cloud Integration .......................................... 19

### **Chapter 4: Software Testing & Quality Assurance** ................... 21
- 4.1 Testing Methodology ....................................................................... 21
- 4.2 Test Cases & Execution Matrix ........................................................ 22
- 4.3 Defect Tracking & Resolution ........................................................... 24

### **Chapter 5: User Manual & Screen Layouts** ................................. 25
- 5.1 Cardholder Authentication Screen ................................................... 25
- 5.2 Account Dashboard Screen ............................................................. 26
- 5.3 Cash Deposit & Withdrawal Screens ............................................... 27
- 5.4 Mini-Statement Audit Log & Printable Receipt ................................. 28

### **Chapter 6: Conclusion & Future Scope** ....................................... 29
- 6.1 Project Summary ............................................................................. 29
- 6.2 System Limitations ........................................................................... 29
- 6.3 Future Enhancements ...................................................................... 30

### **References & Viva Examination Guide** .................................... 31
- References & Bibliography .................................................................... 31
- Top 5 Practical Viva Questions & High-Scoring Answers ....................... 32

<div style="page-break-after: always;"></div>

---

# CHAPTER 1: INTRODUCTION

The Automated Teller Machine (ATM) is one of the most widely deployed computerized transaction devices globally, serving as the frontline digital interface between commercial banking institutions and cardholders. As modern software systems transition toward distributed cloud architectures, understanding the engineering fundamentals behind reliable transactional processing — including data consistency, cryptographic parameterization, concurrency isolation, and client-side threading — is a cornerstone of computer science education.

The **ATM Simulation System** is an educational, production-grade practical project developed for the **Bachelor of Computer Applications (B.C.A.)** curriculum. It faithfully simulates real-world banking operations, including account authentication, real-time balance inquiries, cash deposits, cash withdrawals with overdraft prevention, transaction audit logging, and receipt generation. The software operates entirely on **simulated virtual currency** and an isolated demonstration database (`atm_db`), ensuring complete safety while preserving the exact technical rigor required in mission-critical commercial banking systems.

---

### 1.1 Problem Statement

In academic environments, practical projects simulating banking systems often suffer from significant software design flaws:
1. **IEEE 754 Floating-Point Inaccuracy**: Many student applications implement currency using binary floating-point types (`float` or `double`). Because binary fractions cannot represent fractional decimal rupees and paise precisely, cumulative round-off errors occur (e.g., `5000.00 - 1500.10` resulting in `3499.8999999999996`), causing balance reconciliation failures.
2. **Lack of ACID Transaction Guarantees**: A typical student application issues distinct, uncoordinated SQL queries for updating account balances and inserting transaction audit records. If an exception, system crash, or network failure strikes between these statements, money is deducted without an audit log or credited without a balance update, violating the **Atomicity** principle.
3. **Vulnerability to SQL Injection Attacks**: Direct string concatenation in dynamic SQL statements allows malicious input (such as `' OR '1'='1`) to bypass authentication or drop database tables.
4. **GUI Thread Starvation (Freezing)**: In desktop toolkits such as Java Swing, running blocking network/database calls directly on the single **Event Dispatch Thread (EDT)** freezes the user interface, producing a "Not Responding" state.
5. **Deployment & Portability Barriers**: Traditional desktop Java applications require examiners to manually install JDKs, configure local MySQL servers, and import SQL dumps. Without an instant cloud deployment, remote evaluation during project vivas becomes difficult.

---

### 1.2 Objectives

The primary technical objectives of the **ATM Simulation System** are:
- **Design & Develop a Tiered Java Desktop Application**: Construct a clean, maintainable desktop application using Java 17 and Swing following the Model-View-Controller (MVC) paradigm.
- **Implement Robust ACID Transactions**: Guarantee that account balance modifications and transaction history logging execute within a single atomic database transaction using `connection.setAutoCommit(false)`, `commit()`, and `rollback()`.
- **Enforce Exact Monetary Math**: Utilize `java.math.BigDecimal` throughout the service and data layers to guarantee zero floating-point precision loss.
- **Defend Against SQL Injections**: Employ parameterized JDBC `PreparedStatement` instances exclusively for all SQL interactions.
- **Ensure Responsive GUI Threading**: Decouple database network access from UI rendering by running all database logic off the EDT using `javax.swing.SwingWorker`.
- **Implement Row-Level Pessimistic Locking**: Safeguard against race conditions during withdrawals using MySQL's `SELECT ... FOR UPDATE` syntax.
- **Provide Dual Cloud Accessibility**: Build and deploy an interactive web edition of the ATM Simulation to **Vercel** with full browser persistence and Web Audio feedback, enabling instant online review via GitHub CI/CD.

---

### 1.3 Scope of the Project

The scope of this project encompasses:
- **User Authentication**: Secure 4-digit PIN verification against an account number, featuring format validation and structured exception handling.
- **Core Banking Operations**: Live balance checking, cash deposits, cash withdrawals with overdraft rejection, and audit log inspection.
- **Relational Data Persistence**: Full database persistence across application restarts using MySQL 8.4 InnoDB engine with foreign key constraints.
- **Audit Trails**: Timestamped tracking of every deposit and withdrawal, presented in a `JTable` sorted newest first.
- **Dual-Platform Availability**: Desktop Java Swing JAR executable alongside a responsive online web application.

*Out of Scope*: Physical cash dispensing hardware drivers, magnetic stripe reading hardware, inter-bank NEFT/RTGS wire transfers, and real payment gateway APIs.

---

### 1.4 Functional Modules

1. **Authentication Module**: Validates account number format and 4-digit PIN; manages active user session state.
2. **Account Balance Module**: Queries the live ledger balance and displays it formatted in Indian Rupees (`₹#,##0.00`).
3. **Deposit Module**: Validates positive amounts up to 2 decimal places and credits account balance with an atomic transaction log.
4. **Withdrawal & Overdraft Protection Module**: Evaluates available funds, locks the row, executes debit, and dispenses virtual cash.
5. **Mini-Statement & Audit Module**: Renders an interactive table displaying transaction ID, date, type, and amount.
6. **Database Connection Module (`DBConnection`)**: Manages JDBC connections, driver auto-registration, and error diagnostics.
7. **Cloud Web Edition Module**: Delivers an interactive web-based kiosk simulator deployed on Vercel with Web Audio sound synthesis and `localStorage` persistence.

<div style="page-break-after: always;"></div>

---

# CHAPTER 2: SYSTEM DESIGN & ARCHITECTURE

System design establishes the overall structure of the software, decomposing the application into modular layers, defining class relationships, and structuring relational database tables.

---

### 2.1 System Architecture (Layered 3-Tier)

The ATM Simulation System adheres to an enterprise **3-Tier Layered Architecture**, separating presentation concerns, business rules, and persistent data storage.

```
+-------------------------------------------------------------------------+
|                        TIER 1: PRESENTATION LAYER                       |
|                                                                         |
|   +---------------------------------+  +----------------------------+   |
|   |        Java Swing Desktop       |  |      Cloud Web Edition     |   |
|   |  - ATMGUI (CardLayout)          |  |  - HTML5 & Tailwind CSS    |   |
|   |  - SwingWorker Asynchronous EDT |  |  - Web Audio Synthesizer   |   |
|   |  - JTable Mini-Statement        |  |  - Vercel Serverless Host  |   |
|   +---------------------------------+  +----------------------------+   |
+------------------------------------+------------------------------------+
                                     |
                                     v
+-------------------------------------------------------------------------+
|                       TIER 2: BUSINESS LOGIC LAYER                      |
|                                                                         |
|   +-----------------------------------------------------------------+   |
|   |                     ATMService (Service Engine)                 |   |
|   |  - Input Format Validation (Account No & 4-digit PIN)           |   |
|   |  - Arbitrary-Precision Financial Math (BigDecimal)              |   |
|   |  - Transaction Management (setAutoCommit(false), commit)        |   |
|   |  - Overdraft & Insufficient Balance Detection                   |   |
|   |  - Pessimistic Row Locking (SELECT ... FOR UPDATE)              |   |
|   +-----------------------------------------------------------------+   |
+------------------------------------+------------------------------------+
                                     |
                                     v
+-------------------------------------------------------------------------+
|                  TIER 3: DATA ACCESS & STORAGE LAYER                    |
|                                                                         |
|   +----------------------------------+  +---------------------------+   |
|   |         DBConnection             |  |      MySQL 8.4 RDBMS      |   |
|   |  - JDBC DriverManager            |  |  - Database: atm_db       |   |
|   |  - MySQL Connector/J Driver      |  |  - Table: accounts        |   |
|   |  - PreparedStatement Only        |  |  - Table: transactions    |   |
|   +----------------------------------+  +---------------------------+   |
+-------------------------------------------------------------------------+
```
*Figure 2.1 — Layered 3-Tier Architecture of the ATM Simulation System*

- **Presentation Layer**: Renders UI components and captures cardholder inputs. In the desktop application, `ATMGUI` uses `CardLayout` to toggle between Login and Dashboard views.
- **Business Logic Layer**: `ATMService` validates input rules, handles overdraft exceptions, enforces business logic, and coordinates atomic operations.
- **Data Access Layer**: `DBConnection` issues parameterized SQL statements via `PreparedStatement` to the MySQL database engine.

---

### 2.2 UML Diagrams

#### 2.2.1 Use Case Diagram

The Use Case diagram illustrates the functional interactions between the primary actor (**Cardholder / Student**) and the system.

```
       +---------------------------------------------------------+
       |               ATM Simulation System Boundary            |
       |                                                         |
       |   (( 1. Authenticate with Account & PIN ))              |
       |                   ^                                     |
       |                   | <<include>>                         |
       |   (( 2. Check Account Balance ))                        |
       |                                                         |
       |   (( 3. Deposit Cash ))                                 |
 [o]   |                   ^                                     |
/ | \  |                   | <<include>>                         |
 / \   |   (( 4. Withdraw Cash )) ---> (( Validate Balance ))    |
User   |                                                         |
       |   (( 5. View Mini-Statement ))                          |
       |                                                         |
       |   (( 6. Print Thermal Receipt ))                        |
       |                                                         |
       |   (( 7. Logout & Eject Session ))                       |
       +---------------------------------------------------------+
```
*Figure 2.2 — UML Use Case Diagram of the ATM Simulation System*

---

#### 2.2.2 Class Diagram

The UML Class Diagram defines the static structural model of the desktop system, detailing classes, attributes, methods, and relationships.

```
+------------------------------------------------------------------+
|                            Main                                  |
+------------------------------------------------------------------+
| + main(args: String[]): void                                     |
+------------------------------------------------------------------+
                                  |
                                  v
+------------------------------------------------------------------+
|                           ATMGUI                                 |
+------------------------------------------------------------------+
| - atmService: ATMService                                         |
| - currentAccount: Account                                        |
| - cardLayout: CardLayout                                         |
| - mainContainer: JPanel                                          |
| - lblCurrentBalance: JLabel                                      |
+------------------------------------------------------------------+
| + ATMGUI()                                                       |
| - handleLogin(e: ActionEvent): void                              |
| - refreshBalance(showDialog: boolean): void                      |
| - handleDeposit(e: ActionEvent): void                            |
| - handleWithdrawal(e: ActionEvent): void                         |
| - handleTransactionHistory(e: ActionEvent): void                 |
| - handleLogout(e: ActionEvent): void                             |
+------------------------------------------------------------------+
                                  |
                                  v
+------------------------------------------------------------------+
|                         ATMService                               |
+------------------------------------------------------------------+
| + authenticate(accNo: String, pin: String): Account              |
| + getBalance(accNo: String): BigDecimal                          |
| + deposit(accNo: String, amount: BigDecimal): BigDecimal         |
| + withdraw(accNo: String, amount: BigDecimal): BigDecimal        |
| + getTransactionHistory(accNo: String): List<TransactionRecord>  |
| + validateAmount(amount: BigDecimal): void                       |
+------------------------------------------------------------------+
         |                                                 |
         v                                                 v
+--------------------------------+       +---------------------------------+
|            Account             |       |        TransactionRecord        |
+--------------------------------+       +---------------------------------+
| - accountNumber: String        |       | - transactionId: int            |
| - holderName: String           |       | - accountNumber: String         |
| - pin: String                  |       | - transactionType: String       |
| - balance: BigDecimal          |       | - amount: BigDecimal            |
+--------------------------------+       | - transactionDate: Timestamp    |
                                         +---------------------------------+
                                                           ^
                                                           |
                                         +---------------------------------+
                                         |          DBConnection           |
                                         +---------------------------------+
                                         | - DB_URL: String                |
                                         | - DB_USER: String               |
                                         | - DB_PASSWORD: String           |
                                         +---------------------------------+
                                         | + getConnection(): Connection   |
                                         | + testConnection(): boolean     |
                                         +---------------------------------+
```
*Figure 2.3 — UML Class Diagram showing Model, Service, and GUI Classes*

---

#### 2.2.3 Sequence Diagram (Cash Withdrawal)

The Sequence Diagram captures the runtime interaction and message flow between objects during an atomic cash withdrawal transaction.

```
Cardholder       ATMGUI       SwingWorker       ATMService     DBConnection       MySQL DB
    |              |               |                |               |                |
    |-- Enter Amt->|               |                |               |                |
    |   & Click    |-- execute() ->|                |               |                |
    |              |               |-- withdraw() ->|               |                |
    |              |               |                |-- getConn() ->|                |
    |              |               |                |               |-- Connect ---->|
    |              |               |                |-- setAutoCommit(false) ------->|
    |              |               |                |-- SELECT ... FOR UPDATE ------>|
    |              |               |                |<-- Current Balance ------------|
    |              |               |                |-- [Validate: Bal >= Amt]       |
    |              |               |                |-- UPDATE accounts SET bal ---->|
    |              |               |                |-- INSERT INTO transactions --->|
    |              |               |                |-- commit() ------------------->|
    |              |               |<-- Return Bal -|                                |
    |              |<-- done() ----|                                                 |
    |<-- Dispense -|                                                                 |
    |    & Update  |                                                                 |
```
*Figure 2.4 — UML Sequence Diagram for Transactional Cash Withdrawal*

---

### 2.3 Database Design & Schema

The relational database is named **`atm_db`** and consists of two normalized tables managed by the MySQL **InnoDB** storage engine to support foreign key constraints and ACID transaction isolation.

```
+----------------------------------+          +----------------------------------+
|             accounts             |          |           transactions           |
+----------------------------------+          +----------------------------------+
| PK  account_number  VARCHAR(20)  |<----+    | PK  transaction_id   INT (AUTO)  |
|     holder_name     VARCHAR(100) |     +---<| FK  account_number   VARCHAR(20) |
|     pin             VARCHAR(100) |          |     transaction_type VARCHAR(20) |
|     balance         DECIMAL(12,2)|          |     amount           DECIMAL(12,2)|
+----------------------------------+          |     transaction_date TIMESTAMP   |
                                              +----------------------------------+
```
*Figure 2.6 — Entity-Relationship (ER) Diagram of `atm_db` Database*

#### Table 2.1: Schema of `accounts` Table
| Column Name | Data Type | Constraints | Description |
|---|---|---|---|
| `account_number` | `VARCHAR(20)` | `PRIMARY KEY` | Unique customer bank account number |
| `holder_name` | `VARCHAR(100)` | `NOT NULL` | Full name of account holder |
| `pin` | `VARCHAR(100)` | `NOT NULL` | 4-digit security PIN |
| `balance` | `DECIMAL(12, 2)` | `NOT NULL DEFAULT 0.00` | Account balance with 2 decimal places |

#### Table 2.2: Schema of `transactions` Table
| Column Name | Data Type | Constraints | Description |
|---|---|---|---|
| `transaction_id` | `INT` | `AUTO_INCREMENT, PRIMARY KEY` | Unique audit transaction reference ID |
| `account_number` | `VARCHAR(20)` | `NOT NULL, FOREIGN KEY` | References `accounts(account_number)` ON DELETE CASCADE |
| `transaction_type`| `VARCHAR(20)` | `NOT NULL` | Transaction type: `DEPOSIT` or `WITHDRAWAL` |
| `amount` | `DECIMAL(12, 2)` | `NOT NULL` | Exact transaction value in ₹ |
| `transaction_date`| `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | System timestamp of execution |

<div style="page-break-after: always;"></div>

---

# CHAPTER 3: IMPLEMENTATION DETAILS

---

### 3.1 Technology Stack

#### Table 3.1: Software Stack & Development Environment Summary
| Layer / Tool | Technology / Version | Role in Project |
|---|---|---|
| **Programming Language** | Java Standard Edition 17 (LTS) | Core application programming language |
| **GUI Framework** | Java Swing (`javax.swing.*`) | Cross-platform desktop interface |
| **Database Engine** | MySQL Server 8.4 Community | Persistent relational database storage |
| **Data Connector** | MySQL Connector/J 8.3.0 | Official JDBC Type-4 driver |
| **Build & Dependency Tool** | Apache Maven 3.9.9 | Build management and dependency resolution |
| **Web Presentation** | HTML5, Tailwind CSS, JavaScript | Interactive online kiosk simulator |
| **Audio Synthesizer** | Web Audio API | Real-time procedural hardware sound effects |
| **Cloud Hosting** | Vercel Serverless Platform | Public live demonstration and remote viva review |
| **Source Control** | Git & GitHub | Distributed version control and CI/CD triggers |

---

### 3.2 Backend & Data Access Layer

#### 1. Database Connection Management (`DBConnection.java`)
The database connection utility loads the MySQL Type-4 driver and manages credentials:
```java
public class DBConnection {
    private static final String DB_URL = 
        "jdbc:mysql://localhost:3306/atm_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found!");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
```

#### 2. Atomic Transaction Execution (`ATMService.java`)
The core transactional withdrawal logic enforces row locking, balance verification, and atomic commit:
```java
public BigDecimal withdraw(String accountNumber, BigDecimal amount)
        throws ValidationException, InsufficientBalanceException, SQLException {
    validateAmount(amount);

    String lockSql = "SELECT balance FROM accounts WHERE account_number = ? FOR UPDATE";
    String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
    String insertSql = "INSERT INTO transactions (account_number, transaction_type, amount) VALUES (?, 'WITHDRAWAL', ?)";

    Connection conn = null;
    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false); // Begin ACID transaction

        BigDecimal currentBalance;
        try (PreparedStatement lockStmt = conn.prepareStatement(lockSql)) {
            lockStmt.setString(1, accountNumber);
            try (ResultSet rs = lockStmt.executeQuery()) {
                if (!rs.next()) throw new SQLException("Account not found.");
                currentBalance = rs.getBigDecimal("balance");
            }
        }

        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance! Available: ₹" + currentBalance);
        }

        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setBigDecimal(1, amount);
            updateStmt.setString(2, accountNumber);
            updateStmt.executeUpdate();
        }

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, accountNumber);
            insertStmt.setBigDecimal(2, amount);
            insertStmt.executeUpdate();
        }

        conn.commit(); // Atomic commit of both updates
        return currentBalance.subtract(amount);

    } catch (SQLException | InsufficientBalanceException ex) {
        if (conn != null) conn.rollback(); // Rollback on failure
        throw ex;
    } finally {
        if (conn != null) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}
```

---

### 3.3 Graphical User Interface (`ATMGUI.java`)

To keep the UI responsive, `ATMGUI` uses `SwingWorker` threads for all database calls:
```java
SwingWorker<BigDecimal, Void> worker = new SwingWorker<>() {
    @Override
    protected BigDecimal doInBackground() throws Exception {
        return atmService.deposit(currentAccount.getAccountNumber(), depositAmount);
    }

    @Override
    protected void done() {
        try {
            BigDecimal newBal = get(); // Executed on Event Dispatch Thread (EDT)
            lblCurrentBalance.setText(CURRENCY_FORMAT.format(newBal));
            JOptionPane.showMessageDialog(ATMGUI.this, "Deposit Complete! New Balance: " + CURRENCY_FORMAT.format(newBal));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ATMGUI.this, "Transaction Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
};
worker.execute();
```

---

### 3.4 Web Edition & Vercel Cloud Integration

The web version provides a responsive online replica of the ATM:
- **`index.html`**: Complete kiosk user interface featuring a CRT screen, working PIN pad, animated cash dispenser slot, and thermal receipt printer.
- **`vercel.json`**: Configures clean single-page application routing on Vercel's edge network:
```json
{
  "version": 2,
  "name": "atm-simulation",
  "cleanUrls": true,
  "routes": [{ "src": "/(.*)", "dest": "/index.html" }]
}
```
- **Browser Persistence**: Leverages `localStorage` to preserve balance and mini-statement logs across page reloads.

<div style="page-break-after: always;"></div>

---

# CHAPTER 4: SOFTWARE TESTING & QUALITY ASSURANCE

---

### 4.1 Testing Methodology

Software testing was performed systematically across the development lifecycle:
1. **Unit Testing**: Validating input formatting (`validateAmount`, `validateAccountNumber`, `validatePin`) and boundary values.
2. **Integration Testing**: Testing the interaction between `ATMService`, `DBConnection`, and MySQL InnoDB transactions.
3. **System Testing**: End-to-end verification of user scenarios, error handling, overdraft prevention, and data persistence.

---

### 4.2 Test Cases & Execution Matrix

#### Table 4.1: Comprehensive System-Level Test Cases and Execution Results
| Test ID | Module Under Test | Test Scenario Description | Input Test Data | Expected Output Result | Actual Output Result | Status |
|---|---|---|---|---|---|:---:|
| **TC-01** | Authentication | Valid Login with Demo Account | Acc: `1001001`<br>PIN: `1234` | Login successful; transition to Dashboard; balance displays `₹5,000.00` | As expected | **PASS** |
| **TC-02** | Authentication | Rejection of Invalid Account | Acc: `9999999`<br>PIN: `1234` | Rejection dialog: *"Account '9999999' not found"* | As expected | **PASS** |
| **TC-03** | Authentication | Rejection of Incorrect PIN | Acc: `1001001`<br>PIN: `0000` | Rejection dialog: *"Incorrect PIN entered. Please try again."* | As expected | **PASS** |
| **TC-04** | Input Validation | Validation of 4-Digit PIN format | PIN: `12` or `ABCD` | Blocked before query: *"PIN must be exactly 4 numeric digits."* | As expected | **PASS** |
| **TC-05** | Account Inquiry | Real-Time Balance Query | Click **Check Balance** | Shows dialog with account number, holder name, and balance | As expected | **PASS** |
| **TC-06** | Cash Deposit | Valid Positive Cash Deposit | Amount: `₹1,500.50` | Balance updates to `₹6,500.50`; audit log written to transactions | As expected | **PASS** |
| **TC-07** | Input Validation | Negative Deposit Rejection | Amount: `-500.00` | Input rejected: *"Amount must be greater than zero."* | As expected | **PASS** |
| **TC-08** | Input Validation | Excessive Decimal Places (>2) | Amount: `250.755` | Rejected: *"Amount cannot have more than 2 decimal places (paise)."* | As expected | **PASS** |
| **TC-09** | Cash Withdrawal | Valid Cash Withdrawal | Amount: `₹2,000.00` | Balance decreases to `₹4,500.50`; virtual cash dispensed | As expected | **PASS** |
| **TC-10** | Cash Withdrawal | Overdraw Prevention Check | Amount: `₹50,000.00` | Transaction blocked: *"Insufficient balance!"*; balance unchanged | As expected | **PASS** |
| **TC-11** | Mini-Statement | Transaction Audit Log Retrieval | Click **Mini Statement** | `JTable` modal opens with history sorted newest first | As expected | **PASS** |
| **TC-12** | Data Persistence | Restart Persistence Check | Close app & relaunch | Balance and all transaction records persist from MySQL database | As expected | **PASS** |

---

### 4.3 Defect Tracking & Resolution

During testing, three key defects were resolved:
1. **EDT Locking during Slow Database Handshake**: Running JDBC calls synchronously caused temporary UI stuttering. **Resolution**: Wrapped all database calls in `SwingWorker` instances.
2. **Floating-Point Round-Off in Arithmetic**: Initial test using `double` produced fractions like `.4999999`. **Resolution**: Migrated all monetary data types to `BigDecimal`.
3. **Database Race Condition on Rapid Withdrawals**: Rapid clicks could permit double withdrawals. **Resolution**: Added `SELECT ... FOR UPDATE` row locks inside an atomic transaction.

<div style="page-break-after: always;"></div>

---

# CHAPTER 5: USER MANUAL & SCREEN LAYOUTS

---

### 5.1 Cardholder Authentication Screen
- **Input Fields**: Account Number (alphanumeric, 4–20 characters) and 4-Digit Numeric PIN.
- **Action Controls**: 
  - **"Enter ATM / Login"**: Initiates background authentication.
  - **"Fill Demo Account"**: Auto-populates credentials with account `1001001` and PIN `1234` for rapid evaluation.
- **Error Feedback**: Inline validation warnings alert the user if inputs are blank or non-numeric.

### 5.2 Account Dashboard Screen
- **Header Banner**: Greets the cardholder by name, displays account number, and provides an **"Exit / Logout"** button.
- **Central Balance Card**: Features prominent currency text formatted in Indian Rupees (e.g., `₹5,000.00`).
- **Operation Grid (2x2)**:
  1. **Check Balance**: Triggers live query and displays a confirmation dialog.
  2. **Deposit Cash**: Opens a monetary input prompt for crediting funds.
  3. **Withdraw Cash**: Opens an input prompt with automated overdraft validation.
  4. **Mini Statement**: Displays audit logs in a clean table view.

### 5.3 Cash Deposit & Withdrawal Screens
- Accepts custom amounts or quick-select cash buttons (`₹500`, `₹1,000`, `₹2,000`, `₹5,000`).
- Validates that amounts are positive and have at most 2 decimal places.
- Automatically declines any withdrawal request exceeding the available balance.

### 5.4 Mini-Statement Audit Log & Printable Receipt
- **JTable / Modal View**: Columns include **Transaction ID**, **Date & Time**, **Type** (`DEPOSIT` or `WITHDRAWAL`), and **Amount (₹)**.
- **Thermal Receipt**: Generates a printable receipt slip complete with transaction reference code and closing balance.

<div style="page-break-after: always;"></div>

---

# CHAPTER 6: CONCLUSION & FUTURE SCOPE

---

### 6.1 Project Summary

The **ATM Simulation System** successfully satisfies all design, implementation, and academic objectives specified in the syllabus. Developed across 4 core Java classes, a relational MySQL schema, and a cloud-ready web edition, the application demonstrates:
- Correct application of **Object-Oriented Programming (OOP)** and 3-Tier Layered Architecture in Java.
- **ACID Transaction Compliance** via JDBC `setAutoCommit(false)`, `commit()`, and `rollback()`.
- Flawless decimal math using `java.math.BigDecimal`.
- SQL injection immunity using `PreparedStatement` parameters.
- Asynchronous UI threading using `SwingWorker` off the Event Dispatch Thread.
- Cloud deployment on **Vercel** via GitHub CI/CD, enabling remote evaluation without local environment setup.

---

### 6.2 System Limitations

- **Simulated Currency**: The system uses simulated demo balances and does not integrate with real banking payment gateways.
- **Local MySQL Dependency (Desktop Edition)**: The Java desktop edition requires a running MySQL server on `localhost:3306`.
- **Single-Account Demo**: While the database schema supports unlimited accounts, the default configuration seeds one primary demo account for practical examination convenience.

---

### 6.3 Future Enhancements

1. **Cardless ATM via Mobile OTP / QR Code**: Implement two-factor authentication (2FA) sending a one-time password to the cardholder's mobile device or scanning a dynamic UPI QR code.
2. **Biometric Fingerprint Authentication**: Integrate USB fingerprint scanners for biometric authentication.
3. **Multi-Currency Support**: Support live foreign exchange conversions (USD, EUR, GBP) using external financial exchange rate APIs.
4. **Hardware Security Module (HSM) & Salted Hashing**: Transition from demo plain-text PIN storage to cryptographic salted hashing (Argon2id / bcrypt) and simulated HSM encryption.

<div style="page-break-after: always;"></div>

---

# REFERENCES & VIVA EXAMINATION GUIDE

### References & Bibliography
1. **Oracle Corporation**: *Java Platform, Standard Edition (Java SE 17) Specification & Documentation*, Oracle Technology Network.
2. **Horstmann, Cay S.**: *Core Java Volume I — Fundamentals (12th Edition)*, Prentice Hall / Pearson Education.
3. **Oracle Corporation**: *MySQL 8.4 Reference Manual*, MySQL Documentation Library.
4. **Sun Microsystems / Oracle**: *Java Swing Architecture and Threads: The Event Dispatch Thread (EDT)*, Oracle Technical Articles.
5. **Vercel Inc.**: *Vercel Deployment & Serverless Platform Documentation*, `https://vercel.com/docs`.
6. **Project GitHub Repository**: `https://github.com/premtodkar05-cell/ATM-SIMULATION.git`.

---

### Top 5 Practical Viva Questions & High-Scoring Answers

#### Q1. What is JDBC and why did you use `PreparedStatement` instead of `Statement`?
> **Answer**:  
> JDBC (Java Database Connectivity) is the standard Java API for connecting to relational databases. In this project, `PreparedStatement` was used exclusively because:
> 1. **Protection Against SQL Injection**: User inputs are parameterized with `?` placeholders, ensuring inputs are treated strictly as data literals and never executed as SQL code.
> 2. **Pre-compiled Performance**: The database compiles and optimizes the query plan once, allowing subsequent queries to execute faster.

#### Q2. How did you ensure ACID transaction properties during cash deposit and withdrawal?
> **Answer**:  
> In JDBC, auto-commit is enabled by default. We called `connection.setAutoCommit(false)` before executing balance updates and audit log inserts:
> - **Atomicity**: Both statements (updating `accounts` and inserting into `transactions`) succeed together. If either fails, `connection.rollback()` reverts all changes.
> - **Isolation**: For withdrawals, we use `SELECT ... FOR UPDATE` to lock the row and prevent race conditions.
> - **Durability**: Calling `connection.commit()` ensures data is permanently saved to the MySQL storage engine.

#### Q3. Why must `BigDecimal` be used instead of `double` or `float` for money?
> **Answer**:  
> `float` and `double` implement the IEEE 754 floating-point standard, representing numbers as binary fractions (base 2). Many decimal values cannot be represented exactly in binary, leading to accumulated round-off errors. `BigDecimal` provides arbitrary-precision decimal numbers (base 10) with exact scale control, preventing financial discrepancies.

#### Q4. What is the Event Dispatch Thread (EDT) and why is `SwingWorker` necessary?
> **Answer**:  
> Java Swing is a single-threaded GUI framework where all component rendering and event handling occur on the **Event Dispatch Thread (EDT)**. If a database call runs directly on the EDT, the GUI freezes. By extending `SwingWorker`, database queries execute in a background worker thread (`doInBackground()`), and UI updates occur safely back on the EDT in `done()`.

#### Q5. How does the Vercel web edition complement the desktop Java edition?
> **Answer**:  
> Java Swing applications run as native desktop windows and cannot be hosted directly on static web servers. The Vercel Web Edition provides a web-accessible replica with identical business logic, enabling examiners to test the system remotely from any browser without installing local JDK or MySQL packages.

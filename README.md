# 🚌 Bus Ticket Manager

**Bus Ticket Manager** is a desktop application designed to manage bus ticket booking and customer information. The application allows users to manage buses, locations, seat availability, customer details, and payment processing for ticket reservations. The software also provides the functionality to generate receipts and visual charts for business analysis.

## 🚀 Features

- **Bus Management**: List available buses and seat availability based on user selection.
- **Location Management**: Select bus locations dynamically for ticket booking.
- **Customer Management**: Add, search, and display customer information.
- **Ticket Booking**: Manage ticket reservations with seat number availability and payment processing.
- **Payment**: Record customer payments and generate receipts on successful bookings.
- **Charts and Analytics**: View sales data in graphical format using charts.
- **PDF Receipt Generation**: Generate and preview PDF receipts for customers after successful payment.

## 🛠️ Technologies

- **Java**: Core programming language.
- **JavaFX**: For building the user interface.
- **MySQL**: Database to store bus, customer, and transaction data.
- **iText**: To generate PDF receipts.
- **JDBC**: For database connectivity.

## 📋 Prerequisites

To run this project, you need to have the following installed on your system:

- Java 8 or later
- MySQL database server
- JavaFX SDK (for the user interface)
- iText library (for PDF generation)

### Libraries Used

- `mysql-connector-java` (For database connection)
- `iText` (For generating PDF documents)
- `JavaFX` (For creating the UI and handling events)

## 🔧 Setup and Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/bus-ticket-manager.git
   cd bus-ticket-manager
   ```

2. **Configure the database**:
   - Install and set up MySQL server.
   - Create a database using the following SQL:
     ```sql
     CREATE DATABASE bus_ticket_manager;
     ```
   - Create the tables using the provided SQL scripts (`/db-scripts/schema.sql`).

3. **Set up the `DatabaseConnection`**:
   - In the `DatabaseConnection.java` file, configure your MySQL connection by providing the correct database URL, username, and password.

4. **Run the application**:
   - Compile and run the project using your preferred IDE (Eclipse, IntelliJ, etc.).
   - Make sure to include all required libraries in your project’s classpath.

## 🚦 Usage

1. **Bus and Location Setup**: Add and manage available buses and their locations.
2. **Ticket Booking**: Select a bus, location, and the number of tickets to book.
3. **Customer Details**: Fill in customer information during the booking process.
4. **Payment and Receipt Generation**: After payment, a receipt will be generated with an option to save it as a PDF.

## 📊 Analytics Dashboard

The application also provides a dashboard feature that shows total sales data over time. This is done using a line chart that aggregates and displays the total sales for each booking date.

## 📄 Example SQL Table Definitions

```sql
CREATE TABLE customer (
   customerID INT PRIMARY KEY AUTO_INCREMENT,
   firstName VARCHAR(255),
   lastName VARCHAR(255),
   gender VARCHAR(50),
   phoneNo VARCHAR(50),
   customerDate DATE,
   busID INT,
   location VARCHAR(255),
   ticketNo INT,
   total DOUBLE
);

CREATE TABLE buses (
   busID INT PRIMARY KEY AUTO_INCREMENT,
   busName VARCHAR(255),
   busSeats INT,
   busLocation VARCHAR(255),
   busStatus VARCHAR(50)
);
```

## 🧑‍💻 Contribution

Contributions are welcome! If you'd like to improve this project, please fork the repository and submit a pull request. Alternatively, you can open an issue for feedback or ideas.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

If you have any questions or need assistance, feel free to contact me:

- **Email**: manushadananjaya999@gmail.com
- **GitHub**: [manushadananjaya](https://github.com/manushadananjaya)

# Meetime Technical Test 🚀

This project is a submission for the Meetime technical test, developed using the **Play Framework** in **Java**. It demonstrates my ability to learn new technologies, apply best practices, and deliver a working solution within the specified requirements. 🌟

---

## 📝 Project Overview

The project includes:

- **Implementation of OAuth 2.0 Authorization Code Flow**: Authentication is handled securely using environment variables for sensitive data and tokens cached appropriately.
- **Contact Management**: Integration with HubSpot's CRM API for creating and listing contacts while adhering to rate-limit policies.
- **Webhook Handling**: Listens to and processes `contact.creation` events with proper hash validation.

---

## 🛠 Technologies & Libraries

- **Play Framework**: Framework used to develop the application.
- **Java WS**: For making requests to HubSpot's API.
- **Play JSON**: To handle JSON responses from the API.
- **Caffeine**: Used for caching tokens and managing user-specific rate-limiting.

---

## 🔑 Security Practices

- Environment variables are stored in the `application.conf` file and are excluded from the repository.
- The application respects HubSpot's rate limit of 110 requests per 10 seconds per connected user.
- Tokens (access and refresh) are cached with expiration times and refreshed automatically when needed.
- Webhook validation is implemented to verify the hash (v1) provided by HubSpot.

---

## 👨‍💻 Code Structure

Following the **MVC pattern**:

- **Models**: Represent the application data.
- **Views**: Simple HTML for route management.
- **Controllers**: Handle logic and API calls.
- Additional structure includes folders for annotations, DTOs, and actions.

---

## 📚 How to Run the Application

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/Pentodo/meetime-technical-test.git
   ```
2. Configure your environment variables in the `application.conf` file:
   ```bash
   auth {
       clientId = "your-app-client-id"
       clientSecret = "your-app-client-secret"
       redirectUri = "http://localhost:9000/callback"
       scope = "oauth,crm.objects.contacts.read,crm.objects.contacts.write"
   }
   ```
3. Run the application:
   ```bash
   sbt run
   ```
4. Open your browser and navigate to `http://localhost:9000`.

---

## 📈 Improvements and Future Recommendations

While the project meets all the technical requirements, here are some suggested enhancements:

- **Database Integration**: Store webhook `contact.creation` objects for better scalability.
- **Testing**: Add unit and integration tests to ensure robustness.
- **UI Improvements**: Enhance the views with better HTML/CSS design.

---

Feel free to reach out for questions or feedback! 💬

# Ajax Ticket Alerts

## 🛠️ Tech Stack

This project consists of **three main components**:

### 🔍 Web Scraper ([ajax-scraper](https://github.com/michaelvbend/ajax-scraper))
- **Built with:** `Python`, `Selenium`
- **Functionality:**
    - Uses a **headless browser** to navigate the Ajax resale platform.
    - Scrapes **ticket availability data** for upcoming matches.
    - Sends a **PUT request** to the backend when tickets are found.

### 🖥️ Backend ([ajax-tickets-fe](https://github.com/michaelvbend/ajax-tickets-fe))
- **Built with:** `React`
- **Functionality:**
    - Provides interface with two components, match list and subscription form.

### ✉️ Notifications
- **Uses:** `Twilio SendGrid`


---

How to start the Ajax Alerting application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/ajax-ticket-alerts-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

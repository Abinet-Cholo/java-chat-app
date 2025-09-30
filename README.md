# 📡 Java Chat Server with Authentication & Persistence

A **multi-threaded, real-time chat server** built in Java.  
This project demonstrates key backend engineering concepts: **concurrency, design patterns, dependency injection, persistence, and testing**.  

---

## ✨ Features

- **Multi-client support**: Handle many clients at once with threads.  
- **Authentication & Signup**: Users can sign up once, then log in on future sessions. Credentials are persisted in SQLite.  
- **Persistence**: Messages and users are saved to a local `chat.db` SQLite database. Data survives across restarts.  
- **Design Patterns**:
  - **Observer**: EventBus broadcasts messages to all connected clients.  
  - **Factory**: MessageFactory creates different message types (Text, Image, File).  
  - **Singleton**: ConnectionManager ensures only one active client manager exists.  
  - **Dependency Injection**: Google Guice manages wiring between components.  
- **Testing**: JUnit 5 tests for database logic and server-client interactions.  
- **Extensible**: Built to be extended with commands (`/history`, private chat, channels, etc.).  

---

## 🛠 Tech Stack

- **Language**: Java 21  
- **Build Tool**: Maven  
- **Database**: SQLite (via JDBC)  
- **Dependency Injection**: Google Guice  
- **Testing**: JUnit 5  

---

## 📂 Project Structure

```
chat-app/
 ├── pom.xml                  # Maven config (Guice, SQLite, JUnit)
 ├── .gitignore               # Git ignore file at project root
 ├── chat.db                  # SQLite database (created at runtime, ignored by Git)
 └── src/
     ├── main/java/
     │   ├── ChatServer.java  # Server entrypoint
     │   ├── ChatClient.java  # Client entrypoint
     │   ├── core/            # EventBus, ConnectionManager, Message types
     │   ├── di/              # Guice module
     │   ├── db/              # Database handler
     │   └── model/           # User model
     └── test/java/
         ├── DatabaseTest.java   # Unit tests for DB layer
         └── ChatServerTest.java # Integration test for server & client
```

---

## 🚀 Getting Started

### 1. Clone the repo
```bash
git clone https://github.com/YOUR-USERNAME/java-chat-app.git
cd chat-app
```

### 2. Install dependencies
Make sure [Maven](https://maven.apache.org/install.html) is installed. Then:
```bash
mvn clean install
```

### 3. Run the server
```bash
mvn exec:java -Dexec.mainClass="ChatServer"
```

You should see:
```
Chat server started on port 1234
```

### 4. Run clients
In a new terminal:
```bash
mvn exec:java -Dexec.mainClass="ChatClient"
```

Multiple clients can connect simultaneously.

---

## 💻 Usage

### Authentication
1. On first run, type `signup` to create a new account.  
2. On future runs, type `login` and enter your credentials.  

Example:
```
Connected to chat server
Type 'login' or 'signup':
signup
Enter new username:
alice
Enter new password:
mypassword
✅ Signup successful! You can now log in.

Type 'login' or 'signup':
login
Enter username:
alice
Enter password:
mypassword
✅ Auth success! Welcome alice
```

### Messaging
- Type any message → it’s broadcast to all connected clients.  
- Use `IMG:filename.png` → sends as `[IMAGE] filename.png`.  
- Use `FILE:document.pdf` → sends as `[FILE] document.pdf`.  
- Type `/quit` to leave the chat.  

### Example Run
```
========================================
     java-chat-app (multi-threaded)
========================================
Connected to chat server
Type 'login' or 'signup': signup
Enter new username: alice
Enter new password: mypassword
✅ Signup successful! You can now log in.

Type 'login' or 'signup': login
Enter username: alice
Enter password: mypassword
✅ Auth success! Welcome alice
[INFO] alice has joined the chat
Hello everyone!
[INFO] alice: Hello everyone!
------------- SUMMARY -------------
Active clients : 2
Messages sent  : 5
Database file  : chat.db
-----------------------------------
```

---

## ✅ Running Tests

Run all tests:
```bash
mvn test
```

- **DatabaseTest** → tests signup, login, duplicate users, and saving messages.  
- **ChatServerTest** → starts a server in background, signs up/logs in a client, sends a message, verifies broadcast.  

Example result:
```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🔮 Future Improvements

- `/history` command: fetch last 10 messages from DB.  
- Private messaging between users.  
- Multi-room / channels (like Slack/Discord).  
- JWT-based authentication.  
- REST API for managing users/messages.  

---

This project is a **lightweight but extensible chat backend** built with Java, demonstrating modern design and persistence practices.

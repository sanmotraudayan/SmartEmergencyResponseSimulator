# Smart Emergency Response Simulator

A Java-based Object-Oriented Programming (OOP) mini-project that simulates emergency registration, priority-based dispatch, ambulance allocation, hospital bed management, and emergency record keeping.

## 1. Project Overview

The **Smart Emergency Response Simulator** is a console-based Java application designed to demonstrate how emergency cases can be managed systematically using object-oriented programming principles.

The application allows users to register patients, create emergencies, prioritize cases according to severity, dispatch available resources, complete emergencies, and maintain records of dispatch operations.

It also demonstrates **file handling, object serialization, exception handling, and multithreading** through persistent application data and a background emergency monitoring system.

> **Note:** This is an educational simulation. It does not connect to real emergency services, hospitals, ambulances, or live location systems.

## 2. Objectives

- Develop a practical application using Java OOP principles.
- Manage patient and emergency records.
- Prioritize emergencies based on severity.
- Simulate ambulance dispatch and hospital bed allocation.
- Maintain dispatch history and generate system reports.
- Preserve application data using object serialization.
- Demonstrate custom exception handling and multithreading.

## 3. Features

- **Patient Management:** Register patients with their name, age, and medical condition.
- **Emergency Registration:** Create critical, moderate, and normal emergency cases.
- **Priority-Based Dispatch:** Process pending emergencies according to their priority.
- **Ambulance Management:** Track ambulance availability and dispatch operations.
- **Hospital Management:** Maintain hospital information and available bed capacity.
- **Emergency Completion:** Complete dispatched emergencies and release resources.
- **Dispatch History:** View records of previous dispatch operations.
- **System Reports:** Generate reports containing application information.
- **Data Persistence:** Save and restore application state using Java serialization.
- **Background Monitoring:** Monitor newly registered emergencies using a separate thread.
- **Input Validation:** Handle invalid inputs and inappropriate operations.
- **Custom Exceptions:** Handle attempts to complete emergencies that cannot be found.

## 4. Technology Stack

| Component | Technology |
|---|---|
| Programming Language | Java |
| Programming Paradigm | Object-Oriented Programming |
| User Interface | Console-based menu |
| Data Persistence | Java Object Serialization |
| Report Storage | Text files |
| Concurrency | Java Threads |
| Development Environment | Visual Studio Code |
| Version Control | Git and GitHub |

## 5. Object-Oriented Programming Concepts

The project demonstrates the following OOP concepts:

### Abstraction
The abstract `Person` and `Emergency` classes define common attributes and behavior while allowing subclasses to provide specific implementations.

### Encapsulation
Classes maintain their data through fields and controlled methods, helping protect object state and enforce validation.

### Inheritance
`Patient` extends `Person`, while `CriticalEmergency`, `ModerateEmergency`, and `NormalEmergency` extend `Emergency`.

### Polymorphism
Different emergency subclasses provide their own priority and type behavior. The application can process emergency objects through the common `Emergency` abstraction.

### Method Overriding
Subclasses implement inherited abstract methods, including emergency-specific priority, type, and detail-display behavior.

### Exception Handling
The application uses `try-catch` blocks for invalid input and file operations. The custom `EmergencyNotFoundException` represents an emergency lookup failure during completion.

### Multithreading
`EmergencyMonitor` implements `Runnable` and runs in a separate thread to monitor emergency registration activity.

### Serialization
The application saves its object state to a serialized file and restores it when the application starts again.

## 6. Project Structure

```text
SmartEmergencyResponseSimulator/
│
├── Main.java
├── Person.java
├── Patient.java
├── Emergency.java
├── CriticalEmergency.java
├── ModerateEmergency.java
├── NormalEmergency.java
├── Ambulance.java
├── Hospital.java
├── DispatchRecord.java
├── EmergencyManager.java
├── EmergencyMonitor.java
├── EmergencyNotFoundException.java
├── FileManager.java
├── README.md
├── .gitignore
│
└── data/
    ├── simulator_data.ser
    ├── system_report.txt
    └── dispatch_history.txt
```

The `data/` directory is generated locally by the application and should generally be excluded from version control.

### Class Responsibilities

| Class | Responsibility |
|---|---|
| `Main` | Provides the menu and coordinates user interactions. |
| `Person` | Abstract base class for people in the system. |
| `Patient` | Stores patient information. |
| `Emergency` | Abstract base class for emergency cases. |
| `CriticalEmergency` | Represents critical-priority emergencies. |
| `ModerateEmergency` | Represents moderate-priority emergencies. |
| `NormalEmergency` | Represents normal-priority emergencies. |
| `Ambulance` | Tracks ambulance information and availability. |
| `Hospital` | Manages hospital information and bed capacity. |
| `DispatchRecord` | Stores dispatch-related information and status. |
| `EmergencyManager` | Coordinates patients, emergencies, resources, and history. |
| `EmergencyMonitor` | Monitors emergency registration activity in the background. |
| `EmergencyNotFoundException` | Represents an emergency lookup failure. |
| `FileManager` | Saves and loads application data and generates text reports. |

## 7. Emergency Priority System

Emergencies are assigned priorities according to their severity.

| Emergency Type | Priority Value | Relative Priority |
|---|---:|---|
| Critical | 3 | Highest |
| Moderate | 2 | Medium |
| Normal | 1 | Lowest |

The manager selects the highest-priority eligible pending emergency for dispatch. Actual dispatch success also depends on the availability of suitable resources and the application's allocation rules.

## 8. Application Workflow

1. Start the application.
2. Load previously saved application data, if available.
3. Initialize default ambulances and hospitals for a new session.
4. Display the main menu.
5. Register patients and their emergencies.
6. View patient, emergency, ambulance, and hospital information.
7. Dispatch the next eligible emergency.
8. Complete a dispatched emergency.
9. Review dispatch history and generate reports.
10. Save application data and reports.
11. Stop the background monitor and exit.

## 9. How to Run the Project

### Prerequisites

- Java Development Kit (JDK) installed.
- Java compiler and runtime available through the terminal.
- Git, if you want to clone the repository.

Verify Java installation:

```bash
java -version
javac -version
```

### Run Locally

Clone the repository:

```bash
git clone https://github.com/sanmotraudayan/SmartEmergencyResponseSimulator.git
```

Navigate to the project folder:

```bash
cd SmartEmergencyResponseSimulator
```

Compile the Java source files:

```bash
javac *.java
```

Run the application:

```bash
java Main
```

If you already have the project open in VS Code, you can run the compile and execution commands directly in its terminal.

## 10. Main Menu

The application provides the following operations:

1. Register Patient and Emergency
2. View All Patients
3. View All Emergencies
4. View Ambulances
5. View Hospitals
6. Dispatch Next Emergency
7. Complete Emergency
8. View Dispatch History
9. View System Report
10. Save Data and Reports
11. Exit

The menu allows the user to interact with the simulator without modifying source code.

## 11. Data Persistence and Reports

The application uses Java serialization to preserve the application state between sessions.

| File | Purpose |
|---|---|
| `data/simulator_data.ser` | Stores serialized application state. |
| `data/system_report.txt` | Stores the generated system report. |
| `data/dispatch_history.txt` | Stores the generated dispatch history. |

The `FileManager` class handles serialization and text-file operations. The generated data files are runtime artifacts and do not need to be committed to GitHub.

## 12. Testing

The following test cases can be used to evaluate the application.

| Test Case | Expected Result |
|---|---|
| Register a valid patient and emergency | Records are added successfully. |
| Enter an invalid menu choice | An error message is displayed. |
| Enter a non-numeric menu value | Input is rejected and requested again. |
| Register different emergency types | The correct type and priority are assigned. |
| View emergency records | Registered emergencies are displayed. |
| Dispatch a pending emergency | The manager attempts dispatch according to priority and resource availability. |
| Complete a valid dispatched emergency | The emergency is completed and resources are released according to the implementation. |
| Complete an unknown emergency ID | The custom exception is handled. |
| Save application data | Serialized data is written successfully. |
| Restart after saving | Previously saved application state is restored. |
| Generate reports | Report and history text files are created or updated. |
| Register an emergency while monitoring | The background monitor detects the registration activity. |

**Testing note:** These are intended test cases and expected outcomes. Record the actual result of each test before claiming that it passed.

## 13. Results

The project brings together several Java programming concepts in a single interactive application.

When the relevant features operate correctly, the simulator demonstrates:

- Structured patient and emergency management.
- Severity-based emergency prioritization.
- Simulated ambulance and hospital resource management.
- Emergency completion and dispatch history.
- Persistent application state through serialization.
- Text-based report generation.
- Background monitoring using multithreading.
- Input validation and exception handling.

The project is intended to demonstrate programming concepts and workflow simulation rather than provide a production-ready emergency response service.

## 14. Limitations

- The application uses a console-based interface.
- Emergency information is entered manually.
- Ambulances and hospitals are predefined or managed within the simulation.
- It does not use real-time GPS, traffic data, or live hospital availability.
- Emergency prioritization is based on predefined categories.
- Serialized files are used instead of a database.
- The monitor reports emergency activity but does not independently dispatch ambulances.
- The application has no user authentication or network-based multi-user support.
- Its behavior has not been established as safe or suitable for real emergency operations.

## 15. Future Enhancements

Possible future improvements include:

- A graphical interface or web-based dashboard.
- Database integration using JDBC and MySQL.
- More detailed resource allocation and routing logic.
- Simulated GPS-based ambulance tracking.
- Role-based access for administrators and operators.
- Enhanced logging and automated testing.
- More detailed emergency status tracking and reporting.

These are potential extensions, not features currently claimed as implemented.

## 16. Conclusion

The **Smart Emergency Response Simulator** is a Java OOP mini-project that models the basic workflow of emergency registration, prioritization, dispatch, resource management, and record keeping.

It provides practical experience with abstraction, encapsulation, inheritance, polymorphism, method overriding, exception handling, serialization, file operations, and multithreading.

The project demonstrates how core Java concepts can be combined to build a structured application with multiple interacting classes and persistent data. It also provides a foundation for future enhancements involving databases, graphical interfaces, and more advanced simulation logic.

---

**Project:** Smart Emergency Response Simulator  
**Language:** Java  
**Category:** Object-Oriented Programming Mini-Project  
**Repository:** https://github.com/sanmotraudayan/SmartEmergencyResponseSimulator

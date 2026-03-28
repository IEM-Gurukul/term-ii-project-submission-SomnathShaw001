# Smart Task Scheduler

A Java-based academic project demonstrating core Object-Oriented Programming (OOP) concepts:
- **Abstraction**: `Task` abstract class defining a template for all tasks. Interface `TaskDAO` to abstract persistence operations.
- **Encapsulation**: Protected and private fields to ensure proper data hiding, exposed safely via getters/setters.
- **Inheritance**: `OneTimeTask` and `RecurringTask` extending the base `Task` class.
- **Polymorphism**: Dynamic calculation of priority overridden by each task subclass for polymorphic behavior in `SchedulerManager`.

## Project Structure
- `src/`: Contains all Java source files.
- `src/exceptions/`: Contains custom exceptions.
- `data/`: Used for flat-file database storage (`tasks.csv`).
- `docs/`: Project documentation and UML Diagrams.

## Getting Started

### Prerequisites
Make sure you have [Java JDK](https://www.oracle.com/java/technologies/downloads/) (version 8 or higher) installed. Verify your installation by running:
```bash
java -version
```

### Compile
Navigate into the root directory of the project and run the `javac` compiler on all source files:
```bash
javac -d bin src/*.java src/exceptions/*.java
```
*(Note: If you are using an IDE like IntelliJ, VS Code or Eclipse, it will compile automatically upon building the project.)*

### Run
To run the terminal application, run the generated `.class` files from your `bin` or `src` directory (if compiled in place):
```bash
cd src
java Main
```
Or, if you compiled to a `bin` folder as suggested above:
```bash
java -cp bin Main
```

## UML Diagram
A visual representation of the class hierarchy is present in the `docs/` folder as `uml_diagram.png`.

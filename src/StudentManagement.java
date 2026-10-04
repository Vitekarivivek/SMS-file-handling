import java.io.*;
import java.util.*;

class Student {
    int rollNo;
    String name;
    int age;
    String course;

    public Student(int rollNo, String name, int age, String course) {
        this.rollNo = rollNo;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    // Convert to file format: rollNo,name,age,course
    public String toFileString() {
        return rollNo + "," + name + "," + age + "," + course;
    }

    // Create Student from file line
    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        return new Student(
            Integer.parseInt(parts[0]),
            parts[1],
            Integer.parseInt(parts[2]),
            parts[3]
        );
    }

    @Override
    public String toString() {
        return "Roll No: " + rollNo + " | Name: " + name + " | Age: " + age + " | Course: " + course;
    }
}

public class StudentManagement {
    private static final String FILE_NAME = "vivek.txt";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n*** Student Management System Using File Handling ***");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by Roll No");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewAllStudents(); break;
                case 3: searchStudent(); break;
                case 4: updateStudent(); break;
                case 5: deleteStudent(); break;
                case 6:
                    System.out.println("Exiting... Data saved in " + "vivek.txt");
                    return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter Roll No: ");
        int rollNo = getIntInput();

        if (studentExists(rollNo)) {
            System.out.println("Student with Roll No " + rollNo + " already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Age: ");
        int age = getIntInput();
        System.out.print("Enter Course: ");
        String course = sc.nextLine();

        Student s = new Student(rollNo, name, age, course);

        try (FileWriter fw = new FileWriter("vivek.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(s.toFileString());
            bw.newLine();
            System.out.println("Student added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void viewAllStudents() {
        File file = new File("vivek.txt");
        if (!file.exists() || file.length() == 0) {
            System.out.println("No student records found.");
            return;
        }

        System.out.println("\n--- All Students ---");
        try (BufferedReader br = new BufferedReader(new FileReader("vivek.txt"))) {
            String line;
            int count = 0;
            while ((line = br.readLine())!= null) {
                if (!line.trim().isEmpty()) {
                    System.out.println(Student.fromFileString(line));
                    count++;
                }
            }
            System.out.println("Total students: " + count);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void searchStudent() {
        System.out.print("Enter Roll No to search: ");
        int rollNo = getIntInput();

        try (BufferedReader br = new BufferedReader(new FileReader("vivek.txt"))) {
            String line;
            while ((line = br.readLine())!= null) {
                Student s = Student.fromFileString(line);
                if (s.rollNo == rollNo) {
                    System.out.println("Student Found: " + s);
                    return;
                }
            }
            System.out.println("Student with Roll No " + rollNo + " not found.");
        } catch (FileNotFoundException e) {
            System.out.println("No records found. Add students first.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void updateStudent() {
        System.out.print("Enter Roll No to update: ");
        int rollNo = getIntInput();

        List<Student> students = readAllStudents();
        boolean found = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).rollNo == rollNo) {
                System.out.print("Enter new Name: ");
                String name = sc.nextLine();
                System.out.print("Enter new Age: ");
                int age = getIntInput();
                System.out.print("Enter new Course: ");
                String course = sc.nextLine();

                students.set(i, new Student(rollNo, name, age, course));
                found = true;
                break;
            }
        }

        if (found) {
            writeAllStudents(students);
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student with Roll No " + rollNo + " not found.");
        }
    }

    private static void deleteStudent() {
        System.out.print("Enter Roll No to delete: ");
        int rollNo = getIntInput();

        List<Student> students = readAllStudents();
        boolean removed = students.removeIf(s -> s.rollNo == rollNo);

        if (removed) {
            writeAllStudents(students);
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student with Roll No " + rollNo + " not found.");
        }
    }

    // Helper: Read all students from file
    private static List<Student> readAllStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File("vivek.txt");
        if (!file.exists()) return students;

        try (BufferedReader br = new BufferedReader(new FileReader("vivek.txt"))) {
            String line;
            while ((line = br.readLine())!= null) {
                if (!line.trim().isEmpty()) {
                    students.add(Student.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return students;
    }

    // Helper: Write all students to file
    private static void writeAllStudents(List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("vivek.txt"))) {
            for (Student s : students) {
                bw.write(s.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static boolean studentExists(int rollNo) {
        return readAllStudents().stream().anyMatch(s -> s.rollNo == rollNo);
    }

    private static int getIntInput() {
        while (true) {
            try {
                int num = Integer.parseInt(sc.nextLine());
                return num;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Enter again: ");
            }
        }
    }
}
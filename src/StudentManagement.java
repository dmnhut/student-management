import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class StudentManagement {
    static final String DATABASE_FILE = "/home/who/mydata/student.db";
    static final String DATABASE_TEMP = "/home/who/mydata/student_temp.db";
    static final int ACTIVE_STATUS = 0;
    static final int SUSPENDED_STATUS = 1;
    static final int INACTIVE_STATUS = 2;
    static final String NULL = "";
    static int MAX_SIZE_OF_CLASS_LIST;

    /**
     * Main func
     *
     * @param __
     */
    public static void main(String[] __) {
        Scanner scan;
        while (true) {
            scan = new Scanner(System.in);
            System.out.println("========================================================================================" +
                    "============================================================");
            System.out.println("(1) - Create student | (2) - Get student by code | (3) - Update status student by code |" +
                    " (4) Print students of class | (5) - List class | (6) - Exit");
            System.out.println("========================================================================================" +
                    "============================================================");
            switch (scan.nextLine().trim()) {
                case "1":
                    createStudent();
                    break;
                case "2":
                    System.out.print("Student code = ");
                    getStudent(scan.nextLine().trim());
                    break;
                case "3":
                    System.out.print("Student code = ");
                    Student.code = scan.nextLine().trim();
                    System.out.println("ACTIVE_STATUS = 0 | SUSPENDED_STATUS = 1 | INACTIVE_STATUS = 2");
                    System.out.print("Student status = ");
                    updateStudent(Student.code, Integer.parseInt(scan.nextLine().trim()));
                    break;
                case "4":
                    System.out.print("Student class code = ");
                    printStudentOfClass(scan.nextLine().trim());
                    break;
                case "5":
                    System.out.println("Template: Class, Active Students, Inactive Students, Suspended Students, Total");
                    listClass();
                    break;
                case "6":
                    System.out.print("Exit");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Create students
     */
    public static void createStudent() {
        while (true) {
            Scanner scan = new Scanner(System.in);
            System.out.print("Student code = ");
            Student.code = scan.nextLine();
            if (Student.code.isEmpty()) {
                break;
            }
            System.out.print("Student full name = ");
            Student.fullName = scan.nextLine();
            System.out.print("Student class code = ");
            Student.classCode = scan.nextLine();
            System.out.println("Student default status = 0 (ACTIVE_STATUS)");
            Student.status = ACTIVE_STATUS;
            try {
                DataOutputStream data = new DataOutputStream(new FileOutputStream(DATABASE_FILE, true));
                data.writeUTF(Student.code);
                data.writeUTF(Student.fullName);
                data.writeUTF(Student.classCode);
                data.writeInt(Student.status);
                data.flush();
                data.close();
                System.out.println("Created student");
                System.out.println(
                        Student.code + ", " + Student.fullName + ", " + Student.classCode + ", " + Student.status);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Get student by code
     *
     * @param studentCode
     * @return
     */
    public static String getStudent(String studentCode) {
        boolean isNotFound = true;
        try {
            DataInputStream data = new DataInputStream(new FileInputStream(DATABASE_FILE));
            while (true) {
                try {
                    if (data.available() == 0) {
                        break;
                    }
                    Student.code = data.readUTF();
                    Student.fullName = data.readUTF();
                    Student.classCode = data.readUTF();
                    Student.status = data.readInt();
                    if (Student.code.compareTo(studentCode) == 0) {
                        System.out.println("Found student with code = " + studentCode);
                        System.out.println(Student.code + ", " + Student.fullName + ", " + Student.classCode + ", "
                                + Student.status);
                        isNotFound = false;
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            data.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        if (isNotFound) {
            System.out.println("Not found student with code = " + studentCode);
            return NULL;
        } else {
            return studentCode;
        }
    }

    /**
     * Update status of student with code
     *
     * @param studentCode
     * @param status
     * @return
     */
    public static String updateStudent(String studentCode, int status) {
        boolean isNotFound = true;
        try {
            DataInputStream dataInput = new DataInputStream(new FileInputStream(DATABASE_FILE));
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(DATABASE_TEMP));
            while (true) {
                try {
                    if (dataInput.available() == 0) {
                        break;
                    }
                    Student.code = dataInput.readUTF();
                    Student.fullName = dataInput.readUTF();
                    Student.classCode = dataInput.readUTF();
                    Student.status = dataInput.readInt();
                    if (Student.code.compareTo(studentCode) == 0) {
                        Student.status = status;
                        System.out.println("Found student with code = " + studentCode);
                        System.out.println(Student.code + ", " + Student.fullName + ", " + Student.classCode + ", "
                                + Student.status);
                        isNotFound = false;
                    }
                    dataOutput.writeUTF(Student.code);
                    dataOutput.writeUTF(Student.fullName);
                    dataOutput.writeUTF(Student.classCode);
                    dataOutput.writeInt(Student.status);
                    dataOutput.flush();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            dataInput.close();
            dataOutput.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        if (isNotFound) {
            System.out.println("Not found student with code = " + studentCode);
            return NULL;
        } else {
            File fileOld = new File(DATABASE_FILE);
            fileOld.delete();
            File fileNew = new File(DATABASE_TEMP);
            fileNew.renameTo(fileOld);
            return studentCode;
        }
    }

    /**
     * Print students by class code
     *
     * @param classCode
     */
    public static void printStudentOfClass(String classCode) {
        boolean isNotFound = true;
        try {
            DataInputStream data = new DataInputStream(new FileInputStream(DATABASE_FILE));
            while (true) {
                try {
                    if (data.available() == 0) {
                        break;
                    }
                    Student.code = data.readUTF();
                    Student.fullName = data.readUTF();
                    Student.classCode = data.readUTF();
                    Student.status = data.readInt();
                    if (Student.classCode.compareTo(classCode) == 0) {
                        System.out.println(Student.code + ", " + Student.fullName + ", " + Student.classCode
                                + ", " + Student.status);
                        isNotFound = false;
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            data.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        if (isNotFound) {
            System.out.println("Not found student with class code = " + classCode);
        }
    }

    /**
     * List class view many status of student group by class
     */
    public static void listClass() {
        MAX_SIZE_OF_CLASS_LIST = 0;
        try {
            DataInputStream data = new DataInputStream(new FileInputStream(DATABASE_FILE));
            while (true) {
                try {
                    if (data.available() == 0) {
                        break;
                    }
                    Student.code = data.readUTF();
                    Student.fullName = data.readUTF();
                    Student.classCode = data.readUTF();
                    Student.status = data.readInt();
                    MAX_SIZE_OF_CLASS_LIST++;
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            data.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        ClassCodeGroupByStatus[] classCodeGroupByStatus = new ClassCodeGroupByStatus[MAX_SIZE_OF_CLASS_LIST];
        for (int i = 0; i < MAX_SIZE_OF_CLASS_LIST; i++) {
            classCodeGroupByStatus[i] = new ClassCodeGroupByStatus();
        }
        try {
            DataInputStream data = new DataInputStream(new FileInputStream(DATABASE_FILE));
            while (true) {
                try {
                    if (data.available() == 0) {
                        break;
                    }
                    Student.code = data.readUTF();
                    Student.fullName = data.readUTF();
                    Student.classCode = data.readUTF();
                    Student.status = data.readInt();
                    for (int i = 0; i < MAX_SIZE_OF_CLASS_LIST; i++) {
                        if (classCodeGroupByStatus[i].classCode.compareTo(NULL) == 0 ||
                                classCodeGroupByStatus[i].classCode.compareTo(Student.classCode) == 0) {
                            classCodeGroupByStatus[i].classCode = Student.classCode;
                            switch (Student.status) {
                                case ACTIVE_STATUS:
                                    classCodeGroupByStatus[i].active_status++;
                                    break;
                                case SUSPENDED_STATUS:
                                    classCodeGroupByStatus[i].suspended_status++;
                                    break;
                                case INACTIVE_STATUS:
                                    classCodeGroupByStatus[i].inactive_status++;
                                    break;
                            }
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            data.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        for (int i = 0; i < MAX_SIZE_OF_CLASS_LIST; i++) {
            if (classCodeGroupByStatus[i].classCode.compareTo(NULL) == 0) {
                continue;
            }
            System.out.print("Class: " + classCodeGroupByStatus[i].classCode + ", ");
            System.out.print("Active Students: " + classCodeGroupByStatus[i].active_status + ", ");
            System.out.print("Inactive Students: " + classCodeGroupByStatus[i].suspended_status + ", ");
            System.out.print("Suspended Students: " + classCodeGroupByStatus[i].inactive_status + ", ");
            System.out.println("Total: " + (classCodeGroupByStatus[i].active_status + 
                    classCodeGroupByStatus[i].suspended_status + classCodeGroupByStatus[i].inactive_status));
        }
    }

    /**
     * Contain property of a student
     */
    public static class Student {
        static String code;
        static String fullName;
        static String classCode;
        static int status;
    }

    /**
     * Contain class code grouped
     */
    static final class ClassCodeGroupByStatus {
        String classCode;
        int active_status;
        int suspended_status;
        int inactive_status;

        /**
         * Init func
         */
        public ClassCodeGroupByStatus() {
            classCode = NULL;
            active_status = 0;
            suspended_status = 0;
            inactive_status = 0;
        }
    }
}
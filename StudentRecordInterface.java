import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

public class StudentRecordInterface {
    //The command prompt interface that the user will interact with


    /**
     *Allows the user to select the type of user they are so that the relevant functionality
     *can be provided to the user
     */
    public static void main(String args[]){
        Scanner in = new Scanner(System.in) ;

        System.out.println("Choose an appropriate user" + '\n' +
                            "1) Student 2) Teacher 3) Board Member");
        int command = in.nextInt() ;

        if (command == 1)
            studentMenu();
        if (command == 2)
            teacherMenu();
        if (command == 3)
            boardMenu();
        else
            System.out.println("Invalid Command");
    }

    /**
     * provides the functionality relevant to a student
     */
    private static void studentMenu(){

    }

    /**
     * provides the functionality relevant to a teacher
     */
    private static boolean teacherMenu(){
        Scanner in = new Scanner(System.in) ;

               //ADD CODE TO CATCH EXCEPTIONS
        //Maybe check if teacher has relevant modules

        //Gets teacher
        System.out.println("Enter your faculty id:");
        Teacher teacher = csvEditor.getTeacher(in.next()) ;

        //Gets module
        System.out.println("Enter your module code:");
        String moduleCode = in.next() ;
        System.out.println("Enter the year the module commenced:");
        String year = in.next() ;
        System.out.println("Enter a semester of the module:");
        String semester = in.next() ;
        Module module = csvEditor.getModule(moduleCode + "_" + year + "_" + semester) ;





        //Asks user what they would like to do
         String[] choices = new String[]{
                            "get a modules result" ,
                            "get a students result" ,
                            "alter a students result" ,
                            "alter a tests weighting"};
        int command = getChoice(choices) ;


        if(command == 1){
            //Dictionary with student id mapped to the test results of the student relevant
            //to that module
            HashMap<String, double[]> studentTestResults = teacherModule.getGrades() ;

            //prints out header to give the following data context
            System.out.print("Student id |");

            int count = 0;
            //prints out each students id and relevant test results
            for(Map.Entry<String,double[]> entry : studentTestResults.entrySet()) {
                //prints out the number of tests as a header to give rest of data context
                if (count == 0) {
                    for (int number = 0; number <= entry.getValue().length; number++)
                        System.out.print(" Test" + (number + 1) + " ");
                    count++;
                    System.out.println();
                }
                //prints out the students id
                System.out.print(entry.getKey() + " |");

                //prints out the students test results
                for (double result: entry.getValue()) {
                    System.out.print("  " + result + "%  ");

                }
                //retrns to a new line ready for the next student
                System.out.println();


            }
            //ends the command as all the students results have been printed
            return true;
        }
        else if (command == 2){
            //gets a students results

            //gets a student from the module given their id
            System.out.println("Enter students id number:");
            String studentId = in.next() ;
            StudentModule student = csvEditor.getStudentModule(module, studentId) ;

            //gets an array containing the students results
            double[] grades = studentModule.getGrades() ;

            //prints out the number of tests as a header to give rest of data context
            for (int number = 0; number <= grades.length; number++)
                System.out.print(" Test" + (number + 1) + " ");

            //prints out the students id
            System.out.print(studentId + " |");

            //prints out the students test results
            for (double result: grades) {
                System.out.print("  " + result + "%  ");

            }
            //retrns to a new line
            System.out.println();

            return true;
        }
        else if (command == 3){
            //alter a Student result

            //gets a student from the module given their id
            System.out.println("Enter students id number:");
            String studentId = in.next() ;
            StudentModule student = csvEditor.getStudentModule(module, studentId) ;

            //Get a choice of all the students tests
            double[] grades = student.getGrades() ;
            String[] gradesAsString = new String[grades.length] ;
             for(int number = 0; number <= grades.length; number++)
                 gradesAsString[number] = Double.toString(grades[number]) ;
             int test = getChoice(gradesAsString, 2) ;

            //gets the new result for a student and changes it in their records
            //Ensuring it's within the bounds of (0 <= newResult <= 100)
            double newResult = -1 ;
            System.out.println("Enter students new Result:");
            while (newResult < 0 || newResult > 100) {
                newResult = in.nextDouble();
                if (newResult < 0 || newResult >100)
                    System.out.println("Result must be greater than or equal to 0" + '\n' +
                    "and less than or equal to 100." + '\n' + "Enter new Result:");
            }
            csvEditor.setStudentModuleGrade(test - 1,newResult)  ;

            return true ;
        }
        else if (command == 4) {
            //This asks how a user they would like to alter the tests
            choices = new String[]{"add a test", "remove a test",
                    "alter current tests weighting"};
            command = getChoice(choices);

            //adds a test
            if (command == 1){
                teacher.addTest(module ,module.getNumberTests());
                return true ;

            }
            //removes a test
            else if (command == 2){
                teacher.removeTest(module) ;
                return true ;
            }
            //alters test weighting
            else if (command == 3) {
                teacher.alterTestWeighting(module, module.getNumberTests()) ;
                return true ;
            }
            //Invalid command
            else
                return false;

        }
        else
            System.out.println("Invalid command");
        return false ;
    }

    /**
     * provides the functionality relevant to a board member
     */
    private static void boardMenu(){

    }

    public static int getChoice(String[] options,int format){
        Scanner in = new Scanner(System.in) ;
        int choice = 0 ;

        if (format == 1)
            System.out.println("What would you like to do:");

        if (format == 2)
            System.out.println("Choose a test");

        for (int number = 0; number <= options.length; number++){

            if (format == 1)
                System.out.println((number + 1) + ") " + options[number]);
            else
                System.out.println("Test" + (number + 1) + ") " + options[number]);
        }

        choice = in.nextInt() ;
        while (choice < 1 && choice > options.length + 1){
            System.out.println("Invalid option" + '\n' + "Enter new choice:");
            choice = in.nextInt() ;
        }
        return choice ;
    }


}

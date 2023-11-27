import java.util.ArrayList;
import java.util.Scanner;

public class Teacher {
    /*
    *Teacher needs to:
    * 1) get the grades for all students in a module
    * 2) get the test results of a student in a module
    * 3) change the test result of a student        //uses csvEditor.updateStudentGrade()
    * 4) change the grading scheme (test weighting) //uses csvEditor.updateGradingScheme()
    */

    private String id ;
    private ArrayList<Module> modules;

    private Scanner in ;

    /**
     *Constructs a teacher with a unique id
     */
    public Teacher(String id){
        this.id = id ;
    }

    /**
     *gets a students test results in a given module
     * @param module module that student is in
     * @param studentId the id of the student
     * @return the students result as a String
     * @author Oliver Fitzgerald(22365958)
     */
    public String getStudentResults(Module module,int studentId){

        double[] results ;
        String studentResults = "Test Results for student " + studentId ;
        boolean studentPresent = false ;

        for (Student student:module.getStudents()) {

            if (studentId == Student.getStudentId()){
                studentPresent = true ;
                results = student.getModule(module).getModuleResults();


                for (int number = 0; number <= results.length ;number++ )
                    studentResults = "Test " + (number + 1) + ":" + results[number] + '\n';

            }

        }

        if (studentPresent = false)
            studentResults = "Error: Student is not present in the module" ;

        return studentResults ;
    }

    /**
     *returns a String containg the test results of all the students in a module
     * @param module the module from which you want the result
     * @return the results of the module
     * @author Oliver Fitzgerald(22365958)
     */
    public String getModuleResults(Module module){
        //how are student results stored in a module

        return "Module results" ;
    }

    /**
     *changes the result of a given test for a given student in a given module
     * @param testNumber
     * @param studentId
     * @param module
     * @param newResult
     * @return boolean altered
     * @author Oliver Fitzgerald(22365958)
     */
    public boolean alterStudentResult(Module module,int studentId, int testNumber, double newResult ){

        for (Student student:module.getStudents()) {
            if (studentId == Student.getStudentId()){
                //int testnumber, double percentage
                csvEditor.updateStudentGrade(testNumber -1, newResult) ;
                return true ;

            }

        }

        return false ;
    }

    //4) change the grading scheme changes the requirments for a grade //uses csvEditor.updateGradingScheme()
    /**
     *Changes the requirements for grades within a module
     * @param module
     */


    /**
     * adds a test to a module
     * @return a boolean indicating whether the operation was successful or not
     */
    public boolean addTest(Module module, int currentNumberOfTests){
        Scanner in = new Scanner(System.in) ;

        //get the number of tests that the user would like to add and adds
        //it to the current number of tests
        System.out.println("Enter number of tests you would like to add:");
        int temp = in.nextInt();
        while (temp <= 0){
            System.out.println("You can only add a positive number of tests" + '\n' +
                    "Enter number of tests you would like to add:");
            temp = in.nextInt() ;
        }
        int numberOfTests = temp + currentNumberOfTests;

        //changes the weighting of tests
        boolean newTestWeightings = false ;
        while (newTestWeightings == false)
           newTestWeightings = alterTestWeighting(module, numberOfTests) ;

        return  true ;
    }

    /**
     *removes a test
     * @return a boolean indicating whether the operation was successful or not
     */
    public boolean removeTest(Module module){
        //gets test to be removed
        System.out.println("Enter test you would like to remove:");
        int testToBeRemoved = in.nextInt() - 1 ;
        int numberOfTests = module.getNumberOfTests() ;

        if (0 <= testToBeRemoved && testToBeRemoved <= numberOfTests) {

            //gets the array of current testweightings and intialises
            double[] currentTestWeightings = csvEditor.getTestWeightings(module);
            double[] newTestWeighting = new double[numberOfTests];

            //removes the test and re-aranges the testWeighting array before returning
            int currentIndex = testToBeRemoved;
            while (currentIndex <= numberOfTests) {
                double temp = currentIndex + 1;
                newTestWeighting[currentIndex] = temp;
                currentIndex++;
            }

            //changes the weighting of tests
            boolean newTestWeightings = false ;
            while (newTestWeightings == false)
                newTestWeightings = alterTestWeighting(module, numberOfTests) ;

            return true;
        }

        System.out.println("Error: Test " + (testToBeRemoved + 1) + "doesn't exist");
        return false ;
    }

    /**
     *alters the grade weighting of the existing tests
     *@return a boolean indicating whether the operation was successful or not
     */
    public boolean alterTestWeighting(Module module, int numberOfTests){


        /*
         *Initializes a double testWeighting that will keep track of the test weighting so that
         *we won't have test weightings exceeding 100% which would result in student being graded out of
         *an amount greater than 100
         *Initializes an array to store the new test weightings
         */
        double totalWeighting = 0;
        double[] newTestWeighting = new double[numberOfTests] ;

        //gets the new test weightings from the users
        for(int number = 0; number <= numberOfTests; number++) {

            System.out.println("Enter weighting of test" + number);
            double testWeighting = in.nextDouble();

            //Ensures that the user doesn't input a test weighting that is less than 0
            while (testWeighting < 0){
                System.out.println("A test weighting cannot be less than 0" + '\n' +
                        "Enter a new test weighting");
                testWeighting = in.nextDouble();
             }

            newTestWeighting[number] = testWeighting ;
            totalWeighting += testWeighting ;
        }

        //Ensures that the test weightings don't go over 100 as that would result
        //in a student being graded out of more than a 100
        if (totalWeighting != 100){
            System.out.println("The sum of all test weightings must be equal to 100");
            return false ;
        }

        //updates the module with the new test weightings
        csvEditor.updateTestWeightings(module , newTestWeighting) ;

        //displays the updated test weightings to the user
        System.out.println("Test#: Weighting");
        for(int number = 0; number <= numberOfTests; number++) {
            System.out.println("Test " + (number + 1) + ": " + newTestWeighting[number]);

        }

        return true ;
    }


    //NOTE
    //possibly make newTestWeighting initalised outside of if blocks
}

import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.lang.IllegalArgumentException;

public class Teacher {

    //DATA FIELDS
    private String id ;
    private ArrayList<TeacherModule> modules;

    //CONSTRUCTORS
    /**
     *Constructs a teacher with a unique id
     */
    public Teacher(String id){
        this.id = id ;
        this.modules = new ArrayList<TeacherModule>() ;
    }

    public Teacher(String id, ArrayList<TeacherModule> modules){
        this.id = id;
        this.modules = modules;
    }

    /**
     *gets a students test results in a given module
     * @param module module that student is in
     * @param studentId the id of the student
     * @return the students result as a String
     * @author Oliver Fitzgerald(22365958)
     */
    public String getStudentResults(StudentModule module,String studentID){
        return "Test Results for student " + studentID + ": " + module.getGradesString() ;
    }

    /**
     *returns a String containg the test results of all the students in a module
     * @param module the module from which you want the result
     * @return the results of the module
     * @author Oliver Fitzgerald(22365958)
     */
    public String getModuleResults(TeacherModule module){
        //how are student results stored in a module
        StringBuilder sb = new StringBuilder();
        sb.append(module.getCSVName().replaceAll("_", " ") + " results: \n");
        for(Map.Entry<String, double[]> entry : module.getGrades().entrySet()){
            StringBuilder gradeLine = new StringBuilder();
            gradeLine.append(entry.getKey() + ": ");
            for(double d : entry.getValue()){
                gradeLine.append(", " + d);
            }
            gradeLine.deleteCharAt(sb.indexOf(","));
            sb.append(gradeLine.toString() + "\n");
        }
        return sb.toString();
    }

    //4) change the grading scheme changes the requirments for a grade //uses csvEditor.updateGradingScheme()
    /**
     *Changes the requirements for grades within a module
     * @param module
     * adds a test to a module
     * @return a boolean indicating whether the operation was successful or not
     */
    public boolean addTest(TeacherModule module, Teacher teacher, int currentNumberOfTests){
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
        StudentRecordInterface.alterTestInput(module,teacher) ;

        return  true ;
    }

    /**
     *removes a test
     * @return a boolean indicating whether the operation was successful or not
     */
    public boolean removeTest(TeacherModule module,Teacher teacher, int testToRemove){
        //gets test to be removed
        System.out.println("Enter test you would like to remove:");
        int testToBeRemoved = testToRemove - 1 ;
        int numberOfTests = module.getNumberOfTests() ;

        if (0 <= testToBeRemoved && testToBeRemoved <= numberOfTests) {

            //gets the array of current testweightings and intialises
            double[] currentTestWeightings = module.getWeights();
            double[] newTestWeighting = new double[numberOfTests];

            //removes the test and re-aranges the testWeighting array before returning
            int currentIndex = testToBeRemoved;
            while (currentIndex <= numberOfTests) {
                double temp = currentIndex + 1;
                newTestWeighting[currentIndex] = temp;
                currentIndex++;
            }

            StudentRecordInterface.alterTestInput(module,teacher) ;

            return true;
        }

        System.out.println("Error: Test " + (testToBeRemoved + 1) + "doesn't exist");
        return false ;
    }

    /**
     *alters the grade weighting of the existing tests
     *@return a boolean indicating whether the operation was successful or not
     */
    public boolean alterTestWeighting(double[] newTestWeightings, TeacherModule module) throws IllegalArgumentException, IOException{

        double totalWeighting = 0;
        ArrayList<Double> testWeightings = new ArrayList<Double>();

        //gets the new test weightings from the users
        for(int number = 0; number <= newTestWeightings.length; number++) {
            double testWeighting = newTestWeightings[number];
            totalWeighting += testWeighting ;
            testWeightings.add(newTestWeightings[number]);

            //Ensures that the user doesn't i
            // nput a test weighting that is less than 0
            if (testWeighting >= 0 && testWeighting <= 100){
                totalWeighting += testWeighting ;

            }else
                throw new IllegalArgumentException("A test weighting cannot be less than 0 or greater than 100") ;

        }

        //displays the updated test weightings to the user
        System.out.println("Test#: Weighting");
        for(int number = 0; number <= newTestWeightings.length; number++) {
            System.out.println("Test " + (number + 1) + ": " + newTestWeightings[number]);

        }

        //Ensures that the test weightings don't go over 100 as that would result
        //in a student being graded out of more than a 100
        if (totalWeighting == 100){
            //updates the module with the new test weightings
            CSVEditor.updateWeightings(module, testWeightings.stream().mapToDouble(Double::doubleValue).toArray()) ;
            return true;
        }else
            throw new IllegalArgumentException("The sum of all test weightings must be equal to 100") ;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Teacher " + id + "\n");
        sb.append("Teaches Modules: \n");
        for(TeacherModule mod : this.modules){
            sb.append(mod.toString() + "\n");
        }

        return sb.toString();
    }

    public String getTeacherAsCSVLine(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        for(TeacherModule mod : this.modules){
            sb.append("," + mod.getCSVName());
        }

        return sb.toString();
    }
}
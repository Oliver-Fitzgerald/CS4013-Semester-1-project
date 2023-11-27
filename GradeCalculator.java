import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.DoubleUnaryOperator;

public class GradeCalculator {
    //calculates students grades and QCA

    /**
     * calculates the qca of a semester
     * @param modules
     * @param semesterYear
     * @param  semesterSemester
     * @return a double representing the semesters QCA
     */
    public double semesterQCA(ArrayList<StudentModule> modules, int semesterYear, int semesterSemester) {
        double semesterQca = 0 ;
        for (StudentModule module : modules) {
            semesterQca += moduleGrade(module,semesterYear,semesterSemester) ;
        }

        return semesterQca / modules.size() ;
    }

    /**
     *Gets the qca value for a module
     * @param module the module that we are get the qca
     * @param year  the year that the module took place in
     * @param semester the semester that the module took place in
     * @return the qca of the module
     */
    public static double moduleGrade (StudentModule module,int year, int semester){

        double[] testWeightings = module.getTestWeightings();
        double[] grades = module.getGrades();

        String moduleGradingScheme = module.getGradingScheme();
        //Puts the grading scheme into a hash map and converts grade e.g(a1)
        //and converts to a qca value e.g(4.0)
        //      QPV      %GRADE
        HashMap<Double, Double> gradingScheme = new HashMap<Double, Double>();
        for (int number = 0; number <= 11; number++) {
            gradingScheme.put(Double.parseDouble(moduleGradingScheme.substring(number, number + 2)), Double.parseDouble(moduleGradingScheme.substring(number + 3, number + 5)));
        }

        //gets the total grade for a module from tests completed using
        // the test weightings as a guide
        double totalGrade = 0;
        for (int number = 0; number <= grades.length; number++) {
            totalGrade += grades[number] * (testWeightings[number] / 100);

        }

        //gets the appropriate qpv based on the modules grading scheme and the students results
        for (Map.Entry<Double,Double> entry:gradingScheme.entrySet()) {

            //80 <= 92    is true
            if (entry.getValue() <= totalGrade)
                //return 4.0
                return entry.getKey() ;
            }

            return 0;
        }
    }
/*
                Notes

    // (result 1 (testWeighting / 100) + results 2  (testWeighting / 100) + ...) / number of results
    //= module grade
    //module grade -> grade e.g(a1,a2,a3) //just for student to see
    //module grade -> QPV e.g(3.2,3.6) //used to calculate qca
    //QCA -> (module1 QPV +  module2 QPV) / number of modules
*/

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class gradeCalculator {
    //calculates students grades and QCA

    /**
     * calculates the qca of a semester
     * @param modules
     * @param semsterYear
     * @param  semsterSemester
     * @return a double representing the semesters QCA
     */
    public double semesterQca(Arraylist<StudentModules> modules, int semsterYear, int semsterSemester) {
        double semesterQca = 0 ;
        for (StudentModule module : modules) {
            semesterQca += moduleGrade(module,semsterYear,semsterSemester) ;
        }

        return semesterQca / modules.size() ;
    }

        public static double moduleGrade (StudentModule module,int year, int semester){

            if (module.getYear() != year && module.getSemester() != semester)
                return 0;

            double[] testWeightings = module.getTestWeightings();
            double[] grades = module.getGrades();

            String moduleGradingScheme = module.getGradingScheme();
            double[] qpvValues = new double[11];

            //gets module grading scheme
            for (int number = 0; number <= 11; number++) {
                qpvValues[number] = (Double.parseDouble(moduleGradingScheme.substring(number + 3, number + 5)));
            }

            //gets the total grade for a module from tests completed using
            // the test weightings as a guide
            double totalGrades = 0;
            for (int number = 0; number <= grades.length; number++) {
                totalGrades += grades[number] * (testWeightings[number] / 100);

            }

            //IMPORTANT NEED TO GET % TO QPV VALUES
            //next bit won't work without that info

            //gets the appropriate qpv based on the modulues grading scheme and the students results
            for (int number = 0; number <= grades.length; number++) {

                //80 < 92    is true
                if (qpvValues[number] <= totalGrades)
                    //return 4.0
                    return qpvValues[number];
            }

            return 0;
        }
    }
/*
                Notes and stuff

HashMap<String, Double> gradingScheme = new HashMap<String, Double>();
 for (int number = 0; number <= 11; number++) {
            gradingScheme.put(moduleGradingScheme.substring(number, number + 2), Double.parseDouble(moduleGradingScheme.substring(number + 3, number + 5)));

        }

         // (result 1 (testWeighting / 100) + results 2  (testWeighting / 100) + ...) / number of results
    //= module grade
    //module grade -> grade e.g(a1,a2,a3) //just for student to see
    //module grade -> QPV e.g(3.2,3.6) //used to calculate qca
    //QCA -> (module1 QPV +  module2 QPV) / number of modules
*/

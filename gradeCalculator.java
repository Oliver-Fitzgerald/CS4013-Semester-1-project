import java.util.HashMap;
import java.util.function.DoubleUnaryOperator;

public class gradeCalculator{
    //calculates students grades and QCA

    //I will need to get all the modules a student takes in one semseter
    //To get the the modules taken in the same semester
    //where year and semstser are equal
    /**
      *calculates the qca of a semster
      * @param studentId
      * @return QCA
      */
    // (result 1 (testWeighting / 100) + results 2  (testWeighting / 100) + ...) / number of results
    //= module grade
    //module grade -> grade e.g(a1,a2,a3) //just for student to see
    //module grade -> QPV e.g(3.2,3.6) //used to calculate qca
    //QCA -> (module1 QPV +  module2 QPV) / number of modules
    public double semesterQca(Arraylist<StudentModules> modules,int semsterYear,int semsterSemester) {

        double[] modulesQpvs = new double[modules.size()] ;
        int number = 0;

        for (StudentModule module:modules){

            if (module.getYear() == semsterYear && module.getSemester() == semsterSemester) {
                double[] testWeightings = module.getTestWeightings();
                double[] grades = module.getGrades();
                double moduleGrade = 0 ;
                double moduleQpv = 0 ;
                String moduleGradingScheme = module.getGradingScheme() ;

                /*
                 * if <= then the student hasn't completed every test yet and this programme will
                 * allow a student to see their qca so far it also may be the case that a student
                 * didn't complete a test and the teacher didn't submit a grade for them at all apose
                 * to giving them a 0 ;
                 */
                if (grades.length > testWeightings.length){
                    System.out.println("Error their are either to many tests or missing grade weightings");
                    return 0.0 ;
                }
                //gets the total grade for a module from tests completed using
                //the test weightings as a guide
                for (int i = 0; i <= grades.length)
                    moduleGrade += grades[i] * (testWeightings[i] / 100) ;

                //gets the appropriate qpv based on the modulues grading scheme
                HashMap<String,Double> gradingScheme = new HashMap<String,Double>() ;
                String[] key = new String[11] ;
                //there are " " qpv value
                for (int i = 0;i <= (11 * 6);i += 6) {
                    gradingScheme.put(Double.parseDouble(moduleGradingScheme.substring(i,i + 2)) ,Double.parseDouble(moduleGradingScheme.subString(i + 3,i + 5)))  ;
                    key[i / 6] = moduleGradingScheme.substring(i,i + 2) ;
                }

                for (int i = 0;i <= 11 ;i++) {
                    if (gradingScheme.get(key[i]) <= moduleGrade)
                        moduleQpv = Double.parseDouble(key[i])  ;
                }

                moduleQpvs[number] = moduleQpv ;
                number++ ;
                //Catch errors and stuff
            }


        }


    }

}

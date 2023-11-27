import java.io.IOException;
import java.util.*;

public class GradeCalculator {
    //calculates students grades and QCA

    /**
     * calculates the qca of a semester
     * @param modules
     * @param semsterYear
     * @param  semsterSemester
     * @return a double representing the semesters QCA
     */
    public double semesterQca(ArrayList<StudentModule> modules, int semsterYear, int semsterSemester) {
        double semesterQca = 0 ;
        for (StudentModule module : modules) {
            semesterQca += moduleGrade(module,semsterYear,semsterSemester) ;
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
    public static double moduleGrade (StudentModule module,int year, int semester) {

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
            totalGrade += grades[number] * (testWeightings[number]);

        }

        //gets the appropriate qpv based on the modules grading scheme and the students results
        for (Map.Entry<Double, Double> entry : gradingScheme.entrySet()) {

            //80 <= 92    is true
            if (entry.getValue() <= totalGrade)
                //return 4.0
                return entry.getKey();
        }

        return 0;
    }

    /**
     * returns the average QCA for students in a module
     * @param teacherModules a list of
     */
    public double averageQCA(TeacherModule teacherModule, int year, int semester){
        int count = 0 ;
        double totalQca = 0 ;

        for (Map.Entry<String,double[]> entry : teacherModule.getGrades().entrySet()){
            StudentModule studentModule = new StudentModule() ;
            try {
                studentModule = (StudentModule) CSVEditor.getModule(teacherModule.getCSVName(), entry.getKey());
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            totalQca += moduleGrade(studentModule,year,semester) ;
            count++ ;

        }

        return totalQca / count ;
    }

    /**
     * returns the median QCA for students in a programme
     */
    public double medianQCA(TeacherModule teacherModule,int year, int semester){
        ArrayList<Double> QCAOfStudents = new ArrayList<>() ;

        for (Map.Entry<String,double[]> entry : teacherModule.getGrades().entrySet()){
            StudentModule studentModule = new StudentModule() ;
            try {
                studentModule = (StudentModule) CSVEditor.getModule(teacherModule.getCSVName(), entry.getKey());
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

            QCAOfStudents.add(moduleGrade(studentModule,year,semester)) ;

        }

        Collections.sort(QCAOfStudents);
        if (QCAOfStudents.size() % 2 == 0){
            double num1 = QCAOfStudents.get(QCAOfStudents.size() / 2 - 1) ;
            double num2 = QCAOfStudents.get(QCAOfStudents.size() / 2 + 1) ;

            return (num1 + num2) / 2 ;
        }else
            return QCAOfStudents.get(QCAOfStudents.size() / 2) ;


    }


    /**
         * returns the average QCA for students in a programme
         * @param teacherModules a list of
         */
        public double averageQCA(ArrayList<TeacherModule> teacherModules, int year, int semester){
            int count1 = 0;
            double totalQca1 = 0;

            for (TeacherModule teacherModule : teacherModules) {
                int count = 0;
                double totalQca = 0;

                for (Map.Entry<String, double[]> entry : teacherModule.getGrades().entrySet()) {
                    StudentModule studentModule = new StudentModule();
                    try {
                        studentModule = (StudentModule) CSVEditor.getModule(teacherModule.getCSVName(), entry.getKey());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    totalQca += moduleGrade(studentModule, year, semester);
                    count++;

                }

                totalQca /= count ;

                totalQca1 += totalQca ;
                count1++ ;
            }

            return totalQca1 / count1 ;
        }

        /**
         * returns the median QCA for students in a programme
         */
        public double medianQCA(ArrayList<TeacherModule> teacherModules, int year, int semester) {

            double[] medianOfTeacherModules = new double[teacherModules.size()] ;
            int number = 0;

            for (TeacherModule teacherModule: teacherModules) {

            ArrayList<Double> QCAOfStudents = new ArrayList<>();

            for (Map.Entry<String, double[]> entry : teacherModule.getGrades().entrySet()) {
                StudentModule studentModule = new StudentModule();
                try {
                    studentModule = (StudentModule) CSVEditor.getModule(teacherModule.getCSVName(), entry.getKey());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                QCAOfStudents.add(moduleGrade(studentModule, year, semester));

            }

            Collections.sort(QCAOfStudents);
            if (QCAOfStudents.size() % 2 == 0) {
                double num1 = QCAOfStudents.get(QCAOfStudents.size() / 2 - 1);
                double num2 = QCAOfStudents.get(QCAOfStudents.size() / 2 + 1);

                medianOfTeacherModules[number] = (num1 + num2) / 2;
            } else
                medianOfTeacherModules[number] = QCAOfStudents.get(QCAOfStudents.size() / 2);

            number++ ;
        }

            double median = 0 ;
            Arrays.sort(medianOfTeacherModules);
            if (medianOfTeacherModules.length % 2 == 0) {
                double num1 = medianOfTeacherModules[medianOfTeacherModules.length / 2 - 1] ;
                double num2 = medianOfTeacherModules[medianOfTeacherModules.length / 2 - 1] ;

                return (num1 + num2) / 2;
            } else
                return  medianOfTeacherModules[medianOfTeacherModules.length / 2] ;
        }

}

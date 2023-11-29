import java.io.IOException;
import java.util.*;

public class GradeCalculator {
    //calculates students grades and QCA

    /**
     * calculates the qca of a semester
     * @param modules the modules taken by the student
     * @return a double representing the semesters QCA
     */
    public double semesterQca(ArrayList<StudentModule> modules) {
        double semesterQca = 0 ;
        for (StudentModule module : modules) {
            semesterQca += moduleGrade(module) ;
        }

        return semesterQca / modules.size() ;
    }

    /**
     *Gets the qca value for a module
     * @param module the module that we are get the qca
     * @return the qca of the module
     */
    public static double moduleGrade (StudentModule module) {
        
        //For calculating students overall percentage in the module
        double[] testWeightings = module.getTestWeightings();
        double[] grades = module.getGrades();
        
        //For converting the overall percentage to a QPV    
        HashMap<Double,Double> gradingScheme = new HashMap<String,Double>() ;
        String moduleGradingScheme = module.getGradingScheme();
        double[] gradeQPV = new double{4.0,3.6,3.2,3.0,2.8,2.6,2.4,2.0,1.6,1.2,0.0} ;

       
        //Fills HashMap QPV -> %
        //E.G           4.0 -> 80  4.O QPV is assigned to a grade greater than 80
        HashMap<Double, Double> gradingScheme = new HashMap<Double, Double>();
        for (int number = 0; number <= 11; number++) {
            gradingScheme.put(gradeQPV[number], Double.parseDouble(moduleGradingScheme.substring(number + 3, number + 5)));
        }

        //Gets the students final %
        double totalGrade = 0;
        for (int number = 0; number <= grades.length; number++) {
            totalGrade += grades[number] * (testWeightings[number]);

        }

        //Converts final grade into a percentage % -> QPV
        for (Map.Entry<Double,Double> entry: gradingScheme.entrySet()){
            if (entry.getValue() > totalGrade)
                return entry.getKey() ;
        }

        return 0;
    }

    /**
     * returns the average QCA for students in a module
     * @param teacherModule a list of
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

        /**
        * Checks if a module is passed or failed
         * @param module containing a students details relevant to their grades
         * @return true if module passed else false
        */
        public boolean failedModule(StudentModule module){
            double moduleQCA = moduleGrade(module) ;
            if (moduleQCA < 2.0)
                return false ;
            else 
                return true ;
        }

    /**
     * Checks if a semster is passed or failed
     * @param modules containing a students details relevant to their grades
     * @return true if semester passed else false
     */
    public boolean failedSemester(<StudentModule> modules){
        double SemesterQCA = semesterQca(modules) ;
        if (moduleQCA < 2.0)
            return false ;
        else
            return true ;
    }

}

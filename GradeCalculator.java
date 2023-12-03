import java.io.IOException;
import java.util.*;

public class GradeCalculator {
    //calculates students grades and QCA

    /**
     * calculates the qca of a semester
     * @param modules the modules taken by the student
     * @return a double representing the semesters QCA
     */
    public static double semesterQca(ArrayList<StudentModule> modules) {
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
        double[] testWeightings = module.getWeights();
        double[] grades = module.getGrades();

        String moduleGradingScheme = module.getGradingScheme();
        HashMap<String, Double> gradeQPV = new HashMap<String, Double>();
        gradeQPV.put("A1", 4.0);
        gradeQPV.put("A2", 3.6);
        gradeQPV.put("B1", 3.2);
        gradeQPV.put("B2", 3.0);
        gradeQPV.put("B3", 2.8);
        gradeQPV.put("C1", 2.6);
        gradeQPV.put("C2", 2.4);
        gradeQPV.put("C3", 2.0);
        gradeQPV.put("D1", 1.6);
        gradeQPV.put("D2", 1.2);
        gradeQPV.put("F", 0.0);

        //A1>85.0:A2>80.0:B1>75.0:B2>70.0:B3>65.0:C1>60.0:C2>55.0:C3>50.0:D1>45.0:D2>40.0:F>=0

       
        //Fills HashMap QPV -> %
        //E.G           4.0 -> 80  4.O QPV is assigned to a grade greater than 80
        HashMap<Double, Double> gradingScheme = new HashMap<Double, Double>();

        double lastLim = 100.0;

        for(String term : moduleGradingScheme.split(":")){
            String letterGrade = term.split("(?=>=|>)")[0];
            double percentGrade = Double.parseDouble(term.split(">")[1]);

            for(double i = lastLim; i > percentGrade; i -= 0.1){
                i = round(i);
                gradingScheme.put(i, gradeQPV.get(letterGrade));
            }
            lastLim = percentGrade;
        }

        //The loading part above won't add 0.0 to the hashmap because i > percentGrade so we do it by hand
        gradingScheme.put(0.0, 0.0);

        //Gets the students final %
        double totalGrade = 0;
        for (int number = 0; number < grades.length; number++) {
            totalGrade += grades[number] * (testWeightings[number]);

        }

        totalGrade = round(totalGrade);

        return gradingScheme.get(totalGrade);
    }

    /**
     * returns the average QCA for students in a module
     * @param teacherModule a list of
     */
    public static double averageQCA(TeacherModule teacherModule){
        int count = 0 ;
        double totalQca = 0 ;

        for (Map.Entry<String,double[]> entry : teacherModule.getGrades().entrySet()){
            StudentModule studentModule = new StudentModule() ;
            try {
                studentModule = (StudentModule) CSVEditor.getModule(teacherModule.getCSVName(), entry.getKey());
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            totalQca += moduleGrade(studentModule) ;
            count++ ;

        }

        return round(totalQca / count) ;
    }

    /**
     * returns the median QCA for students in a programme
     */
    public static double medianQCA(TeacherModule teacherModule){
        ArrayList<Double> QCAOfStudents = new ArrayList<>() ;

        for (Map.Entry<String,double[]> entry : teacherModule.getGrades().entrySet()){
            StudentModule studentModule = new StudentModule() ;
            try {
                studentModule = (StudentModule) CSVEditor.getModule(teacherModule.getCSVName(), entry.getKey());
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

            QCAOfStudents.add(moduleGrade(studentModule)) ;

        }

        Collections.sort(QCAOfStudents);
        if (QCAOfStudents.size() % 2 == 0){
            double num1 = QCAOfStudents.get(QCAOfStudents.size() / 2 - 1) ;
            double num2 = QCAOfStudents.get(QCAOfStudents.size() / 2 + 1) ;

            return round((num1 + num2) / 2) ;
        }else
            return round(QCAOfStudents.get(QCAOfStudents.size() / 2)) ;


    }


    /**
         * returns the average QCA for students in a programme
         * @param teacherModules a list of
         */
        public static double averageQCA(ArrayList<TeacherModule> teacherModules){
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
                    totalQca += moduleGrade(studentModule);
                    count++;

                }

                totalQca /= count ;

                totalQca1 += totalQca ;
                count1++ ;
            }

            return round(totalQca1 / count1) ;
        }

        /**
         * returns the median QCA for students in a programme
         */
        public static double medianQCA(ArrayList<TeacherModule> teacherModules) {

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

                QCAOfStudents.add(moduleGrade(studentModule));

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

                return round((num1 + num2) / 2);
            } else
                return  round(medianOfTeacherModules[medianOfTeacherModules.length / 2]);
        }

        /**
        * Checks if a module is passed or failed
         * @param module containing a students details relevant to their grades
         * @return true if module passed else false
        */
        public static boolean failedModule(StudentModule module){
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
    public static boolean failedSemester(ArrayList<StudentModule> modules){
        double SemesterQCA = semesterQca(modules) ;
        if (SemesterQCA < 2.0)
            return true ;
        else
            return false ;
    }

    private static double round(double val){
        long factor = (long) Math.pow(10, 1);
        val = val * factor;
        long tmp = Math.round(val);
        return (double) tmp / factor;
    }

}
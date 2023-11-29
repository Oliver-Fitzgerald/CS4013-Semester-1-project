import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.lang.NumberFormatException;
import java.util.zip.DataFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

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
        Scanner in = new Scanner(System.in);

        Student stu = null;

        //First we load the student by getting the student id
        //This may produce various errors, such as the student csv missing.
        //Or the student not being found. These are mostly handled.
        while(stu == null){
            try{
                System.out.println("Enter your student id: ");
                String stuIn = in.next();

                if(stuIn.equals("q"))
                    return;

                stu = CSVEditor.getStudent(stuIn);

                if(stu == null)
                    System.out.println("Student id not found, try again. q to quit");
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }

        while(true){
            //Now that we have student data we can let them view their data
            //To do this we print a menu
            System.out.println("Enter an option: \n1. View full transcript.\n2. View a module's transcipt.\nor press q to quit.");

            //Next given the user input we do what the menu said it does lol.
            String option = in.next();
            if(option.equals("1")){
                System.out.println(stu.getFullTranscript());
            }
            else if(option.equals("2")){
                //For this option we present another menu to the user
                //that includes all their modules.
                ArrayList<StudentModule> modules = stu.getModules();
                System.out.println("Enter the number corresponding to the module that you want to view.");
                int modIndex = 0;
                for(modIndex = 0; modIndex < modules.size(); modIndex++){
                    System.out.println(modIndex + ". " + modules.get(modIndex).getCSVName().replaceAll("_", " "));
                }
                int modSelected = -1;
                while(modSelected == -1){
                    try {
                       modSelected = Integer.parseInt(in.next());
                    }
                    catch(NumberFormatException e){
                        System.out.println("Please enter a valid option number.");
                    }
                }

                System.out.println(stu.getModuleTranscript(modules.get(modSelected).getCSVName()));
            }
            else if(option.equals("q"))
                break;
            else
                System.out.println("Enter a valid option.");
        }

    }

    /**
     * provides the functionality relevant to a teacher
     */
    private static boolean teacherMenu(){
        Scanner in = new Scanner(System.in) ;

        //Gets teacher
        System.out.println("Enter your faculty id:");
        Teacher teacher = new Teacher("") ;
        try {
            teacher = CSVEditor.getTeacher(in.next());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }


        //Gets module
        System.out.println("Enter your module code:");
        String moduleCode = in.next() ;
        System.out.println("Enter the year the module commenced:");
        String year = in.next() ;
        System.out.println("Enter a semester of the module:");
        String semester = in.next() ;

        String moduleCSVName = moduleCode + "_" + year + "_" + semester ;
        Module module = new Module() ;
        try {
            module = CSVEditor.getModule(moduleCSVName) ;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }






        //Asks user what they would like to do
         String[] choices = new String[]{
                            "get a modules result" ,
                            "get a students result" ,
                            "alter a students result" ,
                            "alter a tests weighting"};
        int command = getChoice(choices,1) ;


        if(command == 1){
            //Dictionary with student id mapped to the test results of the student relevant
            //to that module
            HashMap<String, double[]> studentTestResults =  ((TeacherModule) module).getGrades() ;

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
                //returns to a new line ready for the next student
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
            StudentModule student = new StudentModule() ;
            try {
                student = (StudentModule) CSVEditor.getModule(moduleCSVName, studentId);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            //gets an array containing the students results
            double[] grades = student.getGrades() ;

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
            Student student = new Student();
            try{
                student = CSVEditor.getStudent(studentId);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }


            //Get a choice of all the students tests

            try {
                StudentModule studentModule = (StudentModule) CSVEditor.getModule(moduleCSVName,studentId) ;
                double[] grades = studentModule.getGrades() ;
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
                String newResultString = "" + newResult ;

                CSVEditor.updateStudentGrades(student,studentModule,newResultString,test);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }


            return true ;
        }
        else if (command == 4) {
            //This asks how a user they would like to alter the tests
            choices = new String[]{"add a test", "remove a test",
                    "alter current tests weighting"};
            command = getChoice(choices,1);

            //adds a test
            if (command == 1){
                teacher.addTest((TeacherModule) module , teacher, module.getNumberOfTests());
                return true ;

            }
            //removes a test
            else if (command == 2){
                System.out.println("Enter test to be removed:");
                int testToBeRemoved = in.nextInt() ;
                teacher.removeTest((TeacherModule) module, teacher, testToBeRemoved) ;
                return true ;
            }
            //alters test weighting
            else if (command == 3) {
                //a menu to get input for test weightings
                alterTestInput((TeacherModule) module,teacher) ;
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
        Scanner in = new Scanner(System.in);
        //A board member won't have a login
        //Instead they will select a programme to view
        Programme prog = getProgrammeChoice();

        //Now that the board member has selected the programme that they want to view
        //They are given the programme menu that they can select options from
        while(true && prog != null){
            System.out.println("Please enter the number of the option you wish to view.");
            System.out.println("1. View programme statistics.\n2. View failing students.\n"
                                + "3. Alter a module's grading scheme\n4. Programme info overview.\n5. Add a module to the programme.\n or q to quit");

            String option = in.next();
            if(option.equals("1")){
                System.out.println(prog.getStatistics());
            }
            else if(option.equals("2")){
                System.out.println(prog.getFailingStudents());
            }
            else if(option.equals("3")){
                //This will get a new grading scheme as input
                //and change the relevant module csv to reflect the grading scheme
                //grading schemes are of the form A1>80:A2>72...F<30
                //this can also be(for a pass or fail module for example) be A1>0
                //F=0
                System.out.println("Enter the number corresponding to the module who's grading scheme you are updating: ");
                //We will present a module menu to the user so that they can select the module to be updated
                int moduleCounter = 1;
                ArrayList<TeacherModule> allMods = new ArrayList<TeacherModule>();
                for(Map.Entry<Integer, ArrayList<TeacherModule>> entry : prog.getSemesterModules().entrySet()){
                    System.out.println("Semester " + entry.getKey());
                    allMods.addAll(entry.getValue());
                    for(TeacherModule mod : entry.getValue()){
                        System.out.println(moduleCounter++ + ": " + mod.getCSVName().replaceAll("_", " "));
                    }
                }
                int selectedNum = -1;
                while(selectedNum == -1){
                    try{
                        selectedNum = Integer.parseInt(in.next());
                        if(selectedNum < 0 || selectedNum > allMods.size()){
                            System.out.println("Enter a valid option number.");
                            selectedNum = -1;
                        }
                    }
                    catch(NumberFormatException e){
                        System.out.println("Enter a valid option number.");
                    }
                }

                TeacherModule selectedModule = allMods.get(selectedNum - 1);

                //Now that we have a module in which to change the grading scheme we can get the relevant
                //grading scheme info to change.

                String updatedGradingScheme = getGradingScheme();

                //Now that we have the updated grading scheme we will change it in the module csv.
                try{
                    CSVEditor.updateGradingScheme(selectedModule, updatedGradingScheme);
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
            else if(option.equals("4")){
                System.out.println(prog.getProgDetails());
            }
            else if(option.equals("5")){
                //This will get the information from the user necessary to create a module and
                //add it to the system
                System.out.println("Enter the semester in which to insert the module: ");
                int modSemester = in.nextInt();

                TeacherModule toAdd = getTeacherModule();

                try{
                    CSVEditor.addModule(prog.getCode(), modSemester, toAdd);      
                }   
                catch(IOException e){
                    System.out.println(e.getMessage());
                }   
                catch(DataFormatException e){
                    System.out.println(e.getMessage());
                }
            }
            else if(option.equals("q")){
                break;
            }
            else{
                System.out.println("Please enter a valid option.");
            }
        }
    }

    private static Programme getProgrammeChoice(){
        Scanner in = new Scanner(System.in);
        Programme prog = null;

        while(prog == null){
            try {
                System.out.println("Enter the number corresponding to the programme to view.\nEnter a to enter admin mode.");
                String[] progNames = CSVEditor.getProgrammeNames();
                int i = 0;
                for(i = 0; i < progNames.length; i++){
                    System.out.println((i + 1) + ". " + progNames[i]);
                }
                String entered = in.next();
                if(entered.equals("a")){
                    adminMenu();
                    return null;
                }
                int optionSelected = Integer.parseInt(entered);
                if(optionSelected < 1 || optionSelected > progNames.length){
                    System.out.println("Enter a valid option number.");
                    continue;
                }
                //The argument is simply getting the programme code from the option selected.
                prog = CSVEditor.getProgramme(progNames[optionSelected - 1].split(" ")[0]);
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
            catch(NumberFormatException e){
                System.out.println("Please enter a valid option number.");
            }
        }

        return prog;
    }

    private static TeacherModule getTeacherModule(){
        Scanner in = new Scanner(System.in);

        TeacherModule outMod = new TeacherModule();

        while(true){
            System.out.println("Enter the module code: ");
            String modCode = in.next();
        
            in.nextLine();

            System.out.println("Enter module name: ");
            String modName = in.nextLine();

            System.out.println("Enter the year that the module takes place: ");
            int modYear = Integer.MIN_VALUE;
            while(modYear == Integer.MIN_VALUE){
                try{
                    modYear = Integer.parseInt(in.next());
                }
                catch(NumberFormatException e){
                    System.out.println("Please enter a valid number for the year.");
                }
            }

            in.nextLine();

            System.out.println("Enter which semester period the module takes place(spring or autumn): ");
            String semTime = in.next();
            while(!semTime.equals("spring") && !semTime.equals("autumn")){
                System.out.println("Please enter a valid semester period.");
                semTime = in.next();
            }
            int modSemester = semTime.equals("spring") ? 1 : 2;

            in.nextLine();

            System.out.println("Enter the id of the teacher teaching the class: ");
            String teacherID = in.next();

            in.nextLine();

            System.out.println("Enter how many credits the module is worth: ");
            double modCredits = Double.MIN_VALUE;
            while(modCredits == Double.MIN_VALUE){
                try{
                    modCredits = Double.parseDouble(in.next());
                }
                catch(NumberFormatException e){
                    System.out.println("Please enter a valid number for credits(e.g. 6.00, 5.40, 3.02");
                }
            }

            in.nextLine();

            String modGradingScheme = getGradingScheme();

            ArrayList<Double> modWeights = new ArrayList<Double>();

            HashMap<String, double[]> modGrades = new HashMap<String, double[]>();

            System.out.println("Enter the weighting for each test/assignment. Enter them one by one and once you reach the end enter 'done': ");
            while(true){
                System.out.println("Enter the weight: ");
                String weight = in.next();
                if(weight.equals("done"))
                    break;
                else
                    modWeights.add(Double.parseDouble(weight));
            }

            System.out.println("Lastly, we will insert students, and their grades if there are any. To skip this enter 'skip'. Enter 'done' when all the students have been added.");
            while(true){
                System.out.println("Enter the student id: ");
                String stuID = in.next();
                if(stuID.equals("skip"))
                    break;
                else if(stuID.equals("done"))
                    break;
                else{
                    System.out.println("Next enter the appropriate grades. The number of tests to be entered is the same as the number of weights entered. Enter 'skip' to skip this step or if there are no more test results to add.");
                    double[] studentGrades = new double[modWeights.size()];
                    for(int i = 0; i < studentGrades.length; i++){
                        String gradeIn = in.next();
                        in.nextLine();
                        if(gradeIn.equals("skip"))
                            break;
                            studentGrades[i] = in.nextDouble();
                        }
                    modGrades.put(stuID, studentGrades);
                }
            }

            System.out.println("Is this information correct(y/n): ");

            outMod = new TeacherModule(modCode, modName, modYear, modSemester, teacherID, modCredits, modGradingScheme, modWeights.stream().mapToDouble(Double::doubleValue).toArray(), modGrades);
                    
            System.out.println(outMod.toCSV());

            String correctInfo = in.next();

            if(correctInfo.equals("y"))
                break;
        }  

        return outMod;      
    }

    private static String getGradingScheme(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a grade bound(e.g. A1>80). Once you have entered all grade bounds input 'done'. ");
        StringBuilder gradingScheme = new StringBuilder();
        while(true){
            String gradeBound = in.next();
            if(gradeBound.equals("done"))
                break;
            else{
                gradingScheme.append(gradeBound + ":");
            }
        }

        gradingScheme.deleteCharAt(gradingScheme.length()-1);

        return gradingScheme.toString();
    }

    private static void adminMenu(){
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println("Please enter the number of the option you wish to view.");
            System.out.println("1. Add a student to the system.\n2. Add a teacher to the system.\n3. Add a module to the system.\n4. Add a programme to the system.\n or enter 'q' to quit");
            String option = in.next();
            if(option.equals("1")){
                try{
                    Student stu = getStudentChoice();
                    CSVEditor.addStudent(stu);
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
            else if(option.equals("2")){
                System.out.println("Enter the teacher's id");
                String teacherID = in.next();

                System.out.println("Next we will add the modules that the teacher teaches. Enter 'done' when all modules have been entered.");
                ArrayList<TeacherModule> teachModules = new ArrayList<TeacherModule>();

                Teacher teach = new Teacher(teacherID, teachModules);
                try{
                    CSVEditor.addTeacher(teach);
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
            else if(option.equals("3")){

                System.out.println("Enter the programme Code: ");
                String progCode = in.next();

                in.nextLine();

                System.out.println("Enter the semester in the programme that the module is being added to: ");
                int modSemester = in.nextInt();

                in.nextLine();

                TeacherModule mod = getTeacherModule();

                try{
                    CSVEditor.addModule(progCode, modSemester, mod);
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
                catch(DataFormatException e){
                    System.out.println(e.getMessage());
                }
            }
            else if(option.equals("4")){

                Programme prog = getProgramme();

                try{
                    CSVEditor.addProgramme(prog);
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
                catch(DataFormatException e){
                    System.out.println(e.getMessage());
                }
            }
            else if(option.equals("q")){
                break;
            }
        }
    }

    private static Programme getProgramme(){
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the program code: ");
        String progCode = in.next();

        in.nextLine();

        System.out.println("Enter the program name: ");
        String progName = in.nextLine();

        System.out.println("Enter the year that the program starts of the format yyyy/yy(e.g 2023/24. If it starts in the spring enter 2023/23 for example.)");
        String progYear = in.next();

        in.nextLine();

        System.out.println("Next we will be creating the modules in the programme: Enter 'done' when all modules are added");
        HashMap<Integer, ArrayList<TeacherModule>> progSemesterModules = new HashMap<Integer, ArrayList<TeacherModule>>();
        while(true){
            System.out.println("Enter the semester that the modules will be added to: ");
            String modInput = in.next();
            in.nextLine();
            if(modInput.equals("done"))
                break;

            int modSemesterNumber = Integer.parseInt(modInput);

            System.out.println("Now we will be entering the modules that take place in this semester.");
            System.out.println("Enter the number of modules in the semester: ");
            int modNumber = in.nextInt();
            in.nextLine();

            ArrayList<TeacherModule> modulesInSemester = new ArrayList<TeacherModule>();
            for(int i = 0; i < modNumber; i++){
                modulesInSemester.add(getTeacherModule());
            }
            progSemesterModules.put(modSemesterNumber, modulesInSemester);
        }

        return new Programme(progCode, progName, progYear, progSemesterModules);
    }

    private static Student getStudentChoice(){
        Scanner in = new Scanner(System.in);

        int stuCurSemester = 1;

        System.out.println("Please enter the student's id: ");
        String stuID = in.next();

        in.nextLine();

        System.out.println("Please enter the programme id that they will be attending: ");
        String stuProgID = in.next();

        //Now we retrieve the modules from the programme that the student will attend.
        ArrayList<StudentModule> stuModules = new ArrayList<StudentModule>();

        Programme stuProgramme = null;
        try{
            stuProgramme = CSVEditor.getProgramme(stuProgID);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Next we will be inputting the students grades for the programme they are entering.\n If there are no grades to add enter 'skip'.");

        String enterGradesChoice = in.next();

        in.nextLine();
        
        for(TeacherModule teachMod : stuProgramme.getAllModules()){
            double[] modGrades = null;

            if(!enterGradesChoice.equals("skip")){
                System.out.println("Entering a total of " + teachMod.getWeights().length + " grades for " + teachMod.getCode() + ". If there are no grades enter 'skip'.");
                String curModGradeChoice = in.next();

                in.nextLine();

                

                if(!curModGradeChoice.equals("skip")){
                    modGrades = new double[teachMod.getWeights().length];
                    for(int i = 0; i < teachMod.getWeights().length; i++){
                        System.out.println("Enter the grade for test " + (i + 1) + ": ");
                        modGrades[i] = Double.MIN_VALUE;
                        while(modGrades[i] == Double.MIN_VALUE){
                            try{
                                String gradeInput = in.next();
                                in.nextLine();
                                modGrades[i] = Double.parseDouble(gradeInput);
                            }
                            catch(NumberFormatException e){
                                System.out.println("Please enter a valid grade(e.g. 89.9, 78.6, 98.5)");
                            }
                        }
                    }
                }
            }
            stuModules.add(new StudentModule(teachMod.getModuleFromCurrent(), modGrades));
        }

        return new Student(stuID, stuProgID, stuProgramme.getName(), stuProgramme.getYear(), stuCurSemester, stuModules);
    }

    public static int getChoice(String[] options,int format){
        Scanner in = new Scanner(System.in) ;
        int choice = 0 ;

        if (format == 1)
            System.out.println("What would you like to do:");

        if (format == 2)
            System.out.println("Choose a test");

        for (int number = 0; number < options.length; number++){

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

    public static boolean alterTestInput(TeacherModule module,Teacher teacher){
        Scanner scanner = new Scanner(System.in) ;
        double[] newTestWeightings = new double[module.getWeights().length] ;

        //enter weighting of each test one by one
        for (int num = 0; num < module.getWeights().length; num++){
            System.out.print("Enter weighting for test " + num + ": ");
            newTestWeightings[num] = scanner.nextDouble() ;
            System.out.println(newTestWeightings[num]);
        }

        try{
            teacher.alterTestWeighting(newTestWeightings, module) ;
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        return true ;
    }
}
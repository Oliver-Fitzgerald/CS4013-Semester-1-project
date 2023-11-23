import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class CSVEditor {

	private static final String csvPath = "./csv/";
	private static final String studentPath = csvPath + "Students.csv";
	private static final String teacherPath = csvPath + "Teachers.csv";
	private static final String programmePath = csvPath + "Programmes.csv";

	/**
	 * Reads the csv's and retrieves the relevant students information.
	 * Also throws an IOException if a csv file isn't found. 
	 * The file not found is noted in the IOException's message.
	 * @param stuId The students ID
	 * @return A Student object loaded with the relevant information.
	 **/ 
	public static Student getStudent(String stuID) throws IOException{
		//The haves and needs below were for myself so that I knew what data I have and what data I still need.

		//have: stuID
		//need: progID, progName, startYear, curSemester, StudentModules

		//Here we initialize the arguments for the Student Object.
		String progID = "";
		String progName = "";
		String startYear = "";
		int curSemester = 0;
		ArrayList<StudentModule> modules = new ArrayList<StudentModule>();

		//First we open the students.csv to retrieve the basic student info based on the id(progID, startYear, curSemester#)
		//The try catch is necessary because a non-existant file throws an IOException
		try(BufferedReader studBR = new BufferedReader(new FileReader(studentPath))){
			//We read the file line by line until the end of the file is reached or the student is found.

			String line;
			while((line = removeInvisibleCharacters(studBR.readLine())) != null){
				//We split each line by comma to find each data point.
				//The structure of the lines in students is studentID, programmeID, startYear, and current semester
				String[] vals = line.split(",");
				if(vals[0].equals(stuID)){
					progID = vals[1];
					startYear = vals[2];
					curSemester = Integer.parseInt(vals[3]);
					break;
				}
			}
		} 
		catch(IOException e){
			throw new IOException("Students.csv not found");
		}

		//need: progName, modules
		//Next we go to the programmes.csv file to retrieve the name and load the module data into Module objects
		//This also means we need to access each module file in this.
		try(BufferedReader progBR = new BufferedReader(new FileReader(programmePath))){

			//Again we read the Programme csv line by line or until we find the necessary programme.
			String line;

			while((line = removeInvisibleCharacters(progBR.readLine())) != null){
				
				//The general format of a line in programmes.csv is programme code, programme name, start year, semester #:module code:module code:, ...
				String[] progVals = line.split(",");

				//The two things that are unique to each programme are their id and their start year so we check for that
				if(progVals[0].equals(progID) && progVals[2].equals(startYear)){
					progName = progVals[1];

					//This is where we run through the modules in each semester.
					//We start at 3 because the modules per semester start at the third index.
					for(int i = 3; i < progVals.length; i++){

						//Here we split the modules in each semester entry into a string array and store the semester number for processing.
						String[] moduleMetaData = progVals[i].split(":");
						int semester = Integer.parseInt(moduleMetaData[0]);

						//In this for loop we go through each module and get the necessary information from the correct module csv
						//The format for the module csv title is "module code"_year_semester.csv
						//The year and semester present here differ from the students start year and current semester number
						//This is because the module year is the year it takes place e.g. 2023 and the semester is 1 or 2, 
						//basically which part of the year the module took place. 1 is counted as the spring semester
						//and 2 is counted as the fall semester.
						for(int j = 1; j < moduleMetaData.length; j++){
							
							//As described before we must convert from the students starting year and which semester the module took place
							//to the modules correct year and semester.
							int[] modNameMD = calculateModFromProg(startYear, semester);

							//Creates the name of the correct module csv file as described above.
							String name = moduleMetaData[j] + "_" + modNameMD[0] + "_" + modNameMD[1];

							//Now that we have some of the modules meta data we can find the file and read the necessary information into
							//a StudentModule object.
							modules.add((StudentModule) getModule(name, stuID));
						}
					}
					break;
				}
			}
		} 
		catch(IOException e){
			if(!e.getMessage().contains("not found"))
				throw new IOException("Programmes.csv not found.");
			else
				throw e;
		}

		return new Student(stuID, progID, progName, startYear, curSemester, modules);
	}

	/**
	 * Reads the csv's and retrieves the relevant teachers information.
	 * Also throws an IOException if a csv file isn't found. 
	 * The file not found is noted in the IOException's message.
	 * @param teacherID The teacher's ID
	 * @return A Teacher object loaded with the relevant information.
	 **/
	public static Teacher getTeacher(String teacherID) throws IOException{

		//Since we have the teacherID all we need to do is load the modules.
		ArrayList<TeacherModule> modules = new ArrayList<TeacherModule>();

		//To load the module we load the csv 
		try(BufferedReader teachBR = new BufferedReader(new FileReader(teacherPath))){
			String line;
			while((line = teachBR.readLine()) != null){
				line = removeInvisibleCharacters(line);

				String[] teachVal = line.split(",");
				if(teachVal[0].equals(teacherID)){
					for(int i = 1; i < teachVal.length; i++){
						modules.add((TeacherModule) getModule(teachVal[i]));
					}
				}
			}

		}
		catch(IOException e){
			if(!e.getMessage().contains("not found"))
				throw new IOException("Programmes.csv not found.");
			else
				throw e;
		}

		return new Teacher(teacherID, modules);
	}

	// public static Programme getProgramme(String progName){

	// }

	// public static void updateGradingScheme(Module mod, String gradingScheme){

	// }

	// public static void updateStudentGrades(Student stu, String grade, int testIndex){

	// }

	// public static void addModule(String progName, Module mod){

	// }

	// public static void addProgramme(Programme prog){

	// }

	/**
	 * Creates a TeacherModule from the relevant csv file.
	 * This differs fromt the getModule accepting a student id because a teacher
	 * needs access to all grades while a student should only have access to their individual
	 * grades.
	 * @param name The name of the module csv file.
	 * @return A TeacherModule with the full module information.
	 **/
	public static Module getModule(String name) throws IOException{
		try(BufferedReader modBR = new BufferedReader(new FileReader(csvPath + name + ".csv"))){
			String modLine;
			int lineCount = 0;

			//Stands for module meta data
			String[] modMD = name.split("_");

			//Since we are making a TeacherModule object we initialize the parameters here,
			//some null as we don't have the data yet and others not as we do have the data.
			String modCode = modMD[0];
			String modName = "";
			int modYear = Integer.parseInt(modMD[1]);
			int modSemester = Integer.parseInt(modMD[2]);
			double modCredits = 0.0;
			String modGradingScheme = "";
			double[] modWeights = null;
			HashMap<String, double[]> modGrades = new HashMap<String, double[]>();

			//Here we read through the module file.
			//The structure of the module file is split into separate lines, hence the need for the lineCount variable.
			//The general structure is:
			//1> module name, grading scheme, teacher id, number of credits
			//2> weight for test 1, weight 2, weight 3, ...
			//3> student ID, grade, grade, grade, ...
			//The following grades are for each student in the module.
			while((modLine = modBR.readLine()) != null){
				String[] modVals = modLine.split(",");

				//So then, as described above, we load the relevant data into the relevant variables
				if(lineCount == 0){
					modName = modVals[0];
					modGradingScheme = modVals[1];
					modCredits = Double.parseDouble(modVals[3]);
				}
				else if(lineCount == 1){
					modWeights = new double[modVals.length];
					for(int k = 0; k < modVals.length; k++){
						modWeights[k] = Double.parseDouble(modVals[k]);
					}
				}
				else {
					String stuID = modVals[0];
					double[] curGrades = new double[modVals.length-1];

					for(int k = 1; k < modVals.length; k++){
						curGrades[k - 1] = Double.parseDouble(modVals[k]);
					}
					modGrades.put(stuID, curGrades);
				}
				lineCount++;
			}
			return new TeacherModule(modCode, modName, modYear, modSemester, modCredits, modGradingScheme, modWeights, modGrades);
		}
		catch(IOException e){
			throw new IOException(name + ".csv not found.");
		}
	}

	/**
	 * Returns a Module containing a single students grades by accessing the modules csv file.
	 * @param name The name of the csv file for the module.
	 * @param stuID The student's id for which to get grades.
	 * @return A StudentModule Object with the relevant student's information.
	 **/
	public static Module getModule(String name, String stuID) throws IOException{
		try(BufferedReader modBR = new BufferedReader(new FileReader(csvPath + name + ".csv"))){
			String modLine;
			int lineCount = 0;

			//Stands for module meta data
			String[] modMD = name.split("_");

			//Since we are making a StudentModule object we initialize the parameters here,
			//some null as we don't have the data yet and others not as we do have the data.
			String modCode = modMD[0];
			String modName = "";
			int modYear = Integer.parseInt(modMD[1]);
			int modSemester = Integer.parseInt(modMD[2]);
			double modCredits = 0.0;
			String modGradingScheme = "";
			double[] modWeights = null;
			double[] modGrades = null;

			//Here we read through the module file.
			//The structure of the module file is split into separate lines, hence the need for the lineCount variable.
			//The general structure is:
			//1> module name, grading scheme, teacher id, number of credits
			//2> weight for test 1, weight 2, weight 3, ...
			//3> student ID, grade, grade, grade, ...
			//The following grades are for each student in the module.
			while((modLine = modBR.readLine().replaceAll("\"","")) != null){
				String[] modVals = modLine.split(",");

				//So then, as described above, we load the relevant data into the relevant variables
				if(lineCount == 0){
					modName = modVals[0];
					modGradingScheme = modVals[1];
					modCredits = Double.parseDouble(modVals[3]);
				}
				else if(lineCount == 1){
					modWeights = new double[modVals.length];
					modGrades = new double[modVals.length];
					for(int k = 0; k < modVals.length; k++){
						modWeights[k] = Double.parseDouble(modVals[k]);
					}
				}
				else {
					if(modVals[0].equals(stuID)){
						for(int k = 1; k < modVals.length; k++){
							modGrades[k - 1] = Double.parseDouble(modVals[k]);
						}
						break;
					}
				}
				lineCount++;
			}

			return new StudentModule(modCode, modName, modYear, modSemester, modCredits, modGradingScheme, modWeights, modGrades);
		}
		catch(IOException e){
			throw new IOException(name + " not found.");
		}
	}

	/**
	 * Turns the start year(yyyy/yy e.g. 2023/24) and semester # e.g.(2, 3, 4, ...)
	 * into the module naming scheme values year e.g.(2023) and semester e.g. (1, 2).
	 * It also takes into accounts students that may have started in the spring semester 
	 * which looks like, for example, 2023/23.
	 * @param startYear The academic year the student started. e.g.(2023/24, 2022/23) or (2022/22, 2023/23)
	 * @param semester The semester the student is or was in e.g(1, 2, 3, 4, ...)
	 * @return An int with two values, the year the module took place, and which semester(1 or 2) respectively.
	 **/
	private static int[] calculateModFromProg(String startYear, int semester){
		//The solution I chose to implement was to find the starting year(out[0]) and which part of the year its in(out[1])
		//From there we decrement semester while incrementing out[1]. if out[1] becomes greater than 2, it resets to 0 and
		//increments the year out[0].
		int[] out = new int[2];

		int firstYear = Integer.parseInt(startYear.substring(0, 4));

		//Finds if the start year was during the spring or autumn semester
		boolean springStart = startYear.substring(2, 4).equals(startYear.substring(5, 7));

		out[0] = firstYear;
		out[1] = springStart ? 1 : 2;

		while(semester > 1){
			out[1]++;
			if(out[1] > 2){
				out[0]++;
				out[1] = 1;
			}
			semester--;
		}

		return out;
	}
	/**
	 * This is a method used when reading data from csv files to ensure there are no invisible characters.
	 * @param input the input string to be checked.
	 * @return The corrected string with no invisible characters.
	 **/
	//The fact that I even need this method tells you a lot about my time with this project
	private static String removeInvisibleCharacters(String input) {
        // Use a regular expression to replace all invisible characters with an empty string
        return input.replaceAll("\\p{C}", "");
    }
}
//This file created by the wizard himself, Ben, student id 22360255.

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.lang.Exception;
import java.util.zip.DataFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.Map;


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

	/**
	 * Reads through the csv's and loads the relevant programme's data.
	 * This data is then loaded into a Programme object which is returned.
	 * @param progID The programme's id
	 * @return A Programme object loaded with the relevant information.
	 **/
	public static Programme getProgramme(String progID) throws IOException{
		String progName = "";
		String progYear = "";
		HashMap<Integer, ArrayList<TeacherModule>> progModules = new HashMap<Integer, ArrayList<TeacherModule>>();

		try(BufferedReader progBR = new BufferedReader(new FileReader(programmePath))){
			String line;
			while((line = progBR.readLine()) != null){
				line = removeInvisibleCharacters(line);
				String[] progVals = line.split(",");
				if(progVals[0].equals(progID)){
					progName = progVals[1];
					progYear = progVals[2];

					//Now we get the modules in each semester and add them to the hashmap.
					//We include which semester of the programme it is as the key and
					//we load the modules data into an arraylist of each one
					for(int i = 3; i < progVals.length; i++){
						String[] semVals = progVals[i].split(":");
						int semNum = Integer.parseInt(semVals[0]);
						ArrayList<TeacherModule> semMods = new ArrayList<TeacherModule>();
						for(int j = 1; j < semVals.length; j++){
							int[] convertedModVals = calculateModFromProg(progYear, semNum);
							semMods.add((TeacherModule) getModule(semVals[j] + "_" + convertedModVals[0] + "_" + convertedModVals[1]));
						}
						progModules.put(semNum, semMods);
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

		return new Programme(progID, progName, progYear, progModules);
	}

	/**
	 * Takes a grading scheme and replaces the grading scheme in the specified module with it.
	 * @param mod The module in which to make the changes.
	 * @param gradingScheme The grading scheme to change it to.
	 **/
	public static void updateGradingScheme(Module mod, String gradingScheme) throws IOException{
		//Here we get the path to the module's csv
		String path = csvPath + mod.getCSVName() + ".csv";

		try{
			//Knowing the path we read the contents of the file into a String
			String fileContents = readWholeFile(path);
			
			//We convert the file's contents into a string array splitting on the commas in order to find the grading scheme
			//to switch. We don't need to worry about splitting on newline character because the grading scheme is on the
			//first line. At the end we join the arrays contents back into a string, inserting commas between the strings.
			String[] splitFile = fileContents.split(",");
			splitFile[1] = gradingScheme;
			fileContents = String.join(",", splitFile);

			//Finally, we write the modified file contents to the file.
			writeFile(fileContents, path);
		}
		catch(IOException e){
			throw e;
		}
	}

	/**
	 * Updates a selected student's grades. The test details to be updated include the test index(starting at 1)
	 * as that matches user input(test 1, test 2, test 3...). The grade entered will be a letter grade(A1, A2).
	 * Other grade types will be stored as this, however the grade change will happen elsewhere.(if we add pass or fail
	 * then A1 or F would be passed to this function.
	 * @param stu The student who's grades are being changed.
	 * @param mod The module that the grade is in to be changed.
	 * @param grade the grade to be updated to.
	 * @param testIndex the test number to be changed(1, 2, 3, ...)
	 **/
	public static void updateStudentGrades(Student stu, Module mod, String grade, int testIndex) throws IOException{
		//As we can't update file contents and instead have to rewrite them in their entirety the strategy
		//for this method will be to get the contents of the module in which the grade is stored.
		//We will then find the relevant data field to change, put all the data back together and rewrite it
		//to the file.
		String path = csvPath + mod.getCSVName() + ".csv";

		try{
			//We get the contents of the module file.
			String fileContents = readWholeFile(path);

			//Now that we have the contents of the file we need to change the relevant data.
			//To do this we will split the contents into a string[] as array indexes make it easier
			//to work with. 
			String[] splitFile = fileContents.split("[,|\n]");

			//Once we've split the file we iterate through until finding the student.
			//We start at index 4 because indices 0-4 are module metadata.
			//4 onward is still metadata because it is test weights, however
			//there could be an arbitrary number of test weights so well just begin the search there.
			for(int i = 4; i < splitFile.length; i++){
				if(splitFile[i].equals(stu.getID())){
					//Once we've found the student then me modify the correct grade
					//which is going to be at the index found(student's id) + the testIndex
					//starting at 1!!! because if we add 0 then it'll just be the student id
					//if you get what i mean. I've chugged two coffees if you can't tell.
					splitFile[i + testIndex] = grade;
					break;
				}
			}

			//Next we have to join the string together, however unlike above, we also have to add in the new lines.
			//Were using a StringBuilder because i'm fancy like that and also because i want to be able to
			//remove characters
			StringBuilder finalString = new StringBuilder();
			for(int i = 0; i < splitFile.length; i++){
				//Module name might have period hence the i != 0.
				//Making this check because the newline is inserted after index 3
				//and otherwise always after a double when a student id is hit
				if(i == 3){
					finalString.append(splitFile[i] + "\n");
				}
				//we make this first if because if i is 0 then it will cause an array out of bounds exception
				//not the best coding practice but we make do
				//Other than that we check if the string contains numbers and if the entry before it is a double
				//if so then the current value is a student id, meaning we have to remove the previous comma
				//and we have to add a newline
				else if(i != 0 && isValidStuID(splitFile[i])){
					if(isDouble(splitFile[i-1])){
						finalString.deleteCharAt(finalString.length()-1);
						finalString.append("\n" + splitFile[i] + ",");
					}
					else
						finalString.append(splitFile[i] + ",");
				}
				else {
					finalString.append(splitFile[i] + ",");
				}
			}
			finalString.deleteCharAt(finalString.length()-1);
			//Now that we have our corrected file contents we just write it back to the file
			writeFile(finalString.toString(), path);

		}
		catch(IOException e){
			throw e;
		}
	}

	/**
	 * Updates the weights in the corresponding csv.
	 * @param mod The module in which to change the weightings.
	 * @param weights The new weights to set.
	 */
	public static void updateWeightings(Module mod, double[] weights) throws IOException{
		String path = csvPath + mod.getCSVName() + ".csv";
		String fileContents = readWholeFile(path);
		String[] splitLines = fileContents.split("\n");
		StringBuilder newWeightsLine = new StringBuilder();
		for(double weight : weights){
			newWeightsLine.append("," + weight);
		}
		newWeightsLine.deleteCharAt(0);
		splitLines[1] = newWeightsLine.toString();
		fileContents = String.join("\n", splitLines);
		writeFile(fileContents, path);
	}

	/**
	 * This method adds a Student to the storage system. It does so by accepting a student object.
	 * A new student obviously won't have any grades yet, however it still requires an arraylist of studentmodules.
	 * To work around this the method assumes that the arraylist is correctly loaded with relevant modules,
	 * However it will have incomplete or null information. The only data neccessary in the arraylist of studentmodules
	 * is the id, year and semester it takes place. Name, credits, grading scheme and weights can be null. 
	 * Alternatively, the grades may or may not be null. if they are null then the student is loaded with 0's
	 * and if the student has grades already loaded then they will be added accordingly.
	 * @param stu The student to be added to the system.
	 **/
	public static void addStudent(Student stu) throws IOException{
		//This method affects multiple csv's, namely the students csv as well as all the module
		//csvs that the student is present in.
		try{
			//First we'll make the relevant changes to the student csv.
			writeFile(readWholeFile(studentPath) + stu.getStudentAsCSVLine(), studentPath);

			//Next we'll make the relevant changes to the module csv's
			for(StudentModule mod : stu.getModules()){

				//First we get the path to the module
				String path = csvPath + mod.getCSVName() + ".csv";

				//Next we create the line that is going to be written to the end of the csv
				//This is either the grades present in the module, or, since it can be null
				//0's.
				String toWrite = stu.getID();

				if(mod.getGrades() != null){
					toWrite = toWrite + "," + mod.getGradesString();
				}
				else{
					for(int i = 0; i < mod.getNumberOfTests(); i++){
						toWrite = toWrite + ",0.0";
					}
				}
				writeFile(readWholeFile(path) + toWrite, path);

			}
		}
		catch(IOException e){
			throw e;
		}

	}

	/**
	 * Adds a teacher to the relevant csv's, namely the teachers.csv. When adding the teacher it is assumed that the 
	 * modules haven't been created yet, and such adding a teacher will not affect any of the module csvs. That being
	 * said the modules in the teacher class still have to be loaded minimally(id), so that the relevant information
	 * can be added to the teacher csv.
	 * @param teach The teacher to add to the system. The modules in this object may be null.
	 */
	public static void addTeacher(Teacher teach) throws IOException{
		//The strategy for this method will be to retrieve the contents of the teachers csv
		//then appending the teachers information to it, and writing it back to the csv.
		try{
			String fileContents = readWholeFile(teacherPath);
			String toAdd = teach.getTeacherAsCSVLine();
			writeFile(fileContents+toAdd, teacherPath);
		}
		catch(IOException e){
			throw e;
		}
	}

	/**
	 * This affects multiple csv's, those being the Progamme csv and creates a module csv.
	 * @param progName The code of the programme that the module is being added to.
	 * @param modSemester The semester number that the module is in the program.
	 * @param mod The module to be added to the records. This may or may not have students as
	 * student can be added later, however it must have a teacher. This is also specifically
	 * of type TeacherModule because it can have one or more student grades.
	 */
	public static void addModule(String progCode, int modSemester, TeacherModule mod) throws IOException, DataFormatException{
		//First we will be adding the module to the programme specified
		try{
			//To do this we will get the file contents and then split it on both 
			//newlines to check each programme. After that we split the string again
			//so we can modify the corresponding semester to include the module
			String progContents = removeInvisibleCharacters(readWholeFile(programmePath));
			String[] progLines = progContents.split("\n");
			for(int i = 0; i < progLines.length; i++){
				String[] progVals = progLines[i].split(",");
				if(progVals[0].equals(progCode)){
					System.out.println("in");
					//Once we have found the correct line we must add the module to the relevant semester
					//We assume that the correct number of semesters are already loaded into the programme.
					if(modSemester > progVals.length - 3)
						throw new DataFormatException("The semester number entered exceeds the number of semesters in the programme.");
					for(int j = 3; j < progVals.length; j++){
						//From here we split the semesters into individual values to find the correct semester.
						String[] workingSemester = progVals[j].split(":");
						if(Integer.parseInt(workingSemester[0]) == modSemester){
							//Lastly we join the split semester line back and append the added module.
							//From there we work our way back up the split strings until we have modified
							//the variable containing each programme line.
							String changed = String.join(":", workingSemester);
							changed = changed + ":" + mod.getCode();
							progVals[j] = changed;
							progLines[i] = String.join(",", progVals);
							break;
						}
					}
					break;
				}
			}
			//Finally we write the modified contents back to the programme file.
			progContents = String.join("\n", progLines);
			writeFile(progContents, programmePath);

			//Next we have to create the module file. As the filereader creates a file if it doesnt already
			//exist we simply have to pass it the path of the new module csv.
			writeFile(mod.toCSV(), csvPath+mod.getCSVName()+".csv");
		}
		catch(IOException e){
			throw e;
		}
		catch(DataFormatException e){
			throw e;
		}
	}

	/**
	 * Adds a complete program to the csv's. This takes a completely filled program class and makes all the changes neccessary.
	 * Including creating the modules. What it doesn't do is add teachers or students as these are considered separate from the 
	 * programme, even though they are included in the data. This is because the programme and its aggregate classes don't
	 * have full data on teachers and students, as well as the fact that teachers may already be present before a programme
	 * is created and students as well.
	 * @param prog The programme to be added to the system.
	 */
	public static void addProgramme(Programme prog) throws IOException, DataFormatException{
		//This method will affect multiple csv's those being the programme csv and the individual module csvs.
		try{
			//First we modify the programme csv by appending the programme meta data at the end.
			writeFile(readWholeFile(programmePath)+ prog.getProgAsCSVLine(), programmePath);

			//Next we must create the module files for each teachermodule in the programme
			for(Map.Entry<Integer, ArrayList<TeacherModule>> entry : prog.getSemesterModules().entrySet()){
				for(TeacherModule mod : entry.getValue()){
					addModule(prog.getCode(), entry.getKey(), mod);
				}
			}
		}
		catch(IOException e){
			throw e;
		}
		catch(DataFormatException e){
			throw e;
		}
	}

	/**
	 * Used in the interface to select allow a board member to select a programme without loading all the programmes.
	 * @return a String[] containing the programme id and name.
	 */
	public static String[] getProgrammeNames() throws IOException{
		ArrayList<String> programmeNames = new ArrayList<String>();
		String fileContents = readWholeFile(programmePath);
		String[] progLines = fileContents.split("\n");
		for(String line : progLines){
			String[] progVals = line.split(",");
			programmeNames.add(progVals[0] + " " + progVals[1]);
		}

		return programmeNames.toArray(String[]::new);
	}
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
			String teacherID = "";
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
					teacherID = modVals[2];
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
			return new TeacherModule(modCode, modName, modYear, modSemester, teacherID, modCredits, modGradingScheme, modWeights, modGrades);
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

	//Utility method that returns a string with all of a files contents. Used in update methods.
	private static String readWholeFile(String path) throws IOException {
		String fileContents = "";
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line;
			while((line = br.readLine()) != null){
				fileContents = fileContents + line + "\n";
			}
		}
		catch(IOException e){
			throw new IOException(path + " not found.");
		}

		return fileContents;
	}

	//Utility method that writes a string to a file
	private static void writeFile(String fileContents, String path) throws IOException{
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
			bw.write(fileContents);
		}
		catch(IOException e){
			throw new IOException("Couldn't write to " + path);
		}
	}

	//Utility method that checks if a string is a double or not
	private static boolean isDouble(String toCheck){
		try{
			Double.parseDouble(toCheck);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}

	private static boolean isValidStuID(String toCheck){
		if(toCheck.matches(".*\\d.*") && toCheck.length() == 8)
			return true;
		return false;
	}

	//This is a method used when reading data from csv files to ensure there are no invisible characters.
	//The fact that I even need this method tells you a lot about my time with this project
	private static String removeInvisibleCharacters(String input) {
        // Use a regular expression to replace all invisible characters except newlines with an empty string
        return input.replaceAll("[\\p{C}&&[^\r\n]]", "");
    }
}
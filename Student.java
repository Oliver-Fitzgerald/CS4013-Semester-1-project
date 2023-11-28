import java.lang.StringBuilder;
import java.util.ArrayList;

public class Student {
	private String id;
	private String progID;
	private String progName;
	private String startYear;
	private int curSemester; //e.g. 1, 2, 3, 4...
	private ArrayList<StudentModule> modules;

	/**
	 * Student Constructor.
	 * @param id The student's id.
	 * @param progID The id of the programme the student is in.
	 * @param progName The name of the programme that the student is in.
	 * @param startYear The year that the student started, of format yyyy/yy (e.g. 2020/21, 2022/23)
	 * @param curSemester The current semester that the student is in(1, 2, 3, 4, ...).
	 * @param modules A list of StudentModule that represent all the module data of an individual student.
	 **/
	public Student(String id, String progID, String progName, String startYear, int curSemester, ArrayList<StudentModule> modules){
		this.id = id;
		this.progID = progID;
		this.progName = progName;
		this.startYear = startYear;
		this.curSemester = curSemester;
		this.modules = modules;

	}

	public Student(){
		this.id = "";
		this.progID = "";
		this.progName = "";
		this.startYear = "";
		this.curSemester = 0;
		this.modules = new ArrayList<StudentModule>();
	}

	/**
	 * toString method used for testing.
	 **/
	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append("ID: " + id + "\n");
		sb.append("In Programme: " + progID + " " + progName + "\n");
		sb.append("Year started and current semester: " + startYear + " " + curSemester + "\n");
		sb.append("Modules:\n");
		for(StudentModule mod : modules){
			sb.append(mod.toString() + "\n");
		}
		return sb.toString();
	}

	public String getID(){
		return this.id;
	}

	public String getStudentAsCSVLine(){
		return this.id + "," + this.progID + "," + this.startYear + "," + this.curSemester;
	}

	public ArrayList<StudentModule> getModules(){
		return this.modules;
	}

	// the idea of this method is that it will the students full transcript when its called in the interface
	/*
	method returns the transcript as follows:
	id, progID, progName, StudentModule"\n"
	-
	-     this is the studentmodule "\n"
	-
	 */
	public String getFullTranscript(){
		StringBuilder stuFullTScript = new StringBuilder();

		stuFullTScript.append("ID: " + id + ", ");
		stuFullTScript.append("Programme ID: " + progID + ", ");
		stuFullTScript.append("Programme Name: " + progName + ".");
		for(StudentModule module : this.modules){
			stuFullTScript.append(module.getCSVName() + ": ");
			for(double d : module.getGrades()){
				stuFullTScript.append(d + ", ");
			}
			stuFullTScript.deleteCharAt(stuFullTScript.length()-1);
			stuFullTScript.deleteCharAt(stuFullTScript.length()-1);
			stuFullTScript.append("\n");
		}

		return stuFullTScript.toString();
	}
	
	// this method aims to return a module the student wants to view
	public String getModuleTranscript(String modCode){
		StringBuilder stuModTScript = new StringBuilder();
		for(StudentModule module : this.modules){
			if(modCode.equals(module.getCSVName())){
				stuModTScript.append("Module: " + modCode + ", ");
			}
			stuModTScript.append("Grade Weighting: " + module.getNumberOfTests() + ", ");
			stuModTScript.append("Module Grade: " + module.getGradesString());
		}

		return stuModTScript.toString();
	}
}
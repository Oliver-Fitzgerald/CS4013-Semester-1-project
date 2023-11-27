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

	/**
	 *Constructs a default student
	 */
	public Student(){

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
}
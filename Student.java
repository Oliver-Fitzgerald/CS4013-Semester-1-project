import java.lang.StringBuilder;
import java.util.ArrayList;

public class Student {
	String id;
	String progID;
	String progName;
	String startYear;
	int curSemester; //e.g. 1, 2, 3, 4...
	ArrayList<StudentModule> modules;

	public Student(String id, String progID, String progName, String startYear, int curSemester, ArrayList<StudentModule> modules){
		this.id = id;
		this.progID = progID;
		this.progName = progName;
		this.startYear = startYear;
		this.curSemester = curSemester;
		this.modules = modules;

	}

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
}
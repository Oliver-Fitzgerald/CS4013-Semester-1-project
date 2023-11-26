import java.util.HashMap;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Map;

public class Programme {
	private String code;
	private String name;
	private String year;
	private HashMap<Integer, ArrayList<TeacherModule>> semesterModules;

	public Programme(String code, String name, String year, HashMap<Integer, ArrayList<TeacherModule>> semesterModules){
		this.code = code;
		this.name = name;
		this.year = year;
		this.semesterModules = semesterModules;
	}

	public Programme(){
		this("", "", "", new HashMap<Integer, ArrayList<TeacherModule>>());
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.code + " " + this.name + " " + this.year + "\n");
		for(Map.Entry<Integer, ArrayList<TeacherModule>> entry : this.semesterModules.entrySet()){
			sb.append(entry.getKey() + ": ");
			for(TeacherModule mod : entry.getValue()){
				sb.append(mod.toString() + ", ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Utility method when adding a programme. It does not contain the added modules as that is handled by
	 * the addModule method when using the addProgramme method
	 * @return A string containing the information about the programme as seen in the programmes csv.
	 */
	public String getProgAsCSVLine(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.code + "," + this.name + "," + this.year);
		for(Map.Entry<Integer, ArrayList<TeacherModule>> entry : this.semesterModules.entrySet()){
			sb.append("," + entry.getKey());
		}
		return sb.toString();
	}

	public String getCode(){
		return this.code;
	}

	public HashMap<Integer, ArrayList<TeacherModule>> getSemesterModules(){
		return this.semesterModules;
	}
}
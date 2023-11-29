import java.util.ArrayList;
import java.util.HashMap;
import java.lang.StringBuilder;
import java.util.Map;

public class TeacherModule extends Module{
	//The string is the student id and the Double[] grades are their grades.
	private HashMap<String, double[]> grades;
	String teacherID;

	public TeacherModule(String code, String name, int year, int semester, String teacherID, double credits, String gradingScheme, double[] weights, HashMap<String, double[]> grades){
		super(code, name, year, semester, credits, gradingScheme, weights);
		this.teacherID = teacherID;
		this.grades = grades;
	}

	//Used when adding a teacher
	public TeacherModule(String code, int year, int semester){
		super(code, "", year, semester, 0.0, "", new double[0]);
		this.teacherID = "";
		this.grades = new HashMap<String, double[]>();
	}

	public TeacherModule(){
		super();
		this.teacherID = "";
		this.grades = new HashMap<String, double[]>();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString() + "and the grades are ");
		for(Map.Entry<String, double[]> entry : this.grades.entrySet()){
			sb.append(entry.getKey() + ": ");
			for(int i = 0; i < entry.getValue().length; i++){
				sb.append(entry.getValue()[i] + ", ");
			}
		}
		return sb.toString();
	}

	public HashMap<String, double[]> getGrades(){
		return this.grades;
	}

	/**
	 * Returns a string that is the module converted to the csv format.
	 */
	public String toCSV(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.name + "," + this.gradingScheme + "," + this.teacherID + "," + this.credits + "\n");
		for(double weight : this.weights){
			sb.append(weight + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("\n");
		for(Map.Entry<String, double[]> entry : this.grades.entrySet()){
			sb.append(entry.getKey());
			for(double d : entry.getValue()){
				sb.append("," + d);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public HashMap<String, StudentModule> getStudentModules(){
		HashMap<String, StudentModule> out = new HashMap<String, StudentModule>();
		for(Map.Entry<String, double[]> entry : this.grades.entrySet()){
			out.put(entry.getKey(), new StudentModule(this.code, this.name, this.year, this.semester, this.credits, this.gradingScheme, this.weights, entry.getValue()));
		}

		return out;
	}

	public Module getModuleFromCurrent(){
		return new Module(this.code, this. name, this.year, this.semester, this.credits, this.gradingScheme, this.weights);
	}
}
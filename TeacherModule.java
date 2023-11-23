import java.util.ArrayList;
import java.util.HashMap;
import java.lang.StringBuilder;
import java.util.Map;

public class TeacherModule extends Module{
	//The string is the student id and the Double[] grades are their grades.
	HashMap<String, double[]> grades;

	public TeacherModule(String code, String name, int year, int semester, double credits, String gradingScheme, double[] weights, HashMap<String, double[]> grades){
		super(code, name, year, semester, credits, gradingScheme, weights);
		this.grades = grades;
	}

	public TeacherModule(){
		super();
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
}
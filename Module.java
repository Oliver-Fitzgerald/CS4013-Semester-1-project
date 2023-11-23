import java.lang.StringBuilder;

public class Module {
	String code;
	String name;
	int year;
	int semester;
	double credits;
	String gradingScheme;
	double[] weights;

	public Module(String code, String name, int year, int semester, double credits, String gradingScheme, double[] weights){
		this.code = code;
		this.name = name;
		this.year = year;
		this.semester = semester;
		this.credits = credits;
		this.gradingScheme = gradingScheme;
		this.weights = weights;
	}

	public Module(){
		this("", "", 0, 0, 0.0, "", new double[0]);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(code + " " + name + " " + year + " " + semester + " worth " + credits + " credits. The Grading Scheme is " + gradingScheme
		+ " and the weights for each test are ");
		for(double weight : weights){
			sb.append(weight + ", ");
		}
		return sb.toString();
	}
}
import java.lang.StringBuilder;

public class StudentModule{
	String code;
	String name;
	int year;
	int semester;
	double credits;
	String gradingScheme;
	double[] weights;
	double[] grades;

	public StudentModule(String code, String name, int year, int semester, double credits, String gradingScheme, double[] weights, double[] grades){
		this.code = code;
		this.name = name;
		this.year = year;
		this.semester = semester;
		this.credits = credits;
		this.gradingScheme = gradingScheme;
		this.weights = weights;
		this.grades = grades;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(code + " " + name + " " + year + " " + semester + " worth " + credits + " credits. The Grading Scheme is " + gradingScheme
		+ " and the weights for each test are ");
		for(double weight : weights){
			sb.append(weight + ", ");
		}
		sb.append("and the grades are ");
		for(double grade : grades){
			sb.append(grade + ", ");
		}
		return sb.toString();
	}
}
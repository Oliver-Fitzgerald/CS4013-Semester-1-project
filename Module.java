import java.lang.StringBuilder;

public class Module {
	protected String code;
	protected String name;
	protected int year;
	protected int semester;
	protected double credits;
	protected String gradingScheme;
	protected double[] weights;

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

	public double[] getWeights(){
		return this.weights;
	}

	public String getCSVName(){
		return this.code + "_" + this.year + "_" + this.semester;
	}

	public int getNumberOfTests(){
		int out = 0;
		for(double weight : this.weights){
			out++;
		}

		return out;
	}

	public String getCode(){
		return this.code;
	}
}
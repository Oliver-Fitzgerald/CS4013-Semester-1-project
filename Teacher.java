import java.util.ArrayList;
import java.lang.StringBuilder;

public class Teacher {
	String id;
	ArrayList<TeacherModule> modules;

	public Teacher(String id, ArrayList<TeacherModule> modules){
		this.id = id;
		this.modules = modules;
	}

	public Teacher(){
		this("", new ArrayList<TeacherModule>());
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append("Teacher " + id + "\n");
		sb.append("Teaches Modules: \n");
		for(TeacherModule mod : this.modules){
			sb.append(mod.toString() + "\n");
		}

		return sb.toString();
	}

	public String getTeacherAsCSVLine(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.id);
		for(TeacherModule mod : this.modules){
			sb.append("," + mod.getCSVName());
		}

		return sb.toString();
	}
}
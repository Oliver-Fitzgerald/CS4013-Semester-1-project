import java.io.IOException;
import java.util.ArrayList;

public class CSVEditor_Test {
	public static void main(String[] args){
		try{
			// Student stu = CSVEditor.getStudent("22299211");
			// System.out.println(stu.toString()+"\n\n");

			// Teacher teach = CSVEditor.getTeacher("12345678");
			// System.out.println(teach.toString() + "\n\n");

			// Programme prog = CSVEditor.getProgramme("LM121");
			// System.out.println(prog.toString() + "\n\n");

			// String curGradingScheme = "A1>80:A2>72:B1>64:B2>60:B3>56:C1>52:C2>48:C3>40:D1>35:D2>30:F<30";
			// String newGradingScheme = "A1>85:A2>72:B1>64:B2>60:B3>56:C1>52:C2>48:C3>40:D1>35:D2>30:F<30";

			// Module mod = CSVEditor.getModule("CS4012_2023_2");
			// System.out.println(mod.toString() + "\n");

			// CSVEditor.updateGradingScheme(mod, newGradingScheme);

			// mod = CSVEditor.getModule("CS4012_2023_2");
			// System.out.println(mod.toString() + "\n");

			// CSVEditor.updateGradingScheme(mod, curGradingScheme);

			// Student stu = CSVEditor.getStudent("22365958");
			// Module mod = CSVEditor.getModule("CS4012_2023_2");
			// CSVEditor.updateStudentGrades(stu, mod, "50.0", 1);

			// String stuID = "00000000";
			// String progID = "LM121";
			// String progName = "Computer Science (Common Entry)";
			// String startYear = "2023/24";
			// int curSemester = 2;
			// ArrayList<StudentModule> mods = new ArrayList<StudentModule>();

			// //loading mods array
			// // Student stu2 = CSVEditor.getStudent("22299211");
			// // mods.add(stu2.getModules().get(0));
			// // mods.add(stu2.getModules().get(1));

			// Student stu2 = CSVEditor.getStudent("22299211");
			// StudentModule mod1 = stu2.getModules().get(0);
			// StudentModule mod2 = stu2.getModules().get(1);
			// mod1.setGrades(null);
			// mod2.setGrades(null);
			// mods.add(mod1);
			// mods.add(mod2);

			// Student stu = new Student(stuID, progID, progName, startYear, curSemester, mods);
			// CSVEditor.addStudent(stu);
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
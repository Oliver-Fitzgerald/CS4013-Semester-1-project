import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;

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


			// ArrayList<TeacherModule> mods = new ArrayList<TeacherModule>();
			// mods.add(new TeacherModule("CS4013", 2024, 1));
			// mods.add(new TeacherModule("ET4162", 2023, 2));
			// Teacher teach = new Teacher("00000000", mods);
			// CSVEditor.addTeacher(teach);

			// String progCode = "LM121";
			// int modSemester = 1;
			// double[] weights = new double[] {0.25, 0.25, 0.25, 0.25};
			// HashMap<String, double[]> grades = new HashMap<String, double[]>();
			// grades.put("11111111", new double[] {62.5, 81.9, 92.6, 21.7});
			// grades.put("22222222", new double[] {78.1, 91.2, 75.6, 99.9});
			// TeacherModule mod = new TeacherModule("ET4162", "Introduction to Networking", 2023, 2, "00000000", 6.00, "A1>80:A2>72:B1>64:B2>60:B3>56:C1>52:C2>48:C3>40:D1>35:D2>30:F<30", weights, grades);
			// CSVEditor.addModule(progCode, modSemester, mod);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
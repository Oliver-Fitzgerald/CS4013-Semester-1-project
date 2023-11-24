import java.io.IOException;

public class CSVEditor_Test {
	public static void main(String[] args){
		try{
			Student stu = CSVEditor.getStudent("22299211");
			System.out.println(stu.toString()+"\n\n");

			Teacher teach = CSVEditor.getTeacher("12345678");
			System.out.println(teach.toString() + "\n\n");

			Programme prog = CSVEditor.getProgramme("LM121");
			System.out.println(prog.toString() + "\n\n");

			String curGradingScheme = "A1>80:A2>72:B1>64:B2>60:B3>56:C1>52:C2>48:C3>40:D1>35:D2>30:F<30";
			String newGradingScheme = "A1>85:A2>72:B1>64:B2>60:B3>56:C1>52:C2>48:C3>40:D1>35:D2>30:F<30";

			Module mod = CSVEditor.getModule("CS4012_2023_2");
			System.out.println(mod.toString() + "\n");

			CSVEditor.updateGradingScheme(mod, newGradingScheme);

			mod = CSVEditor.getModule("CS4012_2023_2");
			System.out.println(mod.toString() + "\n");

			CSVEditor.updateGradingScheme(mod, curGradingScheme);
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
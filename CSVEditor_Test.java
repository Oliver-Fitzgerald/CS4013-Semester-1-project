import java.io.IOException;

public class CSVEditor_Test {
	public static void main(String[] args){
		try{
			Student stu = CSVEditor.getStudent("22299211");
			System.out.println(stu.toString()+"\n\n");

			Teacher teach = CSVEditor.getTeacher("12345678");
			System.out.println(teach.toString() + "\n\n");

			Programme prog = CSVEditor.getProgramme("LM121");
			System.out.println(prog.toString());
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
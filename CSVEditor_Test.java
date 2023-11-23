import java.io.IOException;

public class CSVEditor_Test {
	public static void main(String[] args){
		try{
			Student stu = CSVEditor.getStudent("22299211");
			System.out.println(stu.toString());
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
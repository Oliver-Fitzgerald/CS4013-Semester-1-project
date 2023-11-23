import java.util.Scanner;

public class StudentRecordInterface {
	private String userType = "";

	public static void main(String[] args){
		chooseUser();

		switch(userType){
		case "student":
			studentMenu();
			break;
		case "teacher":
			teacherMenu();
			break;
		case "board":
			boardMenu();
			break;
		}
	}

	private void studentMenu(){
		Scanner in = new Scanner();
		System.out.print("Enter programme code: ");
		String programme = in.nextLine();
		System.out.print("Enter Student ID: ");
		String id = in.nextLine();
		while(true){
			System.out.println("1. View Full Transcript\n2. View Module Transcript");
			String option = in.nextLine();
			if(option.equals("1")){
				String out = DataFormatter.getStudentTranscript(programme, id);
				System.out.println(out);
			}
			else if(option.equals("2")){
				System.out.print("Enter Module Code: ");
				String modCode = in.nextLine();
				String out = DataFormatter.getModuleTranscript(programme, id, modCode);
				System.out.println(out);
			}
			else if(option.equals("e")){
				break;
			}
		}
	}

	private void teacherMenu(){

	}

	private void boardMenu(){

	}
}
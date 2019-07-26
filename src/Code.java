import java.util.*;

public class Code {
	private static int cod;
	public static void produce_Code() {
		Random r=new Random();
		cod = r.nextInt(8000)+1000;
	}
	public static int getCode() {
		return cod;
	}
}

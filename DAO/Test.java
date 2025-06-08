package DAO;
import java.sql.Connection;
public class Test {
	public static void main(String[] args) {
		Connection con = SingletonConnection.getInstance();
		System.out.println(":connection established");
	}
}

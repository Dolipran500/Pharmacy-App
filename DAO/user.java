package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modele.Users;

public class user implements IDAO<Users>{

	@Override
	public void creer(Users obj) {
		ajouter(obj);
	}

	@Override
	public void ajouter(Users obj) {
		try {
			Connection cn = SingletonConnection.getInstance();
			String s = "insert into users (nomUser , passwordUser , roleUser) values (?,?,?)";
			PreparedStatement ps;
			ps = cn.prepareStatement(s);
			ps.setString(1, obj.nom());
			ps.setString(2 , obj.nom());
			ps.setString(3, obj.role());
			ps.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println("error");
		}
	}

	@Override
	public void modifier(Users obj) {
		try {
			Connection cn = SingletonConnection.getInstance();
			String s = "update users set passwordUser = ? where  nomUser = ?";
			PreparedStatement ps;
			ps = cn.prepareStatement(s);
			ps.setString(1, obj.pass());
			ps.setString(2, obj.nom());
			ps.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println("error");
		}
	}

	@Override
	public void supprimer(Users obj) {
		try {
			Connection cn = SingletonConnection.getInstance();
			String s = "delete from users where nomUser = ?";
			PreparedStatement ps ; 
			ps = cn.prepareStatement(s);
			ps.setString(1, obj.nom());
			ps.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("error");
		}
	}

	@Override
	public List<Users> getAll() {
	    List<Users> list = new ArrayList<>();
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "SELECT * FROM users";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            int iduser = rs.getInt("idUser");
	            String nom = rs.getString("nomUser");
	            String pass = rs.getString("passwordUser");
	            String role = rs.getString("roleUser");
	            list.add(new Users(iduser, nom, pass, role));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

}

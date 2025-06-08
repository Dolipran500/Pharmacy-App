package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modele.Client;
import modele.LigneMed;

public class lignemed implements IDAO<LigneMed>{

	@Override
	public void creer(LigneMed obj) {
		ajouter(obj);
	}

	@Override
	public void ajouter(LigneMed obj) {
		try {
			Connection cn = SingletonConnection.getInstance();
			String sql = "INSERT INTO ligne_med (idpresc, idmed, quantmed) VALUES (?, ?, ?)";
			PreparedStatement stmt = cn.prepareStatement(sql);
			stmt.setInt(1, obj.prescriptionId());
	        stmt.setInt(2, obj.medicamentId());
	        stmt.setInt(3, obj.stock());
	        stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println("error");
		}
	}

	@Override
	public void modifier(LigneMed obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "UPDATE client SET  idmed = ?, quantmed = ? WHERE idpresc = ?";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setInt(1, obj.medicamentId());
	        ps.setInt(2, obj.stock());
	        ps.setInt(3, obj.prescriptionId());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void supprimer(LigneMed obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "DELETE FROM ligne_med WHERE idpresc = ?";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setInt(1, obj.prescriptionId());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public List<LigneMed> getAll() {
	    List<LigneMed> list = new ArrayList<>();
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "SELECT * FROM ligne_med";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            int idcli = rs.getInt("idpresc");
	            int nom = rs.getInt("idmed");
	            int prenom = rs.getInt("stock");
	            list.add(new LigneMed(idcli, nom, prenom));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
}

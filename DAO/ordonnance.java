package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modele.LigneMed;
import modele.Ordonnance;

public class ordonnance implements IDAO<Ordonnance> {

	@Override
	public void creer(Ordonnance obj) {
		ajouter(obj);
	}
	public void ajouter(Ordonnance prescription) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "insert into prescription(client_id, instructions) VALUES(?, ?)";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setInt(1, prescription.idcli());
	        ps.setString(2, prescription.inst());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



	@Override
	public void modifier(Ordonnance obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "UPDATE prescription SET client_id = ?, instructions = ? WHERE idpresc = ?";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setInt(1, obj.idcli());
	        ps.setString(2, obj.inst());
	        ps.setInt(3, obj.idpresc());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public void supprimer(Ordonnance obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();

	        // Step 1: Get all associated ligne_med rows for this prescription
	        String selectLignes = "SELECT idmed, quantmed FROM ligne_med WHERE idpresc = ?";
	        PreparedStatement selectStmt = cn.prepareStatement(selectLignes);
	        selectStmt.setInt(1, obj.idpresc());
	        ResultSet rs = selectStmt.executeQuery();

	        // Step 2: For each medicament, update its stock
	        while (rs.next()) {
	            int idmed = rs.getInt("idmed");
	            int quantmed = rs.getInt("quantmed");

	            // Update stock in medicaments table
	            String updateStockSQL = "UPDATE medicaments SET stock = stock + ? WHERE idmed = ?";
	            PreparedStatement psUpdate = cn.prepareStatement(updateStockSQL);
	            psUpdate.setInt(1, quantmed); // Add back the quantity
	            psUpdate.setInt(2, idmed);    // Specify which medicament
	            psUpdate.executeUpdate();
	        }

	        // Step 3: Delete from ligne_med
	        String deleteLignes = "DELETE FROM ligne_med WHERE idpresc = ?";
	        PreparedStatement deleteLigneStmt = cn.prepareStatement(deleteLignes);
	        deleteLigneStmt.setInt(1, obj.idpresc());
	        deleteLigneStmt.executeUpdate();

	        // Step 4: Delete from prescription
	        String deletePrescription = "DELETE FROM prescription WHERE idpresc = ?";
	        PreparedStatement ps = cn.prepareStatement(deletePrescription);
	        ps.setInt(1, obj.idpresc());
	        ps.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



	@Override
	public List<Ordonnance> getAll() {
	    List<Ordonnance> list = new ArrayList<>();
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "SELECT * FROM prescription";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            int idpresc = rs.getInt("idpresc");
	            int idcli = rs.getInt("client_id");
	            String inst = rs.getString("instructions");
	            list.add(new Ordonnance(idpresc, idcli, inst));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	public int getLastInsertedPrescriptionId() {
	    int id = -1;
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String sql = "SELECT MAX(idpresc) FROM prescription";
	        PreparedStatement ps = cn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return id;
	}
	
}

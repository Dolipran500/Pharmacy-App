package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modele.Client;

public class client implements IDAO<Client> {

	@Override
	public void creer(Client obj) {
		ajouter(obj);
	}

	@Override
	public void ajouter(Client obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "INSERT INTO client(nomClient, prenomClient, credit) VALUES(?, ?, ?)";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setString(1, obj.nom());
	        ps.setString(2, obj.prenom());
	        ps.setDouble(3, obj.credit());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void modifier(Client obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "UPDATE client SET nomClient = ?, prenomClient = ?, credit = ? WHERE idClient = ?";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setString(1, obj.nom());
	        ps.setString(2, obj.prenom());
	        ps.setDouble(3, obj.credit());
	        ps.setInt(4, obj.idcli());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public void supprimer(Client obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "DELETE FROM client WHERE idClient = ?";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setInt(1, obj.idcli());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public List<Client> getAll() {
	    List<Client> list = new ArrayList<>();
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "SELECT * FROM client";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            int idcli = rs.getInt("idClient");
	            String nom = rs.getString("nomClient");
	            String prenom = rs.getString("prenomClient");
	            double credit = rs.getDouble("credit");
	            list.add(new Client(idcli, nom, prenom, credit));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}



}

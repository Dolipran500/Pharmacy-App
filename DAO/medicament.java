package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modele.Medicaments;

public class medicament implements IDAO<Medicaments>{

	@Override
	public void creer(Medicaments obj) {
		ajouter(obj);
	}

	@Override
	public void ajouter(Medicaments obj) {
		try {
			Connection cn = SingletonConnection.getInstance();
			String s = "insert into medicaments (Nommed,categorie,prix,stock) values (?,?,?,?)";
			PreparedStatement ps;
			ps = cn.prepareStatement(s);
			ps.setString(1, obj.nommed());
			ps.setString(2, obj.categorie());
			ps.setDouble(3, obj.prix());
			ps.setInt(4, obj.stock());
			ps.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println("error");
		}
	}

	@Override
	public void modifier(Medicaments obj) {
		try {
			Connection cn = SingletonConnection.getInstance();
			String s = "update medicaments set stock = ? prix = ? where Nommed = ? ";
			PreparedStatement ps;
			ps = cn.prepareStatement(s);
			ps.setInt(1, obj.stock());
			ps.setDouble(2, obj.prix());
			ps.setString(3, obj.nommed());
			ps.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println("error");
		}
	}
	public void modifierStock(Medicaments obj) {
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "UPDATE medicaments SET stock = ? WHERE Nommed = ?";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ps.setInt(1, obj.stock());
	        ps.setString(2, obj.nommed());
	        
	        int rowsUpdated = ps.executeUpdate();
	        if (rowsUpdated == 0) {
	            System.out.println("Aucun médicament trouvé avec ce nom: " + obj.nommed());
	        } else {
	            System.out.println("Stock mis à jour pour: " + obj.nommed());
	        }
	    } catch (SQLException e) {
	        System.out.println("Erreur SQL lors de la mise à jour du stock.");
	        e.printStackTrace();
	    }
	}



	@Override
	public void supprimer(Medicaments obj) {
		try {
			Connection cn = SingletonConnection.getInstance();
			String s ="delete from medicaments where Nommed=?";
			PreparedStatement ps;
			ps = cn.prepareStatement(s);
			ps.setString(1, obj.nommed());
			ps.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println("error");
		}
	}

	@Override
	public List<Medicaments> getAll() {
	    List<Medicaments> list = new ArrayList<>();
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String s = "SELECT * FROM medicaments";
	        PreparedStatement ps = cn.prepareStatement(s);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            int idmed = rs.getInt("idmed");
	            String categorie = rs.getString("categorie");
	            String nommed = rs.getString("Nommed");
	            double prix = rs.getDouble("prix");
	            int stock = rs.getInt("stock");
	            list.add(new Medicaments(idmed, categorie, nommed, prix, stock));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	public Medicaments getById(int idmed) {
		Medicaments medicament = null;
	    try {
	        Connection cn = SingletonConnection.getInstance();
	        String query = "SELECT * FROM medicaments WHERE idmed = ?";
	        PreparedStatement ps = cn.prepareStatement(query);
	        ps.setInt(1, idmed);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            int id = rs.getInt("idmed");
	            String categorie = rs.getString("categorie");
	            String nommed = rs.getString("Nommed");
	            double prix = rs.getDouble("prix");
	            int stock = rs.getInt("stock");
	            medicament = new Medicaments(id, categorie, nommed, prix, stock);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return medicament;
	}


}

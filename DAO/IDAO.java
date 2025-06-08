package DAO;

import java.util.List;

public interface IDAO<T> {
	public void creer(T obj);
	public void ajouter(T obj);
	public void modifier(T obj);
	public void supprimer(T obj);
	public List<T> getAll();
}

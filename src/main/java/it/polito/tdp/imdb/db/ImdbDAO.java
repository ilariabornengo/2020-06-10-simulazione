package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> listAllGeneres(){
		String sql = "SELECT DISTINCT genre as genere "
				+ "FROM movies_genres g ";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("genere"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertices(String genere,Map<Integer,Actor> idMap){
		String sql = "SELECT DISTINCT  a.id as id,a.first_name as nome,a.last_name as cognome,a.gender as sesso "
				+ "FROM movies m,roles r,actors a, movies_genres g "
				+ "WHERE a.id=r.actor_id AND m.id=r.movie_id "
				+ "AND m.id=g.movie_id "
				+ "AND g.genre=? " ;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id")))
				{
					Actor a=new Actor(res.getInt("id"),res.getString("nome"),res.getString("cognome"),res.getString("sesso"));
					idMap.put(a.getId(), a);
				}
				
			}
			conn.close();
	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer,Actor> idMap,String genere){
		String sql = "SELECT r1.actor_id AS a1,r2.actor_id AS a2, COUNT(DISTINCT r1.movie_id) AS weight "
				+ "FROM roles r1, roles r2, movies_genres m,movies mo "
				+ "WHERE m.movie_id=mo.id "
				+ "AND mo.id=r1.movie_id "
				+ "AND mo.id=r2.movie_id "
				+ "AND r1.actor_id>r2.actor_id "
				+ "AND m.genre=? "
				+ "GROUP BY r1.actor_id,r2.actor_id ";
		Connection conn = DBConnect.getConnection();
		List<Adiacenza> result=new ArrayList<Adiacenza>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("a1")) && idMap.containsKey(res.getInt("a2")))
				{
					Adiacenza a=new Adiacenza(idMap.get(res.getInt("a1")),idMap.get(res.getInt("a2")),res.getInt("weight"));
					result.add(a);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}

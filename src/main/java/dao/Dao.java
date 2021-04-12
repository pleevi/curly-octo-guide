package dao;

import java.sql.DriverManager;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Candidates;

import java.io.IOException;
import java.sql.Connection;

public class Dao {
	private String url;
	private String user;
	private String pass;
	private Connection conn;
	
	public Dao(String url, String user, String pass) {
		this.url=url;
		this.user=user;
		this.pass=pass;
	}
	
	public boolean getConnection() {
		try {
	        if (conn == null || conn.isClosed()) {
	            try {
	                Class.forName("com.mysql.jdbc.Driver").newInstance();
	            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
	                throw new SQLException(e);
	            }
	            conn = DriverManager.getConnection(url, user, pass);
	        }
	        return true;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	protected void disconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
	public List<Candidates> listAllCandidates() throws SQLException {
        List<Candidates> listCandidates = new ArrayList<>();
         
        String sql = "SELECT * FROM ehdokkaat";
         
        getConnection();
         
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
        	Candidates f=new Candidates();
			f.setEhdokas_id(resultSet.getInt("ehdokas_id"));
			f.setSukunimi(resultSet.getString("sukunimi"));
			f.setEtunimi(resultSet.getString("etunimi"));
			f.setPuolue(resultSet.getString("puolue"));
			listCandidates.add(f);
        }  
        resultSet.close();
        statement.close(); 
        disconnect(); 
        return listCandidates;
	}
	
	public boolean insertCandidate(Candidates candidates) throws SQLException {
        String sql = "INSERT INTO ehdokkaat (sukunimi, etunimi, puolue) VALUES (?, ?, ?)";
        getConnection();
         
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, candidates.getSukunimi());
        statement.setString(2, candidates.getEtunimi());
        statement.setString(3, candidates.getPuolue());
         
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }
	
	public ArrayList<Candidates> getEhdokasInfo(String ehdokas_id) {
		ArrayList<Candidates> candidateInfo = new ArrayList<Candidates>();
		try {
			String sql="select * from ehdokkaat where ehdokas_id=?";
			getConnection();
	         
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, ehdokas_id);
	        ResultSet RS = statement.executeQuery();

			while (RS.next()){
				Candidates p=new Candidates();
				p.setEhdokas_id(RS.getInt("ehdokas_id"));
				p.setSukunimi(RS.getString("sukunimi"));
				p.setEtunimi(RS.getString("etunimi"));
				p.setPuolue(RS.getString("puolue"));
				p.setKotipaikkakunta(RS.getString("kotipaikkakunta"));
				p.setIka(RS.getInt("ika"));
				p.setMiksi_eduskuntaan(RS.getString("miksi_eduskuntaan"));
				p.setMita_asioita_haluat_edistaa(RS.getString("mita_asioita_haluat_edistaa"));
				p.setAmmatti(RS.getString("ammatti"));
				candidateInfo.add(p);
			}
			return candidateInfo;
		}
		
		
		catch(SQLException e) {
			return null;
		}
	}	
	

	public Candidates readCandidates(String ehdokas_id) {
		Candidates f=null;
		try {
			String sql="select * from ehdokkaat where ehdokas_id=?";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, ehdokas_id);
			ResultSet RS=pstmt.executeQuery();
			while (RS.next()){
				f=new Candidates();
				f.setEhdokas_id(RS.getInt("ehdokas_id"));
				f.setSukunimi(RS.getString("sukunimi"));
				f.setEtunimi(RS.getString("etunimi"));
				f.setPuolue(RS.getString("puolue"));
			}
			return f;
		}
	
	
		catch(SQLException e) {
			return null;
		}
	}
	

	
//	public ArrayList<Candidates> listAllCandidates() {
//		ArrayList<Candidates> listAllCandidates=new ArrayList<>();
//		try {
//			Statement stmt=conn.createStatement();
//			ResultSet RS=stmt.executeQuery("select * from ehdokkaat");
//			while (RS.next()){
//				Candidates f=new Candidates();
//				f.setEhdokas_id(RS.getInt("ehdokas_id"));
//				f.setSukunimi(RS.getString("sukunimi"));
//				f.setEtunimi(RS.getString("etunimi"));
//				f.setPuolue(RS.getString("puolue"));
//				listAllCandidates.add(f);
//			}
//			return listAllCandidates;
//		}
//		catch(SQLException e) {
//			return null;
//		}
//	}
//			
//	public ArrayList<Candidates> updateCandidates(Candidates f) {
//		try {
//			String sql="update ehdokkaat set sukunimi=? etunimi=? where ehdokas_id=?";
//			PreparedStatement pstmt=conn.prepareStatement(sql);
//			pstmt.setString(1, f.getSukunimi());
//			pstmt.setString(2, f.getEtunimi());
//			pstmt.setString(3, f.getPuolue());
//			pstmt.setInt(4, f.getEhdokas_id());
//			pstmt.executeUpdate();
//			return readAllCandidates();
//		}
//		catch(SQLException e) {
//			return null;
//		}
//	}
//	public ArrayList<Candidates> deleteCandidates(String ehdokas_id) {
//		try {
//			String sql="delete from ehdokkaat where ehdokas_id=?";
//			PreparedStatement pstmt=conn.prepareStatement(sql);
//			pstmt.setString(1, ehdokas_id);
//			pstmt.executeUpdate();
//			return readAllCandidates();
//		}
//		catch(SQLException e) {
//			return null;
//		}
//	}
//
//	public Candidates readCandidates(String ehdokas_id) {
//		Candidates f=null;
//		try {
//			String sql="select * from ehdokkaat where ehdokas_id=?";
//			PreparedStatement pstmt=conn.prepareStatement(sql);
//			pstmt.setString(1, ehdokas_id);
//			ResultSet RS=pstmt.executeQuery();
//			while (RS.next()){
//				f=new Candidates();
//				f.setEhdokas_id(RS.getInt("ehdokas_id"));
//				f.setSukunimi(RS.getString("sukunimi"));
//				f.setEtunimi(RS.getString("etunimi"));
//				f.setPuolue(RS.getString("puolue"));
//			}
//			return f;
//		}
//		catch(SQLException e) {
//			return null;
//		}
//	}
}




package com.tqbdev.funcs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.tqbdev.model.HocSinh;

public class DatabaseConnection {
	private Connection connection = null;

	public void getConnection(String databaseUrl, String portNumber, String databaseName, String userName, String passWord)
			throws ClassNotFoundException, SQLException {
		if (databaseUrl == null || databaseUrl == "" || databaseUrl.length() == 0) {
			databaseUrl = "localhost";
		}
		
		if (portNumber == null || portNumber == "" || portNumber.length() == 0) {
			portNumber = "1433";
		}
		
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String connectionUrl = "jdbc:sqlserver://" + databaseUrl + ":" + portNumber + ";databaseName=" + databaseName + ";user=" + userName
				+ ";password=" + passWord;
		//System.out.println(connectionUrl);
		connection = DriverManager.getConnection(connectionUrl);
	}

	public void closeConnection() throws SQLException {
		connection.close();
	}

	public List<HocSinh> getTable() throws SQLException {
		String sql = "SELECT * FROM HocSinh";
		PreparedStatement pre = connection.prepareStatement(sql);

		ResultSet resultSet = pre.executeQuery();

		List<HocSinh> resultList = new ArrayList<HocSinh>();
		while (resultSet.next()) {
			int MaHS = resultSet.getInt("MaHS");
			HocSinh hs = new HocSinh(MaHS, resultSet.getString("TenHS"),
					new java.util.Date(resultSet.getDate("NgaySinh").getTime()), resultSet.getString("Ghichu"), resultSet.getBytes("ExtInfo"));
			
			resultList.add(hs);
		}
		
		pre.close();
		return resultList;
	}

	public void insert(HocSinh hs) throws SQLException {
		String sql = "INSERT INTO HocSinh(MaHS, TenHS, NgaySinh, Ghichu, ExtInfo) VALUES(?,?,?,?,?)";
		PreparedStatement pre = connection.prepareStatement(sql);

		pre.setInt(1, hs.getMaHS());
		pre.setString(2, hs.getTenHS());
		
		java.sql.Date date = new java.sql.Date(hs.getNgaySinh().getTime());
		pre.setDate(3, date);
		pre.setString(4, hs.getGhichu());
		pre.setBytes(5, hs.getExtInfo_bytes());

		pre.executeUpdate();
		pre.close();
	}
	
	public void delete(int MaHS) throws SQLException {
		String sql = "DELETE FROM HocSinh WHERE MaHS = ?";
		PreparedStatement pre = connection.prepareStatement(sql);
		
		pre.setInt(1, MaHS);
		
		pre.executeUpdate();
		pre.close();
	}
	
	public List<HocSinh> query(int MaHS, String tenHS, java.util.Date ngaySinh, String ghiChu) throws SQLException {
		PreparedStatement pre = null;
		
		if (MaHS != -1) {
			String sql = "SELECT * FROM HocSinh WHERE MaHS=?";
			pre = connection.prepareStatement(sql);
			pre.setInt(1, MaHS);
		}
		
		if (tenHS != null) {
			String sql = "SELECT * FROM HocSinh WHERE TenHS LIKE N'%" + tenHS + "%'";
			pre = connection.prepareStatement(sql);
		}
		
		if (ngaySinh != null) {
			String sql = "SELECT * FROM HocSinh WHERE NgaySinh=?";
			pre = connection.prepareStatement(sql);
			
			java.sql.Date date = new java.sql.Date(ngaySinh.getTime());
			pre.setDate(1, date);
		}
		
		if (ghiChu != null) {
			String sql = "SELECT * FROM HocSinh WHERE Ghichu LIKE N'%" + ghiChu + "%'";
			pre = connection.prepareStatement(sql);
		}

		ResultSet resultSet = pre.executeQuery();
		List<HocSinh> resultList = new ArrayList<HocSinh>();
		while (resultSet.next()) {
			int maHS = resultSet.getInt("MaHS");
			HocSinh hs = new HocSinh(maHS, resultSet.getString("TenHS"),
					new java.util.Date(resultSet.getDate("NgaySinh").getTime()), resultSet.getString("Ghichu"), resultSet.getBytes("ExtInfo"));
			
			resultList.add(hs);
		}
		
		pre.close();
		return resultList;
	}
	
	public void modify(int MaHS, String tenHS, java.util.Date ngaySinh, String ghiChu, byte[] ExtInfo_image) throws SQLException {				
		if (tenHS != null) {
			String sql = "UPDATE HocSinh SET TenHS=?" + " WHERE MaHS=?";
			PreparedStatement pre = connection.prepareStatement(sql);
			
			pre.setString(1, tenHS);
			pre.setInt(2, MaHS);
			
			pre.executeUpdate();
			pre.close();
		}
		
		if (ngaySinh != null) {
			String sql = "UPDATE HocSinh SET NgaySinh=?" + " WHERE MaHS=?";
			PreparedStatement pre = connection.prepareStatement(sql);
			
			java.sql.Date date = new java.sql.Date(ngaySinh.getTime());
			pre.setDate(1, date);
			pre.setInt(2, MaHS);
			
			pre.executeUpdate();
			pre.close();
		}
		
		if (ghiChu != null) {
			String sql = "UPDATE HocSinh SET Ghichu=?" + " WHERE MaHS=?";
			PreparedStatement pre = connection.prepareStatement(sql);
			
			pre.setString(1, ghiChu);
			pre.setInt(2, MaHS);
			
			pre.executeUpdate();
			pre.close();
		}
		
		if (ExtInfo_image != null) {
			String sql = "UPDATE HocSinh SET ExtInfo=?" + " WHERE MaHS=?";
			PreparedStatement pre = connection.prepareStatement(sql);
			
			pre.setBytes(1, ExtInfo_image);
			pre.setInt(2, MaHS);
			
			pre.executeUpdate();
			pre.close();
		}
	}
}

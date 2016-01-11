package com.tetris.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tetris.vo.TetrisVo;

public class TetrisDao {

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "testdb", "testdb");
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패 - " + e);
		}

		return conn;
	}
	
	// 전체 기록 보기
	public List<TetrisVo> getScoreList(){
		List<TetrisVo> list=new ArrayList<>();
		
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		
		try {
			conn=getConnection();
			stmt=conn.createStatement();
			
			String sql="select nickName, score, regDate from tetris";
			rs=stmt.executeQuery(sql);
			
			while(rs.next()){
				
				String nickName=rs.getString(1);
				Long score=rs.getLong(2);
				String regDate=rs.getString(3);
				
				TetrisVo vo=new TetrisVo();
				vo.setNickName(nickName);
				vo.setScore(score);
				vo.setRegDate(regDate);
				
				list.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("에러 - "+e);
		} finally {
			try {
				
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}

	// 기록 등록
	public void insertScore(String nickName, Long score){
		
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		try {
			conn=getConnection();
			String sql="insert into tetris values(?,?,sysdate)";
			
			System.out.println(sql);
			System.out.println(nickName);
			System.out.println(score);
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, nickName);
			pstmt.setLong(2, score);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("에러 - "+e);
		} finally {
			try {
				
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

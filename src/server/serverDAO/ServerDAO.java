package server.serverDAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import protocol.Protocol;

public class ServerDAO {
	
	public ServerDAO() {}
	
	public Boolean insert(String[] str) { // 회원가입
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			
			pstmt = conn.prepareStatement(pro.getProperty("membership_insert"));
			pstmt.setString(1, str[1]);
			pstmt.setLong(2, Long.parseLong(str[2]));
			pstmt.setString(3, str[3]);
			pstmt.setString(4, str[4]);
			pstmt.setInt(5, Integer.parseInt(str[5]));
			pstmt.setString(6, Boolean.parseBoolean(str[6]) ? "남성" : "여성");
			pstmt.setString(7, str[7]);
			
			int i = pstmt.executeUpdate();
			System.out.println(i + "개의 행이 추가되었습니다.");
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
		}
	}
	
	public int getid(String id) {	// 아이디 중복 확인
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		ResultSet rs = null;
		int su = -1;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			pstmt = conn.prepareStatement(pro.getProperty("membership_selectid"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.last();
			su = rs.getRow();
			System.out.println(su);
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
		}
		return su;
	}
	
	public int login(String id, String pw) {	// 로그인 시 확인
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		ResultSet rs = null;
		int su = -1;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			pstmt = conn.prepareStatement(pro.getProperty("membership_login"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			rs.last();
			su = rs.getRow();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
			try { if(rs != null) rs.close(); }catch(SQLException e) { }
		}
		return su;
	}
	
	public String getData(String id) {	// 마이페이지 셋팅 시
		String str = Protocol.LOGIN_SUCCESS;
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		ResultSet rs = null;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			pstmt = conn.prepareStatement(pro.getProperty("membership_getData"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.next();
			str += ":;:" + rs.getString("name") + ":;:" + rs.getLong("jumin") + ":;:" + rs.getString("id") + ":;:" + 
					rs.getString("password") + ":;:" + rs.getInt("age") + ":;:" + rs.getString("gender") + ":;:" + rs.getString("tel");
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
			try { if(rs != null) rs.close(); }catch(SQLException e) { }
		}
		
		return str;
	}
	
	public String searchId(String id) {	// 친구 검색 시
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		ResultSet rs = null;
		int su = -1;
		String str = "";
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			pstmt = conn.prepareStatement(pro.getProperty("membership_searchid"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, "%" + id + "%");
			rs = pstmt.executeQuery();
			rs.last();
			su = rs.getRow();
			if(su >= 1) {
				rs.first();
				str = Protocol.SEARCH_FRIEND_SUCCESS + ":;:" + rs.getString("id");
				int i = 0;
				while(rs.next()) {
					str += ":;:" + rs.getString("id");
					i++;
					System.out.println(i + "번째 아이디 : " + rs.getString("id"));
				}
			}else {
				str = Protocol.SEARCH_FRIEND_FAIL;
			}
			
			System.out.println(str);
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
			try { if(rs != null) rs.close(); }catch(SQLException e) { }
		}
		return str;
	}
	
	public void insertFriend(String[] str) {	// 친구 추가 시
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			
			pstmt = conn.prepareStatement(pro.getProperty("friend_insert"));
			pstmt.setString(1, str[1]);
			pstmt.setString(2, str[2]);
			
			int i = pstmt.executeUpdate();
			System.out.println(i + "개의 행이 추가되었습니다.");
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
		}
	}

	public String getFriend(String id) { // 로그인 시 친구 목록 불러오기
		String str = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		ResultSet rs = null;
		
		try {
			conn = ConnUtil.getConnection();
			int su = -1;
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			pstmt = conn.prepareStatement(pro.getProperty("friend_getid"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.last();
			su = rs.getRow();
			if(su >= 1) {
				rs.first();
				str = Protocol.SET_FRIENDLIST_SUCCESS + ":;:" + rs.getString("friend");
				while(rs.next()) str += ":;:" + rs.getString("friend");
				
			}else {
				str = Protocol.SET_FRIENDLIST_FAIL;
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
			try { if(rs != null) rs.close(); }catch(SQLException e) { }
		}
		return str;
	}
	
	public Boolean delFriend(String[] str) {	// 친구 삭제 시
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			
			pstmt = conn.prepareStatement(pro.getProperty("friend_del"));
			pstmt.setString(1, str[1]);
			pstmt.setString(2, str[2]);
			
			int i = pstmt.executeUpdate();
			System.out.println(i + "개의 행이 삭제되었습니다.");
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
		}
	}
	
	public int serverLogin(String id, String pw) {	// 서버 로그인 시 확인
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		ResultSet rs = null;
		int su = -1;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			pstmt = conn.prepareStatement(pro.getProperty("server_login"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			rs.last();
			su = rs.getRow();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
			try { if(rs != null) rs.close(); }catch(SQLException e) { }
		}
		return su;
	}
	
	public String getAll() {	// 서버 메인 창 모든 유저리스트 목록 뽑기
		String str = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		ResultSet rs = null;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			pstmt = conn.prepareStatement(pro.getProperty("server_alluserlist"), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			while(rs.next()) str += rs.getString("id") + ":;:";
			str = str.substring(0, str.length()-3);
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
			try { if(rs != null) rs.close(); }catch(SQLException e) { }
		}
		
		return str;
	}
	
	public Boolean updateTel(String tel, String id) {	// 전화번호 수정 시
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Properties pro = null;
		
		try {
			conn = ConnUtil.getConnection();
			
			pro = new Properties();
			pro.load(new FileInputStream("src/membership.properties"));
			
			pstmt = conn.prepareStatement(pro.getProperty("membership_update"));
			pstmt.setString(1, tel);
			pstmt.setString(2, id);
			
			int i = pstmt.executeUpdate();
			System.out.println(i + "개의 행이 추가되었습니다.");
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try { if(pstmt != null) pstmt.close(); }catch(SQLException e) { }
			try { if(conn != null) conn.close(); }catch(SQLException e) { }
		}
	}
}

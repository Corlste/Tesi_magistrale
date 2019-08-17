package magistrale.tesi.oracle.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class InsertDAO {

	public String doQuery() {
		
		Connection connMysql = DBConnect.getInstance().getConnectionMysql();
		Connection connPostgre = DBConnect.getInstance().getConnectionPostgre();
		Connection connErr = DBConnect.getInstance().getConnectionErr();
	
		int columnsNumber = 0;
		
		TreeMap<Integer, String> queries = new TreeMap<Integer, String>();
		
		String getQuery = "select queryOutputId, queryEseguita from try;";
		String insertErrMysql = "update try SET testoMessaggioMySql=? WHERE queryOutputId=?;";
		String insertErrPostgre = "update try SET testoMessaggioPostgres=? WHERE queryOutputId=?;";
	
		try {
			PreparedStatement stQuery = connErr.prepareStatement(getQuery);
			ResultSet rsQuery = stQuery.executeQuery();
			while (rsQuery.next()) {
//				String q = rsQuery.getString("queryEseguita");
//				q.replaceAll("\\\", "");
				queries.put(rsQuery.getInt("queryOutputId"), rsQuery.getString("queryEseguita").replaceAll("\\\\", ""));
			}
		}catch(SQLException e) {
			throw new RuntimeException("Error Connection Database try to get the queries");
		}	
		
		for (int k: queries.keySet()) {
			System.out.println(k + " " + queries.get(k));
			String sql = queries.get(k);
			
			ArrayList<Object> className = new ArrayList<Object>() ;
			try {
				PreparedStatement stMysql = connMysql.prepareStatement(sql);
								
				PreparedStatement stErrMysql = connErr.prepareStatement(insertErrMysql);
				
				ResultSet rsMysql = stMysql.executeQuery();
				ResultSetMetaData rsmd = rsMysql.getMetaData();
							
				columnsNumber = rsmd.getColumnCount();
						
				for (int i =0 ; i<columnsNumber; i++) {
					className.add(i, rsmd.getColumnClassName(i+1));
				}
				if (columnsNumber ==1) {
					System.out.println("una colonna");
					if (className.get(0).toString().contentEquals("java.lang.Integer")) {
						System.out.println("numero");
					} else {
						System.out.println("stringa");		
					}
				} else {
					System.out.println("due colonne");
					for (int i =0 ; i<columnsNumber; i++) {
						if (className.get(i).toString().contentEquals("java.lang.Integer")) {
							System.out.println("numero");
						} else {
							System.out.println("stringa");
						}
					}
				}

				stErrMysql.setInt(2, k);
				stErrMysql.setString(1, "Query eseguita correttamente su MySQL");
				stErrMysql.executeUpdate();
				
				stMysql.close();
			} catch(SQLException e) {
				PreparedStatement stErrMysql;
				String errorMessage = e.getMessage();
//				e.printStackTrace();

				try {
					stErrMysql = connErr.prepareStatement(insertErrMysql);
					stErrMysql.setInt(2, k);
					stErrMysql.setString(1, errorMessage);

					stErrMysql.executeUpdate();
				} catch (SQLException e1) {
				}

			}
			
		}
		try {
			connMysql.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int k: queries.keySet()) {
			String sql = queries.get(k);
			try {
				PreparedStatement stPostgre = connPostgre.prepareStatement(sql);
				PreparedStatement stErrPostgre = connErr.prepareStatement(insertErrPostgre);
				ResultSet rsPostgre = stPostgre.executeQuery();
				
				
				stErrPostgre.setInt(2, k);
				stErrPostgre.setString(1, "Query eseguita correttamente su Postgre");
				stErrPostgre.executeUpdate();
				
				stPostgre.close();
			} catch(SQLException e) {
				PreparedStatement stErrPostgre;
				String errorMessage = e.getMessage();
//				e.printStackTrace();
				try {
					stErrPostgre = connErr.prepareStatement(insertErrPostgre);
					stErrPostgre.setInt(2, k);
					stErrPostgre.setString(1, errorMessage);

					stErrPostgre.executeUpdate();
				} catch (SQLException e1) {
				}
			}
		}	
		return ("Query eseguite");
	}
}

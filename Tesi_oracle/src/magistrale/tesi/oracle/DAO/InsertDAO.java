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
		
		Connection conn = DBConnect.getInstance().getConnection();
		Connection connErr = DBConnect.getInstance().getConnectionErr();
	
		int columnsNumber = 0;
		
		TreeMap<Integer, String> queries = new TreeMap<Integer, String>();
		
		String getQuery = "select queryOutputId, queryEseguita from try;";
		String insertErr = "update try SET testoMessaggioMySql=? WHERE queryOutputId=?;";
	
		try {
			PreparedStatement stQuery = connErr.prepareStatement(getQuery);
			ResultSet rsQuery = stQuery.executeQuery();
			while (rsQuery.next()) {
				queries.put(rsQuery.getInt("queryOutputId"), rsQuery.getString("queryEseguita"));
			}
		}catch(SQLException e) {
			throw new RuntimeException("Error Connection Database try to get the queries");
		}	
		
		for (int k: queries.keySet()) {
			System.out.println(k + " " + queries.get(k));
			String sql = queries.get(k);
			
			ArrayList<Object> className = new ArrayList<Object>() ;
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				PreparedStatement stErr = connErr.prepareStatement(insertErr);

				ResultSet rs = st.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
						
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

				stErr.setInt(2, k);
				stErr.setString(1, "Query eseguita correttamente su MySQL");
				stErr.executeUpdate();
				st.close();
			} catch(SQLException e) {
				PreparedStatement stErr;
				String errorMessage = e.getMessage();

				try {
					stErr = connErr.prepareStatement(insertErr);
					stErr.setInt(2, k);
					stErr.setString(1, errorMessage);

					stErr.executeUpdate();
				} catch (SQLException e1) {
				}

			}
			
		}
		try {
			conn.close();
			connErr.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ("Query eseguite");
	}
}

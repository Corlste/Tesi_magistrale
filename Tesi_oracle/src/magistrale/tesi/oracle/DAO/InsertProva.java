package magistrale.tesi.oracle.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class InsertProva {

		public String doQuery() {
			
//			Connection conn = DBConnect.getInstance().getConnection();
			
			Connection connErr = DBConnect.getInstance().getConnectionErr();
			
			Connection connPostgre = DBConnect.getInstance().getConnectionPostgre();
		
			int columnsNumber = 0;
			

			String insertErr = "update try SET testoMessaggioPostgres=? WHERE queryOutputId=?;";
		

			
				String sql = "select mandate from contacts";
				
				ArrayList<Object> className = new ArrayList<Object>() ;
				try {
					PreparedStatement st = connPostgre.prepareStatement(sql);
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
					
					while (rs.next()) {
						String result= rs.getString(1);
						System.out.println(result);
					}
					stErr.setInt(2, 120);
					stErr.setString(1, "Query eseguita correttamente su MySQL");
					stErr.executeUpdate();
					
					st.close();
				} catch(SQLException e) {
					PreparedStatement stErr;
					String errorMessage = e.getMessage();
					e.printStackTrace();

					try {
						stErr = connErr.prepareStatement(insertErr);
						stErr.setInt(2, 120);
						stErr.setString(1, errorMessage);

						stErr.executeUpdate();
					} catch (SQLException e1) {
					}

				}
				
//			}
			try {
				connPostgre.close();
				connErr.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ("Query eseguite");
		}
	

}

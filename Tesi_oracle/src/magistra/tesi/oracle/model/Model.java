package magistra.tesi.oracle.model;

import java.util.HashMap;

import magistrale.tesi.oracle.DAO.*;


public class Model {
	

	public static void main(String[] args) {
		
		InsertDAO inserisci = new InsertDAO();
//		InsertProva inserisci = new InsertProva();
		
//		String miaquery = "SELECT Companyid FROM contacts WHERE delivererid IN (SELECT delivererid FROM deliverers WHERE Name=Baker);";

//		String miaquery = "SELECT DISTINCT NAME, INITIALS FROM DELIVERERS WHERE DELIVERERID NOT IN (SELECT DELIVERERID FROM CONTACTS)";
		
//		String miaquery = "SELECT DISTINCT DELIVERERID FROM PENALTIES WHERE AMOUNT=25 AND DELIVERERID IN (SELECT DELIVERERID FROM PENALTIES WHERE AMOUNT=30)";

//		String miaquery = "select name from BD.deliverehrs;";
		
		
		String risultato = inserisci.doQuery();
		System.out.println(risultato);

	}

}

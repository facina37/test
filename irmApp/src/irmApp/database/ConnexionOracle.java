package irmApp.database;

import java.sql.*;

/**
 * La classe ConnexionOracle permet de gerer la connexion à a base de données.
 *
 * Version : 24/03/2019
 * @author Laure Baudoin & Marie Bogusz
 */
public class ConnexionOracle {
    //Pilote
    String urlPilote = "oracle.jdbc.driver.OracleDriver";
    //voir le nom exact à gauche
    //URL
    String urlBase = "jdbc:oracle:thin:@192.168.254.3:1521:PFPBS";
    //String urlBase = "jdbc:oracle:thin:@localhost:1521:ORCL";
    Connection con;
    
    public ConnexionOracle(){
        //charger le pilote de connexion
        try{
            Class.forName(urlPilote);
            System.out.println("pilote chargé");
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }
        
        //connection a la bdd
        try{
            //utilisateur = login + pwd
            con = DriverManager.getConnection(urlBase,"GROUPE_54","GROUPE_54");
            System.out.println("Connexion bdd reussie");
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    
    /**
     * Connection() permet de retourner la connextion à la base de données.
     * 
     * @return con
     */
    public Connection ObtenirConnection(){
        return con;
    }
}

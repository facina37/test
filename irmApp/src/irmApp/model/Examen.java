package irmApp.model;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * La classe Examen permet de gerer les objets de type examen.
 * Un examen est constitué d'un id, d'une date, d'un nom et d'un prénom.
 * 
 * version 08/04/2019
 * @author Laure Baaudoin & Marie Bogusz
 */
public class Examen {
    //ID de l'examen
    private final StringProperty id;
    //Date à laquelle s'est déroulé l'examen
    private final StringProperty date;
    //Nom de famille du patient qui a passé l'examen
    private final StringProperty firstName;
    //Prénom du patient qui a passé l'examen
    private final StringProperty lastName;
    
    /**
     * Examen() est le constructeur de la classe Examen.
     * 
     * @param idP
     * @param dateP
     * @param firstNameP
     * @param lastNameP 
     */
    public Examen(int idP, Date dateP, String firstNameP, String lastNameP){
        id = new SimpleStringProperty(Integer.toString(idP));
        //dateP doit être sous la forme 
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        date = new SimpleStringProperty(df.format(dateP));
        firstName = new SimpleStringProperty(firstNameP);
        lastName = new SimpleStringProperty(lastNameP);
    }
    
    /**
     * Permettent de retourner des types string.
     * @return StringProperty
     */
    public StringProperty idProperty() {
        return id;
    }
    public StringProperty dateProperty() {
        return date;
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public StringProperty lastNameProperty() {
        return lastName;
    }
    
    /**
     * Getteurs et setteurs de la classe Examen
     */
    public String getFirstName() {
        return firstName.get();
    }
    public String getLastName() {
        return lastName.get();
    }
    public int getId() {
        return Integer.parseInt(id.get());
    }
    public String getDate(){
        return date.get();
    }
}
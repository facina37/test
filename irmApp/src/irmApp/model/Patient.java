package irmApp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * La classe Patient permet de gerer les objets de type Patient.
 * Un patient est constitué d'un id, d'un groupe, d'un nom et prénom, d"un age, 
 * d'un sexe, d'un grade et d'un statut.
 * 
 * version 08/04/2019
 * @author Laure Baaudoin & Marie Bogusz
 */
public class Patient {
    //ID du patient
    private final StringProperty id;
    //Groupe du patient
    private final StringProperty groupe;
    //Nom de famille du patient
    private final StringProperty firstName;
    //Prénom du patient
    private final StringProperty lastName;
    //Age du patient
    private final StringProperty age;
    //Sexe du patient
    private final StringProperty sexe;
    //Grade du gliome du patient
    private final StringProperty grade;
    //Statut du patient dans l'étude : Dans le programme, Exclus du programme ou Programme fini
    private final StringProperty statut;
    
    /**
     * Patient() est le constructeur de la classe Patient.
     * Les booleans expluP et finiP permettent de connaitre le statut du patient dans 
     * l'étude : Dans le programme, Exclus du programme ou Programme fini.
     * Si le patient n'a pas de grade, il sera initialisé à "X" pour l'affichage dans 
     * le tableau des patients.
     * 
     * @param idP
     * @param groupeP
     * @param firstNameP
     * @param lastNameP
     * @param ageP
     * @param excluP
     * @param finiP
     * @param sexeP
     * @param gradeP 
     */
    public Patient(int idP, int groupeP, String firstNameP, String lastNameP,
            int ageP, boolean excluP, boolean finiP, char sexeP, int gradeP){
        id = new SimpleStringProperty(Integer.toString(idP));
        groupe = new SimpleStringProperty(Integer.toString(groupeP));
        firstName = new SimpleStringProperty(firstNameP);
        lastName = new SimpleStringProperty(lastNameP);
        age = new SimpleStringProperty(Integer.toString(ageP));
        sexe = new SimpleStringProperty(Character.toString(sexeP));
        if (gradeP == 0){
            grade = new SimpleStringProperty("X");
        }else grade = new SimpleStringProperty(Integer.toString(gradeP));
        if(finiP == true)
            statut = new SimpleStringProperty("Programme fini");
        else if(excluP == true)
            statut = new SimpleStringProperty("Exclu du programme");
        else statut = new SimpleStringProperty("Dans le programme");
    }
    
    /**
     * Permettent de retourner des types string.
     * @return StringProperty
     */
    public StringProperty idProperty() {
        return id;
    }
    public StringProperty groupeProperty() {
        return groupe;
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public StringProperty lastNameProperty() {
        return lastName;
    }
    public StringProperty ageProperty() {
        return age;
    }
    public StringProperty sexeProperty() {
        return sexe;
    }
    public StringProperty gradeProperty() {
        return grade;
    }
    public StringProperty statutProperty() {
        return statut;
    }
    
    /**
     * Getteurs et setteurs de la classe Patient
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
    public String getStatut(){
        return statut.get();
    }
}
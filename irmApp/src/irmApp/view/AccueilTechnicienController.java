package irmApp.view;

import irmApp.MainApp;
import irmApp.model.Patient;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import irmApp.database.ConnexionOracle;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javax.swing.JOptionPane;

/**
 * La classe AccueilTechnicienController permet de gerer la page d'accueil des techniciens.
 * Permet d'afficher la liste des patients faisant partie de l'étude IRMCare.
 * Permet d'acceder à la page d'ajout d'un examen une fois un patient selectionné 
 * dans la liste.
 *
 * version 30/03/2019
 * @author Laure Baaudoin & Marie Bogusz
 */
public class AccueilTechnicienController implements Initializable {
    //*************************Partie Accueil***********************************
    @FXML
    private TabPane tabpane;
    // TableView de la liste des patients
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> idColumn, groupeColumn, firstNameColumn;
    @FXML
    private TableColumn<Patient, String> lastNameColumn, ageColumn, statutColumn;
    @FXML
    private TableColumn<Patient, String> sexeColumn, gradeColumn;
    // mot clé permettant de faire une reherche ciblée dans la TableView des patients
    @FXML
    private TextField motcle;
    
    //Pop up d'erreur si un patient n'est pas séléctionné avant de passer à la page d'ajout d'un examen
    private Stage dialogStage;

    //*************************Partie Ajout Examen******************************
    @FXML
    private GridPane gridpane;    
    //Champs du formulaire d'ajout d'un examen
    @FXML
    private DatePicker dateExamen;
    @FXML
    private TextField idMachine, volCrane, axeCrane, volTumeur, ttp, rcbv, mtt;
    @FXML
    private TextField rcbf, lac, naa_cho, cho_cr, lip_cr, naa_cr;
    @FXML
    private Label titre;
    
    //*************************Partie Connexion à la bdd************************
    // connexion à la base de données
    private ConnexionOracle maconnection = new ConnexionOracle();
    // créer une variable de la requête  
    private Statement stmt;
    
    //Patient séléctionné
    private Patient aPatient;
    //id du technicien connecté
    private Integer idtech;
    
    /**
     * Initializes the controller class.
     * Permet de lier les colonnes du tableView patient avec les données de la 
     * base de données.
     * Permet de remplir le tableView de patient à partie d'une liste de patient qui
     * correspond à la base de données.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        groupeColumn.setCellValueFactory(cellData -> cellData.getValue().groupeProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        sexeColumn.setCellValueFactory(cellData -> cellData.getValue().sexeProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        
        patientTable.setItems(recuperationPatients());
        
        //Adapattion de la taille des colonnes à la taille de la fenetre
        patientTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        idColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        groupeColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 6 );
        firstNameColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 34 );
        lastNameColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 30 );
        ageColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        statutColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        sexeColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        gradeColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );
        
        gridpane.setVisible(false);
    }   
    
    /** 
     * intiData() permet de récuperer l'Id du technicien qui s'est connecté
     * @param thisIdtech 
     */
    public void initData(Integer thisIdtech){
        idtech = thisIdtech;
        System.out.println(idtech);
    }

    /**
     * handleDeconnexion() permet de se déconnecter de l'interface technicien.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void setDeco(MouseEvent event) throws IOException{
 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/Connexion.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("IRM Care");
        window.getIcons().add(new Image("file:ressources/logo.jpg"));
        window.setScene(tableViewScene);
        window.show();               
    } 
    
    /**
     * handleAnnuler() est appelé lorsque le technicien veut annuler l'ajout d'un examen.
     * Permet de revenir à la page d'accueil médecin.
     * 
     * @param event 
     */
    @FXML
    private void handleAnnuler(ActionEvent event){
        tabpane.setVisible(true);
        gridpane.setVisible(false);        
    }
    
    /**
     * recuperationPatients() est appelé par l'initialiseur de la calsse.
     * Permet de remplire une liste de patient à partir de la base de données.
     */
    public ObservableList<Patient> recuperationPatients() {
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient";
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                System.out.println(result.getInt("GRADEGLIOMEACTUEL"));
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), 
                        result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"), 
                        result.getInt("AGEPATIENT"), result.getBoolean("EXCLUS"), 
                        result.getBoolean("PROGRAMMEFINI"), result.getString("SEXEPATIENT").charAt(0), 
                        result.getInt("GRADEGLIOMEACTUEL"));
                data.add(patient);
            }
            System.out.println("Liste remplie par la bdd");
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Liste non remplie par la bdd");
        }
        catch(NullPointerException e){
            System.out.println(e);
            System.out.println("Liste non remplie par la bdd");
        }
        return data;
    }
    
    /**
    * handleRechercher() est appelé lorsque le boutton rechercher est utilisé
    * et permet d'appliquer la recherche ciblée à l'aide d'un mot clé.
    */ 
    @FXML
    public void handleRechercher(){
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient";
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), 
                        result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"), 
                        result.getInt("AGEPATIENT"), result.getBoolean("EXCLUS"), 
                        result.getBoolean("PROGRAMMEFINI"), result.getString("SEXEPATIENT").charAt(0), 
                        result.getInt("GRADEGLIOMEACTUEL"));
                //on garde le patient si on trouve le mot clé dans son nom ou son prénom
                if(patient.getFirstName().toUpperCase().contains(motcle.getText().toUpperCase()) 
                        || patient.getLastName().toUpperCase().contains(motcle.getText().toUpperCase())) {
                    data.add(patient);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }        
        patientTable.setItems(data);
    }
    
    /**
    * handleAjoutIrm() est appelé lorsque le boutton ajouter un examen est utilisé.
    * Permet de passer a la page AjoutExamen quand un patient est sélectionné sinon 
    * affiche un message d'erreur.
    *
    * @param event
    */ 
    @FXML
    public void handleAjout(ActionEvent event){
        aPatient = patientTable.getSelectionModel().getSelectedItem();    
        if (aPatient != null) {
            patientTable.setItems(recuperationPatients());
            tabpane.setVisible(false);
            gridpane.setVisible(true);
            titre.setText("Ajouter un examen au patient " + aPatient.getLastName()+" "+aPatient.getFirstName());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Attention !");
            alert.setHeaderText("Rien n'a été sélectionné !");
            alert.setContentText("Il faut sélectionner un patient");
            alert.showAndWait();
        }
    }
    
    //====================================================Partie Ajout Examen=====================
    /**
     * isInputExamenValid() permet de vérifier si les champs du formulaire ne 
     * sont pas vides.
     * 
     * @return boolean
     */
    private boolean isInputExamenValid() {
        String errorMessage = "";
        
        if (dateExamen.getValue() == null) {
            errorMessage += "Date invalide !\n";
        } 
        if (idMachine.getText() == null || idMachine.getText().length() == 0) {
            errorMessage += "ID machine invalide !\n";
        }
        if (volCrane.getText() == null || volCrane.getText().length() == 0) {
            errorMessage += "Volume du crâne invalide !\n";
        }
        if (axeCrane.getText() == null || axeCrane.getText().length() == 0) {
            errorMessage += "Valeur maximum de l'axe du crâne invalide !\n";
        }
        if (volTumeur.getText() == null || volTumeur.getText().length() == 0) {
            errorMessage += "Volume de la tumeur invalide !\n";
        }
        if (ttp.getText() == null || ttp.getText().length() == 0) {
            errorMessage += "TTP invalide !\n";
        }
        if (rcbv.getText() == null || rcbv.getText().length() == 0) {
            errorMessage += "rCBV invalide !\n";
        }
        if (mtt.getText() == null || mtt.getText().length() == 0) {
            errorMessage += "MTT invalide !\n";
        }
        if (rcbf.getText() == null || rcbf.getText().length() == 0) {
            errorMessage += "rCBF invalide !\n";
        }
        if (lac.getText() == null || lac.getText().length() == 0) {
            errorMessage += "Lac invalide !\n";
        }
        if (naa_cho.getText() == null || naa_cho.getText().length() == 0) {
            errorMessage += "NAA/Cho invalide !\n";
        }
        if (cho_cr.getText() == null || cho_cr.getText().length() == 0) {
            errorMessage += "Cho/Cr invalide !\n";
        }
        if (lip_cr.getText() == null || lip_cr.getText().length() == 0) {
            errorMessage += "Lip/Cr invalide !\n";
        }
        if (naa_cr.getText() == null || naa_cr.getText().length() == 0) {
            errorMessage += "NAA/Cr invalide !\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(null);
            alert.setTitle("Attention !");
            alert.setHeaderText("Veuillez corriger.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
    /**
     * handleAjoutExamen() permet d'ajouter l'examen remplie dans le fomrulaire 
     * à un patient dans la base de données.
     * 
     * @param event
     */
    @FXML
    private void handleAjoutExamen(ActionEvent event){
        if (isInputExamenValid()) {
            String requeteAjout = "Insert into Examen (idMachine, idPatient, idTechnicien, dateExam,"
                   + " volCrane, valMaxAxeCrane, volTumeur, Cho_Cr, Naa_Cr, Naa_Cho, lip_cr, mtt,"
                   + " ttp, rcbv, rcbf, lac) values ("+idMachine.getText()+","+aPatient.getId()+","+idtech+",TO_DATE('"+dateExamen.getValue()+"','YYYY-MM-DD'),"
                   + volCrane.getText()+","+axeCrane.getText()+","+volTumeur.getText()+","+cho_cr.getText()+","+naa_cr.getText()+","
                   + naa_cho.getText()+","+lip_cr.getText()+","+mtt.getText()+","+ttp.getText()+","+rcbv.getText()+","+rcbf.getText()+","+lac.getText()+")";
            try{
                stmt = maconnection.ObtenirConnection().createStatement();
                stmt.executeQuery(requeteAjout);
                //petit pop up
                JOptionPane.showMessageDialog(null, "Enregistré avec succès");
                System.out.println("Enregistré");
                //Revenir à la page d'accueil technicien
                tabpane.setVisible(true);
                gridpane.setVisible(false);
            }
            catch(SQLException e){
                System.out.println(e);
                System.out.println("Non enregistré");
            }
        }
    }     
}

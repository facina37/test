package irmApp.view;

import irmApp.MainApp;
import irmApp.database.ConnexionOracle;
import irmApp.model.Examen;
import irmApp.model.Patient;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * La classe AccueilMedecinController permet de gerer la page d'accueil des médecins.
 * Permet d'afficher la liste des patients faisant partie de l'étude IRMCare.
 * Permet d'afficher la liste des examens à vérifier.
 * 
 * Permet d'acceder à la page d'ajout d'une previsite une fois un patient selectionné dans la liste.
 * La page d'ajout d'un prévisite permet de gerer l'ajout des informations récoltées par le médecin 
 * lors d'une prévisite à un patient.
 * 
 * Permet d'acceder à la page de vérification d'un examen une fois l'examen selectionné dans la liste.
 * La page de vérification d'un examen permet au médecin d'emettre son avis sur les examens réalisés 
 * par les techniciens.
 * 
 * version 08/04/2019
 * @author Laure Baaudoin & Marie Bogusz
 */
public class AccueilMedecinController implements Initializable {

    //*************************Partie Accueil***********************************
    @FXML
    private TabPane tabpane;
    
    //TableView de la liste des patients
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
    private TextField motclePatient;
    
    //TableView de la liste des examens
    @FXML
    private TableView<Examen> examenTable;
    @FXML
    private TableColumn<Examen, String> idExamColumn, dateColumn, prenomColumn, nomColumn; 
    // mot clé permettant de faire une reherche ciblée dans la TableView des examens
    @FXML
    private TextField motcleExamen;
    
    //Pop up d'erreur si un patient n'est pas séléctionné avant de passer à la page d'ajout d'un examen
    private Stage dialogStage;
    
    //*************************Partie Ajout Prévisite***************************
    @FXML
    private GridPane gridpane;
    
    //Champs du formulaire d'ajout d'une previsite
    @FXML
    private DatePicker dateVisite;
    @FXML
    private TextField poids, freqCardiaque;
    @FXML
    private ComboBox<String> typeLot;//Choix entre DiOrZen ou placebo
    @FXML
    private TextField tension, leucocytes, hemoglobine;
    @FXML
    private Label messageSucces, titre;
    @FXML
    private Button ajoutVisite;    
    
    //Partie Ajout Médicament du formulaire
    @FXML
    private TextField medicament;
    @FXML
    private TextArea raisonPrise;
    
    //*************************Partie Verification Examen***********************
    @FXML
    private GridPane gridpaneExamen;
    
    //données
    @FXML
    private Label gradeMachine, risqueTotal, volCrane, axeCrane, volTumeur, titreExam;
    @FXML
    private Label mtt, ttp, rcbv, rcbf, cho_cr, naa_cr, naa_cho, lac, lip_cr;
    private boolean valide;
    
    //partie erreur
    @FXML
    private Label messageErreur, titreErreur;
    @FXML
    private RadioButton refaire, suppression;
    @FXML
    private Button valideErreur;
    
    //partie choix du grade
    @FXML
    private ComboBox grade;
    @FXML
    private Button valideGrade;
    @FXML
    private Label titreGrade;
    
    //*************************Partie Connexion BDD*****************************
    // connexion à la base de données
    private ConnexionOracle maconnection = new ConnexionOracle();
    //créer une variable de la requête
    private Statement stmt; 
    
    //Patient séléctionné
    private Patient aPatient;
    //Examen séléctionné
    private Examen examen;
    //Id du médecin qui s'est conneccté
    private Integer idmed;

    /**
     * Initializes the controller class.
     * Permet de lier les colonnes des tableView patient et examen avec les données 
     * de la base de données.
     * Permet de remplir les tableView patient et examen à partie d'une liste de patient qui
     * correspond à la base de données.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        //partie patient
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        groupeColumn.setCellValueFactory(cellData -> cellData.getValue().groupeProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        sexeColumn.setCellValueFactory(cellData -> cellData.getValue().sexeProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        
        patientTable.setItems(recuperationPatients());
        
        //partie examens
        idExamColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        
        examenTable.setItems(recuperationExamens());
        //Adapattion de la taille des colonnes à la taille de la fenetre
        examenTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        idExamColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );
        dateColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        prenomColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 35 );
        nomColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 35 );        
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
        gridpaneExamen.setVisible(false);
    }    
    
    /** 
     * intiData() permet de récuperer l'Id du médecin qui s'est connecté.
     * 
     * @param thisIdmed 
     */
    public void initData(Integer thisIdmed){
        idmed = thisIdmed;
        System.out.println(idmed);
    }    

    /**
     * handleDeconnexion() permet de se déconnecter de l'interface médecin.
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
    *  recuperationPatients() permet d'afficher la liste de tous les patients dans le TableView.
    */
    public ObservableList<Patient> recuperationPatients() {
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient";
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), 
                        result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"), 
                        result.getInt("AGEPATIENT"), result.getBoolean("EXCLUS"), result.getBoolean("PROGRAMMEFINI"), 
                        result.getString("SEXEPATIENT").charAt(0), result.getInt("GRADEGLIOMEACTUEL"));
                data.add(patient);
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
        return data;
    }
    
    /**
    * handleRechercherPatients() permet d'afficher la liste de tous les patients 
    * recherchés à l'aide d'un mot clé.
    */
    @FXML
    public void handleRechercherPatients(){
        ObservableList<Patient> data = FXCollections.observableArrayList();
        Patient patient;
        String requete = "select * from Patient";
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                patient = new Patient(result.getInt("IDPATIENT"), result.getInt("IDGROUPE"), 
                        result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"), 
                        result.getInt("AGEPATIENT"), result.getBoolean("EXCLUS"), result.getBoolean("PROGRAMMEFINI"), 
                        result.getString("SEXEPATIENT").charAt(0), result.getInt("GRADEGLIOMEACTUEL"));
                //on garde le patient si on trouve le mot clé dans son nom ou son prénom
                if(patient.getFirstName().toUpperCase().contains(motclePatient.getText().toUpperCase()) 
                        || patient.getLastName().toUpperCase().contains(motclePatient.getText().toUpperCase())) {
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
    * handleAjoutVisite() est appelé lorsque le boutton ajouter une prévisite est utilisé.
    * Permet de passer a la page AjoutVisite quand un patient est sélectionné.
    *
    * @param event
    */
    @FXML
    private void handleAjoutVisite(ActionEvent event){
        aPatient = patientTable.getSelectionModel().getSelectedItem();
        if (aPatient != null) {
            if (aPatient.getStatut().equals("Dans le programme")){
                initializePrevisite();
                gridpane.setVisible(true);
                tabpane.setVisible(false);
            }  else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(dialogStage);
                alert.setTitle("Attention !");
                alert.setHeaderText("Patient incorrect !");
                alert.setContentText("Il faut sélectionner un patient qui fait partie du programme.");

                alert.showAndWait();
            }                  
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Attention !");
            alert.setHeaderText("Rien n'a été sélectionné !");
            alert.setContentText("Il faut sélectionner un patient.");

            alert.showAndWait();
        }       
    }    
    
    /**
     *  recuperationExamens() permet de récupère tous les examens à valider.
    */
    public ObservableList<Examen> recuperationExamens()
    {
        ObservableList<Examen> data = FXCollections.observableArrayList();
        Examen examen;
        String requete = "select * from Examen join patient on examen.idpatient = patient.idpatient where examen.gradeMedecin is null";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next())
            {
                examen = new Examen(result.getInt("IDEXAMEN"), result.getDate("DATEEXAM"), 
                        result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"));
                data.add(examen);
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
     *  handleRechercherExamens() permet d'afficher la liste de tous les examens recherchés.
    */
    @FXML
    public void handleRechercherExamens(){
        ObservableList<Examen> data = FXCollections.observableArrayList();
        Examen examen;
        String requete = "select * from Examen join patient on examen.idpatient = patient.idpatient where examen.gradeMedecin is null";
        
        try{
            
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                examen = new Examen(result.getInt("IDEXAMEN"), result.getDate("DATEEXAM"), 
                        result.getString("PRENOMPATIENT"), result.getString("NOMPATIENT"));
                
                //on garde le patient si on trouve le mot clé dans son nom ou son prénom
                if(examen.getFirstName().toUpperCase().contains(motcleExamen.getText().toUpperCase()) 
                        || examen.getLastName().toUpperCase().contains(motcleExamen.getText().toUpperCase()))
                {
                    data.add(examen);
                }
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
        examenTable.setItems(data);
    }
    
    /**
     * handleVerifExam() permet de passer à la page Examen perso quand un patient est sélectionné.
     * 
     * @param event
    */
    @FXML
    public void handleVerifExam(ActionEvent event){
        examen = examenTable.getSelectionModel().getSelectedItem();
        if (examen != null) {
            initializerExamen();
            gridpaneExamen.setVisible(true);
            tabpane.setVisible(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Attention !");
            alert.setHeaderText("Rien n'a été sélectionné !");
            alert.setContentText("Il faut sélectionner un examen.");
            alert.showAndWait();
        }
    }
    
    //====================================================Partie Ajout Prévisite===============================
    /**
     * initializePrevisite() permet d'initialiser les information de la page 
     * d'ajout d'une prévisite.
     * Cette page est vivisble seulement quand un patient est séléctionné dans 
     * la page d'accueil du médecin.
     */
    private void initializePrevisite(){
        titre.setText("Ajout d'une pré-visite au patient "+aPatient.getLastName()+" "+aPatient.getFirstName());
        ObservableList<String> list = FXCollections.observableArrayList(
            "DiOrZen", "Placebo"
        );
        typeLot.setItems(list);
        messageSucces.setText("");
        
        dateVisite.setDisable(false);
        poids.setDisable(false);
        freqCardiaque.setDisable(false);     
        typeLot.setDisable(false);
        tension.setDisable(false);
        leucocytes.setDisable(false);
        hemoglobine.setDisable(false);
        ajoutVisite.setDisable(false);
        
        raisonPrise.setDisable(true);
        medicament.setDisable(true);
    }
    
    /**
     * handleAjoutPrevisite() permet de spécifier si l'ajout de la previsite est validé.
     * Permet l'acces à la partie du formulaire permettant l'ajout d'un ou 
     * plusieurs médicaments.
     * 
     * @param event
     */
    @FXML
    private void handleAjoutPrevisite(ActionEvent event){
        if (isInputVisiteValid()) {
            AjoutPrevisite(event);
            messageSucces.setText("Vous avez ajouté une prévisite à la base de données");            
        }
    } 
    
    /**
     * AjoutPrevisite() permet d'ajouter la prévisite à la base de données.
     * 
     * @param event
     */
    private void AjoutPrevisite(ActionEvent event) {
        
        String requeteAjout = "Insert into Previsite (idPatient, idMed, dateVisite,"
                   + " poids, freqcardiaque, tension, tauxleuco, tauxhemoglo) values "
                + "("+aPatient.getId()+","+idmed+",TO_DATE('"+dateVisite.getValue()+"','YYYY-MM-DD'),"
                   + poids.getText()+","+freqCardiaque.getText()+","+tension.getText()+","
                +leucocytes.getText()+","+hemoglobine.getText()+")";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            stmt.executeQuery(requeteAjout);
            //petit pop up
            JOptionPane.showMessageDialog(null, "Enregistré avec succès");
            //Grise la partie ajout prévisite
            dateVisite.setDisable(true);
            poids.setDisable(true);
            poids.setText("");
            freqCardiaque.setDisable(true); 
            freqCardiaque.setText("");
            typeLot.setDisable(true);
            tension.setDisable(true);
            tension.setText("");
            leucocytes.setDisable(true);
            leucocytes.setText("");
            hemoglobine.setDisable(true);
            hemoglobine.setText("");
            ajoutVisite.setDisable(true);
            
            raisonPrise.setDisable(false);
            medicament.setDisable(false);
        }
        catch(SQLException e){
            System.out.println(e); 
        }
    }
    
    /**
     * handleAjoutMedicament() permet de spécifier si le médicament est bien ajouté 
     * à une previsite.
     * 
     * @param event
     */
    @FXML
    private void handleAjoutMedicament(ActionEvent event){
        if(ajoutVisite.isDisable() == true){
            if (isInputMedicamentValid()) {
                
                int idMedicament;
                int idPrevisite;
                
                idPrevisite = recupIdPrevisite();
                ajoutMedicament();
                
                idMedicament = recupIdMedicament();
                if(idMedicament != -1 && idPrevisite != -1)
                {
                    ajoutIngerer(idMedicament, idPrevisite);
                    JOptionPane.showMessageDialog(null, "Médicament enregistré avec succès.");
                    raisonPrise.setText("");
                    medicament.setText("");
                    messageSucces.setText(""); 
                }
            }
        }
        else {
            messageSucces.setText("Vous devez ajouter une prévisite auparavant");
        }
    }
    
    /**
     * recupIdPrevisite() permet de récuperer l'id de la dernière prévisite ajouté à la bdd.
     * 
     * @return integer
     */
    private int recupIdPrevisite(){
        String requeteVerif = "select * from Previsite ORDER BY idVisite DESC";
        System.out.println(requeteVerif);
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requeteVerif);
            while(result.next()){
                return result.getInt("idvisite");
            }
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");
        }
        return -1;
    }
    
    /**
     * ajoutMedicament() permet de vérifier si le médicament existe déjà dans 
     * la base de données.
     * Si non, il y est ajouté.
     */
    private void ajoutMedicament() 
    {
        //verifie si il y a deja un medoc avec le mm nom
        String requeteVerif = "select * from Medicament";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requeteVerif);
            boolean dejaExistant = false;
            while(result.next()){
                if(result.getString("nommedic").equals(medicament.getText())) {
                    dejaExistant = true;
                }
            }      
            if(dejaExistant == false) {
                String AjoutMedoc = "insert into Medicament values (1, '"+medicament.getText()+"')";
                System.out.println(AjoutMedoc);
                stmt.executeQuery(AjoutMedoc);
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    
    /**
     * recupIdMedicament() permet de récuperer l'id du médicament correspondant 
     * à la prise du patient.
     * 
     * @return integer
     */
    private int recupIdMedicament()
    {
        String requeteVerif = "select * from Medicament where nommedic like '"+medicament.getText()+"'";
        System.out.println(requeteVerif);
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requeteVerif);
            while(result.next()){
                return result.getInt("idmedicament");
            }
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");
        }
        System.out.println("idmedicament non obtenu");
        return -1;
    }
    
    /**
     * ajoutIngerer() permet de lier un médicament et une prévisite.
     * 
     * @param idMedicament
     * @param idPrevisite 
     */
    private void ajoutIngerer(int idMedicament, int idPrevisite){
        String requeteAjout = "insert into Ingerer values ("+idMedicament+","+idPrevisite+",'"+raisonPrise.getText()+"')";
        try{
            stmt = maconnection.ObtenirConnection().createStatement();
            stmt.executeQuery(requeteAjout);
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Non enregistré");
        }
    }
        
    /**
    * handleTerlmine() est appelé lorsque le médecin à fini d'ajouter une prévisite.
    * Permet de revenir à la page d'accueil médecin.
    */
    @FXML
    private void handleTermine(ActionEvent event) throws IOException {
        patientTable.setItems(recuperationPatients());
        tabpane.setVisible(true);
        gridpane.setVisible(false);        
    }
    
    /**
    * isInputVisiteValid() est appelé lorsque le boutton ajouter un médicament à 
    * une prévisite est utilisé.
    * Permet de tester la validité des champs du formulaire d'ajout d'une prévisite.
    */
    private boolean isInputVisiteValid() {
        String errorMessage = "";
        
        if (dateVisite.getValue() == null) {
            errorMessage += "Date invalide !\n";
        } 
        if (poids.getText() == null || poids.getText().length() == 0 || !isNumber(poids.getText())){
            errorMessage += "Poids invalide !\n";
        }
        if (freqCardiaque.getText() == null || freqCardiaque.getText().length() == 0 || !isNumber(freqCardiaque.getText())) {
            errorMessage += "Fréquence cardiaque invalide !\n";
        }
        if(typeLot.getValue() == null)
            errorMessage += "Type de lot invalide !\n";
        else
            if (!typeLot.getValue().equals("DiOrZen") && !typeLot.getValue().equals("Placebo"))
                errorMessage += "Type de lot invalide !\n";
        if (tension.getText() == null || tension.getText().length() == 0) {
            errorMessage += "Tension invalide !\n";
        }
        if (leucocytes.getText() == null || leucocytes.getText().length() == 0) {
            errorMessage += "Taux de leucocytes invalide !\n";
        }
        if (hemoglobine.getText() == null || hemoglobine.getText().length() == 0) {
            errorMessage += "Taux d'hémoglobine invalide !\n";
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
     * renvoie True si le String envoyé peut bien être converti en nombre.
     * 
     * @param str
     * @return boolean
     */
    public boolean isNumber(String str)
    {
        try {
            Float.parseFloat(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    
    /**
    * isInputMedicamentValid() est appelé lorsque le boutton ajouter une prévisite 
    * est utilisé.
    * Permet de tester la validité des champs du formulaire d'ajout d'un médicament
    * lors d'une prévisite.
    */
    private boolean isInputMedicamentValid() {
        String errorMessage = "";
        if (medicament.getText() == null || medicament.getText().length() == 0) {
            errorMessage += "Principe actif invalide!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(null);
            alert.setTitle("Attention!");
            alert.setHeaderText("Veuillez corriger.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
    //====================================================Partie Verif Examen================
    /**
     * initializerExamen() permet d'initiaiser la page d'ajout d'un examen.
     */
    private void initializerExamen(){
        titreExam.setText("Examen de "+examen.getLastName()+" "+examen.getFirstName()+" le "+examen.getDate());
        ObservableList<String> data = FXCollections.observableArrayList("1","2","3","4");
        grade.setPromptText("Faites votre choix");
        grade.setItems(data);
        
        ToggleGroup group = new ToggleGroup();
        refaire.setToggleGroup(group);
        refaire.setSelected(true);
        suppression.setToggleGroup(group);
        
        //Initialisation de l'affichage pour la partie validité de l'examen
        refaire.setVisible(false);
        suppression.setVisible(false);
        valideErreur.setVisible(false);
        grade.setVisible(false);
        valideGrade.setVisible(false);
        titreGrade.setVisible(false);
        titreErreur.setVisible(false);
        
        recuperationInfos();
    }
    
    /**
     * handleAnnuler() est appelé lorsque le médecin ne veux plus vérifier un examen.
     * Permet de revenir à la page d'accueil médecin.
     * 
     * @param event 
     */
    @FXML
    private void handleAnnuler(ActionEvent event){
        tabpane.setVisible(true);
        gridpaneExamen.setVisible(false);        
    }
    
    
    /**
     * handleValideErreur() permet de valider la décision du médecin sur l'examen 
     * qui a une erreur d'IRM et de revenir sur la page d'accueil du médecin.
     * 
     * @param event
     */
    @FXML
    private void handleValideErreur(ActionEvent event){        
        if (refaire.isSelected() == false && suppression.isSelected() == false) {
            JOptionPane.showMessageDialog(null, "Veuillez faire un choix.");
        }else{
            if (refaire.isSelected()){ 
                //==> on doit supprimer les données de cet examen
                String requeteSuppr = "delete from examen where idexamen = "+examen.getId()+"";
                try {
                    stmt = maconnection.ObtenirConnection().createStatement();
                    stmt.executeQuery(requeteSuppr);
                    JOptionPane.showMessageDialog(null, "La date de l'examen à été mise à jour.");
                    examenTable.setItems(recuperationExamens());
                    tabpane.setVisible(true);
                    gridpaneExamen.setVisible(false);   
                }
                catch(SQLException e) {
                    System.out.println("Erreur, l'examen n'a pas été supprimé");
                }
                //==> on doit en reprogrammer un dans deux jours
            }
            if (suppression.isSelected()){
                //==> on doit supprimer les données de cet examen
                String requeteSuppr = "delete from examen where idexamen = "+examen.getId()+"";
                try {
                    stmt = maconnection.ObtenirConnection().createStatement();
                    stmt.executeQuery(requeteSuppr);
                    JOptionPane.showMessageDialog(null, "L'examen a bien été supprimé.");
                    examenTable.setItems(recuperationExamens());
                    tabpane.setVisible(true);
                    gridpaneExamen.setVisible(false);   
                }
                catch(SQLException e) {
                    System.out.println("Erreur, l'examen n'a pas été supprimé");
                }
            }
        }        
    }
    
    /**
     * handleValideGrade() valider la décision du médecin sur le grade de l'examen
     * et de revenir sur la page d'accueil du médecin.
     * 
     * @param event
     */
    @FXML
    private void handleValideGrade(ActionEvent event) {
        if (grade.getValue() == null){
            //petit pop up
            JOptionPane.showMessageDialog(null, "Veuillez faire un choix.");
        }else {
            String requeteGrade = "update examen set gradeMedecin = "+grade.getValue()
                    +", idmed = "+idmed+" where idexamen = "+examen.getId()+"";
            try {
                //petit pop up
                JOptionPane.showMessageDialog(null, "Choix enregistré avec succès.");
                stmt = maconnection.ObtenirConnection().createStatement();
                stmt.executeQuery(requeteGrade);
                examenTable.setItems(recuperationExamens());
                tabpane.setVisible(true);
                gridpaneExamen.setVisible(false);   
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * recuperationInfos() permet d'afficher les données de l'examen à vérifier.
     */
    public void recuperationInfos(){
        String requete = "select * from examen where idexamen = '"+examen.getId()+"'";
        try {
            stmt = maconnection.ObtenirConnection().createStatement();
            ResultSet result = stmt.executeQuery(requete);
            while(result.next()) {
                gradeMachine.setText("Grade machine : "+result.getString("GRADEMACHINE"));
                risqueTotal.setText("Risque total : "+result.getString("RISQUE"));
                volCrane.setText("Volume crânien : "+result.getString("VOLCRANE"));
                axeCrane.setText("Volume max. axe crânien : "+result.getString("VALMAXAXECRANE"));
                volTumeur.setText("Volume tumeur : "+result.getString("VOLTUMEUR"));
                ttp.setText("TTP : "+result.getString("TTP"));
                rcbv.setText("rCBV : "+result.getString("RCBV"));
                mtt.setText("MTT : "+result.getString("MTT"));
                rcbf.setText("rCBF : "+result.getString("RCBF"));
                lac.setText("Lac : "+result.getString("LAC"));
                naa_cho.setText("Naa/Cho : "+result.getString("NAA_CHO"));
                cho_cr.setText("Cho/Cr : "+result.getString("CHO_CR"));
                lip_cr.setText("Lip/Cr : "+result.getString("LIP_CR"));
                naa_cr.setText("Naa/Cr : "+result.getString("NAA_CR"));
                valide = result.getBoolean("ERREURANNA");
                //Affiche les bons champs selon la vlidité de l'examen
                gestionErreurs();
               // idPatient = result.getInt("IDPATIENT");
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }
    
    /**
     * gestionErreurs() permet d'acceder au bon formulaire selon la validité 
     * (si l'examen à une erreur anatomique ou non) de l'examen à vérifier.
     */
    public void gestionErreurs(){
        if(valide == true){
            messageErreur.setText("L'IRM a présenté des erreurs anatomiques");
            refaire.setVisible(true);
            suppression.setVisible(true);
            valideErreur.setVisible(true);
            titreErreur.setVisible(true);
        } else {
            messageErreur.setVisible(false);
            grade.setVisible(true);
            valideGrade.setVisible(true);
            titreGrade.setVisible(true);
        }
    }    
}

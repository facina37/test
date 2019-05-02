package irmApp;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** La classe MainApp permet de gerer le lancement de l'application.
 * 
 * Version : 30/03/2019
 * Author : Laure Baudoin & Marie Bogusz
*/
public class MainApp extends Application {
    
    private Stage primaryStage;
    private AnchorPane root;
       
     @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("IRM Care");
        this.primaryStage.getIcons().add(new Image("file:ressources/logo.png"));

        showConnexion();
        
        //Pop up vérification fermetuere application
        this.primaryStage.setOnCloseRequest(event -> {           
            Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,"Etes vous sûr de vouloir quitter l'application?");
            Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK);
            exitButton.setText("Quitter");
            closeConfirmation.setHeaderText("Confirmer la fermeture");
            closeConfirmation.initModality(Modality.APPLICATION_MODAL);
            closeConfirmation.initOwner(primaryStage);

            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get())) {
                event.consume();
            }
        });    
    }
    
    /**
     * showConnexion() permet d'ouvrir la page de connexion, 1ere page de 
     * l'application.
     */
    public void showConnexion(){
         try {
            // nouvelle scene pour pop up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/Connexion.fxml"));
            root = (AnchorPane) loader.load();

            // Creer nouvelle scene et l'ouvre.
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
               
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
}

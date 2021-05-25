/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.ComparatorCognome;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	this.txtResult.clear();
    	Actor attore=this.boxAttore.getValue();
    	if(attore==null)
    	{
    		txtResult.setText("SCEGLI UN ATTORE");
    	}
    	else
    	{
    		List<Actor> simili=new ArrayList<Actor>(this.model.getConnectedActors(attore));
    		txtResult.appendText("GLI ATTORI SIMILI A "+attore.toString()+" SONO :\n");
    		for(Actor a:simili)
    		{
    		txtResult.appendText(a.toString()+"\n");
    		}
    		
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	String genere=this.boxGenere.getValue();
    	if(genere==null)
    	{
    		txtResult.setText("SCEGLI UN VALORE");
    		
    	}
    	else
    	{
    	this.model.creaGrafo(genere);
    	List<Actor> attoriBox=new ArrayList<Actor>(this.model.getIdMap().values());
    	Collections.sort(attoriBox,new ComparatorCognome());
    	this.boxAttore.getItems().addAll(attoriBox);
    	txtResult.appendText("GRAFO CREATO\n");
    	txtResult.appendText("# ARCHI: "+this.model.getNumeroArchi()+"\n");
    	txtResult.appendText("# VERTICI: "+this.model.getNumeroVertici()+"\n");
    	}
    	
    	
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    	String num=this.txtGiorni.getText();
    	int numPause=0;
    	List<Actor> attoriIntervistati=new ArrayList<Actor>();
    	Integer numI=0;
    	try {
    		numI=Integer.parseInt(num);
    	}catch(NumberFormatException e)
    	{
    		e.printStackTrace();
    	}
    	model.simulate(numI);
    	numPause=this.model.getPause();
    	//attoriIntervistati=this.model.getAttoriSim();
    	this.txtResult.appendText("L'INTERVISTATORE PER INTERVISTARE SI E' PRESO "+numPause+" PAUSE\n ");
    	for(Actor a:model.getAttoriSim())
    	{
    		txtResult.appendText(a.toString()+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(this.model.getGeneri());
    }
}

package zpracovani_namerenych_dat;


import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NovySouborOkno extends Stage
{
	Button potvrzeni;
	Button ukonceni;
	TextField nazevSouboru;
	HlavniOkno okno;
	
	public NovySouborOkno()
	{
		this.setWidth(410);
		this.setHeight(140);
		this.setTitle("Vytvoøení nového souboru");
		this.setScene(vytvorScene());
		this.setResizable(false);
	}
	
	public Scene vytvorScene()
	{
		int velikostSceny = 400;
		Scene scene = new Scene(vytvorObjekty(), velikostSceny, velikostSceny);	
		return scene;
	}
	
	public BorderPane vytvorObjekty()
	{
		BorderPane root = new BorderPane();
		String color = "357EBD";
		root.setStyle("-fx-background-color: #" + color);
		root.setCenter(vytvorKontrolni());
		return root;
	}
	

	
	public GridPane vytvorKontrolni()
	{
		GridPane panel = new GridPane();
		
		panel.setHgap(5);
		panel.setVgap(5);
		
		Label label = new Label("Zadejte název souboru (rok) - napø. 1999");
		label.setFont(new Font("Arial", 15));
		
		potvrzeni = new Button("Potvrdit");
		ukonceni = new Button("Zpìt");
		nazevSouboru = new TextField() 
		{
		    @Override 
		    public void replaceText(int start, int end, String text) 
		    {
		    	int delka = nazevSouboru.getLength();
		        if (text.matches("[0-9]*") && delka < 5)
		        {
		            super.replaceText(start, end, text);
		        }
		        else nazevSouboru.setText(nazevSouboru.getText().substring(0, 4));
		    }
		 
		    @Override 
		    public void replaceSelection(String text) 
		    {
		    	int delka = nazevSouboru.getLength();
		        if (text.matches("[0-9]*") && delka < 5)
		        {
		            super.replaceSelection(text);
		        }
		        else nazevSouboru.setText(nazevSouboru.getText().substring(0, 4));
		    }
		};
		
		nazevSouboru.setPromptText("Zadejte název souboru (rok)");
		nazevSouboru.setPrefWidth(200);
		
		potvrzeni.setPrefWidth(80);
		potvrzeni.setPrefHeight(30);
		
		ukonceni.setPrefWidth(80);
		ukonceni.setPrefHeight(30);
		
		potvrzeni.setOnAction(event -> odesliNazev());
		ukonceni.setOnAction(event -> this.close());
		
		panel.add(label, 3, 1, 1, 1);
		panel.add(nazevSouboru, 3, 2, 2, 1);
		panel.add(potvrzeni,3, 3);
		panel.add(ukonceni, 4, 3);
		panel.setAlignment(Pos.CENTER);
		return panel;
	}
	
	public void odesliNazev()
	{
		String nazev = nazevSouboru.getText();
		ObservableList<String> listCSV = okno.vyberRoku.getItems();
		if (listCSV.contains(nazev) == false && nazev.length() == 4)
		{
			okno.vytvorSouborVProjektu(nazev + ".csv");
			this.close();
		}
		else 
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Zadejte název souboru");
			alert.setContentText("Soubor s tímto názvem už existuje nebo jste zadal špatný poèet èísel (musí být 4). ");
			alert.show();	
		}
	}
	
	public void setHlavniOkno(HlavniOkno okno)
	{
		this.okno = okno;
	}
}

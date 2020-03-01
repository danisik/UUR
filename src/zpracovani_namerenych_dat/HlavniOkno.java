package zpracovani_namerenych_dat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HlavniOkno extends Application
{
	Callback<TableColumn<Mesto,String>, TableCell<Mesto,String>> editableFactoryMesto;
	Callback<TableColumn<Mesto,String>, TableCell<Mesto,String>> editableFactoryMesic;
	Stage primaryStage;
	TableView<Mesto> tabulka;
	GrafOkno grafOkno;
	NovySouborOkno souborOkno;
	Label nazevRokuLabel;
	
	/** dolni buttony*/
	Button zobrazGraf;
	Button ulozSoubor;
	Button vymazUdaje;
	Button vymazOznaceneUdaje;
	Button novyRadek;
	
	/** horni buttony*/
	Button nactiSoubor;
	ChoiceBox<String> vyberRoku;
	Button vymazSoubor;
	
	
	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception 
	{
		primaryStage = vytvorStage(stage);
		primaryStage.setMinWidth(900);
		primaryStage.setMinHeight(500);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		souborOkno = new NovySouborOkno();
		grafOkno = new GrafOkno();
	}
	
	public Stage vytvorStage(Stage stage)
	{
		stage.setScene(vytvorScene());
		stage.setTitle("Zpracování namìøených teplot ve mìstech");
		return stage;
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
		root.setTop(vytvorHorni());
		root.setCenter(vytvorTabulku());
		root.setBottom(vytvorDolni());
		ziskejCSV();
		return root;
	}
	
	public GridPane vytvorHorni()
	{
		GridPane horniPanel = new GridPane();
		
		nactiSoubor = new Button("Naèti soubor");
		vymazSoubor = new Button("Vymaž soubor");
		vyberRoku = new ChoiceBox<>();
		nazevRokuLabel = new Label("Aktuálnì vybraný rok: ");
		horniPanel.setHgap(30);
		horniPanel.setVgap(10);
		
		nactiSoubor.setPrefWidth(90);
		nactiSoubor.setPrefHeight(30);
		vymazSoubor.setPrefWidth(100);
		vymazSoubor.setPrefHeight(30);
		vyberRoku.setPrefWidth(120);
		
		vyberRoku.valueProperty().addListener(new ChangeListener<String>() 
		{
	        @Override 
	        public void changed(ObservableValue ov, String t, String t1) 
	        {
	        	try
	        	{
		        	if (!vyberRoku.getSelectionModel().getSelectedItem().toString().contains("Vytvoøte"))
		        	{
			        	otevriSouborVProjektu(vyberRoku.getSelectionModel().getSelectedItem().toString());
		        	}
		        	else
		        	{
		        		ObservableList<Mesto> mesta = FXCollections.observableArrayList(tabulka.getItems());
		        		tabulka.getItems().removeAll(mesta);
		        	}
	        	}
	        	catch (Exception e)
	        	{
	        		
	        	}
	        }    
	    });
		
		nactiSoubor.setOnAction(event -> otevriSouborMimoProjekt());
		vymazSoubor.setOnAction(event -> vymazSoubor(vyberRoku
				.getSelectionModel().getSelectedItem()));
		
		horniPanel.add(vyberRoku, 0, 0);
		horniPanel.add(nazevRokuLabel, 0, 1);
		horniPanel.add(nactiSoubor, 2, 0);
		horniPanel.add(vymazSoubor, 3, 0);
		
		horniPanel.setPadding(new Insets(10));
		
		return horniPanel;
	}
	
	@SuppressWarnings("unchecked")
	private TableView<Mesto> vytvorTabulku() 
	{
		editableFactoryMesto = new Callback<TableColumn<Mesto,String>, TableCell<Mesto,String>>() 
	    {
	        @Override
	        public TableCell call(TableColumn p) 
	        {
	            return new BunkaMesto();
	        }
	    };
	    
		editableFactoryMesic = new Callback<TableColumn<Mesto,String>, TableCell<Mesto,String>>() 
	    {
	        @Override
	        public TableCell call(TableColumn p) 
	        {
	            return new BunkaMesice();
	        }
	    };
	    
		tabulka = new TableView<Mesto>();		
		
		TableColumn<Mesto, String> mesto = new TableColumn<Mesto, String>("Mìsto");
		TableColumn<Mesto, String> leden = new TableColumn<Mesto, String>("Leden");
		TableColumn<Mesto, String> unor = new TableColumn<Mesto, String>("Únor");
		TableColumn<Mesto, String> brezen = new TableColumn<Mesto, String>("Bøezen");
		TableColumn<Mesto, String> duben = new TableColumn<Mesto, String>("Duben");
		TableColumn<Mesto, String> kveten = new TableColumn<Mesto, String>("Kvìten");
		TableColumn<Mesto, String> cerven = new TableColumn<Mesto, String>("Èerven");
		TableColumn<Mesto, String> cervenec = new TableColumn<Mesto, String>("Èervenec");
		TableColumn<Mesto, String> srpen = new TableColumn<Mesto, String>("Srpen");
		TableColumn<Mesto, String> zari = new TableColumn<Mesto, String>("Záøí");
		TableColumn<Mesto, String> rijen = new TableColumn<Mesto, String>("Øíjen");
		TableColumn<Mesto, String> listopad = new TableColumn<Mesto, String>("Listopad");
		TableColumn<Mesto, String> prosinec = new TableColumn<Mesto, String>("Prosinec");
		
		mesto.setCellValueFactory(new PropertyValueFactory<Mesto, String>("mesto"));
		mesto.setMinWidth(100);
		mesto.setCellFactory(editableFactoryMesto);  
		mesto.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setMesto(t.getNewValue())
                );
		
		leden.setCellValueFactory(new PropertyValueFactory<Mesto, String>("leden"));
		leden.setMinWidth(50);
		leden.setCellFactory(editableFactoryMesic);
		leden.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setLeden(t.getNewValue())
                );
		
		unor.setCellValueFactory(new PropertyValueFactory<Mesto, String>("unor"));
		unor.setMinWidth(50);
		unor.setCellFactory(editableFactoryMesic);
		unor.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setUnor(t.getNewValue())
                );
		
		brezen.setCellValueFactory(new PropertyValueFactory<Mesto, String>("brezen"));
		brezen.setMinWidth(50);
		brezen.setCellFactory(editableFactoryMesic);
		brezen.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setBrezen(t.getNewValue())
                );
		
		duben.setCellValueFactory(new PropertyValueFactory<Mesto, String>("duben"));
		duben.setMinWidth(50);
		duben.setCellFactory(editableFactoryMesic);
		duben.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setDuben(t.getNewValue())
                );
		
		kveten.setCellValueFactory(new PropertyValueFactory<Mesto, String>("kveten"));
		kveten.setMinWidth(50);
		kveten.setCellFactory(editableFactoryMesic);
		kveten.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setKveten(t.getNewValue())
                );
		
		cerven.setCellValueFactory(new PropertyValueFactory<Mesto, String>("cerven"));
		cerven.setMinWidth(50);
		cerven.setCellFactory(editableFactoryMesic);
		cerven.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setCerven(t.getNewValue())
                );
		
		cervenec.setCellValueFactory(new PropertyValueFactory<Mesto, String>("cervenec"));
		cervenec.setMinWidth(50);
		cervenec.setCellFactory(editableFactoryMesic);
		cervenec.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setCervenec(t.getNewValue())
                );
		
		srpen.setCellValueFactory(new PropertyValueFactory<Mesto, String>("srpen"));
		srpen.setMinWidth(50);
		srpen.setCellFactory(editableFactoryMesic);
		srpen.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setSrpen(t.getNewValue())
                );
		
		zari.setCellValueFactory(new PropertyValueFactory<Mesto, String>("zari"));
		zari.setMinWidth(50);
		zari.setCellFactory(editableFactoryMesic);
		zari.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setZari(t.getNewValue())
                );
		
		rijen.setCellValueFactory(new PropertyValueFactory<Mesto, String>("rijen"));
		rijen.setMinWidth(50);
		rijen.setCellFactory(editableFactoryMesic);
		rijen.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setRijen(t.getNewValue())
                );
		
		listopad.setCellValueFactory(new PropertyValueFactory<Mesto, String>("listopad"));
		listopad.setMinWidth(50);
		listopad.setCellFactory(editableFactoryMesic);
		listopad.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setListopad(t.getNewValue())
                );
		
		prosinec.setCellValueFactory(new PropertyValueFactory<Mesto, String>("prosinec"));
		prosinec.setMinWidth(50);
		prosinec.setCellFactory(editableFactoryMesic);
		prosinec.setOnEditCommit(
                (TableColumn.CellEditEvent<Mesto, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setProsinec(t.getNewValue())
                );
		
		tabulka.getColumns().addAll(mesto, leden, unor, brezen, duben, kveten, cerven
				, cervenec, srpen, zari, rijen, listopad, prosinec);
		tabulka.setEditable(true);
		tabulka.setOnKeyReleased(event -> vymazOznaceny(event));
		tabulka.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tabulka.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		BorderPane.setMargin(tabulka, new Insets(10));
		
		return tabulka;
	}
	
	public GridPane vytvorDolni()
	{
		GridPane dolniPanel = new GridPane();
		zobrazGraf = new Button("Grafy");
		ulozSoubor = new Button("Ulož");
		vymazUdaje = new Button("Vymaž list");
		novyRadek = new Button("Nový øádek");
		vymazOznaceneUdaje = new Button("Vymaž oznaèené");
		
		dolniPanel.setHgap(30);
		dolniPanel.setVgap(10);
		
		zobrazGraf.setPrefWidth(80);
		zobrazGraf.setPrefHeight(30);
		ulozSoubor.setPrefWidth(80);
		ulozSoubor.setPrefHeight(30);
		vymazUdaje.setPrefWidth(80);
		vymazUdaje.setPrefHeight(30);
		novyRadek.setPrefWidth(100);
		novyRadek.setPrefHeight(30);
		vymazOznaceneUdaje.setPrefWidth(110);
		vymazOznaceneUdaje.setPrefHeight(30);
		
		zobrazGraf.setOnAction(event -> 
		{
			if (grafOkno.isShowing() == false)
			{
				grafOkno = new GrafOkno();
				grafOkno.setHlavniOkno(this);
				ObservableList<String> roky = FXCollections.observableArrayList(vyberRoku.getItems());
				roky.remove(0);
				grafOkno.vyberRok.setItems(roky);
				grafOkno.vyberRok.getSelectionModel().selectFirst();
				grafOkno.vyberRok.getSelectionModel()
			    .selectedItemProperty()
			    .addListener( (ObservableValue<? extends String> observable, 
			    		String oldValue, String newValue) -> grafOkno.zmenMesta() );
				grafOkno.mesta = grafOkno.zjistiMesta();
				grafOkno.vykresliGraf();
				grafOkno.show();
			}
			else 
			{
				grafOkno.requestFocus();
			}
		});
		ulozSoubor.setOnAction(event -> ulozDoSouboru(vyberRoku.
				getSelectionModel().getSelectedItem()));
		vymazUdaje.setOnAction(event -> vymazList());
		vymazOznaceneUdaje.setOnAction(event ->
		{
			ObservableList<Mesto> oznacene = FXCollections.observableArrayList(tabulka.getSelectionModel().getSelectedItems());
			if (oznacene.size() == 0) 
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Musite vybrat nejaka data pro smazani");
				alert.show();	
			} 
			else 
			{			
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Opravdu chcete vymazat tyto hodnoty ?");
				ListView<String> hodnotySmazani = new ListView<>();
				for (int i = 0; i < oznacene.size(); i++)
				{
					hodnotySmazani.getItems().add(oznacene.get(i).getMesto());
				}
				alert.setGraphic(hodnotySmazani);
				alert.showAndWait()
					.filter(potvrzeni -> potvrzeni == ButtonType.OK)
					.ifPresent(potvrzeni -> 
					{
						 tabulka.getItems().removeAll(oznacene);
						 tabulka.getSelectionModel().clearSelection();
					});
			}
		});
		
		novyRadek.setOnAction(event -> 
		tabulka.getItems().add(new Mesto("Mesto", "0", "0", "0", "0", "0", "0", "0"
				, "0", "0", "0", "0", "0")));
		
		dolniPanel.add(zobrazGraf, 0, 0);
		dolniPanel.add(novyRadek, 8, 0);
		dolniPanel.add(ulozSoubor, 11, 0);
		dolniPanel.add(vymazOznaceneUdaje, 13, 0);
		dolniPanel.add(vymazUdaje, 14, 0);
		
		dolniPanel.setPadding(new Insets(10));
		
		return dolniPanel;
	}
	
	/**
	 * 
	 * 
	 * Zacatek metod pro funkcionalitu
	 * 
	 * 
	 */
	
	
	private void vymazOznaceny(KeyEvent event) 
	{
		ObservableList<Mesto> observable = FXCollections.observableArrayList(tabulka.getSelectionModel().getSelectedItems());
		
		if (event.getCode() == KeyCode.DELETE) 
		{			
			if (observable.size() == 0) 
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Musite vybrat nejaka data pro smazani");
				alert.show();	
			} 
			else 
			{			
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Opravdu chcete vymazat tyto hodnoty ?");
				ListView<String> hodnotySmazani = new ListView<>();
				for (int i = 0; i < observable.size(); i++)
				{
					hodnotySmazani.getItems().add(observable.get(i).getMesto());
				}
				alert.setGraphic(hodnotySmazani);
				alert.showAndWait()
					.filter(potvrzeni -> potvrzeni == ButtonType.OK)
					.ifPresent(potvrzeni -> 
					{
						 tabulka.getItems().removeAll(observable);
						 tabulka.getSelectionModel().clearSelection();
					});
			}
		}
	}
	
	public void otevriSouborVProjektu(String r)
	{
		try
		{
			nazevRokuLabel.setText("Aktuálnì vybraný rok: " + r);
			ObservableList<Mesto> observable = FXCollections.observableArrayList(tabulka.getItems());
			tabulka.getItems().removeAll(observable);
			String rok = r;	
        	BufferedReader read = new BufferedReader(new FileReader(rok + ".csv"));     
        	String radka;
        	String[] hodnoty;
        	while ((radka = read.readLine()) != null)
        	{
        		hodnoty = radka.split(";");
        		Mesto mesto = vytvorMesto(hodnoty);
        		tabulka.getItems().add(mesto);
        	}
        	read.close();
		}
		catch(Exception e)
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Tento soubor neexistuje!");
			alert.setContentText("Vyberte prosím platný soubor.");
			alert.show();
		}
	}
	
	public void otevriSouborMimoProjekt()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Vyber si soubor");
		File soubor = fileChooser.showOpenDialog(primaryStage);
		if (soubor != null)
		{
			ObservableList<Mesto> observable = FXCollections.observableArrayList(tabulka.getItems());
			tabulka.getItems().removeAll(observable);
			try
			{
				String radek;
				String[] hodnoty;
				BufferedReader cti = new BufferedReader(new FileReader(soubor.getAbsolutePath()));
				while ((radek = cti.readLine()) != null) 
				{
					hodnoty = radek.split(";");
					Mesto mesto = vytvorMesto(hodnoty);
					tabulka.getItems().add(mesto);
				}
				cti.close();
				vytvorSouborVProjektu(soubor.getName());
			}
			catch (Exception e)
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Tento soubor neexistuje!");
				alert.setContentText("Vyberte prosím platný soubor.");
				alert.show();
			}
		}
	}
	
	
	public void vytvorSouborVProjektu(String nazev)
	{
		try
		{
			Path fileToDeletePath = Paths.get(nazev);
			try
			{
				Files.delete(fileToDeletePath);
			}
			catch(Exception e)
			{
				
			}
			
			File soubor = new File(nazev);
			if (!soubor.exists()) 
			{
				soubor.createNewFile();
			}
			FileWriter fw = new FileWriter(soubor.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			ObservableList<Mesto> observable = FXCollections.observableArrayList(tabulka.getItems());
			for (int i = 0; i < observable.size(); i++)
			{
				bw.write(observable.get(i).getMesto());
				bw.write(";");
				bw.write(observable.get(i).getLeden());
				bw.write(";");
				bw.write(observable.get(i).getUnor());
				bw.write(";");
				bw.write(observable.get(i).getBrezen());
				bw.write(";");
				bw.write(observable.get(i).getDuben());
				bw.write(";");
				bw.write(observable.get(i).getKveten());
				bw.write(";");
				bw.write(observable.get(i).getCerven());
				bw.write(";");
				bw.write(observable.get(i).getCervenec());
				bw.write(";");
				bw.write(observable.get(i).getSrpen());
				bw.write(";");
				bw.write(observable.get(i).getZari());
				bw.write(";");
				bw.write(observable.get(i).getRijen());
				bw.write(";");
				bw.write(observable.get(i).getListopad());
				bw.write(";");
				bw.write(observable.get(i).getProsinec());
				bw.write("\n");
			}
			bw.close();
			ziskejCSV();
		}
		catch(Exception e)
		{

		}
	}
	
	public void vymazList()
	{
		ObservableList<Mesto> observable = FXCollections.observableArrayList(tabulka.getItems());
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Opravdu chcete vymazat všechny hodnoty z listu ?");
		alert.showAndWait()
			.filter(potvrzeni -> potvrzeni == ButtonType.OK)
			.ifPresent(potvrzeni -> 
			{
				 tabulka.getItems().removeAll(observable);
				 tabulka.getSelectionModel().clearSelection();
			});
	}
	
	public Mesto vytvorMesto(String[] hodnoty)
	{
		String nazevMesta = hodnoty[0];
		String leden = hodnoty[1];
		String unor = hodnoty[2];
		String brezen = hodnoty[3];
		String duben = hodnoty[4];
		String kveten = hodnoty[5];
		String cerven = hodnoty[6];
		String cervenec = hodnoty[7];
		String srpen = hodnoty[8];
		String zari = hodnoty[9];
		String rijen = hodnoty[10];
		String listopad = hodnoty[11];
		String prosinec = hodnoty[12];
		
		Mesto mesto = new Mesto(nazevMesta, leden, unor, brezen, duben, kveten,
				cerven, cervenec, srpen, zari, rijen, listopad, prosinec);
		return mesto;
	}
	
	public void ziskejCSV()
	{
		//ziskani vsech nazvu souboru s priponou csv
		File pathF = new File("");
		String path = pathF.getAbsolutePath();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		List<String> listOfCSV = new ArrayList<>();
		listOfCSV.add("Vytvoøte nový rok");
		
		for (File file : listOfFiles) 
		{
		    if (file.isFile()) 
		    {
		    	if (file.toString().contains(".csv"))  
		    	{
		    		String rok = file.toString();
		    		rok = rok.substring(rok.length()-8);
		    		rok = rok.substring(0, 4);
		    		listOfCSV.add(rok);
		    	}
		    }
		}
		ObservableList<String> observable = FXCollections.observableArrayList(vyberRoku.getItems());
		vyberRoku.getItems().removeAll(observable);
		vyberRoku.getItems().addAll(listOfCSV);
		vyberRoku.getSelectionModel().selectFirst();
	}
	
	public void vymazSoubor(String nazev)
	{
		if (nazev != null)
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Opravdu chcete smazat soubor " + nazev + ".csv ?");
			alert.showAndWait()
			.filter(potvrzeni -> potvrzeni == ButtonType.OK)
			.ifPresent(potvrzeni -> 
			{
				 tabulka.getSelectionModel().clearSelection();
				 try
				 {
					 Path fileToDeletePath = Paths.get(nazev + ".csv");
					 Files.delete(fileToDeletePath);
					 ziskejCSV();
					 
					 ObservableList<Mesto> observable = FXCollections.observableArrayList(tabulka.getItems());
					 tabulka.getItems().removeAll(observable);
					 tabulka.getSelectionModel().clearSelection();
				 }
				 catch(Exception e)
				 {
						Alert alert2 = new Alert(AlertType.WARNING);
						alert2.setHeaderText("Soubor se nepodaøilo smazat.");
						alert2.show();
				 }
			});
		}
		else 
		{
			
		}
	}
	
	public void ulozDoSouboru(String nazev)
	{
		if (nazev != null && nazev != "" && nazev.contains("Vytvoøte") == false)
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Opravdu chcete uložit hodnoty do souboru " + nazev + ".csv ?");
			alert.showAndWait()
			.filter(potvrzeni -> potvrzeni == ButtonType.OK)
			.ifPresent(potvrzeni -> 
			{
				 tabulka.getSelectionModel().clearSelection();
				 try
				 {
					 vytvorSouborVProjektu(nazev + ".csv");
					 vyberRoku.getSelectionModel().select(nazev);
				 }
				 catch(Exception e)
				 {
						Alert alert2 = new Alert(AlertType.WARNING);
						alert2.setHeaderText("Soubor se nepodaøilo uložit.");
						alert2.show();
				 }
			});
		}
		else 
		{
			if (souborOkno.isShowing() == false)
			{
				souborOkno = new NovySouborOkno();
				souborOkno.setHlavniOkno(this);
				souborOkno.show();
			}
			else
			{
				souborOkno.requestFocus();
			}
		}
	}
}

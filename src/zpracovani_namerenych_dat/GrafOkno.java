package zpracovani_namerenych_dat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GrafOkno extends Stage
{
	HlavniOkno okno = new HlavniOkno();
	
	//graf
	FlowPane graphPane;
	TextArea popis; 
	
	//horni cast
	ChoiceBox<String> typGrafu;
	ChoiceBox<String> vyberRok;
	ChoiceBox<String> vyber1;
	ChoiceBox<String> vyber2;
	Button potvrd;
	Label labelRok;
	Label labelMesic_Mesto;
	String typGraf = "";
	List<String> nazevMesicu;
	
	ObservableList<Mesto> mesta;
	
	//dolni cast
	Button ukonceni;
	
	public GrafOkno()
	{
		this.setWidth(800);
		this.setHeight(620);
		this.setTitle("Zpracov�n� nam��en�ch teplot ve m�stech - grafy");
		this.setScene(vytvorScene());
		this.setResizable(false);
		nazevMesicu = new ArrayList<>();
		nazevMesicu.add("Leden");
		nazevMesicu.add("�nor");
		nazevMesicu.add("B�ezen");
		nazevMesicu.add("Duben");
		nazevMesicu.add("Kv�ten");
		nazevMesicu.add("�erven");
		nazevMesicu.add("�ervenec");
		nazevMesicu.add("Srpen");
		nazevMesicu.add("Z���");
		nazevMesicu.add("��jen");
		nazevMesicu.add("Listopad");
		nazevMesicu.add("Prosinec");
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
		root.setPadding(new Insets(10));
		root.setStyle("-fx-background-color: #" + color);
		root.setTop(vytvorHorni());
		root.setCenter(vytvorPanelGraf());
		root.setBottom(vytvorSpodni());
		return root;
	}
	
	public GridPane vytvorSpodni() 
	{
		GridPane dolniPanel = new GridPane();
		ukonceni = new Button("Zp�t");
		ukonceni.setMinWidth(80);
		
		ukonceni.setOnAction(event -> this.close());
		
		dolniPanel.add(ukonceni, 1, 1);
		
		dolniPanel.setPadding(new Insets(10));
		dolniPanel.setAlignment(Pos.CENTER_LEFT);
		return dolniPanel;
	}

	public void setHlavniOkno(HlavniOkno okno)
	{
		this.okno = okno;
	}
	
	public FlowPane vytvorPanelGraf() 
	{
		graphPane = new FlowPane();
		graphPane.setMinWidth(400);
		graphPane.setMinHeight(450);	
		graphPane.setStyle("-fx-background-color: #FFFFFF;");
		popis = new TextArea();
		graphPane.setPadding(new Insets(10));
		popis.setMaxWidth(250);
		popis.setPrefHeight(300);
		popis.setEditable(false);
		return graphPane;
	}
	
	public GridPane vytvorHorni()
	{
		GridPane horniPanel = new GridPane();
		horniPanel.setPadding(new Insets(10));
		horniPanel.setHgap(6);
		horniPanel.setVgap(6);
		
		typGrafu = new ChoiceBox<>();
		vyberRok = new ChoiceBox<>();
		vyber1 = new ChoiceBox<>();
		vyber2 = new ChoiceBox<>();
		potvrd = new Button("Potvr�");
		labelRok = new Label("Vyberte rok: ");
		labelMesic_Mesto = new Label("Vyberte m�s�c: ");
		
		vyberRok.setMinWidth(100);
		vyber1.setMinWidth(100);
		vyber2.setMinWidth(100);
		typGrafu.setMinWidth(295);
		potvrd.setMinWidth(80);
		
		vyber1.setDisable(true);
		vyber2.setDisable(true);
		labelMesic_Mesto.setText("");
		
		typGrafu.getItems().addAll("1. Pr�m�rn� teplota ve v�ech m�stech po m�s�ci."
				,"2. Porovn�n� 2 m�st po m�s�c�ch."
				,"3. Teplota ve m�stech za m�s�c");
		typGrafu.getSelectionModel().selectFirst();
		typGrafu.getSelectionModel()
	    .selectedItemProperty()
	    .addListener( (ObservableValue<? extends String> observable, 
	    		String oldValue, String newValue) -> zmenLabel(newValue) );
		
		typGrafu.valueProperty().addListener(new ChangeListener<String>() 
		{
	        @Override 
	        public void changed(ObservableValue ov, String t, String t1) 
	        {
	        	if (typGrafu.getSelectionModel().getSelectedItem().contains("1"))
	        	{
	        		vyber1.setDisable(true);
	        		vyber2.setDisable(true);
	        		labelMesic_Mesto.setText("");
	        	}
	        	else if (typGrafu.getSelectionModel().getSelectedItem().contains("2"))
	        	{
	        		vyber1.setDisable(false);
	        		vyber2.setDisable(false);
	        		zmenLabel(typGrafu.getSelectionModel().getSelectedItem());
	        	}
	        	else
	        	{
	        		vyber1.setDisable(false);
	        		vyber2.setDisable(true);
	        		zmenLabel(typGrafu.getSelectionModel().getSelectedItem());
	        	}
	        }    
	    });
		
		vyber1.valueProperty().addListener(new ChangeListener<String>() 
		{
	        @Override 
	        public void changed(ObservableValue ov, String t, String t1) 
	        {
	        	try
	        	{
		        	if (vyber1.getSelectionModel().getSelectedItem().contains(
		        			vyber2.getSelectionModel().getSelectedItem()))
		        	{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setHeaderText("Vybrali jste 2x stejn� m�sto");
						alert.setContentText("Vyberte si pros�m jin� m�sto");
						alert.show();
			        	vyber1.getSelectionModel().clearSelection();
		        	}
	        	}
	        	catch (Exception e)
	        	{
	        		
	        	}
	        }    
	    });
		
		vyber2.valueProperty().addListener(new ChangeListener<String>() 
		{
	        @Override 
	        public void changed(ObservableValue ov, String t, String t1) 
	        {
	        	try
	        	{
		        	if (vyber2.getSelectionModel().getSelectedItem().contains(
		        			vyber1.getSelectionModel().getSelectedItem()))
		        	{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setHeaderText("Vybrali jste 2x stejn� m�sto");
						alert.setContentText("Vyberte si pros�m jin� m�sto");
						alert.show();
			        	vyber2.getSelectionModel().clearSelection();
		        	}
	        	}
	        	catch (Exception e)
	        	{
	        		
	        	}
	        }    
	    });
		
		
		potvrd.setOnAction(event -> vykresliGraf());
		
		horniPanel.add(typGrafu, 3, 1, 3, 1);
		horniPanel.add(potvrd, 7, 1);
		horniPanel.add(labelRok, 1, 3);
		horniPanel.add(vyberRok, 2, 3);
		horniPanel.add(labelMesic_Mesto, 3, 3);
		horniPanel.add(vyber1, 4, 3);
		horniPanel.add(vyber2, 5, 3);
		
		horniPanel.setAlignment(Pos.CENTER);
		return horniPanel;
	}
	
	
	
	
	
	
	
	public void vykresliGraf() 
	{
		typGraf = typGrafu.getSelectionModel().getSelectedItem();
		String typ = typGraf.substring(0, 1);
		if ((vyberRok.getSelectionModel().getSelectedItem() == null) == false)
		{	
			if (typ.contains("1") && vyberRok.getSelectionModel().getSelectedItem().contains("rok") == false)
			{
				if (graphPane.getChildren().size() > 0) graphPane.getChildren().remove(0);
				graphPane.getChildren().add(0, vykresli());
			}
			else if (typ.contains("2") && vyberRok.getSelectionModel().getSelectedItem().contains("rok") == false)
			{
				if ((vyber1.getSelectionModel().getSelectedItem() == null) == false && (vyber2.getSelectionModel().getSelectedItem() == null) == false)
				{
					if (graphPane.getChildren().size() > 0) graphPane.getChildren().remove(0);
					graphPane.getChildren().add(0, vykresli());
				}
				else
				{
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Nejsou vybr�na 2 m�sta.");
					alert.setContentText("Vyberte si pros�m dv� m�sta.");
					alert.show();
				}
			}
			else if (typ.contains("3") && vyberRok.getSelectionModel().getSelectedItem().contains("rok") == false)
			{
				if ((vyber1.getSelectionModel().getSelectedItem() == null) == false)
				{
					if (graphPane.getChildren().size() > 0) graphPane.getChildren().remove(0);
					graphPane.getChildren().add(0, vykresli());
				}
				else
				{
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Nen� vybr�n ��dn� m�s�c.");
					alert.setContentText("Vyberte si pros�m n�jak� m�s�c.");
					alert.show();
				}
			}
			else 
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Nevybral jste si ��dn� graf nebo rok.");
				alert.setContentText("Vyberte si pros�m n�jak� graf nebo rok.");
				alert.show();
			}
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Nevybral jste si ��dn� graf nebo nen� zad�n rok.");
			alert.setContentText("Vyberte si pros�m n�jak� graf nebo zadejte rok.");
			alert.show();
		}
	}
	
	public BorderPane vykresli()
	{
		BorderPane panel = new BorderPane();
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();	
		
		if (typGraf.contains("1"))
		{
	        xAxis.setLabel("M�s�c");
	        yAxis.setLabel("Teplota[�C]"); 
	        
			BarChart<String, Double> detailChart = new BarChart(xAxis, yAxis);
			XYChart.Series<String, Double> serie = new XYChart.Series<String, Double>();
			List<String> mesice = mesta.get(0).getMesice();
			for (int i = 0; i < mesice.size(); i++)
			{
				double prumHodnota = 0;
				for (int j = 0; j < mesta.size(); j++)
				{
					prumHodnota += Integer.parseInt(mesta.get(j).getMesice().get(i));
				}
				prumHodnota /= mesta.size();
				serie.getData().add(new XYChart.Data<String, Double>(nazevMesicu.get(i), prumHodnota));
			}
			
			detailChart.setTitle("Pr�m�rn� teploty ve v�ech m�stech za rok "  
					+ vyberRok.getSelectionModel().getSelectedItem());
			detailChart.setLegendVisible(false);
			detailChart.getData().addAll(serie);
			
			popis.setText("Naje�te na sloupe�ek a zobraz� se �daje.");
			for (final XYChart.Series<String, Double> graphSerie : detailChart.getData()) 
		    {
				for (final XYChart.Data<String, Double> graphData : graphSerie.getData()) 
				{
					graphData.getNode().setOnMouseEntered(event -> 
					{
						popis.setText("");
						popis.appendText("M�s�c: " + graphData.getXValue());
						popis.appendText("\nPr�m�rn� teplota: " + graphData.getYValue() + "�C");
					});				
					graphData.getNode().setOnMouseExited(event -> 
					{
						popis.setText("Naje�te na ��ru a zobraz� se �daje.");
					});
				}
			}	
			
	        panel.setCenter(detailChart);
	        panel.setRight(popis);
		}
		else if (typGraf.contains("2"))
		{
	        xAxis.setLabel("M�s�c");
	        yAxis.setLabel("Teplota[�C]"); 
	        
			BarChart<String, Integer> detailChart = new BarChart(xAxis, yAxis);
			XYChart.Series<String, Integer> serie = new XYChart.Series<String, Integer>();
			XYChart.Series<String, Integer> serie2 = new XYChart.Series<String, Integer>();
			
			
			int mestoIndex1 = vyber1.getSelectionModel().getSelectedIndex();
			int mestoIndex2 = vyber2.getSelectionModel().getSelectedIndex();
			String mesto1 = mesta.get(mestoIndex1).getMesto();
			String mesto2 = mesta.get(mestoIndex2).getMesto();
			
			serie.setName(mesto1);
			serie2.setName(mesto2);
			
			List<String> mesice = mesta.get(0).getMesice();
			for (int i = 0; i < mesice.size(); i++)
			{
				serie.getData().add(new XYChart.Data<String, Integer>(nazevMesicu.get(i), Integer.parseInt(mesta.get(mestoIndex1).getMesice().get(i))));
				serie2.getData().add(new XYChart.Data<String, Integer>(nazevMesicu.get(i), Integer.parseInt(mesta.get(mestoIndex2).getMesice().get(i))));
				
			}
			
			detailChart.setTitle("Porovn�n� teplot v " + mesto1 + " a " + mesto2 + " v roce " 
					+ vyberRok.getSelectionModel().getSelectedItem());
			
			detailChart.getData().addAll(serie, serie2);
			
			popis.setText("Naje�te na ��ru a zobraz� se �daje.");
			for (final XYChart.Series<String, Integer> graphSerie : detailChart.getData()) 
		    {
				for (final XYChart.Data<String, Integer> graphData : graphSerie.getData()) 
				{
					graphData.getNode().setOnMouseEntered(event -> 
					{
						popis.setText("");
						popis.appendText("M�s�c: " + graphData.getXValue());
						popis.appendText("\nM�sto: " + graphSerie.getName());
						popis.appendText("\nTeplota: " + graphData.getYValue() + "�C");
					});				
					graphData.getNode().setOnMouseExited(event -> 
					{
						popis.setText("Naje�te na ��ru a zobraz� se �daje.");
					});
				}
			}	
			
	        panel.setCenter(detailChart);
	        panel.setRight(popis);
		}
		else if (typGraf.contains("3"))
		{
	        xAxis.setLabel("M�sto");
	        yAxis.setLabel("Teplota[�C]"); 
	        
			BarChart<String, Integer> detailChart = new BarChart(xAxis, yAxis);
			XYChart.Series<String, Integer> serie = new XYChart.Series<String, Integer>();
			
			
			int indexMesice = vyber1.getSelectionModel().getSelectedIndex();
			
			for (int i = 0; i < mesta.size(); i++)	
			{
				String hodnota = mesta.get(i).getMesice().get(indexMesice);
				serie.getData().add(new XYChart.Data<String, Integer>(mesta.get(i).getMesto(), Integer.parseInt(hodnota)));
			}
			detailChart.setTitle("Porovn�n� teplot ve v�ech m�stech za m�s�c " 
								+ vyber1.getSelectionModel().getSelectedItem() + " v roce " 
								+ vyberRok.getSelectionModel().getSelectedItem());
			detailChart.getData().addAll(serie);
			detailChart.setLegendVisible(false);
			
			popis.setText("Naje�te na ��ru a zobraz� se �daje.");
			for (final XYChart.Series<String, Integer> graphSerie : detailChart.getData()) 
		    {
				for (final XYChart.Data<String, Integer> graphData : graphSerie.getData()) 
				{
					graphData.getNode().setOnMouseEntered(event -> 
					{
						popis.setText("");
						popis.appendText("M�sto: " + graphData.getXValue());
						popis.appendText("\nTeplota: " + graphData.getYValue() + "�C");
					});				
					graphData.getNode().setOnMouseExited(event -> 
					{
						popis.setText("Naje�te na ��ru a zobraz� se �daje.");
					});
				}
			}
			
	        panel.setCenter(detailChart);
	        panel.setRight(popis);
		}
       
        return panel;
	}
	
	
	
	
	
	
	
	
	
	
	public void zmenMesta()
	{
		String rok = vyberRok.getSelectionModel().getSelectedItem();
		mesta = FXCollections.observableArrayList();
		mesta = zjistiMesta();
		if (rok != null && rok != "" && typGraf.contains("2"))
		{
			naplnMestama();
		}
	}
	
	public void naplnMesicema()
	{
		ObservableList<String> mesta = FXCollections.observableArrayList(vyber1.getItems());
		vyber1.getItems().removeAll(mesta);
		vyber2.getItems().removeAll(mesta);
		vyber1.getItems().addAll(nazevMesicu);
		vyber2.hide();
	}
	
	public void naplnMestama()
	{
		ObservableList<String> mesice = FXCollections.observableArrayList(vyber1.getItems());
		ObservableList<String> mesice2 = FXCollections.observableArrayList(vyber1.getItems());
		vyber1.getItems().removeAll(mesice);
		vyber2.getItems().removeAll(mesice);
		
		ObservableList<Mesto> observable = zjistiMesta();
		List<String> mesta = new ArrayList<>();
		for (int i = 0; i < observable.size(); i++)
		{
			mesta.add(observable.get(i).getMesto());
		}
		vyber1.getItems().addAll(mesta);
		vyber2.getItems().addAll(mesta);
	}
	
	public ObservableList<Mesto> zjistiMesta()
	{
		ObservableList<Mesto> observable = FXCollections.observableArrayList();
		try
		{
			String rok = vyberRok.getSelectionModel().getSelectedItem();	
        	BufferedReader read = new BufferedReader(new FileReader(rok + ".csv"));     
        	String radka;
        	String[] hodnoty;
        	while ((radka = read.readLine()) != null)
        	{
        		hodnoty = radka.split(";");
        		Mesto mesto = okno.vytvorMesto(hodnoty);
        		observable.add(mesto);
        	}
        	read.close();
		}
		catch(Exception e)
		{

		}
    	return observable;
	}
	
	public void zmenLabel(String typ)
	{
		typGraf = typ;
		String typp = typGraf.substring(0, 1);
		if (typp.contains("1"))
		{
			labelMesic_Mesto.setText("");
			vyber1.getItems().removeAll(vyber1.getItems());
			vyber2.getItems().removeAll(vyber2.getItems());
		}
		else if (typp.contains("2"))
		{
			labelMesic_Mesto.setText("Vyberte m�sta: ");
			naplnMestama();
		}
		else 
		{
			labelMesic_Mesto.setText("Vyberte m�s�c: ");
			naplnMesicema();
		}
	}
}

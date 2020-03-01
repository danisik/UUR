package zpracovani_namerenych_dat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BunkaMesto extends TableCell<Mesto, String> 
{
	private TextField textField;

	public BunkaMesto() 
	{
	}

	@Override
	public void startEdit() 
	{
		super.startEdit();

		if( textField == null ) 
		{
			createTextField();
		}
		setText(null);
		setGraphic(textField);
		textField.selectAll();
	}

	@Override
	public void cancelEdit()
	{
		super.cancelEdit();
		setText((String) getItem());
		setGraphic(null);
	}

	@Override
	public void updateItem(String item, boolean empty) 
	{
		super.updateItem(item, empty);
		if (empty) 
		{
			setText(null);
			setGraphic(null);
		} 
		else 
		{
			if (isEditing()) 
			{
				if (textField != null) 
				{
					textField.setText(getString());
				}
			setText(null);
            setGraphic(textField);
			} 
			else 
			{
				setText(getString());
            	setGraphic(null);
			}
		}
	}

	private void createTextField() 
	{
		textField = new TextField(getString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) 
			{
				if (!arg2) 
				{ 
					commitEdit(textField.getText()); 
				}
			}
		});

		textField.setOnKeyReleased(new EventHandler<KeyEvent>() 
		{
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) 
				{
					String value = textField.getText();
					if (value != null) 
					{ 
						if (value.matches("^([a-zA-Z]+)"))
						{
							commitEdit(value); 
						}
						else
						{
							commitEdit("Mesto");
							Alert alert = new Alert(AlertType.WARNING);
							alert.setHeaderText("Špatný vstup!");
							alert.setContentText("Zadávejte pouze písmena!");
							alert.show();	
							textField.setText("");
						}
					} 
					else 
					{ 
						commitEdit("Mesto");
					}
				} 
				else if (t.getCode() == KeyCode.ESCAPE) 
				{
                cancelEdit();
				}
			}
		});
	}

	private String getString() 
	{
		return getItem() == null ? "" : getItem().toString();
	}
}
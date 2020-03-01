package zpracovani_namerenych_dat;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class Mesto 
{
	private SimpleStringProperty mesto;
	private SimpleStringProperty leden;
	private SimpleStringProperty unor;
	private SimpleStringProperty brezen;
	private SimpleStringProperty duben;
	private SimpleStringProperty kveten;
	private SimpleStringProperty cerven;
	private SimpleStringProperty cervenec;
	private SimpleStringProperty srpen;
	private SimpleStringProperty zari;
	private SimpleStringProperty rijen;
	private SimpleStringProperty listopad;
	private SimpleStringProperty prosinec;
	
	public Mesto(String mesto, String leden, String unor, String brezen, String duben
			, String kveten, String cerven, String cervenec, String srpen, String zari
			, String rijen, String listopad, String prosinec)
	{
		this.mesto = new SimpleStringProperty(mesto);
		this.leden = new SimpleStringProperty(leden);
		this.unor = new SimpleStringProperty(unor);
		this.brezen = new SimpleStringProperty(brezen);
		this.duben = new SimpleStringProperty(duben);
		this.kveten = new SimpleStringProperty(kveten);
		this.cerven = new SimpleStringProperty(cerven);
		this.cervenec = new SimpleStringProperty(cervenec);
		this.srpen = new SimpleStringProperty(srpen);
		this.zari = new SimpleStringProperty(zari);
		this.rijen = new SimpleStringProperty(rijen);
		this.listopad = new SimpleStringProperty(listopad);
		this.prosinec = new SimpleStringProperty(prosinec);
	}
	
	//get metody
	//-----------
	public String getMesto()
	{
		return mesto.get();
	}
	public String getLeden()
	{
		return leden.get();
	}
	public String getUnor()
	{
		return unor.get();
	}
	public String getBrezen()
	{
		return brezen.get();
	}
	public String getDuben()
	{
		return duben.get();
	}
	public String getKveten()
	{
		return kveten.get();
	}
	public String getCerven()
	{
		return cerven.get();
	}
	public String getCervenec()
	{
		return cervenec.get();
	}
	public String getSrpen()
	{
		return srpen.get();
	}
	public String getZari()
	{
		return zari.get();
	}
	public String getRijen()
	{
		return rijen.get();
	}
	public String getListopad()
	{
		return listopad.get();
	}
	public String getProsinec()
	{
		return prosinec.get();
	}
	
	public List<String> getMesice()
	{
		List<String> mesice = new ArrayList<>();
		mesice.add(getLeden());
		mesice.add(getUnor());
		mesice.add(getBrezen());
		mesice.add(getDuben());
		mesice.add(getKveten());
		mesice.add(getCerven());
		mesice.add(getCervenec());
		mesice.add(getSrpen());
		mesice.add(getZari());
		mesice.add(getRijen());
		mesice.add(getListopad());
		mesice.add(getProsinec());
		
		return mesice;
	}
	//konec get
	//---------
	
	//set metody
	//-----------
	
	public void setMesto(String mest)
	{
		mesto.set(mest);
	}
	public void setLeden(String led)
	{
		leden.set(led);
	}
	public void setUnor(String uno)
	{
		unor.set(uno);
	}
	public void setBrezen(String brez)
	{
		brezen.set(brez);
	}
	public void setDuben(String dub)
	{
		duben.set(dub);
	}
	public void setKveten(String kvet)
	{
		kveten.set(kvet);
	}
	public void setCerven(String cerv)
	{
		cerven.set(cerv);
	}
	public void setCervenec(String cervene)
	{
		cervenec.set(cervene);
	}
	public void setSrpen(String srp)
	{
		srpen.set(srp);
	}
	public void setZari(String zar)
	{
		zari.set(zar);
	}
	public void setRijen(String rij)
	{
		rijen.set(rij);
	}
	public void setListopad(String list)
	{
		listopad.set(list);
	}
	public void setProsinec(String pros)
	{
		prosinec.set(pros);
	}
	//konec set
	//---------
}

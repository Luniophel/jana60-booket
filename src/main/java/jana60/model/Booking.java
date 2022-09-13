package jana60.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "booking")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer numberBooket;
	
	@ManyToOne
	@JoinColumn (name = "event_booket")
	private Events eventBooket;	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumberBooket() {
		return numberBooket;
	}

	public void setNumberBooket(Integer numberBooket) {
		this.numberBooket = numberBooket;
	}

	public Events getEventBooket() {
		return eventBooket;
	}

	public void setEventBooket(Events eventBooket) {
		this.eventBooket = eventBooket;
	}
}

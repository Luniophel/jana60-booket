package jana60.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "location")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "Location must have a name")
	private String name;
	
	@NotEmpty(message = "Location must have an address")
	private String address;
	
	@Lob
	private String description;
	
	@NotNull(message = "Location must have a capacity")
	@Min(value = 1)
	private Integer capacity;
	
	@OneToMany
	@JoinColumn(name = "location_id")
	public List<Events> event;
			
	@Column(nullable = true)
	public Integer booketAvailable;
		
	//getter and setter
	
	//id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	//name
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//address
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	//description
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//capacity
	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public List<Events> getEvent() {
		return event;
	}

	public void setEvent(List<Events> event) {
		this.event = event;
	}

	public Integer getBooketAvailable() {
		return booketAvailable;
	}

	public void setBooketAvailable(Integer booketAvailable) {
		this.booketAvailable = booketAvailable;
	}	
}

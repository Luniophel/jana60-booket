package jana60.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "events")
public class Events {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@NotEmpty(message = "Il nome e' obbliogatorio")
	@Column(nullable = false)
	private String name;
	
	@NotEmpty(message = "la descrizione e' obbliogatorio")
	@Column(nullable = false)
	private String description;
	
	@NotEmpty(message = "la locandina e' obbliogatorio")
	@Column(nullable = false)
	private String poster;
	
	@NotEmpty(message = "la data di inizio e' obbliogatorio")
	@Column(nullable = false)
	private String startDate;
	
	@NotEmpty(message = "la data di fine e' obbliogatorio")
	@Column(nullable = false)
	private String endDate;
	
	@NotEmpty(message = "lo stato e' obbliogatorio")
	@Column(nullable = false)
	private String publishedStatus;
	
	@NotEmpty(message = "lo stato e' obbliogatorio")
	@Column(nullable = false)
	private String canceledStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPublishedStatus() {
		return publishedStatus;
	}

	public void setPublishedStatus(String publishedStatus) {
		this.publishedStatus = publishedStatus;
	}

	public String getCanceledStatus() {
		return canceledStatus;
	}

	public void setCanceledStatus(String canceledStatus) {
		this.canceledStatus = canceledStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	
	
}

package cashtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("tag")
@Entity
@Table(name = "tags")
@NamedQuery(name = "Tag.findAll", query = "SELECT t FROM Tag AS t ORDER BY t.name")
public class Tag extends AppEntity {

	@Column(nullable = false, unique = true, length = 32)
	private String name;

	//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Tag[" + id + ", " + name + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}

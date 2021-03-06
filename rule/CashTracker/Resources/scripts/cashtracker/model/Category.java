package cashtracker.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.dataengine.jpa.AppEntity;

import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.converter.TransactionTypeConverter;


@JsonRootName("category")
@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = { "parent_id", "name" }) )
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category AS c WHERE c.parent IS NULL ORDER BY c.name")
public class Category extends AppEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List <Category> children;

	@Column(nullable = false, length = 64)
	private String name;

	@Convert(converter = TransactionTypeConverter.class)
	@Column(name = "transaction_type", nullable = false, length = 3)
	private TransactionType transactionType = TransactionType.EXPENSE;

	private boolean enabled;

	@Column(length = 256)
	private String note;

	@Column(length = 7)
	private String color;

	//
	public List <Category> getChildren() {
		return children;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	@JsonGetter("parent")
	public Long getParentId() {
		if (parent == null || parent.id == 0) {
			return null;
		}
		return parent.id;
	}

	@JsonSetter("parent")
	public void setParentId(Long id) {
		if (id == null || id == 0) {
			setParent(null);
			return;
		}

		Category parent = new Category();
		parent.setId(id);
		setParent(parent);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@JsonGetter("transactionType")
	public String getTransactionTypeValue() {
		if (transactionType == null) {
			return null;
		}
		return transactionType.toValue();
	}

	@JsonSetter("transactionType")
	public void setTransactionTypeByValue(String value) {
		setTransactionType(TransactionType.typeOf(value));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Category[" + id + ", " + name + ", " + enabled + ", " + parent + ", " + transactionType + ", "
				+ getAuthor() + ", " + getRegDate() + "]";
	}
}

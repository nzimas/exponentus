package cashtracker.model;

import java.util.Date;

import com.flabser.solutions.cashtracker.constants.BudgetStatusType;
import com.flabser.users.User;


public class Budget {

	private long id;

	private String name;

	private Date regDate;

	private String owner;

	private BudgetStatusType status;

	//
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegDate() {
		return regDate;
	}

	public Date setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(User user) {
		this.owner = user.getUserID();
	}

	public BudgetStatusType getStatus() {
		return status;
	}

	public void setStatus(BudgetStatusType status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Budget[" + name + "]";
	}
}
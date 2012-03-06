package nerdydog.domoHomeProd.object;

import java.util.Date;

public class Counter {
	
	int id, domo_id;
	String value;
	Date created_at;	
	
	public Counter(int id, int domo_id, String value, Date created_at){
		this.id = id;
		this.domo_id = domo_id;
		this.value = value;
		this.created_at = created_at;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDomo_id() {
		return domo_id;
	}

	public void setDomo_id(int domoId) {
		domo_id = domoId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date createdAt) {
		created_at = createdAt;
	}	
}

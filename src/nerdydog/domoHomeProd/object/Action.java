package nerdydog.domoHomeProd.object;

import java.util.Date;

public class Action {
	
	int id, domo_id, parent_id, root_id, delay, is_trigger, pos;
	

	Date starttime, endtime, created_at;
	String name, scheduled;
	
	public Action(int id, int domo_id, int parent_id, Date starttime, Date endtime, Date created_at, String name, int delay, int root_id, int is_trigger, int pos, String scheduled ){
		this.id = id;
		this.domo_id = domo_id;
		this.parent_id = parent_id;
		this.starttime = starttime;
		this.endtime = endtime;
		this.created_at = created_at;
		this.name = name;
		this.delay = delay;
		this.root_id = root_id;
		this.is_trigger = is_trigger;
		this.pos = pos;
		this.scheduled = scheduled;
	}
	
	public void setScheduled(String scheduled){
		this.scheduled = scheduled;
	}
	
	public String getScheduled(){
		return this.scheduled;
	}
	
	public int getRoot_id() {
		return root_id;
	}


	public void setRoot_id(int rootId) {
		root_id = rootId;
	}
	
	public int getIs_trigger() {
		return is_trigger;
	}


	public void setIs_trigger(int _is_trigger) {
		is_trigger = _is_trigger;
	}
	
	public int getPos() {
		return pos;
	}


	public void setPos(int _pos) {
		pos = _pos;
	}


	public int getDelay() {
		return delay;
	}


	public void setDelay(int delay) {
		this.delay = delay;
	}	
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
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

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parentId) {
		parent_id = parentId;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date createdAt) {
		created_at = createdAt;
	}	
}

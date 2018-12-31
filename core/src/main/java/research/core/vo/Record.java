package research.core.vo;

import java.util.List;

public class Record extends BaseVo {
	private static final long serialVersionUID = -8994083765802855424L;
	private User user;
	private Item item;
	private Record record;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	@Override
	public List<Double> doubleValue() {
		// TODO Auto-generated method stub
		return null;
	}
}

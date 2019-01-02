package research.core.vo;

import java.util.List;

/**
 * ÆÀ·Ö
 * 
 * @author liweigu714@163.com
 *
 */
public class Rating extends BaseVo {
	private static final long serialVersionUID = -8994083765802855424L;
	private User user;
	private Item item;
	private Label label;

	public Rating(User user, Item item, Label label) {
		this.user = user;
		this.item = item;
		this.label = label;
	}

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

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public List<Double> doubleValue() {
		// TODO Auto-generated method stub
		return null;
	}

}

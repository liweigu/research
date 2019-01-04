package research.core.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 评分
 * 
 * @author liweigu714@163.com
 *
 */
public class Rating extends BaseVo {
	private static final long serialVersionUID = -8994083765802855424L;
	private User user;
	private Item item;
	private Label label;

	/**
	 * 构造函数
	 * 
	 * @param user 用户
	 * @param item 物品
	 * @param label 标签
	 */
	public Rating(User user, Item item, Label label) {
		this.user = user;
		this.item = item;
		this.label = label;
	}

	/**
	 * 返回用户
	 * 
	 * @return 用户
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置用户
	 * 
	 * @param user 用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 返回物品
	 * 
	 * @return 物品
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * 设置物品
	 * 
	 * @param item 物品
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * 返回标签
	 * 
	 * @return 标签
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * 设置标签
	 * 
	 * @param label 标签
	 */
	public void setLabel(Label label) {
		this.label = label;
	}

	/**
	 * 返回数值
	 * 
	 * @return 数值
	 */
	@Override
	public List<Double> doubleValue() {
		List<Double> list = new ArrayList<Double>();

		if (this.getUser() != null) {
			List<Double> userDoubleValue = this.getUser().doubleValue();
			if (userDoubleValue != null) {
				list.addAll(userDoubleValue);
			}
		}

		if (this.getItem() != null) {
			List<Double> itemDoubleValue = this.getItem().doubleValue();
			if (itemDoubleValue != null) {
				list.addAll(itemDoubleValue);
			}
		}

		if (this.getLabel() != null) {
			List<Double> labelDoubleValue = this.getLabel().doubleValue();
			if (labelDoubleValue != null) {
				list.addAll(labelDoubleValue);
			}
		}

		return list;
	}

}

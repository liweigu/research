package research.core.vo;

import java.util.List;

/**
 * 物品
 * 
 * @author liweigu714@163.com
 *
 */
public class Item extends BaseVo {
	private static final long serialVersionUID = 7488113199147911961L;

	private List<Double> values;

	/**
	 * 构造函数
	 * 
	 * @param values 数值
	 */
	public Item(List<Double> values) {
		this.values = values;
	}

	/**
	 * 返回数值
	 * 
	 * @return 数值
	 */
	@Override
	public List<Double> doubleValue() {
		return values;
	}

}

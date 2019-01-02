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

	public Item(List<Double> values) {
		this.values = values;
	}

	@Override
	public List<Double> doubleValue() {
		return values;
	}

}

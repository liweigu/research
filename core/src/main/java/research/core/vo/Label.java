package research.core.vo;

import java.util.List;

/**
 * 标签
 * 
 * @author liweigu714@163.com
 *
 */
public class Label extends BaseVo {
	private static final long serialVersionUID = -683249698941076369L;

	private List<Double> values;

	public Label(List<Double> values) {
		this.values = values;
	}

	@Override
	public List<Double> doubleValue() {
		return values;
	}

}

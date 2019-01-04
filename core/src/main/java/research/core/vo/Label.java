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

	/**
	 * 构造函数
	 * 
	 * @param values 数值
	 */
	public Label(List<Double> values) {
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

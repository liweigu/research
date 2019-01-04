package research.core.vo;

import java.io.Serializable;
import java.util.List;

import research.core.data.DataFrame;

/**
 * VO基础类接口
 * 
 * @author liweigu714@163.com
 *
 */
public interface BaseVoI extends Serializable {
	/**
	 * 返回数值
	 * 
	 * @return 数值
	 */
	List<Double> doubleValue();

	/**
	 * 返回数据框架
	 * 
	 * @return 数据框架
	 */
	DataFrame dataFrame();
}

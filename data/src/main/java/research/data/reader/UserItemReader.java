package research.data.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import research.core.vo.Item;
import research.core.vo.Label;
import research.core.vo.Rating;
import research.core.vo.User;

/**
 * 用户物品读取器
 * 
 * @author liweigu714@163.com
 *
 */
public class UserItemReader {
	public static Map<String, Map<Double, User>> CachedUsers = new HashMap<String, Map<Double, User>>();
	public static Map<String, Map<Double, Item>> CachedItems = new HashMap<String, Map<Double, Item>>();

	public static Map<Double, User> readUsers(String userDataPath) {
		Map<Double, User> users;
		if (CachedUsers.containsKey(userDataPath)) {
			users = CachedUsers.get(userDataPath);
		} else {
			synchronized (CachedUsers) {
				users = new HashMap<Double, User>();

				List<String> lines = FileDataReader.readFile(userDataPath);
				for (String line : lines) {
					String[] arr = line.split("::");
					double userId = Double.parseDouble(arr[0]);
					double gender = "M".equals(arr[1]) ? 0 : 1;
					double age = Double.parseDouble(arr[2]);
					double occupation = Double.parseDouble(arr[3]);
					// 只取前5位
					double zipCode = Double.parseDouble(arr[4].substring(0, 5));

					// 归一化
					age /= 100.0;
					occupation /= 20.0;
					zipCode /= 100000;

					List<Double> values = new ArrayList<Double>();
					values.add(gender);
					values.add(age);
					values.add(occupation);
					values.add(zipCode);
					User user = new User(values);
					users.put(userId, user);
				}
				CachedUsers.put(userDataPath, users);
			}
		}

		return users;
	}

	public static Map<Double, Item> readItems(String itemDataPath) {
		Map<Double, Item> items;

		if (CachedItems.containsKey(itemDataPath)) {
			items = CachedItems.get(itemDataPath);
		} else {
			synchronized (CachedItems) {
				items = new HashMap<Double, Item>();

				List<String> lines = FileDataReader.readFile(itemDataPath);
				for (String line : lines) {
					String[] arr = line.split("::");
					double movieId = Double.parseDouble(arr[0]);
					String movieTypes = arr[2];

					List<Double> values = new ArrayList<Double>();
					// 归一化
					double normedMovieId = movieId / 10000.0;
					values.add(normedMovieId);
					// types.length == 18
					String[] types = "Action,Adventure,Animation,Children's,Comedy,Crime,Documentary,Drama,Fantasy,Film-Noir,Horror,Musical,Mystery,Romance,Sci-Fi,Thriller,War,Western"
							.split(",");
					for (String type : types) {
						// movieTypes包含type则设置该列为1，否则设置该列为0
						double typeValue = movieTypes.contains(type) ? 1.0 : 0;
						values.add(typeValue);
					}
					Item item = new Item(values);
					items.put(movieId, item);
				}
				CachedItems.put(itemDataPath, items);
			}
		}

		return items;
	}

	public static List<Rating> readRatings(String userDataPath, String itemDataPath, String ratingDataPath) {
		return readRatings(userDataPath, itemDataPath, ratingDataPath, 0, -1);
	}

	public static List<Rating> readRatings(String userDataPath, String itemDataPath, String ratingDataPath, int start, int size) {
		List<Rating> ratings = new ArrayList<Rating>();

		Map<Double, User> users = readUsers(userDataPath);
		Map<Double, Item> items = readItems(itemDataPath);

		List<String> lines = FileDataReader.readFile(ratingDataPath, start, size);
		for (String line : lines) {
			String[] arr = line.split("::");
			double userId = Double.parseDouble(arr[0]);
			double movieId = Double.parseDouble(arr[1]);
			double ratingValue = Double.parseDouble(arr[2]);
			long time = Long.parseLong(arr[3]); // TODO

			// 归一化
			ratingValue /= 5.0;

			User user = users.get(userId);
			// System.out.println(user.doubleValue().size());
			Item item = items.get(movieId);
			// System.out.println(item.doubleValue().size());
			Label label = new Label(Collections.singletonList(ratingValue));

			Rating rating = new Rating(user, item, label);
			ratings.add(rating);
		}

		return ratings;
	}
}

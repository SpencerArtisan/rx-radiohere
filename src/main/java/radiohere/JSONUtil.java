package radiohere;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {
	public static List<JSONObject> convertToList(JSONArray array) {
		List<JSONObject> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			list.add(array.getJSONObject(i));
		}
		return list;
	}
}

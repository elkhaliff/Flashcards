fun containsKeyAndValue(map: Map<String, String>, value: String) =
    map.containsValue(value) && map.containsKey(value)

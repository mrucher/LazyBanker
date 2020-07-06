package Area;

import Config.Config;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AreaParser {
    Map<String, String> areaMapNicknames = new HashMap<>();
    Map<String, String> areaMapURL = new HashMap<>();
    GlobalArea globalArea;

    public AreaParser() throws IOException {
        globalArea = initGlobalArea();
        for (Area area : globalArea.areas) {
            for (Nickname nickname : area.nicknames) {
                areaMapNicknames.put(nickname.name.toLowerCase(), area.areaName);
            }
            areaMapURL.put(area.areaName, area.URL);
        }

    }

    private GlobalArea initGlobalArea() throws IOException {
        GlobalArea globalArea;
        Gson g = new Gson();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(Config.defaultAreaJsonPath), StandardCharsets.UTF_8);
        globalArea = g.fromJson(reader, GlobalArea.class);
        reader.close();
        return globalArea;
    }

    public Area parse(String msg) {
        Area result = new Area();
        result.areaName = "Empty";
        result.URL = "";
        String ms = msg.toLowerCase();
        for (Map.Entry<String, String> cur : areaMapNicknames.entrySet()) {
            if (ms.contains(cur.getKey().toLowerCase())) {
                result.areaName = cur.getValue();
                result.URL = areaMapURL.get(cur.getValue());
                return result;
            }
        }
        return result;

    }
}
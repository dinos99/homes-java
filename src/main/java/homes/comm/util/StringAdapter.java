package homes.comm.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class StringAdapter implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            // This forces any JSON primitive (number, boolean, or string) to be treated as a string
            return json.getAsString(); 
        }
        // Handle non-primitive cases if necessary (e.g., objects/arrays)
        return json.toString();
    }

}

package de.crazything.json.dao;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Flyweight DAO for all.
 * 
 * @author roger
 * 
 * @param <T>
 *            Type of Class the DAO is reponsible for.
 */
public final class CommonJsonDao<T> {
    /**
     * Static map of DAOs.
     */
    private static final Map<Class<?>, CommonJsonDao<?>> MAP = new HashMap<Class<?>, CommonJsonDao<?>>();
    /**
     * Type tag. Ahh, f... it! RTFC!
     */
    private final Class<T> forClass;

    /**
     * Lazily instantatied.
     */
    private ParameterizedType listType;

    /**
     * Ctor.
     * 
     * @param forClass
     *            what for.
     */
    private CommonJsonDao(final Class<T> forClass) {
	this.forClass = forClass;
    }

    /**
     * Yes, it's a flyweight.
     * 
     * @param <T>
     *            Type of Object for DAO.
     * @param clazz
     *            Class of Object for DAO.
     * @return Instance concerning T.
     */
    // No synchronized! HashMap does not allow duplettes...
    public static <T> CommonJsonDao<T> getInstance(final Class<T> clazz) {
	@SuppressWarnings("unchecked")
	CommonJsonDao<T> result = (CommonJsonDao<T>) MAP.get(clazz);
	if (result == null) {
	    result = new CommonJsonDao<T>(clazz);
	    MAP.put(clazz, result);
	}
	return result;
    }

    /**
     * Jsonifies a Json String.
     * 
     * @param jsonString
     *            The string.
     * @return An object.
     */
    public T createJsonObject(final String jsonString) {
	// System.out.println(jsonString);
	return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create()
		.fromJson(jsonString, this.forClass);
    }

    public List<T> createJsonArray(final String jsonString) {
	if (this.listType == null) {
	    synchronized (this) {
		this.listType = new ParameterizedTypeImpl(List.class, new Type[] { this.forClass }, null);
	    }
	}
	return new Gson().fromJson(jsonString, this.listType);
    }

    /**
     * Stringifies an Object.
     * 
     * Type of embedded object.
     * 
     * @param obj
     *            the object.
     * @return Json String.
     */
    public String createJsonString(final Object obj) {
	final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	return gson.toJson(obj, this.forClass);
    }

    /**
     * Stringifies an Object.
     * 
     * Type of embedded object.
     * 
     * @param obj
     *            the object.
     * @return Json String.
     */
    public String createPrettyJsonString(final Object obj) {
	final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).setPrettyPrinting().create();
	return gson.toJson(obj, this.forClass);
    }

    /**
     * Stringifies an Object with one embedded object.
     * 
     * @param <S>
     *            Type of embedded object.
     * @param obj
     *            the object.
     * @param embedded
     *            The embedded object.
     * @return Json String.
     */
    public <S> String createJsonString(final Object obj, final S embedded) {
	final GsonBuilder gsonBuilder = new GsonBuilder();
	gsonBuilder.excludeFieldsWithModifiers(Modifier.PRIVATE);
	gsonBuilder.registerTypeAdapter(embedded.getClass(), new JsonSerializer<S>() {
	    @Override
	    public JsonElement serialize(final S src, final Type t, final JsonSerializationContext context) {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJsonTree(src, t);
	    }
	});
	final Gson gson = gsonBuilder.create();
	return gson.toJson(obj);
    }

    static class ParameterizedTypeImpl implements ParameterizedType {

	private final Type rawType;
	private final Type[] actualTypeArguments;
	private final Type owner;

	public ParameterizedTypeImpl(final Type rawType, final Type[] actualTypeArguments, final Type owner) {
	    this.rawType = rawType;
	    this.actualTypeArguments = actualTypeArguments;
	    this.owner = owner;
	}

	@Override
	public Type getRawType() {
	    return this.rawType;
	}

	@Override
	public Type[] getActualTypeArguments() {
	    return this.actualTypeArguments;
	}

	@Override
	public Type getOwnerType() {
	    return this.owner;
	}
    }
}

package de.crazything.sql.typernate.factory;

import de.crazything.json.dao.CommonJsonDao;

public class TypeFactory {
    public static final String JSON_TEST_TYPE = "{\"id\":123456,\"name\":\"ad'min\",\"inner\":{\"innerId\":1,\"string\":\"foo\"}}";

    public static final String JSON_TEST_TYPE_WITH_LIST = "{\"id\":123456,\"name\":\"admin\",\"list\":[{\"innerId\":1,\"string\":\"foo\"},{\"innerId\":2,\"string\":\"bar\"}]}";

    public static final String JSON_TEST_ENTITY = "{\"testType\":{\"id\":123456,\"name\":\"admin\",\"list\":[{\"innerId\":1,\"string\":\"foo\"},{\"innerId\":1}]}}";

    public static final String JSON_TEST_ENTITY_WITH_LIST = "{\"testType\":[{\"id\":123456,\"name\":\"ad'min\",\"list\":[{\"innerId\":1,\"string\":\"foo\"},{\"innerId\":1}]},"
	    + "{\"id\":987654,\"name\":\"roger#\",\"list\":[{\"innerId\":1,\"string\":\"foo\"},{\"innerId\":2,\"string\":\"bar\"}]}"
	    + "]}";

    public static final String JSON_TEST_TYPE_NULL = "{\"id\":0,\"inner\":{\"innerId\":0}}";

    public static final String JSON_TEST_TYPE_WITH_LIST_NULL = "{\"id\":0}";

    public static final String JSON_TEST_NASTY_LIST = "{\"id\":123456,\"name\":\"admin\",\"list\":"
	    + "[{\"innerId\":1,\"string\":\"nasty:f(o},'''#o\"},{\"innerId\":2,\"string\":\"nasty:b\\\"ar\"}]}";
    /**
     * Keep in mind: admi___\\\\___n will be admi___\___n in the end. It parsed
     * from JSON to Java to database (database will escape as well, but shown as
     * admi___\\___n).
     */
    public static final String JSON_TEST_VERY_NASTY_LIST = "{\"id\":123456,\"name\":\"a\\nd\\tmi___\\\\___\",\"list\":"
	    + "[{\"innerId\":1,\"string\":\"nasty###\\\"###:f(o},'''#o\"},{\"innerId\":2,\"string\":\"nasty:b###\\\"###ar\"}]}";

    public static <T> T generateObject(final Class<T> clazz, final String json) {
	return CommonJsonDao.getInstance(clazz).createJsonObject(json);
    }
}

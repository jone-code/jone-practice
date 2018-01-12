@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
MappedStatement.class, Object.class, RowBounds.class,
ResultHandler.class }) })
public class test implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(OffsetLimitInterceptor.class);
	static int MAPPED_STATEMENT_INDEX = 0;// 这是对应上面的args的序号
	static int PARAMETER_INDEX = 1;
	static int ROWBOUNDS_INDEX = 2;
	static int RESULT_HANDLER_INDEX = 3;
	String dialectClass;// 在mybatise-config的配制属性，可多个


	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final Executor executor = (Executor) invocation.getTarget();
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		final Object parameter = queryArgs[PARAMETER_INDEX];
		final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
		final PageBounds pageBounds = new PageBounds(rowBounds);


		final Dialect dialect;
try {
Class clazz = Class.forName(dialectClass);// 这是在mybatise-config里配制，就是设置你选用了哪种数据库，mysql
// or oracle
Constructor constructor = clazz.getConstructor(
MappedStatement.class, Object.class, PageBounds.class);
dialect = (Dialect) constructor.newInstance(new Object[] { ms,
parameter, pageBounds });
} catch (Exception e) {
throw new ClassNotFoundException("Cannot create dialect instance: "
+ dialectClass, e);
}
final BoundSql boundSql = ms.getBoundSql(parameter);// 获得查询语句对像
GlobalUser globalUser = GlobalUser.getGlobalUser();// 获得线程中的全局用户信息
if (globalUser != null) {
System.out.println("user:_______"
+ globalUser.getUser().getUserid());
String sql = boundSql.getSql();// 获得查询语句
System.out.println("orginsql_________________:" + sql);
sql = sql + " where userid='" + globalUser.getUser().getUserid()
+ "'";// 改变查询语句
BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql,
boundSql.getParameterMappings(),
boundSql.getParameterObject());// 重新new一个查询语句对像
MappedStatement newMs = copyFromMappedStatement(ms,
new BoundSqlSqlSource(newBoundSql));// 把新的查询放到statement里
queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
}
String methonName = invocation.getMethod().getName();
System.out.println("methodName__________:" + methonName);
return invocation.proceed();
}


@Override
public Object plugin(Object target) {
return Plugin.wrap(target, this);// 这些莫认调用
}


@Override
public void setProperties(Properties properties) {
// TODO Auto-generated method stub
PropertiesHelper propertiesHelper = new PropertiesHelper(properties);
String dialectClass = propertiesHelper
.getRequiredString("dialectClass");
setDialectClass(dialectClass);// 这个方法主要就是获得mybatis-config里的配制
// 比如：<!-- <plugin
// interceptor="com.teamsun.net.common.utils.MyBatisePlugin">
// <property name="dialectClass"
// value="com.github.miemiedev.mybatis.paginator.dialect.OracleDialect"
// />
// </plugin> -->
// 上面是在mybatise-config的配制
}


public void setDialectClass(String dialectClass) {
logger.debug("dialectClass: {} ", dialectClass);
this.dialectClass = dialectClass;
}


private MappedStatement copyFromMappedStatement(MappedStatement ms,
SqlSource newSqlSource) {
Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
ms.getId(), newSqlSource, ms.getSqlCommandType());


builder.resource(ms.getResource());
builder.fetchSize(ms.getFetchSize());
builder.statementType(ms.getStatementType());
builder.keyGenerator(ms.getKeyGenerator());
if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
builder.keyProperty(ms.getKeyProperties()[0]);
}
// setStatementTimeout()
builder.timeout(ms.getTimeout());


// setStatementResultMap()
builder.parameterMap(ms.getParameterMap());


// setStatementResultMap()
builder.resultMaps(ms.getResultMaps());
builder.resultSetType(ms.getResultSetType());


// setStatementCache()
builder.cache(ms.getCache());
builder.flushCacheRequired(ms.isFlushCacheRequired());
builder.useCache(ms.isUseCache());


return builder.build();
}


public static class BoundSqlSqlSource implements SqlSource {


private BoundSql boundSql;


public BoundSqlSqlSource(BoundSql boundSql) {
this.boundSql = boundSql;
}


public BoundSql getBoundSql(Object parameterObject) {
return boundSql;
}
}


}
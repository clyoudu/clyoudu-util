# clyoudu-util

[English](README.md) | 中文

`clyoudu-util` 是一个基于 Java 8 和 Maven 的工具及示例代码集合。仓库既包含可复用的格式化、文件、密码、SSH 等工具，也包含任务流水线、算法题解和 JVM 故障演示。本文档在翻译英文 README 核心内容的基础上，补充了当前源码中的其他模块；算法与故障演示代码不应被视为稳定的通用 API。

## byteformat

`ByteUnitFormat` 用于将字节大小转换为指定单位，或格式化为便于阅读的字符串。支持 `B`、`K`、`M`、`G`、`T`、`P`、`E` 单位。

- `rate`：进位制，支持 `Rate.SI`（1000）和 `Rate.IEC`（1024）。
- `scale`：返回值保留的小数位数。
- `roundingMode`：舍入方式，类型为 `java.math.RoundingMode`。
- `displayUnitName`：是否在结果中显示单位。

```java
System.out.println(ByteUnitFormat.B.to(ByteUnitFormat.K, 1024L, Rate.SI));  // 1.02K
System.out.println(ByteUnitFormat.B.to(ByteUnitFormat.K, 1024L, Rate.IEC)); // 1K
System.out.println(ByteUnitFormat.M.to(ByteUnitFormat.G, 4096L, Rate.IEC)); // 4G
System.out.println(ByteUnitFormat.M.to(ByteUnitFormat.G, 4096L));           // 4G
System.out.println(ByteUnitFormat.B.humanReadable(174541987L));             // 166.5M
System.out.println(ByteUnitFormat.M.humanReadable(1024L));                  // 1G
System.out.println(ByteUnitFormat.M.humanReadable(1024L, false));           // 1
```

`to(...)` 的调用者是原始单位，`targetFormat` 是目标单位；简化重载默认使用 IEC、`HALF_UP` 和显示单位。`humanReadable(...)` 会根据数值自动选择单位。

## consoletable

`ConsoleTable.ConsoleTableBuilder` 用于在控制台生成简单的 ASCII 表格。

- `lineSep`：表格各行之间的分隔符。
- `verticalSep`：单元格的纵向分隔符。
- `horizontalSep`：单元格的横向分隔符。
- `joinSep`：横线与竖线的连接符。
- `nullPolicy`：空值处理策略，包括空字符串、字符串 `null` 和抛出异常等策略。
- `restrict`：启用后要求表头和每一行的列数相同。
- `Cell` 可通过 `Align.LEFT`、`Align.CENTER`、`Align.RIGHT` 指定对齐方式。

```java
List<Cell> header = new ArrayList<Cell>() {{
    add(new Cell("name"));
    add(new Cell("email"));
    add(new Cell("tel"));
}};

List<List<Cell>> body = new ArrayList<List<Cell>>() {{
    add(new ArrayList<Cell>() {{
        add(new Cell("kat"));
        add(new Cell(Align.CENTER, "kat@gmail.com"));
        add(new Cell(Align.RIGHT, "54321"));
    }});
    add(new ArrayList<Cell>() {{
        add(new Cell("ashe"));
        add(new Cell("ashe_111@hotmail.com"));
        add(new Cell("9876543210"));
    }});
    add(new ArrayList<Cell>() {{
        add(null);
        add(new Cell(null));
        add(new Cell(Align.LEFT, "11"));
    }});
}};

System.out.println(
    new ConsoleTable.ConsoleTableBuilder()
        .addHeaders(header)
        .addRows(body)
        .build()
);

// 无表头
System.out.println(new ConsoleTable.ConsoleTableBuilder().addRows(body).build());

// 严格检查列数
System.out.println(
    new ConsoleTable.ConsoleTableBuilder()
        .addHeaders(header)
        .addRows(body)
        .restrict(true)
        .build()
);

// 空值策略和自定义分隔符
System.out.println(
    new ConsoleTable.ConsoleTableBuilder()
        .addHeaders(header)
        .addRows(body)
        .nullPolicy(NullPolicy.NULL_STRING)
        .lineSep("\n\n")
        .verticalSep("*")
        .horizontalSep("*")
        .joinSep("*")
        .build()
);
```

默认输出示意：

```text
+------+----------------------+------------+
| name | email                | tel        |
+------+----------------------+------------+
| kat  |    kat@gmail.com     |      54321 |
| ashe | ashe_111@hotmail.com | 9876543210 |
|      |                      | 11         |
+------+----------------------+------------+
```

类似工具：

- [TableList.java](https://github.com/therealfarfetchd/crogamp/blob/master/src/com/github/mrebhan/crogamp/cli/TableList.java)
- [java-ascii-table](http://bethecoder.com/applications/products/asciiTable.action)
- [asciitable](https://github.com/vdmeer/asciitable)

## packagegenerator

`PackageGenerator` 将指定目录的层级结构生成为字符串。

- `rootDir`：需要扫描的根目录，必须存在且为目录。
- `ignoreDirs`：需要忽略的目录名集合；父目录被忽略时，其子项也不会生成。
- `packageCollapse`：为 `true` 时，将连续的单一目录折叠为类似 `github.clyoudu.util` 的包名形式。
- `generateFile`：为 `true` 时同时输出文件，否则只输出目录。

```java
String tree = PackageGenerator.generate(
    System.getProperty("user.dir") + "/src/main/java",
    new HashSet<>(Arrays.asList(".idea", "target", ".git")),
    true,
    true
);
System.out.print(tree);
```

输出示意：

```text
java.github.clyoudu
├── byteformat
│   └── ByteUnitFormat.java
├── consoletable
│   ├── ConsoleTable.java
│   ├── enums
│   │   ├── Align.java
│   │   └── NullPolicy.java
│   └── table
│       └── Cell.java
└── packagegenerator
    └── PackageGenerator.java
```

## collectorsext

`CollectorsExt` 扩展了 `java.util.stream` 的收集和过滤能力。

- `toJsonArray()`：将流元素收集为 Fastjson 的 `JSONArray`。
- `distinctBy(keyExtractor)`：返回可用于 `Stream.filter(...)` 的谓词，按照提取出的键去重；内部使用 `ConcurrentHashMap` 记录已经出现的键，因此键不能为 `null`。

```java
Map<String, String> map = new HashMap<>();
map.put("a", "a");
map.put("b", "b");
map.put("c", "c");

System.out.println(map.entrySet().stream().collect(CollectorsExt.toJsonArray()));
System.out.println(Stream.of("a", "b", "c").collect(CollectorsExt.toJsonArray()));
System.out.println(
    Stream.of("a", "b", "c")
        .map(s -> new JSONObject().fluentPut(s, s))
        .collect(CollectorsExt.toJsonArray())
);

List<JSONObject> values = Arrays.asList(
    new JSONObject().fluentPut("name", "a").fluentPut("age", 1),
    new JSONObject().fluentPut("name", "b").fluentPut("age", 2),
    new JSONObject().fluentPut("name", "a").fluentPut("age", 3)
);
values.stream()
    .filter(CollectorsExt.distinctBy(value -> value.getString("name")))
    .forEach(System.out::println);
```

## proxysqlclient

`ProxysqlClient` 通过 [jasync-sql](https://github.com/jasync-sql/jasync-sql) 连接 ProxySQL 管理端或 MySQL。

- `executeQuery(...)` 执行单条查询，返回 `List<Map<String, Object>>`，字段名保持数据库原始名称。
- `execute(...)` 顺序执行 SQL 列表，可选择是否使用事务，并返回是否成功。
- `database` 为空时使用 `information_schema`；`timeout` 为空或不大于 0 时不设置等待超时，单位为秒。
- 模块支持批量顺序执行；MySQL 可使用事务。ProxySQL 本身不支持事务，传入事务选项并不代表 ProxySQL 获得事务能力。
- 可处理异常会包装为 `ProxysqlException`。

```java
List<Map<String, Object>> rows = ProxysqlClient.executeQuery(
    "192.0.2.10", 6032, "main", "admin_user", "<password>",
    "select * from mysql_users", 10
);

boolean success = ProxysqlClient.execute(
    "192.0.2.10", 6032, "main", "admin_user", "<password>",
    Arrays.asList(
        "INSERT INTO mysql_users(username,password,default_hostgroup) "
            + "VALUES ('app','<app-password>',2)",
        "load mysql users to runtime",
        "save mysql users to disk"
    ),
    10,
    false
);
```

示例中的地址和凭据均为不可用占位值。不要把真实数据库地址、用户名或密码提交到仓库。

## fileutil

`FileUtil` 提供 classpath 和操作系统文件的读取、保存、删除及路径处理方法。classpath 读取基于 Spring `ClassPathResource`，可用于普通运行、JAR 或容器环境中的资源读取。

- `readClasspathFile(String)`：读取 classpath 文件。
- `readOsFile(String)`、`readFile(File)`、`readFile(InputStream)`：读取操作系统文件、`File` 或输入流。
- `saveOsFile(String, String, boolean)`：写入操作系统文件，`append` 控制覆盖或追加，并在需要时创建父目录和文件。
- `deleteOsFile(String)`：删除操作系统文件。
- `formatFilePath(String)`：合并重复的 `/` 或 `\`。
- `getParentDir(String)`：获取使用 `/` 分隔的路径的父目录。

```java
String classpathText = FileUtil.readClasspathFile("/test.txt");
String osText = FileUtil.readOsFile(
    System.getProperty("user.dir") + "/src/main/resources/test.txt"
);

String output = System.getProperty("java.io.tmpdir") + "/clyoudu-util/example.txt";
FileUtil.saveOsFile(output, "first line", false);
FileUtil.saveOsFile(output, "\nsecond line", true);
System.out.println(FileUtil.readOsFile(output));
FileUtil.deleteOsFile(output);
```

`readFile(InputStream)` 会关闭传入的输入流，调用方不应在读取后继续使用该流。

## randompassword

`RandomPasswordUtil` 生成由大小写字母、数字和特殊字符组成的随机密码。特殊字符集合为 `~!@#$%^&*`。

`randomPassword(length, lower, upper, special, number)` 的参数顺序是：总长度、小写字母最少数量、大写字母最少数量、特殊字符最少数量、数字最少数量。四类字符的最少数量之和不能超过总长度。

```java
System.out.println(RandomPasswordUtil.randomPassword(16, 2, 2, 2, 2));
System.out.println(RandomPasswordUtil.randomPassword(8, 1, 1, 1, 1));
System.out.println(RandomPasswordUtil.randomPassword(8, 1, 1, 0, 1));
System.out.println(RandomPasswordUtil.defaultModelRandomPassword());
```

`defaultModelRandomPassword()` 等价于 `randomPassword(16, 1, 1, 1, 1)`。输出每次随机变化，不应依赖 README 中的某个固定示例值。

## coordinate

`coordinate` 是一个基于内存列表的二维坐标示例：

- `Coordinate` 保存 `id`、`x`、`y`，搜索后还会记录 `distance`。
- `CoordinateSystem` 定义 `create`、`update`、`delete`、`contains` 和 `search` 等操作。
- `AbstractCoordinateSystem` 提供按 `id/x/y` 参数创建坐标，以及按 ID 或坐标判断是否存在的便捷重载。
- `DefaultCoordinateSystem` 从输入流初始化坐标，并提供线性扫描和并行流搜索；`searchSquare(...)` 先按外接正方形过滤，再计算欧氏距离。

初始化输入按行使用 `id,x,y` CSV 格式，例如：

```text
1,10.5,20.0
2,12.0,19.5
3,30.0,40.0
```

```java
try (InputStream input = new ByteArrayInputStream(
    "1,10.5,20.0\n2,12.0,19.5\n3,30.0,40.0".getBytes(StandardCharsets.UTF_8))) {
    DefaultCoordinateSystem system = new DefaultCoordinateSystem(input);

    system.create(4L, 11.0, 20.5);
    System.out.println(system.contains(4L));

    Coordinate nearest = system.search(10.0, 20.0, 5.0);
    Coordinate squareFiltered = system.searchSquare(10.0, 20.0, 5.0);
    Coordinate removed = system.delete(4L);
}
```

`search(...)` 和 `searchSquare(...)` 返回给定半径内距离目标点最近的坐标，没有匹配项时返回 `null`。`update(...)` 是接口的一部分，但当前 `DefaultCoordinateSystem` 的实现会在校验后再次调用 `create(...)`，对已有 ID 可能触发重复坐标异常；该模块也使用进程内静态列表，不提供持久化、空间索引或线程安全保证，应按实验性实现使用。

## ssh

`ssh` 和 `ssh.pool` 基于 Apache MINA SSHD 与 Commons Pool2，提供 SSH 命令执行和 SFTP 文件上传。主要对象关系如下：

1. `SshConfig` 保存连接、认证、超时、连接池和会话校验配置。
2. 启动 `SshClient`，再由 `SshConnectionFactory` 根据配置创建和验证 `ClientSession`。
3. `SshConnectionPool` 负责借出、归还及销毁会话。
4. `SshExecutor.exec(...)` 执行命令并返回包含退出状态、标准输出和标准错误的 `SshResult`；`upload(...)` 通过 SFTP 上传输入流。

认证类型支持 `PASSWORD` 和 `KEYPAIR`。密码认证使用 `password`；密钥认证使用 `keypairType`、`privateKey` 和 `publicKey`。`connectTimeout`、`authTimeout`、`executeTimeout` 分别控制连接、认证和命令等待时间。连接池可通过 `maxIdle`、`maxTotal`、`minIdle`、`testOnBorrow`、`testWhileIdle`、`validateCommand`、`validateTimeout` 等参数配置。

以下代码只展示对象装配和公开 API，地址及凭据是不可用占位值：

```java
SshClient client = ClientBuilder.builder().build();
CoreModuleProperties.IDLE_TIMEOUT.set(client, Duration.ZERO);
client.start();

SshConfig sshConfig = new SshConfig();
sshConfig.setIp("192.0.2.10");
sshConfig.setPort(22);
sshConfig.setUsername("example-user");
sshConfig.setAuthType("PASSWORD"); // 或 KEYPAIR
sshConfig.setPassword("<password>");

SshConnectionFactory factory = new SshConnectionFactory(sshConfig, client);
GenericObjectPoolConfig<ClientSession> poolConfig = new GenericObjectPoolConfig<>();
poolConfig.setMaxIdle(sshConfig.getMaxIdle());
poolConfig.setMaxTotal(sshConfig.getMaxTotal());
poolConfig.setMinIdle(sshConfig.getMinIdle());
poolConfig.setTestWhileIdle(sshConfig.getTestWhileIdle());
poolConfig.setTestOnBorrow(sshConfig.getTestOnBorrow());
poolConfig.setBlockWhenExhausted(sshConfig.getBlockWhenExhausted());
poolConfig.setMaxWaitMillis(sshConfig.getMaxWaitMillis());
poolConfig.setJmxEnabled(false);

AbandonedConfig abandonedConfig = new AbandonedConfig();
abandonedConfig.setLogAbandoned(true);

SshConnectionPool pool = new SshConnectionPool(factory, poolConfig, abandonedConfig);
SshExecutor executor = new SshExecutor(pool, sshConfig);

try {
    SshResult result = executor.exec("printf 'hello'");
    System.out.println(result);

    try (InputStream input = new ByteArrayInputStream(
        "example".getBytes(StandardCharsets.UTF_8))) {
        executor.upload("/tmp/clyoudu-util-example", "example.txt", input);
    }
} finally {
    pool.close();
    client.close();
}
```

`exec(String)` 和 `upload(...)` 会在内部借用并归还会话；如果调用 `exec(String, ClientSession)`，会话生命周期由调用方负责。使用结束后必须关闭 `SshConnectionPool` 和 `SshClient`。不要在 README、源码或版本库中提交真实 IP、用户名、密码、私钥、公钥路径或其他可用凭据。

## taskpipeline

`taskpipeline` 是一个可组合的任务阶段示例：

- `AbstractStage` 定义执行生命周期，包括 `before()`、`executeInternal()`、`after()`、完成上报和状态处理。
- `Stage` 是普通任务节点；仓库中的默认实现会模拟耗时任务，实际业务可继承 `AbstractStage` 或相关阶段类实现自己的执行逻辑。
- `LinearStage` 按列表顺序执行子阶段，任一子阶段失败后停止。
- `ParallelStage` 使用 `TaskContext.getExecutor()` 返回的线程池，通过 `CompletableFuture` 并行执行子阶段。
- `TaskContext` 统计阶段总数和已完成数量，提供总体进度、取消状态及默认固定大小线程池。取消任务会调用 `shutdownNow()`。

```java
TaskContext context = new TaskContext();
List<AbstractStage> children = new ArrayList<>();
LinearStage pipeline = new LinearStage(context, null, "deploy", children);

children.add(new Stage(context, pipeline, "prepare"));

List<AbstractStage> parallelChildren = new ArrayList<>();
ParallelStage parallel = new ParallelStage(context, pipeline, "checks", parallelChildren);
parallelChildren.add(new Stage(context, parallel, "check-a"));
parallelChildren.add(new Stage(context, parallel, "check-b"));
children.add(parallel);

boolean success = pipeline.execute();
System.out.println(success);
System.out.println(context.getProgress());
context.getExecutor().shutdown();
```

`LinearTaskTest`、`ConcurrentTaskTest` 和 `CompositeTaskTest` 分别展示了串行、并行及从 JSON 递归组合阶段的方式。阶段在构造时计入 `TaskContext` 总数；正常完成后，调用方应关闭 `ExecutorService`，避免线程池继续存活。

## oom（危险：仅限隔离故障实验）

> **警告：** `oom` 下的类会刻意耗尽 JVM 或操作系统资源，可能造成内存溢出、栈溢出、线程耗尽、JVM 崩溃或进程异常。只能在资源受限、可丢弃且与日常开发及共享环境隔离的 JVM 中谨慎分析。尤其不要直接运行 `UnableToCreateNewNativeThread`，其源码已明确提示可能导致操作系统不可用。

这些类不是普通工具：

- `JavaHeapSpace`：不断保存对象以触发 `java.lang.OutOfMemoryError: Java heap space`；源码要求 `-Xmx32M`。
- `GCOverheadLimitExceeded`：不断保存字符串以触发 `GC overhead limit exceeded`；源码要求 `-Xmx64M`。
- `DirectBufferMemory`：申请超大直接缓冲区；源码示例参数为 `-Xmx64M -XX:MaxDirectMemorySize=20M`。
- `Metaspace`：通过 CGLIB 持续生成类；JDK 8+ 示例参数为 `-XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M`，JDK 7 及更早版本注释使用 `-XX:PermSize=10M -XX:MaxPermSize=10M`。
- `RequestedArraySizeExceedsVmLimit`：尝试创建超大集合底层数组，演示 `Requested array size exceeds VM limit`。
- `StackOverflow`：无限递归，演示 `StackOverflowError`；源码示例参数为 `-Xss128k`。
- `UnableToCreateNewNativeThread`：持续创建休眠线程，演示 `unable to create new native thread`，风险最高。
- `OOMUtil`：检查演示所需的最大堆或元空间参数，并输出 JVM 版本；它是故障示例的辅助类。

本文档不提供直接运行命令，以避免这些故障代码被误当作常规示例执行。若确需研究，应先设置外部资源限制，并确保可以强制终止和丢弃整个实验环境。

## leetcode（算法示例）

`leetcode` 包保存题解和数据结构/算法练习，不是统一的工具 API。当前包括 `Q1`、`Q20`、`Q38`、`Q207`、`Q404`、`Q455`、`Q523`、`Q665`、`Q718`、`Q1431`、`Q1470`、`Q1480`、`Q1486`、`Q1512`、`Q1603` 等 `Q*` 类，以及 `JianZhiOffer1_58`、`MianShiTi0203`、`Mianshiti1604` 和 `JosephusRing`。这些类主要用于阅读、调试和算法验证，接口风格及边界处理可能因题目而异。

## sqlrewrite（当前为实验代码）

`sqlrewrite` 目录当前只包含 `MonteCarloPi`，它使用 `CompletableFuture`、`ForkJoinPool.commonPool()` 和随机投点方式并发估算 π。目录名称与实际实现内容不一致；当前源码没有可供说明或使用的 SQL 重写能力，因此不应把该目录视为 SQL 解析或改写模块。

## 文档维护

英文 [README.md](README.md) 仍是仓库默认首页；本中文文档额外覆盖了源码中的算法和实验模块。新增或变更核心功能时，应同步维护两份文档中的语言入口、模块名称和关键 API，减少双语内容漂移。

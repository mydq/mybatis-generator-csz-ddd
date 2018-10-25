# mybatis generator

> MyBatis 自动生成工具，集成了ddd、分页、批量插入、序列化功能，本例只针对于mysql数据库

## 打开方式

> maven pom.xml add 
 
       <dependency>
            <groupId>com.github.mydq</groupId>
            <artifactId>mybatis-generator-csz-ddd</artifactId>
            <version>1.0.0</version>
       </dependency>
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.5</version>
            <configuration>
                <configurationFile>
                    mybatis-generator/generatorMybatis-mysql.xml
                </configurationFile>
                <overwrite>true</overwrite>
                <verbose>true</verbose>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>com.github.mydq</groupId>
                    <artifactId>mybatis-generator-csz-ddd</artifactId>
                    <version>1.0.0</version>
                </dependency>
            </dependencies>
        </plugin>
## 使用方式

> 修改xml文件路径
* 修改其中的数据库连接配置；allowMultiQueries=true
* 修改相关mybatis对象的包名及生成路径；
* 配置需要生成的数据表(表中必须有必须的6个字段)；
* 配置需要使用的plugin
* 执行plugin mybatis-generator 插件

** 注意：当表中有大字段时，自行添加覆盖，例如：
*        <!--数据库类型原为text-->
*        <columnOverride column="content01" property="content01"
*                        javaType="java.lang.String" jdbcType="VARCHAR" />
*        <!--数据库类型原为blob-->
*        <columnOverride column="content" property="content"
*                        javaType="java.lang.Byte[]" jdbcType="VARCHAR" />

> 具体使用方式
* 批量更新，插入，传集合 
* 分页在example中添加setPage()，将page对象传于bo的getPageByExample()之中，获得结果

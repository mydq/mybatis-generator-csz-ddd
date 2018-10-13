package org.asia.mybatis.generator.plugins;

import org.apache.commons.lang3.StringUtils;
import org.beans.LavaDo;
import org.beans.LavaExample;
import org.beans.LavaMapper;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class MySqlPaginationPlugin extends PluginAdapter {
    private static FullyQualifiedJavaType longType = new FullyQualifiedJavaType("java.lang.Long");
    private static String XMLFILE_POSTFIX = "Ext";
    private static String JAVAFILE_POTFIX = "Ext";
    private static String SQLMAP_COMMON_POTFIX = "and is_deleted = 'n'";
    private static String SQLMAP_COMMON_POTFIX_PVG = "and full_org_path like CONCAT(#{fullOrgPath}, '%')";
    private static String SQLMAP_COMMON_POTFIX_OWNER = "and owner =#{owner,jdbcType=VARCHAR}";
    private static String ANNOTATION_RESOURCE = "javax.annotation.Resource";

    public MySqlPaginationPlugin() {
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType baseDoType = new FullyQualifiedJavaType(LavaDo.class.getName());
        topLevelClass.addImportedType(baseDoType);
        topLevelClass.setSuperClass(baseDoType);
        this.addBaseColumns(introspectedTable);
        String packagePath = introspectedTable.getTableConfiguration().getProperties().getProperty("boPackage");
        if (StringUtils.isBlank(packagePath)) {
            throw new RuntimeException("StateAction模式中，Do必须指定Bo");
        } else {
            String doName = introspectedTable.getBaseRecordType();
            String boName = doName.substring(doName.lastIndexOf(".") + 1).replace("Do", "") + BoPlugin.JAVAFILE_POTFIX;
            boName = packagePath + "." + boName;
            this.createMethod(JavaVisibility.PUBLIC, "getBoQualifiedIntfName", "@Override", FullyQualifiedJavaType.getStringInstance(), (List)null, "return \"" + boName + "\";", topLevelClass);
            return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        }
    }

    protected void createMethod(JavaVisibility visi, String name, String annotation, FullyQualifiedJavaType returnType, List<Parameter> parms, String body, TopLevelClass topLevelClass) {
        Method method = new Method(name);
        method.setVisibility(visi);
        if (StringUtils.isNotBlank(annotation)) {
            method.addAnnotation(annotation);
        }

        if (returnType != null) {
            method.setReturnType(returnType);
            topLevelClass.addImportedType(returnType);
        }

        if (parms != null && !parms.isEmpty()) {
            Iterator i$ = parms.iterator();

            while(i$.hasNext()) {
                Parameter parm = (Parameter)i$.next();
                method.addParameter(parm);
                topLevelClass.addImportedType(parm.getType());
            }
        }

        method.addBodyLine(body);
        topLevelClass.addMethod(method);
    }

    protected void createInnerMethod(JavaVisibility visi, String name, String annotation, FullyQualifiedJavaType returnType, List<Parameter> parms, String body, InnerClass innerClass, TopLevelClass outClass) {
        Method method = new Method(name);
        method.setVisibility(visi);
        if (StringUtils.isNotBlank(annotation)) {
            method.addAnnotation(annotation);
        }

        if (returnType != null) {
            method.setReturnType(returnType);
            outClass.addImportedType(returnType);
        }

        if (parms != null && !parms.isEmpty()) {
            Iterator i$ = parms.iterator();

            while(i$.hasNext()) {
                Parameter parm = (Parameter)i$.next();
                method.addParameter(parm);
                outClass.addImportedType(parm.getType());
            }
        }

        method.addBodyLine(body);
        innerClass.addMethod(method);
    }

    protected void createCriteriaMethod(String fieldName, String propertyName, FullyQualifiedJavaType type, InnerClass returnClazz, InnerClass innerClazz, TopLevelClass outClazz) {
        boolean hasLike = false;
        if (FullyQualifiedJavaType.getStringInstance().equals(type)) {
            hasLike = true;
        }

        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "IsNull", (String)null, returnClazz.getType(), (List)null, "addCriterion(\"" + fieldName + " is null\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "IsNotNull", (String)null, returnClazz.getType(), (List)null, "addCriterion(\"" + fieldName + " is not null\");\n            return (Criteria) this;", innerClazz, outClazz);
        List<Parameter> typeParms = new ArrayList();
        Parameter typeParm = new Parameter(type, "value");
        typeParms.add(typeParm);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "EqualTo", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " =\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "NotEqualTo", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " <>\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "GreaterThan", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " >\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "GreaterThanOrEqualTo", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " >=\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "LessThan", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " <\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "LessThanOrEqualTo", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " <=\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        List<Parameter> listParms = new ArrayList();
        Parameter listParm = new Parameter(new FullyQualifiedJavaType(List.class.getName() + "<" + type.getFullyQualifiedName() + ">"), "values");
        listParms.add(listParm);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "In", (String)null, returnClazz.getType(), listParms, "addCriterion(\"" + fieldName + " in\", values, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "NotIn", (String)null, returnClazz.getType(), listParms, "addCriterion(\"" + fieldName + " not in\", values, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        List<Parameter> twoParms = new ArrayList();
        Parameter oneParm = new Parameter(type, "value1");
        Parameter twoParm = new Parameter(type, "value2");
        twoParms.add(oneParm);
        twoParms.add(twoParm);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "Between", (String)null, returnClazz.getType(), twoParms, "addCriterion(\"" + fieldName + " between\", value1, value2, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "NotBetween", (String)null, returnClazz.getType(), twoParms, "addCriterion(\"" + fieldName + " not between\", value1, value2, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        if (hasLike) {
            this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "Like", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " like\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
            this.createInnerMethod(JavaVisibility.PUBLIC, "and" + propertyName + "NotLike", (String)null, returnClazz.getType(), typeParms, "addCriterion(\"" + fieldName + " not like\", value, \"" + propertyName + "\");\n            return (Criteria) this;", innerClazz, outClazz);
        }

    }

    protected void addBaseColumns(IntrospectedTable introspectedTable) {
        this.addColumn("ID", "INTEGER", "id", longType, true, true, introspectedTable);
        this.addColumn("GMT_CREATE", "TIMESTAMP", "gmtCreate", FullyQualifiedJavaType.getDateInstance(), false, false, introspectedTable);
        this.addColumn("CREATOR", "VARCHAR", "creator", FullyQualifiedJavaType.getStringInstance(), false, false, introspectedTable);
        this.addColumn("GMT_MODIFIED", "TIMESTAMP", "gmtModified", FullyQualifiedJavaType.getDateInstance(), false, false, introspectedTable);
        this.addColumn("MODIFIER", "VARCHAR", "modifier", FullyQualifiedJavaType.getStringInstance(), false, false, introspectedTable);
        this.addColumn("IS_DELETED", "CHAR", "isDeleted", FullyQualifiedJavaType.getStringInstance(), false, false, introspectedTable);
    }

    protected void addColumn(String name, String jdbcType, String javaName, FullyQualifiedJavaType javaType, boolean isSequence, boolean isKey, IntrospectedTable introspectedTable) {
        IntrospectedColumn column = new IntrospectedColumn();
        column.setActualColumnName(name);
        column.setJdbcTypeName(jdbcType);
        column.setJavaProperty(javaName);
        column.setSequenceColumn(isSequence);
        column.setFullyQualifiedJavaType(javaType);
        introspectedTable.addColumn(column);
        if (isKey) {
            introspectedTable.addPrimaryKeyColumn(name);
        }

    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String lavaExample = LavaExample.class.getName();
        List<InnerClass> innerClasses = topLevelClass.getInnerClasses();
        InnerClass innerClass = null;
        InnerClass returnClass = null;
        Iterator i$ = innerClasses.iterator();

        while(i$.hasNext()) {
            InnerClass ic = (InnerClass)i$.next();
            FullyQualifiedJavaType type = ic.getType();
            if ("GeneratedCriteria".equals(type.getShortName())) {
                innerClass = ic;
            } else if ("Criteria".equals(type.getShortName())) {
                returnClass = ic;
            }
        }

        this.createCriteriaMethod("ID", "Id", longType, returnClass, innerClass, topLevelClass);
        this.createCriteriaMethod("GMT_CREATE", "GmtCreate", FullyQualifiedJavaType.getDateInstance(), returnClass, innerClass, topLevelClass);
        this.createCriteriaMethod("CREATOR", "Creator", FullyQualifiedJavaType.getStringInstance(), returnClass, innerClass, topLevelClass);
        this.createCriteriaMethod("GMT_MODIFIED", "GmtModified", FullyQualifiedJavaType.getDateInstance(), returnClass, innerClass, topLevelClass);
        this.createCriteriaMethod("MODIFIER", "Modifier", FullyQualifiedJavaType.getStringInstance(), returnClass, innerClass, topLevelClass);
        this.createCriteriaMethod("IS_DELETED", "IsDeleted", FullyQualifiedJavaType.getStringInstance(), returnClass, innerClass, topLevelClass);
        topLevelClass.setSuperClass(lavaExample);
        topLevelClass.addImportedType(lavaExample);
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        this.updateDocumentNameSpace(introspectedTable, parentElement);
        this.moveDocumentInsertSql(parentElement);
        this.updateDocumentInsertSelective(parentElement);
        this.moveDocumentUpdateByPrimaryKeySql(parentElement);
        this.generateMysqlPageSql(parentElement, introspectedTable);
        this.generateDataAccessSql(parentElement);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private void generateMysqlPageSql(XmlElement parentElement, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getTableConfiguration().getTableName();
        XmlElement paginationPrefixElement = new XmlElement("sql");
        this.context.getCommentGenerator().addComment(paginationPrefixElement);
        paginationPrefixElement.addAttribute(new Attribute("id", "MysqlDialectPrefix"));
        XmlElement pageStart = new XmlElement("if");
        pageStart.addAttribute(new Attribute("test", "page != null"));
        pageStart.addElement(new TextElement("from " + tableName + " where id in ( select id from ( select id "));
        paginationPrefixElement.addElement(pageStart);
        parentElement.addElement(paginationPrefixElement);
        XmlElement paginationSuffixElement = new XmlElement("sql");
        this.context.getCommentGenerator().addComment(paginationSuffixElement);
        paginationSuffixElement.addAttribute(new Attribute("id", "MysqlDialectSuffix"));
        XmlElement pageEnd = new XmlElement("if");
        pageEnd.addAttribute(new Attribute("test", "page != null"));
        pageEnd.addElement(new TextElement("<![CDATA[ limit #{page.begin}, #{page.length} ) as temp_page_table) ]]>"));
        paginationSuffixElement.addElement(pageEnd);
        parentElement.addElement(paginationSuffixElement);
    }

    private void generateDataAccessSql(XmlElement parentElement) {
        XmlElement fullOrgPathElement = new XmlElement("sql");
        this.context.getCommentGenerator().addComment(fullOrgPathElement);
        fullOrgPathElement.addAttribute(new Attribute("id", "fullOrgPath"));
        XmlElement pageStart = new XmlElement("if");
        pageStart.addAttribute(new Attribute("test", "fullOrgPath != null"));
        pageStart.addElement(new TextElement(SQLMAP_COMMON_POTFIX_PVG));
        fullOrgPathElement.addElement(pageStart);
        parentElement.addElement(fullOrgPathElement);
        XmlElement ownerElement = new XmlElement("sql");
        this.context.getCommentGenerator().addComment(ownerElement);
        ownerElement.addAttribute(new Attribute("id", "owner"));
        XmlElement pageEnd = new XmlElement("if");
        pageEnd.addAttribute(new Attribute("test", "owner != null"));
        pageEnd.addElement(new TextElement(SQLMAP_COMMON_POTFIX_OWNER));
        ownerElement.addElement(pageEnd);
        parentElement.addElement(ownerElement);
    }

    private void moveDocumentUpdateByPrimaryKeySql(XmlElement parentElement) {
        XmlElement updateElement = null;
        Iterator i$ = parentElement.getElements().iterator();

        while(true) {
            while(true) {
                XmlElement xmlElement;
                do {
                    if (!i$.hasNext()) {
                        parentElement.getElements().remove(updateElement);
                        return;
                    }

                    Element element = (Element)i$.next();
                    xmlElement = (XmlElement)element;
                } while(!xmlElement.getName().equals("update"));

                Iterator a$ = xmlElement.getAttributes().iterator();

                while(a$.hasNext()) {
                    Attribute attribute = (Attribute)a$.next();
                    if (attribute.getValue().equals("updateByPrimaryKey")) {
                        updateElement = xmlElement;
                        break;
                    }
                }
            }
        }
    }

    private void updateDocumentInsertSelective(XmlElement parentElement) {
        XmlElement oldElement = null;
        XmlElement newElement = null;
        Iterator i$ = parentElement.getElements().iterator();

        while(true) {
            while(true) {
                XmlElement xmlElement;
                do {
                    if (!i$.hasNext()) {
                        parentElement.getElements().remove(oldElement);
                        parentElement.getElements().add(newElement);
                        return;
                    }

                    Element element = (Element)i$.next();
                    xmlElement = (XmlElement)element;
                } while(!xmlElement.getName().equals("insert"));

                Iterator a$ = xmlElement.getAttributes().iterator();

                while(a$.hasNext()) {
                    Attribute attribute = (Attribute)a$.next();
                    if (attribute.getValue().equals("insertSelective")) {
                        oldElement = xmlElement;
                        newElement = xmlElement;
                        xmlElement.addAttribute(new Attribute("useGeneratedKeys", "true"));
                        xmlElement.addAttribute(new Attribute("keyProperty", "id"));
                        break;
                    }
                }
            }
        }
    }

    private void moveDocumentInsertSql(XmlElement parentElement) {
        XmlElement insertElement = null;
        Iterator i$ = parentElement.getElements().iterator();

        while(true) {
            while(true) {
                XmlElement xmlElement;
                do {
                    if (!i$.hasNext()) {
                        parentElement.getElements().remove(insertElement);
                        return;
                    }

                    Element element = (Element)i$.next();
                    xmlElement = (XmlElement)element;
                } while(!xmlElement.getName().equals("insert"));

                Iterator a$ = xmlElement.getAttributes().iterator();

                while(a$.hasNext()) {
                    Attribute attribute = (Attribute)a$.next();
                    if (attribute.getValue().equals("insert")) {
                        insertElement = xmlElement;
                        break;
                    }
                }
            }
        }
    }

    private void updateDocumentNameSpace(IntrospectedTable introspectedTable, XmlElement parentElement) {
        Attribute namespaceAttribute = null;
        Iterator i$ = parentElement.getAttributes().iterator();

        while(i$.hasNext()) {
            Attribute attribute = (Attribute)i$.next();
            if (attribute.getName().equals("namespace")) {
                namespaceAttribute = attribute;
            }
        }

        parentElement.getAttributes().remove(namespaceAttribute);
        parentElement.getAttributes().add(new Attribute("namespace", introspectedTable.getMyBatis3JavaMapperType() + JAVAFILE_POTFIX));
    }
    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        TextElement text = new TextElement(SQLMAP_COMMON_POTFIX);
        element.addElement(text);
        return super.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
    }
    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elements = element.getElements();
        XmlElement setItem = null;
        int modifierItemIndex = -1;
        int gmtModifiedItemIndex = -1;
        Iterator i$ = elements.iterator();

        while(true) {
            Element e;
            do {
                if (!i$.hasNext()) {
                    if (modifierItemIndex != -1 && setItem != null) {
                        this.addXmlElementModifier(setItem, modifierItemIndex);
                    }

                    if (gmtModifiedItemIndex != -1 && setItem != null) {
                        this.addGmtModifiedXmlElement(setItem, gmtModifiedItemIndex);
                    }

                    TextElement text = new TextElement(SQLMAP_COMMON_POTFIX);
                    element.addElement(text);
                    return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
                }

                e = (Element)i$.next();
            } while(!(e instanceof XmlElement));

            setItem = (XmlElement)e;

            for(int i = 0; i < setItem.getElements().size(); ++i) {
                XmlElement xmlElement = (XmlElement)setItem.getElements().get(i);
                Iterator a$ = xmlElement.getAttributes().iterator();

                while(a$.hasNext()) {
                    Attribute att = (Attribute)a$.next();
                    if (att.getValue().equals("modifier != null")) {
                        modifierItemIndex = i;
                        break;
                    }

                    if (att.getValue().equals("gmtModified != null")) {
                        gmtModifiedItemIndex = i;
                        break;
                    }
                }
            }
        }
    }

    private void addGmtModifiedXmlElement(XmlElement setItem, int gmtModifiedItemIndex) {
        XmlElement defaultGmtModified = new XmlElement("if");
        defaultGmtModified.addAttribute(new Attribute("test", "gmtModified == null"));
        defaultGmtModified.addElement(new TextElement("GMT_MODIFIED = current_timestamp,"));
        setItem.getElements().add(gmtModifiedItemIndex + 1, defaultGmtModified);
    }

    private void addXmlElementModifier(XmlElement setItem, int modifierItemIndex) {
        XmlElement defaultmodifier = new XmlElement("if");
        defaultmodifier.addAttribute(new Attribute("test", "modifier == null"));
        defaultmodifier.addElement(new TextElement("MODIFIER = 'system',"));
        setItem.getElements().add(modifierItemIndex + 1, defaultmodifier);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        TextElement text = new TextElement(SQLMAP_COMMON_POTFIX);
        element.addElement(text);
        return super.sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        element.setName("update");
        int replaceInd = -1;

        for(int i = 0; i < element.getAttributes().size(); ++i) {
            Attribute attr = (Attribute)element.getAttributes().get(i);
            if (attr.getName().equals("parameterType")) {
                replaceInd = i;
                break;
            }
        }

        if (replaceInd >= 0) {
            element.getAttributes().remove(replaceInd);
            element.getAttributes().add(replaceInd, new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        }

        List<Element> removeElement = new ArrayList();

        for(int i = 5; i < element.getElements().size(); ++i) {
            removeElement.add(element.getElements().get(i));
        }

        element.getElements().removeAll(removeElement);
        element.getElements().add(new TextElement("update " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + " set is_deleted = 'y',modifier=#{modifier,jdbcType=VARCHAR},gmt_Modified=current_timestamp where ID = #{id,jdbcType=BIGINT}"));
        return super.sqlMapDeleteByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elements = element.getElements();
        XmlElement fieldItem = null;
        XmlElement valueItem = null;
        Iterator i$ = elements.iterator();

        while(true) {
            while(true) {
                XmlElement xmlElement;
                do {
                    Element e;
                    do {
                        if (!i$.hasNext()) {
                            XmlElement defaultGmtCreate;
                            XmlElement defaultGmtModified;
                            XmlElement defaultCreator;
                            XmlElement defaultIsDeleted;
                            if (fieldItem != null) {
                                defaultGmtCreate = new XmlElement("if");
                                defaultGmtCreate.addAttribute(new Attribute("test", "gmtCreate == null"));
                                defaultGmtCreate.addElement(new TextElement("GMT_CREATE,"));
                                fieldItem.addElement(1, defaultGmtCreate);
                                defaultGmtModified = new XmlElement("if");
                                defaultGmtModified.addAttribute(new Attribute("test", "gmtModified == null"));
                                defaultGmtModified.addElement(new TextElement("GMT_MODIFIED,"));
                                fieldItem.addElement(1, defaultGmtModified);
                                xmlElement = new XmlElement("if");
                                xmlElement.addAttribute(new Attribute("test", "modifier == null"));
                                xmlElement.addElement(new TextElement("MODIFIER,"));
                                fieldItem.addElement(1, xmlElement);
                                defaultCreator = new XmlElement("if");
                                defaultCreator.addAttribute(new Attribute("test", "creator == null"));
                                defaultCreator.addElement(new TextElement("CREATOR,"));
                                fieldItem.addElement(1, defaultCreator);
                                defaultIsDeleted = new XmlElement("if");
                                defaultIsDeleted.addAttribute(new Attribute("test", "isDeleted == null"));
                                defaultIsDeleted.addElement(new TextElement("IS_DELETED,"));
                                fieldItem.addElement(1, defaultIsDeleted);
                            }

                            if (valueItem != null) {
                                defaultGmtCreate = new XmlElement("if");
                                defaultGmtCreate.addAttribute(new Attribute("test", "gmtCreate == null"));
                                defaultGmtCreate.addElement(new TextElement("current_timestamp,"));
                                valueItem.addElement(1, defaultGmtCreate);
                                defaultGmtModified = new XmlElement("if");
                                defaultGmtModified.addAttribute(new Attribute("test", "gmtModified == null"));
                                defaultGmtModified.addElement(new TextElement("current_timestamp,"));
                                valueItem.addElement(1, defaultGmtModified);
                                xmlElement = new XmlElement("if");
                                xmlElement.addAttribute(new Attribute("test", "modifier == null"));
                                xmlElement.addElement(new TextElement("'system',"));
                                valueItem.addElement(1, xmlElement);
                                defaultCreator = new XmlElement("if");
                                defaultCreator.addAttribute(new Attribute("test", "creator == null"));
                                defaultCreator.addElement(new TextElement("'system',"));
                                valueItem.addElement(1, defaultCreator);
                                defaultIsDeleted = new XmlElement("if");
                                defaultIsDeleted.addAttribute(new Attribute("test", "isDeleted == null"));
                                defaultIsDeleted.addElement(new TextElement("'n',"));
                                valueItem.addElement(1, defaultIsDeleted);
                            }

                            return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
                        }

                        e = (Element)i$.next();
                    } while(!(e instanceof XmlElement));

                    xmlElement = (XmlElement)e;
                } while(!xmlElement.getName().equals("trim"));

                Iterator a$ = xmlElement.getAttributes().iterator();

                while(a$.hasNext()) {
                    Attribute arr = (Attribute)a$.next();
                    if (arr.getValue().equals("(")) {
                        fieldItem = xmlElement;
                        break;
                    }

                    if (arr.getValue().equals("values (")) {
                        valueItem = xmlElement;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        Parameter parameter = new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record");
        method.getParameters().clear();
        method.addParameter(parameter);
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement lastXmlE = (XmlElement)element.getElements().remove(element.getElements().size() - 1);
        XmlElement pageStart = new XmlElement("include");
        pageStart.addAttribute(new Attribute("refid", "MysqlDialectPrefix"));
        element.getElements().add(3, pageStart);
        XmlElement isdeletedElement = new XmlElement("if");
        isdeletedElement.addAttribute(new Attribute("test", "oredCriteria.size != 0"));
        XmlElement isdeletedChooseElement = new XmlElement("choose");
        XmlElement isdeletedWhenElement = new XmlElement("when");
        isdeletedWhenElement.addAttribute(new Attribute("test", "oredCriteria.size == 1 and !oredCriteria[0].valid"));
        isdeletedWhenElement.addElement(new TextElement("where is_deleted = 'n'"));
        isdeletedChooseElement.addElement(isdeletedWhenElement);
        XmlElement isdeletedOtherwiseElement = new XmlElement("otherwise");
        isdeletedOtherwiseElement.addElement(new TextElement(SQLMAP_COMMON_POTFIX));
        isdeletedChooseElement.addElement(isdeletedOtherwiseElement);
        isdeletedElement.addElement(isdeletedChooseElement);
        element.addElement(isdeletedElement);
        isdeletedElement = new XmlElement("if");
        isdeletedElement.addAttribute(new Attribute("test", "oredCriteria.size == 0"));
        isdeletedElement.addElement(new TextElement("where is_deleted = 'n'"));
        element.addElement(isdeletedElement);
        XmlElement fullOrgPath = new XmlElement("include");
        fullOrgPath.addAttribute(new Attribute("refid", "fullOrgPath"));
        element.addElement(fullOrgPath);
        XmlElement owner = new XmlElement("include");
        owner.addAttribute(new Attribute("refid", "owner"));
        element.addElement(owner);
        element.addElement(lastXmlE);
        XmlElement isNotNullElement = new XmlElement("include");
        isNotNullElement.addAttribute(new Attribute("refid", "MysqlDialectSuffix"));
        element.getElements().add(isNotNullElement);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }
    @Override
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement isNotNullElement = new XmlElement("if");
        isNotNullElement.addAttribute(new Attribute("test", "oredCriteria.size != 0"));
        XmlElement isdeletedChooseElement = new XmlElement("choose");
        XmlElement isdeletedWhenElement = new XmlElement("when");
        isdeletedWhenElement.addAttribute(new Attribute("test", "oredCriteria.size == 1 and !oredCriteria[0].valid"));
        isdeletedWhenElement.addElement(new TextElement("where is_deleted = 'n'"));
        isdeletedChooseElement.addElement(isdeletedWhenElement);
        XmlElement isdeletedOtherwiseElement = new XmlElement("otherwise");
        isdeletedOtherwiseElement.addElement(new TextElement(SQLMAP_COMMON_POTFIX));
        isdeletedChooseElement.addElement(isdeletedOtherwiseElement);
        isNotNullElement.addElement(isdeletedChooseElement);
        element.addElement(isNotNullElement);
        isNotNullElement = new XmlElement("if");
        isNotNullElement.addAttribute(new Attribute("test", "oredCriteria.size == 0"));
        isNotNullElement.addElement(new TextElement("where is_deleted = 'n'"));
        element.addElement(isNotNullElement);
        XmlElement fullOrgPath = new XmlElement("include");
        fullOrgPath.addAttribute(new Attribute("refid", "fullOrgPath"));
        element.addElement(fullOrgPath);
        XmlElement owner = new XmlElement("include");
        owner.addAttribute(new Attribute("refid", "owner"));
        element.addElement(owner);
        return super.sqlMapCountByExampleElementGenerated(element, introspectedTable);
    }
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        String[] splitFile = introspectedTable.getMyBatis3XmlMapperFileName().split("\\.");
        String fileNameExt = null;
        if (splitFile[0] != null) {
            fileNameExt = splitFile[0] + XMLFILE_POSTFIX + ".xml";
        }

        if (this.isExistExtFile(this.context.getSqlMapGeneratorConfiguration().getTargetProject(), introspectedTable.getMyBatis3XmlMapperPackage(), fileNameExt)) {
            return super.contextGenerateAdditionalXmlFiles(introspectedTable);
        } else {
            Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
            XmlElement root = new XmlElement("mapper");
            document.setRootElement(root);
            String namespace = introspectedTable.getMyBatis3SqlMapNamespace() + XMLFILE_POSTFIX;
            root.addAttribute(new Attribute("namespace", namespace));
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, fileNameExt, introspectedTable.getMyBatis3XmlMapperPackage(), this.context.getSqlMapGeneratorConfiguration().getTargetProject(), false, this.context.getXmlFormatter());
            List<GeneratedXmlFile> answer = new ArrayList(1);
            answer.add(gxf);
            return answer;
        }
    }
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType() + JAVAFILE_POTFIX);
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        this.context.getCommentGenerator().addJavaFileComment(interfaze);
        FullyQualifiedJavaType annotation = new FullyQualifiedJavaType(ANNOTATION_RESOURCE);
        interfaze.addAnnotation("@Resource");
        interfaze.addImportedType(annotation);
        String doName = introspectedTable.getBaseRecordType();
        String exmpName = introspectedTable.getExampleType();
        FullyQualifiedJavaType lavaMapperType = new FullyQualifiedJavaType(LavaMapper.class.getName() + "<" + doName + "," + exmpName + ">");
        interfaze.addSuperInterface(lavaMapperType);
        interfaze.addImportedType(lavaMapperType);
        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(interfaze, this.context.getJavaModelGeneratorConfiguration().getTargetProject(), this.context.getProperty("javaFileEncoding"), this.context.getJavaFormatter());
        if (this.isExistExtFile(generatedJavaFile.getTargetProject(), generatedJavaFile.getTargetPackage(), generatedJavaFile.getFileName())) {
            return super.contextGenerateAdditionalJavaFiles(introspectedTable);
        } else {
            List<GeneratedJavaFile> generatedJavaFiles = new ArrayList(1);
            generatedJavaFile.getFileName();
            generatedJavaFiles.add(generatedJavaFile);
            return generatedJavaFiles;
        }
    }

    private boolean isExistExtFile(String targetProject, String targetPackage, String fileName) {
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            return true;
        } else {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(targetPackage, ".");

            while(st.hasMoreTokens()) {
                sb.append(st.nextToken());
                sb.append(File.separatorChar);
            }

            File directory = new File(project, sb.toString());
            if (!directory.isDirectory()) {
                boolean rc = directory.mkdirs();
                if (!rc) {
                    return true;
                }
            }

            File testFile = new File(directory, fileName);
            return testFile.exists();
        }
    }
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    public static void main(String[] args) {
        String config = MySqlPaginationPlugin.class.getClassLoader().getResource("generatorConfig.xml").getFile();
        String[] arg = new String[]{"-configfile", config};
        ShellRunner.main(arg);
    }
}

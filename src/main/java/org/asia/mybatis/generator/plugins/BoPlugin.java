package org.asia.mybatis.generator.plugins;

/**
 * @Author: csz
 * @Date: 2018/10/12 13:49
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.beans.AbstractLavaBoImpl;
import org.beans.DataResult;
import org.beans.LavaBo;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.springframework.beans.factory.annotation.Autowired;

public class BoPlugin extends PluginAdapter {
    private static FullyQualifiedJavaType longType = new FullyQualifiedJavaType("java.lang.Long");
    public static String JAVAFILE_POTFIX = "Bo";
    public static String JAVAFILE_IMPL_POTFIX = "Impl";

    public BoPlugin() {
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        String bathPath = this.properties.getProperty("basePath");
        String packagePath = introspectedTable.getTableConfiguration().getProperties().getProperty("boPackage");
        String doName = introspectedTable.getBaseRecordType();
        String exmpName = introspectedTable.getExampleType();
        String mapperName = introspectedTable.getMyBatis3JavaMapperType() + "Ext";
        String boName = doName.substring(doName.lastIndexOf(".") + 1).replace("Do", "") + JAVAFILE_POTFIX;
        FullyQualifiedJavaType boType = new FullyQualifiedJavaType(packagePath + "." + boName);
        Interface boInterfaze = new Interface(boType);
        boInterfaze.setVisibility(JavaVisibility.PUBLIC);
        this.context.getCommentGenerator().addJavaFileComment(boInterfaze);
        FullyQualifiedJavaType supperInterface = new FullyQualifiedJavaType(LavaBo.class.getName() + "<" + doName + "," + exmpName + ">");
        boInterfaze.addImportedType(supperInterface);
        boInterfaze.addSuperInterface(supperInterface);
        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(boInterfaze, bathPath, this.context.getProperty("javaFileEncoding"), this.context.getJavaFormatter());
        if (this.isExistExtFile(bathPath, generatedJavaFile.getTargetPackage(), generatedJavaFile.getFileName())) {
            return super.contextGenerateAdditionalJavaFiles(introspectedTable);
        } else {
            List<GeneratedJavaFile> generatedJavaFiles = new ArrayList(2);
            generatedJavaFiles.add(generatedJavaFile);
            FullyQualifiedJavaType implType = new FullyQualifiedJavaType(packagePath + ".impl." + boName + JAVAFILE_IMPL_POTFIX);
            TopLevelClass clazz = new TopLevelClass(implType);
            FullyQualifiedJavaType supperType = new FullyQualifiedJavaType(AbstractLavaBoImpl.class.getName() + "<" + doName + "," + mapperName + "," + exmpName + ">");
            clazz.addImportedType(supperType);
            clazz.setSuperClass(supperType);
            clazz.addImportedType(boType);
            clazz.addSuperInterface(boType);
            clazz.setVisibility(JavaVisibility.PUBLIC);
            this.createMethod("setBaseMapper", (FullyQualifiedJavaType)null, new FullyQualifiedJavaType(mapperName), "mapper", "@Autowired", "setMapper(mapper);", clazz);
            GeneratedJavaFile implFile = new GeneratedJavaFile(clazz, bathPath, this.context.getProperty("javaFileEncoding"), this.context.getJavaFormatter());
            generatedJavaFiles.add(implFile);
            return generatedJavaFiles;
        }
    }

    protected void addBaseMethods(String doName, String exmpName, Interface interfaze) {
        FullyQualifiedJavaType intType = FullyQualifiedJavaType.getIntInstance();
        FullyQualifiedJavaType doType = new FullyQualifiedJavaType(doName);
        FullyQualifiedJavaType examType = new FullyQualifiedJavaType(exmpName);
        this.createMethod("insert", intType, doType, "dataObject", interfaze);
        this.createMethod("delete", intType, longType, "id", interfaze);
        this.createMethod("selectByExample", new FullyQualifiedJavaType("java.util.List<" + doName + ">"), examType, "example", interfaze);
        this.createMethod("selectByPrimaryKey", doType, longType, "id", interfaze);
        this.createMethod("update", intType, doType, "dataObject", interfaze);
        this.createMethod("getPageByExample", new FullyQualifiedJavaType(DataResult.class.getName() + "<" + doName + ">"), examType, "example", interfaze);
        this.createMethod("isValidDo", FullyQualifiedJavaType.getBooleanPrimitiveInstance(), doType, "dataObject", interfaze);
    }

    private void createMethod(String name, FullyQualifiedJavaType rsType, FullyQualifiedJavaType parmType, String parmName, Interface interfaze) {
        Method method = new Method(name);
        method.setVisibility(JavaVisibility.PUBLIC);
        if (rsType != null) {
            method.setReturnType(rsType);
            interfaze.addImportedType(rsType);
        }

        if (parmType != null) {
            Parameter param = new Parameter(parmType, parmName);
            method.addParameter(param);
            interfaze.addImportedType(parmType);
        }

        interfaze.addMethod(method);
    }

    private void createMethod(String name, FullyQualifiedJavaType rsType, FullyQualifiedJavaType parmType, String parmName, String annotation, String body, TopLevelClass clazz) {
        Method method = new Method(name);
        method.setVisibility(JavaVisibility.PUBLIC);
        if (rsType != null) {
            method.setReturnType(rsType);
            clazz.addImportedType(rsType);
        }

        if (parmType != null) {
            Parameter param = new Parameter(parmType, parmName);
            method.addParameter(param);
            clazz.addImportedType(parmType);
        }

        method.addAnnotation(annotation);
        clazz.addImportedType(Autowired.class.getName());
        method.addBodyLine(body);
        clazz.addMethod(method);
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
}


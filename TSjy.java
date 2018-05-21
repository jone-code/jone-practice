package com.thunisoft.zipper.pojo;

public class TSjy {
    private String cBh;
    private String cName;
    private String cUrl;
    private String cSchema;
    private String cUserName;
    private String cPassword;

    public TSjy() {
    }

    public TSjy(String cName, String cUrl, String cSchema, String cUserName, String cPassword) {
        this.cName = cName;
        this.cUrl = cUrl;
        this.cSchema = cSchema;
        this.cUserName = cUserName;
        this.cPassword = cPassword;
    }

    public TSjy(String cBh, String cName, String cUrl, String cSchema, String cUserName, String cPassword) {
        this.cBh = cBh;
        this.cName = cName;
        this.cUrl = cUrl;
        this.cSchema = cSchema;
        this.cUserName = cUserName;
        this.cPassword = cPassword;
    }

    public String getcBh() {
        return cBh;
    }

    public void setcBh(String cBh) {
        this.cBh = cBh;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcUrl() {
        return cUrl;
    }

    public void setcUrl(String cUrl) {
        this.cUrl = cUrl;
    }

    public String getcSchema() {
        return cSchema;
    }

    public void setcSchema(String cSchema) {
        this.cSchema = cSchema;
    }

    public String getcUserName() {
        return cUserName;
    }

    public void setcUserName(String cUserName) {
        this.cUserName = cUserName;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }
}

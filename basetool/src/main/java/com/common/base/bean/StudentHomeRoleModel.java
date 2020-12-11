package com.common.base.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentHomeRoleModel extends BaseModel {
    private ArrayList<CommonListModelA> data;
    public ArrayList<CommonListModelA> getData() {
        return data;
    }

    public void setData(ArrayList<CommonListModelA> data) {
        this.data = data;
    }

    /**
     * 一级模块，如首页，规培等
     */
    public static class CommonListModelA implements Serializable {
        String name;
        String icon;
        String page;
        ArrayList<CommonListModelB> child = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public ArrayList<CommonListModelB> getChild() {
            return child;
        }

        public void setChild(ArrayList<CommonListModelB> child) {
            this.child = child;
        }
    }

    /**
     * 二级模块，如首页-》总览模块等
     */
    public static class CommonListModelB implements Serializable {
        String name;
        String icon;
        String page;
        ArrayList<CommonListModelC> child = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public ArrayList<CommonListModelC> getChild() {
            return child;
        }

        public void setChild(ArrayList<CommonListModelC> child) {
            this.child = child;
        }
    }

    /**
     * 三级模块，如首页-》数据总览模块——》规培数据，本科数据等
     */
    public static class CommonListModelC implements Serializable {
        String name;
        String icon;
        String page;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }
    }
}

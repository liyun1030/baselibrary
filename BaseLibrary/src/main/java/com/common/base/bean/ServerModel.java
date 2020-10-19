/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.base.bean;

import java.io.Serializable;

public class ServerModel implements Serializable {
    private static final long serialVersionUID = -828322761336296999L;

    public String method;
    public String ip;
    public String url;
    public String des;
    public String upload;
    public Author author;

    public class Author implements Serializable {
        private static final long serialVersionUID = 2701611773813762723L;

        public String name;
        public String fullname;
        public String github;
        public String address;
        public String qq;
        public String email;
        public String des;

        @Override
        public String toString() {
            return "Author{\n" +//
                   "\tname='" + name + "\'\n" +//
                   "\tfullname='" + fullname + "\'\n" +//
                   "\tgithub='" + github + "\'\n" +//
                   "\taddress='" + address + "\'\n" +//
                   "\tqq='" + qq + "\'\n" +//
                   "\temail='" + email + "\'\n" +//
                   "\tdes='" + des + "\'\n" +//
                   '}';
        }
    }

    @Override
    public String toString() {
        return "ServerModel{\n" +//
               "\tmethod='" + method + "\'\n" +//
               "\tip='" + ip + "\'\n" +//
               "\turl='" + url + "\'\n" +//
               "\tdes='" + des + "\'\n" +//
               "\tupload='" + upload + "\'\n" +//
               "\tauthor=" + author + "\n" +//
               '}';
    }
}

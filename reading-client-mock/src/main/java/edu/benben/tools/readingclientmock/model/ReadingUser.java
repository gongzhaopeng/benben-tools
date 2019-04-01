package edu.benben.tools.readingclientmock.model;

import lombok.Data;

@Data
public class ReadingUser {

    private String id;
    private String openid;
    private String name;
    private String only;
    private String grade;
    private String sex;
    private String schoolName;
    private String status;
    private boolean report;
    private String birthday;
    private String joinYear;
    private String cls;
    private String tags;
    private Region region;

    @Data
    public static class Region {

        private String province;
        private String city;
        private String district;

        public String concat() {

            return String.format(
                    "%s %s %s",
                    province,
                    city,
                    district
            );
        }
    }
}

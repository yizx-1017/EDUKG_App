package com.example.gkude.bean;


public enum CourseType {
    CHINESE("chinese"),
    English("english"),
    MATH("math"),
    PHYSICS("physics"),
    CHEMISTRY("chemistry"),
    BIOLOGY("biology"),
    HISTORY("history"),
    GEO("geo"),
    POLITICS("politics");

    private final String courseType;
    CourseType(String courseType){
        this.courseType = courseType;
    }

    public String getCourseType() {
        return courseType;
    }
}

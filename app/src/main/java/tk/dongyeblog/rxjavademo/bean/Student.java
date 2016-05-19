package tk.dongyeblog.rxjavademo.bean;

import java.util.List;

/**
 * description: RxJava demo bean
 * author： dongyeforever@gmail.com
 * date: 2016-01-21 15:25
 */
public class Student {
    private String name;
    private List<Course> courses;
    private String id;
    private String avatar_url;
    private String type;
    private String contributions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContributions() {
        return contributions;
    }

    public void setContributions(String contributions) {
        this.contributions = contributions;
    }

    public Student() {
    }

    public Student(String name, List<Course> list) {
        this.name = name;
        this.courses = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public class Course {
        private String name;

        public Course() {
        }

        public Course(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "[name:" + getName() + "]";
        }
    }
}

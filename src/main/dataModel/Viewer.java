package main.dataModel;

public class Viewer {
    boolean subscribed;
    String age;
    String gender;
    String[] interests;

    public Viewer(boolean subscribed, String age, String gender, String[] interests) {
        this.subscribed = subscribed;
        this.age = age;
        this.gender = gender;
        this.interests = interests;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public String getAge() {
        return age;
    }

    public String[] getInterests() {
        return interests;
    }
}

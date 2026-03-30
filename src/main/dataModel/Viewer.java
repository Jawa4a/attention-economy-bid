package main.dataModel;

public class Viewer {
    private final boolean subscribed;
    private final String age;
    private final String gender;
    private final String[] interests;

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

    public String getGender() {
        return gender;
    }
}

package main.dataModel;

public class Viewer {
   private final boolean subscribed;
   private final String age;
   private final String gender;
   private final String[] interests;

   public Viewer(boolean var1, String var2, String var3, String[] var4) {
      this.subscribed = var1;
      this.age = var2;
      this.gender = var3;
      this.interests = var4;
   }

   public boolean isSubscribed() {
      return this.subscribed;
   }

   public String getAge() {
      return this.age;
   }

   public String[] getInterests() {
      return this.interests;
   }

   public String getGender() {
      return this.gender;
   }
}

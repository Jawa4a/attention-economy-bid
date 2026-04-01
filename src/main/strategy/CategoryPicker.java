package main.strategy;

import java.util.List;
import java.util.Random;

public final class CategoryPicker {
   public static final List<String> CATEGORIES = List.of("Music", "Sports", "Kids", "DIY", "Video Games", "ASMR", "Beauty", "Cooking", "Finance");
   private final String preferredCategory;

   public CategoryPicker() {
      this.preferredCategory = (String)CATEGORIES.get((new Random()).nextInt(CATEGORIES.size()));
   }

   public CategoryPicker(String var1) {
      if (var1 != null && !var1.isBlank() && CATEGORIES.contains(var1)) {
         this.preferredCategory = var1;
      } else {
         this.preferredCategory = (String)CATEGORIES.get((new Random()).nextInt(CATEGORIES.size()));
      }

   }

   public String chooseCategory() {
      return this.preferredCategory;
   }
}

package main.strategy;

import java.util.List;
import java.util.Random;

public final class CategoryPicker {

    public static final List<String> CATEGORIES = List.of(
            "Music", "Sports", "Kids", "DIY", "Video Games",
            "ASMR", "Beauty", "Cooking", "Finance"
    );

    private final String preferredCategory;

    public CategoryPicker() {
        this.preferredCategory = CATEGORIES.get(new Random().nextInt(CATEGORIES.size()));
    }

    public CategoryPicker(String preferredCategory) {
        if (preferredCategory != null && !preferredCategory.isBlank() && CATEGORIES.contains(preferredCategory)) {
            this.preferredCategory = preferredCategory;
        } else {
            this.preferredCategory = CATEGORIES.get(new Random().nextInt(CATEGORIES.size()));
        }
    }

    public String chooseCategory() {
        return preferredCategory;
    }
}
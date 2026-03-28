package main.strategy;

import java.util.List;

public final class CategoryPicker {

    public static final List<String> CATEGORIES = List.of(
            "Music", "Sports", "Kids", "DIY", "Video Games", "ASMR", "Beauty", "Cooking", "Finance"
    );

    private final String preferredCategory;

    public CategoryPicker() {
        this("ASMR");
    }

    public CategoryPicker(String preferredCategory) {
        if (preferredCategory == null || preferredCategory.isBlank()) {
            this.preferredCategory = "ASMR";
        } else if (CATEGORIES.contains(preferredCategory)) {
            this.preferredCategory = preferredCategory;
        } else {
            this.preferredCategory = "ASMR";
        }
    }

    public String chooseCategory() {
        return preferredCategory;
    }
}
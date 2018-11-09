package be.pxl.project.cookaid;

public class LikedRecipe {

    String level;
    String name;
    String recipe;
    String category;
    String uri;

    public LikedRecipe() {
    }

    public LikedRecipe(String level, String name, String recipe, String category, String uri) {
        this.level = level;
        this.name = name;
        this.recipe = recipe;
        this.category = category;
        this.uri = uri;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

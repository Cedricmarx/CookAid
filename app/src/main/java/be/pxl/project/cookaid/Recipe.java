package be.pxl.project.cookaid;

public class Recipe {
    private String id;
    private String name;
    private String category;
    private String level;
    private String recipe;
    private String uri;

    public Recipe() {
    }

    public Recipe(String id, String name, String category, String level, String recipe, String uri) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.level = level;
        this.recipe = recipe;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package be.pxl.project.cookaid;

public class Recipe {
    private String name;
    private String category;
    private String level;
    private String recipe;
    private byte[] photo;

    public Recipe() {
        // Default constructor
    }

    public Recipe(String name, String category, String level, String recipe, byte[] photo) {
        this.name = name;
        this.category = category;
        this.level = level;
        this.recipe = recipe;
        this.photo = photo;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}

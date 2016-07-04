package cc.lijingbo.zhihudemo.bean;

import java.util.List;

public class ThemesBean {
  private int limit;
  private List<Theme> others;

  public List<Theme> getOthers() {
    return others;
  }

  public void setOthers(List<Theme> others) {
    this.others = others;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public static class Theme {
    private int id;
    private String name;
    private String description;
    private String thumbnail;
    private int color;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getThumbnail() {
      return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
      this.thumbnail = thumbnail;
    }

    public int getColor() {
      return color;
    }

    public void setColor(int color) {
      this.color = color;
    }
  }
}

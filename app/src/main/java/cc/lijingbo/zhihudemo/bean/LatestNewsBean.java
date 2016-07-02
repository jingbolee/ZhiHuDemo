package cc.lijingbo.zhihudemo.bean;

import java.util.List;

public class LatestNewsBean {

  private int date;
  private List<StoryBean> stories;

  public List<StoryBean> getStories() {
    return stories;
  }

  public void setStories(List<StoryBean> stories) {
    this.stories = stories;
  }

  public int getDate() {
    return date;
  }

  public void setDate(int date) {
    this.date = date;
  }

  public static class StoryBean {
    private int id;
    private String title;
    private String[] images;
    private boolean multipic;

    public String[] getImages() {
      return images;
    }

    public void setImages(String[] images) {
      this.images = images;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public boolean isMultipic() {
      return multipic;
    }

    public void setMultipic(boolean multipic) {
      this.multipic = multipic;
    }
  }
}

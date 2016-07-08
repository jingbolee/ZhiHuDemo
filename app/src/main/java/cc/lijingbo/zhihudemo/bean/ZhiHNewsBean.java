package cc.lijingbo.zhihudemo.bean;

import java.util.List;

public class ZhiHNewsBean {

  private int date;
  private List<StoryBean> stories;
  private List<TopStoryBean> top_stories;

  public List<TopStoryBean> getTop_stories() {
    return top_stories;
  }

  public void setTop_stories(List<TopStoryBean> top_stories) {
    this.top_stories = top_stories;
  }

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

  public static class TopStoryBean{
    private int id;
    private String title;
    private String image;

    public String getImage() {
      return image;
    }

    public void setImage(String image) {
      this.image = image;
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

  }


}

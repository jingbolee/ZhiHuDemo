package cc.lijingbo.zhihudemo.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class LatestNewsBean implements Parcelable {

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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.date);
    dest.writeList(this.stories);
  }

  public LatestNewsBean() {
  }

  protected LatestNewsBean(Parcel in) {
    this.date = in.readInt();
    this.stories = new ArrayList<StoryBean>();
    in.readList(this.stories, StoryBean.class.getClassLoader());
  }

  public static final Parcelable.Creator<LatestNewsBean> CREATOR =
      new Parcelable.Creator<LatestNewsBean>() {
        @Override public LatestNewsBean createFromParcel(Parcel source) {
          return new LatestNewsBean(source);
        }

        @Override public LatestNewsBean[] newArray(int size) {
          return new LatestNewsBean[size];
        }
      };
}

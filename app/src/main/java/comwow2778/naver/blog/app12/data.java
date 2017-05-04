package comwow2778.naver.blog.app12;

/**
 * Created by seon on 2017-05-04.
 */

public class data {
    private String name;
    private String url;
    public data(String name, String url){
        this.name = name;
        this.url = url;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

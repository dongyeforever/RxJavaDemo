// (c)2016 Flipboard Inc, All Rights Reserved.

package tk.dongyeblog.rxjavademo.bean;

public class FakeToken {
    public String token;
    public boolean expired;

    public FakeToken() {
    }

    public FakeToken(boolean expired) {
        this.expired = expired;
    }
}

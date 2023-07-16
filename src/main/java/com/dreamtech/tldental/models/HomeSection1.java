package com.dreamtech.tldental.models;

public class HomeSection1 {
    ContentPage heading;
    ContentPage subItem1;
    ContentPage subItem2;
    ContentPage subItem3;

    public HomeSection1() {
    }

    public HomeSection1(ContentPage heading, ContentPage subItem1, ContentPage subItem2, ContentPage subItem3) {
        this.heading = heading;
        this.subItem1 = subItem1;
        this.subItem2 = subItem2;
        this.subItem3 = subItem3;
    }

    public ContentPage getHeading() {
        return heading;
    }

    public void setHeading(ContentPage heading) {
        this.heading = heading;
    }

    public ContentPage getSubItem1() {
        return subItem1;
    }

    public void setSubItem1(ContentPage subItem1) {
        this.subItem1 = subItem1;
    }

    public ContentPage getSubItem2() {
        return subItem2;
    }

    public void setSubItem2(ContentPage subItem2) {
        this.subItem2 = subItem2;
    }

    public ContentPage getSubItem3() {
        return subItem3;
    }

    public void setSubItem3(ContentPage subItem3) {
        this.subItem3 = subItem3;
    }
}

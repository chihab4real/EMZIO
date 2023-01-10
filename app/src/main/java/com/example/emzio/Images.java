package com.example.emzio;

public class Images {
    private String image1;
    private String image2;
    private String image3 ;
    private String image4;
    private int images_nb;

    public Images(){
        this.images_nb=0;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage4() {
        return image4;
    }

    public int getImages_nb() {
        return images_nb;
    }

    public void setImages_nb(int images_nb) {
        this.images_nb = images_nb;
    }
}

package com.example.soonbox_findpw.data;
/*
* (* 표시가 된 것은 홈 화면에서도 보여지는 변수 입니다.)*/

public class Posts {
    private String postid;//개시글 개별식별자

    private String postimage; //이미지가 스트링 *: 파베에서 데이터 가져오면 URL링크로 가져옴
    private String product;//제품명*
    private String price;//가격*
    private String name; //계좌주명*  :로그인 정보 아직이라
    private String username; //로그인 정보에서 불러올 수 있을까??
    private String account;
    private String etc;
    private int forsale;// 판매 여부 0 = 판매 1 =판매금지

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public int getForsale() { return forsale; }
    public void setForsale(int forsale) { this.forsale = forsale; }

    public Posts(){}


}

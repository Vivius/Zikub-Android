package info706.zikub.models;

public class User {
    public int id;
    public String name;
    public String email;
    public String password;
    public String remember_token;
    public String created_at;
    public String updated_at;

    public static String getOauthToken() {
        return "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQxMmFlYjNhZjJhMDIzNDE1ZDNkNTg2YzJhZWIzZGEwZTUwOGYxM2QxM2IwODJlOWY5N2FlYjM4MTY4MjkwZGQyZWQ4ZDhlZDQxMWMxMDA2In0.eyJhdWQiOiIxIiwianRpIjoiZDEyYWViM2FmMmEwMjM0MTVkM2Q1ODZjMmFlYjNkYTBlNTA4ZjEzZDEzYjA4MmU5Zjk3YWViMzgxNjgyOTBkZDJlZDhkOGVkNDExYzEwMDYiLCJpYXQiOjE1MDcwNTM2NzYsIm5iZiI6MTUwNzA1MzY3NiwiZXhwIjoxNTM4NTg5Njc2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.mx6Yt3DkUAHOerJjIQvvjjNauXN0etVQMUCNMCgY6xWxKufm8N26UeTPjCJVew-CMlpcCB8zP1uQdY-8cxdIUQoIpfCDeXK3Gd-MeLJ5bqN1ksj3cengVXBMjrUST9Cr_ywy-Wn1pga4sXheEkN6LcivscBwCTJr5qTNSzYrQ9JloJR-GKdGdLLi3cH-thp8OzH_RjBdHGBA5Yb7R6t5GcKwvfwq6ZfXdpFolQa_ZcZ8Tv811A2Xd5p-TdUK_36i8Yns5WibbVTbDZUPwj_-IzGttLGxgjBita736R-18Q1XvaLWn2NIFqi3DO41-fmih_XzZ9Omq8F9_4tFxZJiqEIWUUkiXpmPEDZQVPk37KYInQMNE0olATFV4YNuSETVvnyrjSV4IMHWxHCz0gG9BPmjRYy1yfEAVwi41_ZtaNXJ1IHx6hwYQsDqqWhnrVvAnyZGA8qH46iWqps5Ds1iMSM0AQOnyg0PzW9GCFmlWeH19HrVgjPHHWFmcf_SpP6j_ghPoFYUPTFp_DvTbqSkU6kK69ialtO-uI2Py_OOdemGa1D3G7LnVwbDfBzefmCWwyfjEJhatfx97uMcD2biNkzQ9uEI9J-Ciw0NFkvrJeSX6PHK2_tSn-lRXhFJHetGTncDB96Qeo4xl4kiAQD4lU2Gekr4vt0onkiLE2AM4JU";
    }
}

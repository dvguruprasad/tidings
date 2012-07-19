package com.tidings.backend.repository;

public class Link {

    private String link;

    public Link() {
    }

    public Link(String url) {
        this.link = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (this.link != null ? !this.link.equals(link.link) : link.link != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return link != null ? link.hashCode() : 0;
    }
}

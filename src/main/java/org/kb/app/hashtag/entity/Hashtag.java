package org.kb.app.hashtag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY) //자식 Entity 저장 시 persist됨
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Hashtag parent; //주인

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST, orphanRemoval = true) //자식 list추가 후 저장 시 해당 entity도 persist
    @ToString.Exclude
    @JsonIgnore
    private List<Hashtag> child = new ArrayList<>(); //종속

    //==연관관계 메서드==양방향일 때//
    public void addChildrenHashtag(Hashtag... child) {
        for (Hashtag c : child) {
            this.child.add(c);
            c.setParent(this);
        }
    }

    public static Hashtag createHashtag(String name){
        Hashtag hashtag = new Hashtag();
        hashtag.setName(name);
        return hashtag;
    }

    public static Hashtag createHashtagWithParent(String name, Hashtag parent){
        Hashtag hashtag = new Hashtag();
        hashtag.setName(name);
        hashtag.setParent(parent);
        return hashtag;
    }

}

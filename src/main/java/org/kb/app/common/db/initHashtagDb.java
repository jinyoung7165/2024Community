//package org.kb.app.common.db;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManager;
//
//@Profile("local")
//@Component
//@RequiredArgsConstructor
//public class initHashtagDb {
//
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.hashtagEventInit();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//
//        private final EntityManager em;
//
//        public void hashtagEventInit() {
//            Hashtag parent1 = Hashtag.createHashtag("상황");
//            Hashtag parent2 = Hashtag.createHashtag("스타일");
//            Hashtag parent3 = Hashtag.createHashtag("장소");
//
//            Hashtag child1 = Hashtag.createHashtag("결혼식");
//            Hashtag child2 = Hashtag.createHashtag("여행");
//            Hashtag child3 = Hashtag.createHashtag("휴가");
//            Hashtag child4 = Hashtag.createHashtag("데이트");
//            Hashtag child5 = Hashtag.createHashtag("학교");
//            Hashtag child6 = Hashtag.createHashtag("출근");
//            Hashtag child7 = Hashtag.createHashtag("데일리");
//
//
//            parent1.addChildrenHashtag(child1, child2, child3, child4, child5, child6, child7);
//
//            em.persist(parent1);
//            em.persist(parent2);
//            em.persist(parent3);
//        }
//
//    }
//}

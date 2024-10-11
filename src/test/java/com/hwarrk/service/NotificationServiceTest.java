package com.hwarrk.service;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.NotificationBindingType;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.dto.res.NotificationRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Notification;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.NotificationRepository;
import com.hwarrk.repository.PostRepository;
import com.hwarrk.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

    Member member_01;
    Member member_02;
    Member member_03;

    @BeforeEach
    void setup() {
        member_01 = new Member("test_01", OauthProvider.KAKAO);
        member_02 = new Member("test_02", OauthProvider.KAKAO);
        member_03 = new Member("test_03", OauthProvider.KAKAO);

        memberRepository.save(member_01);
        memberRepository.save(member_02);
        memberRepository.save(member_03);
    }

    @Test
    void 알림_보내기_성공() {
        //given
        Project project = projectRepository.save(new Project("name", "description", member_01));
        Post post = postRepository.save(new Post(project, member_01, false));

        //when
        // 프로젝트 신청 및 수락은 생략..
        notificationService.sendNotification(member_01, NotificationBindingType.POST, post, "member_02님이 ㅁㅁ프로젝트에 지원했습니다!!");
        notificationService.sendNotification(member_01, NotificationBindingType.PROJECT, project, "member_02님이 팀에 합류했습니다!");

        //then
        List<Notification> notifications = notificationRepository.findAllByMemberId(member_01.getId());
        assertThat(notifications).hasSize(2);

        Notification notification_01 = notifications.get(0);
        assertThat(notification_01.getMember().getId()).isEqualTo(member_01.getId());
        assertThat(notification_01.getPost().getId()).isEqualTo(post.getId());
        assertThat(notification_01.getProject()).isNull();
        assertThat(notification_01.getMessage()).isEqualTo("member_02님이 ㅁㅁ프로젝트에 지원했습니다!!");

        Notification notification_02 = notifications.get(1);
        assertThat(notification_02.getMember().getId()).isEqualTo(member_01.getId());
        assertThat(notification_02.getPost()).isNull();
        assertThat(notification_02.getProject().getId()).isEqualTo(project.getId());
        assertThat(notification_02.getMessage()).isEqualTo("member_02님이 팀에 합류했습니다!");
    }

    @Test
    void 알림_보내기_실패() {
        //given

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> notificationService.sendNotification(member_01, NotificationBindingType.PROJECT, member_01, "ClassCastException"));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus._BAD_REQUEST);
    }

    @Test
    void 알림__모두_조회() {
        //given
        Notification notification_01 = notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg_01", false));
        Notification notification_02 = notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg_02", false));

        //when
        SliceRes<NotificationRes> res_01 = notificationService.getNotifications(member_01.getId(), null, PageRequest.of(0, 1));
        SliceRes<NotificationRes> res_02 = notificationService.getNotifications(member_01.getId(), res_01.lastElementId(), PageRequest.of(1, 1));

        //then
        assertThat(res_01.content().size()).isEqualTo(1);
        assertThat(res_01.content().get(0).notificationId()).isEqualTo(notification_02.getId());
        assertThat(res_01.hasNext()).isTrue();

        assertThat(res_02.content().size()).isEqualTo(1);
        assertThat(res_02.content().get(0).notificationId()).isEqualTo(notification_01.getId());
        System.out.println(res_02.content().get(0).bindingId());
        assertThat(res_02.hasNext()).isFalse();
    }

    @Test
    void 알림_읽기_성공() {
        //given
        Notification notification = notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg", false));

        //when
        notificationService.readNotification(member_01.getId(), notification.getId());

        //then
        Notification findNotification = notificationRepository.findAll().get(0);
        assertThat(findNotification.isRead()).isTrue();
    }

    @Test
    void 알림_읽기_실패() {
        //given
        Notification notification = notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg", false));

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> notificationService.readNotification(member_02.getId(), notification.getId()));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
    }

    @Test
    void 알림_모두_읽기() {
        //given
        notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg_01", false));
        notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg_02", false));

        //when
        notificationService.readNotifications(member_01.getId());

        //then
        List<Notification> all = notificationRepository.findAll();
        all.forEach(n -> assertThat(n.isRead()).isTrue());
    }

    @Test
    void 안_읽은_알림_카운트() {
        //given
        notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg_01", false));
        notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg_02", false));
        notificationRepository.save(new Notification(null, member_01, null, null, NotificationBindingType.MY_PAGE, "msg_03", true));

        //when
        int cnt = notificationService.countUnreadNotifications(member_01.getId());

        //then
        assertThat(cnt).isEqualTo(2);
    }
}

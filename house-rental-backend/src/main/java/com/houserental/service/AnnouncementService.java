package com.houserental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.houserental.entity.Announcement;

import java.util.List;

public interface AnnouncementService extends IService<Announcement> {

    IPage<Announcement> pageAnnouncement(Long pageNum, Long pageSize, Integer type, Integer status);

    List<Announcement> listPublished();

    Announcement getAnnouncementDetail(Long id);

    void addAnnouncement(Announcement announcement);

    void updateAnnouncement(Announcement announcement);

    void deleteAnnouncement(Long id);

    void toggleStatus(Long id);

}

package com.houserental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houserental.entity.Announcement;
import com.houserental.exception.BusinessException;
import com.houserental.mapper.AnnouncementMapper;
import com.houserental.service.AnnouncementService;
import com.houserental.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    private static final String CACHE_KEY = "announcement:list";

    @Resource
    private RedisUtils redisUtils;

    @Override
    public IPage<Announcement> pageAnnouncement(Long pageNum, Long pageSize, Integer type, Integer status) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(Announcement::getType, type);
        }
        if (status != null) {
            wrapper.eq(Announcement::getStatus, status);
        }
        wrapper.orderByDesc(Announcement::getCreateTime);
        Page<Announcement> page = new Page<>(pageNum, pageSize);
        return page(page, wrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Announcement> listPublished() {
        Object cacheObj = redisUtils.get(CACHE_KEY);
        if (cacheObj != null) {
            return (List<Announcement>) cacheObj;
        }
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1);
        wrapper.orderByDesc(Announcement::getCreateTime);
        List<Announcement> list = list(wrapper);
        redisUtils.set(CACHE_KEY, list);
        return list;
    }

    @Override
    public Announcement getAnnouncementDetail(Long id) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        return announcement;
    }

    @Override
    public void addAnnouncement(Announcement announcement) {
        if (announcement.getStatus() == null) {
            announcement.setStatus(0);
        }
        save(announcement);
        redisUtils.delete(CACHE_KEY);
    }

    @Override
    public void updateAnnouncement(Announcement announcement) {
        if (announcement.getId() == null) {
            throw new BusinessException("公告ID不能为空");
        }
        Announcement existAnnouncement = getById(announcement.getId());
        if (existAnnouncement == null) {
            throw new BusinessException("公告不存在");
        }
        updateById(announcement);
        redisUtils.delete(CACHE_KEY);
    }

    @Override
    public void deleteAnnouncement(Long id) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        removeById(id);
        redisUtils.delete(CACHE_KEY);
    }

    @Override
    public void toggleStatus(Long id) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        Integer currentStatus = announcement.getStatus();
        Integer newStatus = currentStatus == 1 ? 0 : 1;
        announcement.setStatus(newStatus);
        updateById(announcement);
        redisUtils.delete(CACHE_KEY);
    }

}

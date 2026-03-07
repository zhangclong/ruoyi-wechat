package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysMemberMapper;
import com.ruoyi.system.domain.SysMember;
import com.ruoyi.system.service.ISysMemberService;

/**
 * 会员Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-07
 */
@Service
public class SysMemberServiceImpl implements ISysMemberService
{
    @Autowired
    private SysMemberMapper sysMemberMapper;

    /**
     * 查询会员
     *
     * @param memberId 会员主键
     * @return 会员
     */
    @Override
    public SysMember selectSysMemberByMemberId(Long memberId)
    {
        return sysMemberMapper.selectSysMemberByMemberId(memberId);
    }

    /**
     * 根据手机号查询会员
     *
     * @param phonenumber 手机号
     * @return 会员
     */
    @Override
    public SysMember selectSysMemberByPhonenumber(String phonenumber)
    {
        return sysMemberMapper.selectSysMemberByPhonenumber(phonenumber);
    }

    /**
     * 查询会员列表
     *
     * @param sysMember 会员
     * @return 会员
     */
    @Override
    public List<SysMember> selectSysMemberList(SysMember sysMember)
    {
        return sysMemberMapper.selectSysMemberList(sysMember);
    }

    /**
     * 新增会员
     *
     * @param sysMember 会员
     * @return 结果
     */
    @Override
    public int insertSysMember(SysMember sysMember)
    {
        sysMember.setCreateTime(DateUtils.getNowDate());
        return sysMemberMapper.insertSysMember(sysMember);
    }

    /**
     * 修改会员
     *
     * @param sysMember 会员
     * @return 结果
     */
    @Override
    public int updateSysMember(SysMember sysMember)
    {
        sysMember.setUpdateTime(DateUtils.getNowDate());
        return sysMemberMapper.updateSysMember(sysMember);
    }

    /**
     * 批量删除会员
     *
     * @param memberIds 需要删除的会员主键
     * @return 结果
     */
    @Override
    public int deleteSysMemberByMemberIds(Long[] memberIds)
    {
        return sysMemberMapper.deleteSysMemberByMemberIds(memberIds);
    }

    /**
     * 删除会员信息
     *
     * @param memberId 会员主键
     * @return 结果
     */
    @Override
    public int deleteSysMemberByMemberId(Long memberId)
    {
        return sysMemberMapper.deleteSysMemberByMemberId(memberId);
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param sysMember 会员信息
     * @return 结果
     */
    @Override
    public boolean checkPhoneUnique(SysMember sysMember)
    {
        Long memberId = StringUtils.isNull(sysMember.getMemberId()) ? -1L : sysMember.getMemberId();
        SysMember info = sysMemberMapper.checkPhoneUnique(sysMember.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return false;
        }
        return true;
    }
}

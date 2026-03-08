package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMember;

/**
 * 会员Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-07
 */
public interface SysMemberMapper
{
    /**
     * 查询会员
     *
     * @param memberId 会员主键
     * @return 会员
     */
    public SysMember selectSysMemberByMemberId(Long memberId);

    /**
     * 根据手机号查询会员
     *
     * @param phonenumber 手机号
     * @return 会员
     */
    public SysMember selectSysMemberByPhonenumber(String phonenumber);

    /**
     * 查询会员列表
     *
     * @param sysMember 会员
     * @return 会员集合
     */
    public List<SysMember> selectSysMemberList(SysMember sysMember);

    /**
     * 新增会员
     *
     * @param sysMember 会员
     * @return 结果
     */
    public int insertSysMember(SysMember sysMember);

    /**
     * 修改会员
     *
     * @param sysMember 会员
     * @return 结果
     */
    public int updateSysMember(SysMember sysMember);

    /**
     * 删除会员
     *
     * @param memberId 会员主键
     * @return 结果
     */
    public int deleteSysMemberByMemberId(Long memberId);

    /**
     * 批量删除会员
     *
     * @param memberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMemberByMemberIds(Long[] memberIds);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysMember checkPhoneUnique(String phonenumber);
}

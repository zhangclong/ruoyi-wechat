package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会员对象 sys_member
 *
 * @author ruoyi
 * @date 2025-03-07
 */
public class SysMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员ID */
    private Long memberId;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String avatar;

    /** 微信UnionID */
    @Excel(name = "微信UnionID")
    private String wechatUnionId;

    /** 微信OpenIDs */
    @Excel(name = "微信OpenIDs")
    private String wechatOpenids;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setWechatUnionId(String wechatUnionId)
    {
        this.wechatUnionId = wechatUnionId;
    }

    public String getWechatUnionId()
    {
        return wechatUnionId;
    }

    public void setWechatOpenids(String wechatOpenids)
    {
        this.wechatOpenids = wechatOpenids;
    }

    public String getWechatOpenids()
    {
        return wechatOpenids;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("memberId", getMemberId())
            .append("nickName", getNickName())
            .append("phonenumber", getPhonenumber())
            .append("avatar", getAvatar())
            .append("wechatUnionId", getWechatUnionId())
            .append("wechatOpenids", getWechatOpenids())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

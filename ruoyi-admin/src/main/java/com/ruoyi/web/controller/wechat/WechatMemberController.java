package com.ruoyi.web.controller.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysMember;
import com.ruoyi.system.service.ISysMemberService;
import com.ruoyi.common.utils.StringUtils;

/**
 * 微信会员API Controller
 *
 * @author ruoyi
 * @date 2025-03-07
 */
@RestController
@RequestMapping("/wechat/member")
public class WechatMemberController extends BaseController
{
    @Autowired
    private ISysMemberService sysMemberService;

    /**
     * 会员注册
     */
    @Log(title = "会员注册", businessType = BusinessType.INSERT)
    @PostMapping("/register")
    public AjaxResult register(@RequestBody SysMember sysMember)
    {
        // 校验必填参数
        if (StringUtils.isEmpty(sysMember.getNickName()))
        {
            return error("昵称不能为空");
        }
        if (StringUtils.isEmpty(sysMember.getPhonenumber()))
        {
            return error("手机号不能为空");
        }

        // 校验手机号是否已注册
        if (!sysMemberService.checkPhoneUnique(sysMember))
        {
            return error("该手机号已注册");
        }

        // 设置默认状态
        sysMember.setStatus("0");
        sysMember.setDelFlag("0");
        sysMember.setCreateBy("wechat");

        int result = sysMemberService.insertSysMember(sysMember);
        if (result > 0)
        {
            SysMember member = sysMemberService.selectSysMemberByPhonenumber(sysMember.getPhonenumber());
            return success(member);
        }
        return error("注册失败");
    }

    /**
     * 会员登录（根据手机号）
     */
    @Log(title = "会员登录", businessType = BusinessType.OTHER)
    @PostMapping("/login")
    public AjaxResult login(@RequestParam String phonenumber)
    {
        if (StringUtils.isEmpty(phonenumber))
        {
            return error("手机号不能为空");
        }

        SysMember member = sysMemberService.selectSysMemberByPhonenumber(phonenumber);
        if (StringUtils.isNull(member))
        {
            return error("该手机号未注册");
        }

        if ("1".equals(member.getStatus()))
        {
            return error("账号已停用，请联系管理员");
        }

        return success(member);
    }

    /**
     * 获取会员信息
     */
    @GetMapping("/info")
    public AjaxResult getInfo(@RequestParam String phonenumber)
    {
        if (StringUtils.isEmpty(phonenumber))
        {
            return error("手机号不能为空");
        }

        SysMember member = sysMemberService.selectSysMemberByPhonenumber(phonenumber);
        if (StringUtils.isNull(member))
        {
            return error("会员不存在");
        }

        return success(member);
    }

    /**
     * 修改会员信息
     */
    @Log(title = "修改会员信息", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult update(@RequestBody SysMember sysMember)
    {
        if (StringUtils.isNull(sysMember.getMemberId()))
        {
            return error("会员ID不能为空");
        }

        SysMember existMember = sysMemberService.selectSysMemberByMemberId(sysMember.getMemberId());
        if (StringUtils.isNull(existMember))
        {
            return error("会员不存在");
        }

        // 不允许修改手机号
        sysMember.setPhonenumber(null);
        sysMember.setUpdateBy("wechat");

        int result = sysMemberService.updateSysMember(sysMember);
        if (result > 0)
        {
            SysMember updatedMember = sysMemberService.selectSysMemberByMemberId(sysMember.getMemberId());
            return success(updatedMember);
        }
        return error("修改失败");
    }
}

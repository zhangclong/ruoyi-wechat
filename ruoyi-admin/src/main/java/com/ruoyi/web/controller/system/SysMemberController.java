package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysMember;
import com.ruoyi.system.service.ISysMemberService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;

/**
 * 会员Controller
 *
 * @author ruoyi
 * @date 2025-03-07
 */
@RestController
@RequestMapping("/system/member")
public class SysMemberController extends BaseController
{
    @Autowired
    private ISysMemberService sysMemberService;

    /**
     * 查询会员列表
     */
    @PreAuthorize("@ss.hasPermi('system:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysMember sysMember)
    {
        startPage();
        List<SysMember> list = sysMemberService.selectSysMemberList(sysMember);
        return getDataTable(list);
    }

    /**
     * 导出会员列表
     */
    @PreAuthorize("@ss.hasPermi('system:member:export')")
    @Log(title = "会员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMember sysMember)
    {
        List<SysMember> list = sysMemberService.selectSysMemberList(sysMember);
        ExcelUtil<SysMember> util = new ExcelUtil<SysMember>(SysMember.class);
        util.exportExcel(response, list, "会员数据");
    }

    /**
     * 获取会员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return success(sysMemberService.selectSysMemberByMemberId(memberId));
    }

    /**
     * 新增会员
     */
    @PreAuthorize("@ss.hasPermi('system:member:add')")
    @Log(title = "会员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMember sysMember)
    {
        if (!sysMemberService.checkPhoneUnique(sysMember))
        {
            return error("新增会员'" + sysMember.getNickName() + "'失败，手机号码已存在");
        }
        sysMember.setCreateBy(getUsername());
        return toAjax(sysMemberService.insertSysMember(sysMember));
    }

    /**
     * 修改会员
     */
    @PreAuthorize("@ss.hasPermi('system:member:edit')")
    @Log(title = "会员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMember sysMember)
    {
        if (!sysMemberService.checkPhoneUnique(sysMember))
        {
            return error("修改会员'" + sysMember.getNickName() + "'失败，手机号码已存在");
        }
        sysMember.setUpdateBy(getUsername());
        return toAjax(sysMemberService.updateSysMember(sysMember));
    }

    /**
     * 删除会员
     */
    @PreAuthorize("@ss.hasPermi('system:member:remove')")
    @Log(title = "会员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        return toAjax(sysMemberService.deleteSysMemberByMemberIds(memberIds));
    }
}

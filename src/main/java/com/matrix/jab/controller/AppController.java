package com.matrix.jab.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.matrix.jab.common.JsonResult;
import com.matrix.jab.model.dao.FileRecordDao;
import com.matrix.jab.model.dao.InfoDao;
import com.matrix.jab.model.dao.UserDao;
import com.matrix.jab.model.entity.FileRecord;
import com.matrix.jab.model.entity.Info;
import com.matrix.jab.model.entity.User;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/")
public class AppController {
    @Autowired
    private InfoDao infoDao;

    @Autowired
    private FileRecordDao fileRecordDao;

    @Autowired
    private UserDao userDao;


    @RequestMapping
    public String index(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) {
            // 未登录
            return "login";
        }
        model.addAttribute("key", LocalDate.now());
        model.addAttribute("currentUser", user.getId());
        return "index";
    }

    @RequestMapping("/login")
    public String login(String username, String password, HttpSession session, RedirectAttributes rda) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            rda.addFlashAttribute("error", "请输入账号和密码");
        } else {
            User user = userDao.findFirstByUserName(username);
            if (user != null && user.getPassword().equals(password)) {
                session.setAttribute("user", user);
            } else {
                rda.addFlashAttribute("error", "帐号和密码不匹配");
            }
        }
        return "redirect:/";
    }


    private List<Info> searchList(Info info, String startTime, String endTime) {
        Specification<Info> s1 = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(info.getProductName())) {
                predicates.add(cb.like(root.get("productName"), "%" + info.getProductName() + "%"));
            }
            if (StringUtils.hasText(info.getProductCode())) {
                predicates.add(cb.like(root.get("productCode"), "%" + info.getProductCode() + "%"));
            }
            if (StringUtils.hasText(info.getCustomer())) {
                predicates.add(cb.like(root.get("customer"), "%" + info.getCustomer() + "%"));
            }
            if (StringUtils.hasText(info.getCustomerCode())) {
                predicates.add(cb.like(root.get("customerCode"), "%" + info.getCustomerCode() + "%"));
            }
            if (StringUtils.hasText(info.getSupplier())) {
                predicates.add(cb.like(root.get("supplier"), "%" + info.getSupplier() + "%"));
            }
            if (StringUtils.hasText(info.getBatchNo())) {
                predicates.add(cb.like(root.get("batchNo"), "%" + info.getBatchNo() + "%"));
            }
            if (StringUtils.hasText(info.getBatchCode())) {
                predicates.add(cb.like(root.get("batchCode"), "%" + info.getBatchCode() + "%"));
            }
            if (StringUtils.hasText(startTime)) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salesDate").as(String.class), startTime));
            }
            if (StringUtils.hasText(endTime)) {
                String tmp = endTime + " 23:59:59";
                cb.lessThanOrEqualTo(root.get("salesDate").as(String.class), tmp);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<Info> list = infoDao.findAll(s1, Sort.by(Sort.Direction.DESC, "salesDate"));
        return list;
    }

    @RequestMapping("/infoList")
    @ResponseBody
    public List<Info> infoList(Info info, String startTime, String endTime) {
        return searchList(info, startTime, endTime);
    }

    @RequestMapping("/crud")
    public String crud() {
        return "crud";
    }


    @PostMapping(value = "/upload")
    @ResponseBody
    @Transactional
    public JsonResult upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String name = multipartFile.getOriginalFilename();
            FileRecord fr = fileRecordDao.findByFileName(name);
            if (fr != null) {
                return JsonResult.error("上传失败:已上传过相同名称的文件!");
            }
            EasyExcel.read(multipartFile.getInputStream(), Info.class, new PageReadListener<Info>(dataList -> {
                infoDao.saveAll(dataList);
            })).sheet().doRead();
            FileRecord record = new FileRecord();
            record.setFileName(name);
            fileRecordDao.save(record);
            return JsonResult.success("上传成功!");
        } catch (Exception e) {
            return JsonResult.error("上传异常");
        }
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response, Info info, String startTime, String endTime) throws IOException {
        List<Info> list = searchList(info, startTime, endTime);
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteCellStyle.setWriteFont(headWriteFont);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, (List<WriteCellStyle>) null);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), Info.class).registerWriteHandler(horizontalCellStyleStrategy).sheet("流向数据").doWrite(list);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/delete")
    @ResponseBody
    @Transactional
    public JsonResult multiple(Long[] id) {
        infoDao.deleteAllById(Arrays.asList(id));
        return JsonResult.success("删除成功");
    }

    @PostMapping("/passwd")
    @ResponseBody
    public JsonResult changePassword(@SessionAttribute(value = "user", required = false) User user, String oldPassword, String newPassword1, String newPassword2) {
        User one = userDao.findById(user.getId()).get();
        if (one.getPassword().equals(oldPassword)) {
            if (newPassword1.equals(newPassword2)) {
                one.setPassword(newPassword1);
                userDao.save(one);
                return JsonResult.success("修改成功");
            } else {
                return JsonResult.error("两次密码输入不一致");
            }
        }
        return JsonResult.error("愿密码错误");
    }
}

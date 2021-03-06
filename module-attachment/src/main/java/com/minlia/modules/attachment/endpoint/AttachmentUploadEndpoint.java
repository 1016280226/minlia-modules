package com.minlia.modules.attachment.endpoint;


import com.google.common.collect.Lists;
import com.minlia.cloud.body.StatefulBody;
import com.minlia.cloud.body.impl.SuccessResponseBody;
import com.minlia.cloud.constant.ApiPrefix;
import com.minlia.cloud.utils.ApiPreconditions;
import com.minlia.modules.aliyun.oss.api.constant.UploadCode;
import com.minlia.modules.aliyun.oss.api.service.OssService;
import com.minlia.modules.aliyun.oss.bean.OssFile;
import com.minlia.modules.aliyun.oss.builder.PathBuilder;
import com.minlia.modules.attachment.entity.Attachment;
import com.minlia.modules.attachment.service.AttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by will on 6/28/17.
 */
@RestController
@RequestMapping(value = ApiPrefix.V1 + "/attachment/upload")
@Api(tags = "Attachment", description = "附件")
@Slf4j
public class AttachmentUploadEndpoint {

    @Autowired
    private OssService ossService;
    @Autowired
    private AttachmentService attachmentService;

    @ApiOperation(value = "file update", notes = "文件上传服务", httpMethod = "POST")
    @PostMapping(value = "/{businessId}/{businessType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatefulBody upload(MultipartFile file, @PathVariable String businessId, @PathVariable String businessType) throws Exception {
        //检查是否满足上传条件 TODO 上传数量、businessType是否存在

        List<Attachment> attachments = Lists.newArrayList();
//        for (MultipartFile file:files) {
            String key = keyGenerate(file,businessId,businessType);
            OssFile ossFile=null;
            try {
                ossFile = ossService.upload(file, key);
            } catch (Exception e) {
                ApiPreconditions.checkNotNull(e, UploadCode.E22, e.getMessage());
            }

            //附件记录
            Attachment attachment = Attachment.builder().businessType(businessType).businessId(businessId).name(ossFile.getOriginalName()).type(ossFile.getContentType()).url(ossFile.getUrl()).size(ossFile.getSize()).accessKey(ossFile.geteTag()).build();
            attachments.add(attachment);
//        }
        return  SuccessResponseBody.builder().message("上传成功").payload(attachmentService.create(attachments)).build();
    }

    @ApiOperation(value = "file update", notes = "文件上传服务", httpMethod = "POST")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatefulBody upload(MultipartFile file) throws Exception {
        String key = keyGenerate(file);
        OssFile ossFile=null;
        try {
            ossFile = ossService.upload(file, key);
        } catch (Exception e) {
            ApiPreconditions.checkNotNull(e, UploadCode.E22, e.getMessage());
        }
        return  SuccessResponseBody.builder().message("上传成功").payload(ossFile).build();
    }

    private String keyGenerate(MultipartFile file){
        return String.format("%s", PathBuilder.uuidNameBuild(file.getOriginalFilename()));
    }

    private String keyGenerate(MultipartFile file, String businessId, String businessType){
        return String.format("%s/%s/%s",businessId,businessType, PathBuilder.uuidNameBuild(file.getOriginalFilename()));
    }

}
